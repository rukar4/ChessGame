import client.ui.LoginRepl;

public class ClientMain {
   public static void main(String[] args) {
      String serverUrl = "http://localhost:8080";
      if (args.length == 1) {
         serverUrl = args[0];
      }

      new LoginRepl(serverUrl).run();
   }
}
