package de.primeapi.primeplugins.bungeeapi.websocket;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.primeapi.primeplugins.bungeeapi.PrimeCore;
import de.primeapi.primeplugins.bungeeapi.configs.CoreConfig;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;

/**
 * @author Lukas S. PrimeAPI
 * created on 18.05.2021
 * crated for PrimePlugins
 */
@WebSocket
public class WebPanelSocket {

    // Store sessions if you want to, for example, broadcast a message to all users
    private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();

    @OnWebSocketConnect
    public void connected(Session session) {
        sessions.add(session);
    }

    @OnWebSocketClose
    public void closed(Session session, int statusCode, String reason) {
        sessions.remove(session);
    }


    @OnWebSocketMessage
    public void message(Session session, String message) throws IOException {
        try {
            JsonElement element = new JsonParser().parse(message);
            JsonObject object = element.getAsJsonObject();
            SocketCommand command = PrimeCore.getInstance().getSocketProvider().commands.getOrDefault(object.get("command").getAsString(), null);
            if(command != null){
                String authUUID = object.get("auth").getAsJsonObject().get("uuid").getAsString();
                String authKey = object.get("auth").getAsJsonObject().get("key").getAsString();
                String hash = PrimeCore.getInstance().getDatabase().select("SELECT password FROM core_web_accounts WHERE player = ?")
                        .parameters(authUUID).getAs(String.class).toBlocking().singleOrDefault(null);
                if(hash == null || authUUID == null){
                    return;
                }
                if(authKey.equals(hash)){
                    PrimeCore.getInstance().getLogger().log(Level.INFO, "Ein " + command.name + " Command wurde von '" + authUUID + "' authentifiziert!");
                    command.execute(session, object);
                }
            }
        }catch (Exception exception){
        }

    }

}
