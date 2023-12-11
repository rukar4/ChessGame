package svc.WebSocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
   public String username;
   public int gameID;
   public Session session;

   public Connection(String username, int gameID, Session session) {
      this.username = username;
      this.session = session;
      this.gameID = gameID;
   }

   public void send(String message) throws IOException {
      session.getRemote().sendString(message);
   }
}
