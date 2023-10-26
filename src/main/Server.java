import com.google.gson.Gson;
import java.util.*;

import spark.*;

public class Server {

   public static void main(String[] args) {
      new Server().run();
   }

   private void run() {
      Spark.port(8080);
      Spark.staticFileLocation("../web");
   }
}
