package de.primeapi.primeplugins.bungeeapi.websocket.commands;

import com.google.gson.JsonObject;
import de.primeapi.primeplugins.bungeeapi.websocket.SocketCommand;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.eclipse.jetty.websocket.api.Session;

import java.util.UUID;

/**
 * @author Lukas S. PrimeAPI
 * created on 18.05.2021
 * crated for PrimePlugins
 */
public class SudoWebsocketCommand extends SocketCommand {
    public SudoWebsocketCommand() {
        super("sudo");
    }

    @Override
    public void execute(Session sender, JsonObject data) {
        try {
        String playerUUID = data.get("player").getAsString();
        String command = data.get("message").getAsString();
        ProxiedPlayer p = ProxyServer.getInstance().getPlayer(UUID.fromString(playerUUID));
        if(p == null) return;
        ProxyServer.getInstance().getPluginManager().dispatchCommand(p, command);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
