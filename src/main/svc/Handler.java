package svc;

import dao.AuthDAO;
import models.AuthToken;
import spark.Request;
import spark.Response;
import svc.Result;
import svc.account.*;
import com.google.gson.Gson;
import svc.clearApp.ClearAppService;
import svc.game.*;

import java.util.Set;

public class Handler {
   private final AuthDAO authDAO = AuthDAO.getInstance();

   public Object handler(Request req, Response res, String endPoint) {
      String reqBody = req.body();
      String authToken = new Gson().fromJson(req.headers("authorization"), String.class);
      Result result = new Result();

      switch (endPoint) {
         case "clearApp":
            ClearAppService clearAppService = new ClearAppService();
            result = clearAppService.clearApp();
            break;
         case "register":
            RegisterService registerService = new RegisterService();
            RegisterRequest registerRequest = new Gson().fromJson(reqBody, RegisterRequest.class);
            result = registerService.register(registerRequest);
            break;
         case "login":
            LoginService loginService = new LoginService();
            LoginRequest loginRequest = new Gson().fromJson(reqBody, LoginRequest.class);
            result = loginService.login(loginRequest);
            break;
         case "logout":
            if (!isAuthorized(authToken, result)) return result;

            LogoutService logoutService = new LogoutService();
            result = logoutService.logout(authToken);
            break;
         case "listGames":
            if (!isAuthorized(authToken, result)) return result;

            ListGamesService listGamesService = new ListGamesService();
            result = listGamesService.getGames(authToken);
            break;
         case "createGame":
            if (!isAuthorized(authToken, result)) return result;

            CreateGameService createGameService = new CreateGameService();
            CreateGameRequest createGameRequest = new Gson().fromJson(reqBody, CreateGameRequest.class);
            result = createGameService.createGame(authToken, createGameRequest);
            break;
         case "joinGame":
            if (!isAuthorized(authToken, result)) return result;

            JoinGameService joinGameService = new JoinGameService();
            JoinGameRequest joinGameRequest = new Gson().fromJson(reqBody, JoinGameRequest.class);
            result = joinGameService.joinGame(authToken, joinGameRequest);
            break;
         default:
            // If none of the cases are hit, something is horribly wrong
            result = new Result();
            result.setApiRes(Result.ApiRes.INTERNAL_ERROR);
            break;
      }
      res.type("application/json");
      res.status(result.getApiRes().getCode());

      return new Gson().toJson(result);
   }

   private boolean isAuthorized(String authToken, Result result) {
      try {
         if (authToken == null) {
            result.setApiRes(Result.ApiRes.UNAUTHORIZED);
            return false;
         }

         AuthToken userToken = authDAO.getToken(authToken);

         if (userToken == null || !userToken.getAuthToken().equals(authToken)) {
            result.setApiRes(Result.ApiRes.UNAUTHORIZED);
            return false;
         }
         return true;
      } catch (Exception e) {
         ErrorLogger err = new ErrorLogger();
         err.errMessage(e, "Handler", result);

         return false;
      }
   }
}
