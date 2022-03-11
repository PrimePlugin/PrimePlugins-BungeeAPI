package de.primeapi.primeplugins.bungeeapi.listeners;

import de.primeapi.primeplugins.bungeeapi.api.Cache;
import de.primeapi.primeplugins.bungeeapi.api.PermissionsPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PermissionCheckEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author Lukas S. PrimeAPI
 * created on 11.03.2022
 * crated for PrimePlugins-ROOT
 */
public class PermissionCheckListener implements Listener {

	public static HashMap<UUID, Cache<String, Boolean>> cacheHashMap = new HashMap<>();

	@EventHandler
	public void onPermissionCheck(PermissionCheckEvent e) {
		if (e.getSender() instanceof ProxiedPlayer) {
			UUID uuid = ((ProxiedPlayer) e.getSender()).getUniqueId();
			if (!cacheHashMap.containsKey(uuid)) {
				cacheHashMap.put(uuid, new Cache<>());
			}
			String s = e.getPermission().toLowerCase();
			Boolean b = cacheHashMap.get(uuid).getCachedValue(s);
			if (b == null) {
				PermissionsPlayer p = new PermissionsPlayer((ProxiedPlayer) e.getSender());
				b = p.hasSelfPermission(e.getPermission().toLowerCase()).complete();
				cacheHashMap.get(uuid).cacheEntry(s, b);
			}
			e.setHasPermission(b);
		}
	}

	@EventHandler
	public void onQuit(PlayerDisconnectEvent e) {
		cacheHashMap.remove(e.getPlayer().getUniqueId());
	}

}
