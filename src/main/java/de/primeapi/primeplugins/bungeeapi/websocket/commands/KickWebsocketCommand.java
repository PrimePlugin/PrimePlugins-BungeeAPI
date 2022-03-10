package de.primeapi.primeplugins.bungeeapi.websocket.commands;

import com.google.gson.JsonObject;
import de.primeapi.primeplugins.bungeeapi.managers.messages.CoreMessage;
import de.primeapi.primeplugins.bungeeapi.sql.SQLPlayer;
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
public class KickWebsocketCommand extends SocketCommand {
	public KickWebsocketCommand() {
		super("kick");
	}

	@Override
	public void execute(Session sender, JsonObject data) {
		try {
			String playerUUID = data.get("player").getAsString();
			ProxiedPlayer p = ProxyServer.getInstance().getPlayer(UUID.fromString(playerUUID));
			if (p == null) return;
			SQLPlayer issuer = new SQLPlayer(
					UUID.fromString(data.get("auth").getAsJsonObject().get("uuid").getAsString()));
			p.disconnect(
					CoreMessage.KICK_WEB.replace("name", issuer.retrieveRealName().complete().replace("<br>", "\n"))
					                    .getContent());
		} catch (Exception ex) {

		}
	}
}
