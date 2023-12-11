package svc;

import dataAccess.Database;
import spark.Spark;
import svc.WebSocket.WSHandler;

public class Server {
   public static Database db = new Database();
   private final Handler handler = new Handler();
   private final WSHandler wsHandler = new WSHandler();

   public static void main(String[] args) {
      new Server().run();
   }

   private void run() {
      Spark.port(8080);

      Spark.externalStaticFileLocation("public");

      Spark.webSocket("/connect", wsHandler);

      Spark.staticFileLocation("web");
      Spark.get("/", (req, res) -> {
         res.redirect("web/index.html");
         return null;
      });

      Spark.post("/user", (req, res) -> handler.handler(req, res, "register"));

      Spark.post("/session", (req, res) -> handler.handler(req, res, "login"));
      Spark.delete("/session", (req, res) -> handler.handler(req, res, "logout"));

      Spark.get("/game", (req, res) -> handler.handler(req, res, "listGames"));
      Spark.get("/game/:gameID", (req, res) -> {
         // Get game id from url
         String gameIDParam = req.params(":gameID");
         int gameID = Integer.parseInt(gameIDParam);
         req.attribute("gameID", gameID);

         return handler.handler(req, res, "getGame");
      });
      Spark.post("/game", (req, res) -> handler.handler(req, res, "createGame"));
      Spark.put("/game", (req, res) -> handler.handler(req, res, "joinGame"));

      Spark.delete("/db", (req, res) -> handler.handler(req, res, "clearApp"));

      Spark.awaitInitialization();
   }
}
