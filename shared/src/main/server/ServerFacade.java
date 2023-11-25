package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exception.ResponseException;
import svc.Result;
import svc.account.LoginRequest;
import svc.account.LoginResult;
import svc.account.RegisterRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {
   private final String serverUrl;

   public ServerFacade(String url) {
      serverUrl = url;
   }

   public LoginResult register(RegisterRequest req) throws ResponseException {
      var path = "/user";
      return this.makeRequest("POST", path, req, LoginResult.class);
   }

   public LoginResult login(LoginRequest req) throws ResponseException {
      var path = "/session";
      return this.makeRequest("POST", path, req, LoginResult.class);
   }

   public Result logout(String token) throws ResponseException {
      var path = "/session";
      return this.makeRequest("DELETE", path, token, Result.class);
   }

   private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
      try {
         URL url = (new URI(serverUrl + path)).toURL();
         HttpURLConnection http = (HttpURLConnection) url.openConnection();
         http.setRequestMethod(method);
         http.setDoOutput(true);

         writeBody(request, http);
         http.connect();
         throwIfNotSuccessful(http);
         return readBody(http, responseClass);
      } catch (Exception e) {
         throw new ResponseException(500, e.getMessage());
      }
   }

   private static void writeBody(Object request, HttpURLConnection http) throws IOException {
      if (request != null) {
         http.addRequestProperty("Content-Type", "application/json");
         String reqData = new Gson().toJson(request);
         try (OutputStream reqBody = http.getOutputStream()) {
            reqBody.write(reqData.getBytes());
         }
      }
   }

   private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
      var status = http.getResponseCode();
      if (status != 200) {
         throw new ResponseException(status, "failure: " + http.getResponseMessage());
      }
   }

   private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
      T response = null;
      if (http.getContentLength() < 0) {
         try (InputStream respBody = http.getInputStream()) {
            InputStreamReader reader = new InputStreamReader(respBody);
            if (responseClass != null) {
               response = new GsonBuilder().create().fromJson(reader, responseClass);
            }
         }
      }
      return response;
   }
}
