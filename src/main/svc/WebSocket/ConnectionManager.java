package svc.WebSocket;

import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.Notification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
   public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

   public void add(String user, Session session) {
      Connection connection = new Connection(user, session);
      connections.put(user, connection);
   }

   public void remove(String username) {
      connections.remove(username);
   }

   public void broadcast(String currentUser, Notification notification) throws IOException {
      var removeList = new ArrayList<Connection>();
      for (Connection c : connections.values()) {
         if (c.session.isOpen()) {
            if (!c.username.equals(currentUser)) {
               c.send(notification.toString());
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
