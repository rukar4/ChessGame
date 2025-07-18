package svc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.AuthDAO;
import models.AuthToken;
import spark.Request;
import spark.Response;
import svc.ServiceClasses.*;
import svc.account.LoginRequest;
import svc.account.RegisterRequest;
import svc.game.CreateGameRequest;
import svc.game.JoinGameRequest;

import java.lang.reflect.Modifier;

public class Handler {
   private final AuthDAO authDAO = AuthDAO.getInstance();

   public Object handler(Request req, Response res, String endPoint) {
      String reqBody = req.body();
      String authToken = req.headers("Authorization");
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
            if (isNotAuthorized(authToken, result)) break;

            LogoutService logoutService = new LogoutService();
            result = logoutService.logout(authToken);
            break;
         case "listGames":
            if (isNotAuthorized(authToken, result)) break;

            ListGamesService listGamesService = new ListGamesService();
            result = listGamesService.getGames();
            break;
         case "getGame":
            if (isNotAuthorized(authToken, result)) break;

            GetGameService getGameService = new GetGameService();
            result = getGameService.getGame(req.attribute("gameID"));

            res.type("application/json");
            res.status(result.getApiRes().getCode());

            // Include transient game variable in response
            Gson gson = new GsonBuilder()
                    .excludeFieldsWithModifiers(Modifier.STATIC)
                    .create();

            return gson.toJson(result);
         case "createGame":
            if (isNotAuthorized(authToken, result)) break;

            CreateGameService createGameService = new CreateGameService();
            CreateGameRequest createGameRequest = new Gson().fromJson(reqBody, CreateGameRequest.class);
            result = createGameService.createGame(createGameRequest);
            break;
         case "joinGame":
            if (isNotAuthorized(authToken, result)) break;

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

   public boolean isNotAuthorized(String authToken, Result result) {
      try {
         if (authToken == null) {
            result.setApiRes(Result.ApiRes.UNAUTHORIZED);
            return true;
         }

         AuthToken userToken = authDAO.getToken(authToken);

         if (!userToken.getAuthToken().equals(authToken)) {
            result.setApiRes(Result.ApiRes.UNAUTHORIZED);
            return true;
         }
         return false;
      } catch (Exception e) {
         ErrorLogger err = new ErrorLogger();
         err.errMessage(e, "Handler", result);

         return true;
      }
   }
}
