package de.primeapi.primeplugins.bungeeapi.configs;


import de.primeapi.primeplugins.bungeeapi.managers.config.Config;

public class CoreConfig extends Config {

    private static CoreConfig instance;

    public static CoreConfig getInstance() {
        return instance;
    }

    public CoreConfig() {
        super("Core-Config", "plugins/primeplugin/core","config.yml");
        instance = this;
    }

    @Override
    public void loadContent() {
        saveAddEntry("settings.coins.startAmount", 1000);
    }
}
