package client;

import server.ServerFacade;
import svc.account.LoginRequest;
import svc.account.LoginResult;
import svc.account.RegisterRequest;

public class ChessClient {
   private String username = null;
   private String authToken = null;
   private boolean signedIn = false;
   private final ServerFacade server;

   public ChessClient(String serverUrl) {
      server = new ServerFacade(serverUrl);
   }

   public String login(String username, String password) {
      LoginRequest loginRequest = new LoginRequest(username, password);

      try {
         LoginResult res = server.login(loginRequest);
         this.username = res.getUsername();
         this.authToken = res.getAuthToken();
         signedIn = true;

         return String.format("You are signed in as %s.\n", username);

      } catch (Exception e) {
         return String.format("Login failed:\n, %s\n", e.getMessage());
      }
   }

   public String register(String username, String password, String email) {
      RegisterRequest registerRequest = new RegisterRequest(username, password, email);

      try {
         LoginResult res = server.register(registerRequest);
         signedIn = true;

         return String.format("Register successful! You are logged in as %s.\n", res.getUsername());

      } catch (Exception e) {
         return String.format("Register failed:\n %s\n", e.getMessage());
      }
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getAuthToken() {
      return authToken;
   }

   public void setAuthToken(String authToken) {
      this.authToken = authToken;
   }

   public boolean isSignedIn() {
      return signedIn;
   }

   public void setSignedIn(boolean signedIn) {
      this.signedIn = signedIn;
   }
}
