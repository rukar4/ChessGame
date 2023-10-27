package svc.handler;

import models.AuthToken;
import spark.Request;
import spark.Response;
import svc.Result;
import svc.account.*;
import com.google.gson.Gson;

import java.util.Set;

public class Handler {
   public Object handler(Request req, Response res, String endPoint) {
      String reqBody = req.body();
      Result result;

      switch (endPoint) {
         case "login":
            LoginService loginService = new LoginService();
            LoginRequest loginRequest = new Gson().fromJson(reqBody, LoginRequest.class);
            result = loginService.login(loginRequest);
            break;
         case "register":
            RegisterService registerService = new RegisterService();
            RegisterRequest registerRequest = new Gson().fromJson(reqBody, RegisterRequest.class);
            result = registerService.register(registerRequest);
            break;
         case "logout":
            LogoutService logoutService = new LogoutService();
            String authToken = new Gson().fromJson(req.headers("authorization"), String.class);
            result = logoutService.logout(authToken);
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
}
