import spark.Spark;
import svc.Handler;

public class Server {
   Handler handler = new Handler();
   public static void main(String[] args) {
      new Server().run();
   }

   private void run() {
      Spark.port(8080);
      Spark.staticFileLocation("web");

      Spark.get("/", (req, res) -> {
         res.redirect("web/index.html");
         return null;
      });

      Spark.post("/user", (req, res) -> handler.handler(req, res, "register"));

      Spark.post("/session", (req, res) -> handler.handler(req, res, "login"));
      Spark.delete("/session", (req, res) -> handler.handler(req, res, "logout"));

      Spark.get("/game", (req, res) -> handler.handler(req, res, "listGames"));
      Spark.put("/game", (req, res) -> handler.handler(req, res, "joinGame"));

      Spark.delete("/db", (req, res) -> handler.handler(req, res, "clearApp"));
   }
}
