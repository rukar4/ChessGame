import spark.Spark;
import svc.handler.Handler;

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
   }
}
