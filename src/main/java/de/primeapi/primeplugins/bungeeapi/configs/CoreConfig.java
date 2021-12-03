package de.primeapi.primeplugins.bungeeapi.configs;


import de.primeapi.primeplugins.bungeeapi.managers.config.Config;
import de.primeapi.primeplugins.bungeeapi.util.RandomString;

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
        saveAddEntry("webinterface.link", "https://server.de/cp");
        saveAddEntry("webinterface.websocket.socketKey", new RandomString().nextString());
        saveAddEntry("webinterface.websocket.port", 8081);
        saveAddEntry("coins.pay", true);
    }
}
