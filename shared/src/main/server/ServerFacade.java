package server;

import chess.ChessMove;
import chess.ChessPiece;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exception.ResponseException;
import svc.Result;
import svc.account.LoginRequest;
import svc.account.LoginResult;
import svc.account.RegisterRequest;
import svc.game.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {
   private final String serverUrl;

   public ServerFacade(String url) {
      serverUrl = url;
   }

   private static void writeBody(Object request, HttpURLConnection http) throws IOException {
      if (request != null) {
         http.addRequestProperty("Content-Type", "application/json");
         String reqData = new GsonBuilder().registerTypeAdapter(ChessMove.class, new ChessMoveAdapter()).create().toJson(request);
         try (OutputStream reqBody = http.getOutputStream()) {
            reqBody.write(reqData.getBytes());
         }
      }
   }

   private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException, ResponseException {
      int statusCode = http.getResponseCode();
      boolean wasSuccessful = statusCode == 200;

      T result = null;
      if (http.getContentLength() < 0) {
         InputStream respBody;

         if (wasSuccessful) {
            respBody = http.getInputStream();
         } else {
            respBody = http.getErrorStream();
         }

         InputStreamReader reader = new InputStreamReader(respBody);
         if (responseClass != null) {
            result = new GsonBuilder()
                    .excludeFieldsWithModifiers(Modifier.STATIC)
                    .registerTypeAdapter(ChessPiece.class, new ChessPieceAdapter())
                    .registerTypeAdapter(ChessMove.class, new ChessMoveAdapter())
                    .create()
                    .fromJson(reader, responseClass);
            if (!wasSuccessful)
               throw new ResponseException(statusCode, String.format("[%d] %s", statusCode, getMessage(result)));
         }
      }

      return result;
   }

   private static <T> String getMessage(T result) {
      try {
         Method getMessageMethod = result.getClass().getMethod("getMessage");
         return (String) getMessageMethod.invoke(result);
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
         System.out.println(e.getMessage());
         return null;
      }
   }

   public LoginResult register(RegisterRequest req) throws ResponseException {
      var path = "/user";
      return this.makeRequest("POST", path, null, req, LoginResult.class);
   }

   public LoginResult login(LoginRequest req) throws ResponseException {
      var path = "/session";
      return this.makeRequest("POST", path, null, req, LoginResult.class);
   }

   public Result logout(String token) throws ResponseException {
      var path = "/session";
      return this.makeRequest("DELETE", path, token, null, Result.class);
   }

   public CreateGameResult createGame(CreateGameRequest req, String token) throws ResponseException {
      var path = "/game";
      return this.makeRequest("POST", path, token, req, CreateGameResult.class);
   }

   public Result joinGame(JoinGameRequest req, String token) throws ResponseException {
      var path = "/game";
      return this.makeRequest("PUT", path, token, req, CreateGameResult.class);
   }

   public GetGameResult getGame(int gameID, String token) throws ResponseException {
      var path = "/game/" + gameID;
      return this.makeRequest("GET", path, token, null, GetGameResult.class);
   }

   public ListGamesResult listGames(String token) throws ResponseException {
      var path = "/game";
      return this.makeRequest("GET", path, token, null, ListGamesResult.class);
   }

   public Result clearApp() throws ResponseException {
      var path = "/db";
      return this.makeRequest("DELETE", path, null, null, Result.class);
   }

   private <T> T makeRequest(String method, String path, String authorization, Object request, Class<T> responseClass) throws ResponseException {
      try {
         URL url = (new URI(serverUrl + path)).toURL();
         HttpURLConnection http = (HttpURLConnection) url.openConnection();
         http.setRequestMethod(method);
         http.setDoOutput(true);

         if (authorization != null) {
            http.setRequestProperty("Authorization", authorization);
         }

         if (request != null) {
            writeBody(request, http);
         }

         http.connect();
         return readBody(http, responseClass);
      } catch (Exception e) {
         if (e instanceof ResponseException) {
            throw (ResponseException) e;
         }
         throw new ResponseException(500, e.getMessage());
      }
   }
}
