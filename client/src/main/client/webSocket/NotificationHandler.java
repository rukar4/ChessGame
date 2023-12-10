package client.webSocket;

import webSocketMessages.serverMessages.Notification;

public interface NotificationHandler {
   void notify(Notification notification);
}
