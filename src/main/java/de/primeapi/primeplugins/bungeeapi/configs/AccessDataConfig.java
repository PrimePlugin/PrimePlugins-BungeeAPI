package de.primeapi.primeplugins.bungeeapi.configs;


import de.primeapi.primeplugins.bungeeapi.managers.config.Config;

public class AccessDataConfig extends Config {

    private static AccessDataConfig instance;

    public static AccessDataConfig getInstance() {
        return instance;
    }

    public AccessDataConfig() {
        super("Zugansgdaten", "plugins/primeplugin", "config.yml");
        instance = this;
    }

    @Override
    public void loadContent() {
        saveAddEntry("mysql.host", "localhost:3306");
        saveAddEntry("mysql.database", "primeplugin");
        saveAddEntry("mysql.username", "root");
        saveAddEntry("mysql.password", "password");
    }
}
