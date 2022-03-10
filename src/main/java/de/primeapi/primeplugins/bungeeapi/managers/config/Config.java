package de.primeapi.primeplugins.bungeeapi.managers.config;

import lombok.Getter;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class Config {


	private final File file;
	private final String name;
	private Configuration configuration;

	public Config(String name, String path, String filename) {
		this.name = name;
		file = new File(path, filename);
		file.getParentFile().mkdirs();
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
		try {
			configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		loadContent();
		save();
	}

	public abstract void loadContent();

	public void reload() {
		try {
			configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}


	public void saveAddEntry(String path, Object object) {
		if (!configuration.contains(path)) {
			if (object instanceof String) {
				String s = (String) object;
				s.replaceAll("§", "&");
				configuration.set(path, s);
			} else {
				configuration.set(path, object);
			}
		}
	}

	public void saveAddEntry(String path, List<String> object) {
		if (!configuration.contains(path)) {
			List<String> list = new ArrayList<>();
			for (String s :
					object) {
				list.add(s.replaceAll("§", "&"));
			}
			configuration.set(path, list);
		}
	}

	public void save() {
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, file);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}


	public String getString(String path) {
		return configuration.getString(path);
	}

	public Boolean getBoolean(String path) {
		return configuration.getBoolean(path);
	}

	public Integer getInt(String path) {
		return configuration.getInt(path);
	}

	public List<String> getStringList(String path) {
		return configuration.getStringList(path);
	}

	public List<Integer> getIntegerList(String path) {
		return configuration.getIntList(path);
	}
}
