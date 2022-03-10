package de.primeapi.primeplugins.bungeeapi.websocket;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import org.eclipse.jetty.websocket.api.Session;

/**
 * @author Lukas S. PrimeAPI
 * created on 18.05.2021
 * crated for PrimePlugins
 */
@AllArgsConstructor
public abstract class SocketCommand {

	String name;

	public abstract void execute(Session sender, JsonObject data);
}
