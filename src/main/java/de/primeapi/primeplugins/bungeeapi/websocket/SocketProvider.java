package de.primeapi.primeplugins.bungeeapi.websocket;

import de.primeapi.primeplugins.bungeeapi.configs.CoreConfig;
import spark.Spark;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Lukas S. PrimeAPI
 * created on 18.05.2021
 * crated for PrimePlugins
 */
public class SocketProvider {

    HashMap<String, SocketCommand> commands = new HashMap<>();

    public SocketProvider(){
        Spark.port(CoreConfig.getInstance().getInt("webinterface.websocket.port"));
        Spark.webSocket("/", WebPanelSocket.class);
        Spark.init();
    }

    public void registerCommand(SocketCommand command){
        commands.put(command.name, command);
    }
}
