package svc.WebSocket;

import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
   public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

   public void add(String user, int gameID, Session session) {
      var connection = new Connection(user, gameID, session);
      connections.put(user, connection);
   }

   public void remove(String username) {
      connections.remove(username);
   }

   public void broadcast(String userToSkip, int gameID, ServerMessage message) throws IOException {
      var removeList = new ArrayList<Connection>();
      for (Connection c : connections.values()) {
         if (c.session.isOpen()) {
            if (!c.username.equals(userToSkip) && c.gameID == gameID) {
               c.send(message.toString());
            }
         } else {
            removeList.add(c);
         }
      }

      for (Connection c : removeList) {
         connections.remove(c.username);
      }
   }
}
