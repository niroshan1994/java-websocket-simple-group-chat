package websocket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ServerEndpoint("/test")
public class HelloWebsocketServer {

    Map<Session, String> activeSessions = new HashMap<>();

    @OnOpen
    public void onOpen(Session session) throws IOException {
        Map<String, List<String>> parameters = session.getRequestParameterMap();
        String user = null;
        if (parameters.containsKey("user")) {
            user = parameters.get("user").get(0);
            for (Session s : session.getOpenSessions()) {
                s.getBasicRemote().sendText(user + " : Connected");
            }
        }

        if (user == null) {
            session.close();
            return;
        }

        activeSessions.put(session, user);
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        for (Session s : session.getOpenSessions()) {
            s.getBasicRemote().sendText(activeSessions.get(session) + " : " + message);
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        if (activeSessions.containsKey(session)) {
            for (Session s : session.getOpenSessions()) {
                if (!s.equals(session)) {
                    s.getBasicRemote().sendText(activeSessions.get(session) + " : Disconnected");
                }
            }
            activeSessions.remove(session);
        }
        System.out.println("Closed");
    }

    @OnError
    public void onError(Session session, Throwable thr) {
        System.out.println("Error");
        thr.printStackTrace();
    }

}
