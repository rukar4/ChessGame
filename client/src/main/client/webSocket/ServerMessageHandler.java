package client.webSocket;

import webSocketMessages.serverMessages.ServerMessage;

public interface ServerMessageHandler {
   void displayMessage(ServerMessage message);
}
