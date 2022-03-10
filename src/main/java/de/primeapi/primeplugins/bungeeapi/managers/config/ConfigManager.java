package de.primeapi.primeplugins.bungeeapi.managers.config;

import de.primeapi.primeplugins.bungeeapi.PrimeCore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class ConfigManager {

	private final List<Config> registeredConfigs;

	public ConfigManager() {
		registeredConfigs = new ArrayList<>();
		{
			File ord = new File("plugins/primeplugin");
			if (!ord.exists()) ord.mkdir();
		}
	}

	public List<File> getALLFiles() {
		return registeredConfigs.stream().map(Config::getFile).collect(Collectors.toList());
	}

	public void register(Config config) {
		registeredConfigs.add(config);
		PrimeCore.getInstance().getLogger().log(Level.INFO, "Config '" + config.getName() + "' geladen!");
	}
}
