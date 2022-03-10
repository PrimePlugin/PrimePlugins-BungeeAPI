package de.primeapi.primeplugins.bungeeapi.api.debugmessage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class DebugPluginInfo {

	String name;
	String version;
	String author;

	public static List<DebugPluginInfo> getPluginInfos() {
		return ProxyServer.getInstance().getPluginManager().getPlugins().stream()
		                  .filter(plugin -> !plugin.getDescription().getAuthor().contains("PrimeAPI"))
		                  .map(plugin -> {
			                  try {
				                  return new DebugPluginInfo(
						                  plugin.getDescription().getName(),
						                  plugin.getDescription().getVersion(),
						                  plugin.getDescription().getAuthor()
				                  );
			                  } catch (Exception ex) {
				                  return null;
			                  }
		                  })
		                  .collect(Collectors.toList())
				;
	}


}
