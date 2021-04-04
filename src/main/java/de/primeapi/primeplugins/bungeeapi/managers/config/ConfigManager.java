package de.primeapi.primeplugins.bungeeapi.managers.config;

import de.primeapi.primeplugins.bungeeapi.PrimeCore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class ConfigManager {

    private List<Config> registeredConfigs;

    public ConfigManager(){
        registeredConfigs = new ArrayList<>();
        {
            File ord = new File("plugins/primeplugins");
            if(!ord.exists()) ord.mkdir();
        }
    }

    public void register(Config config){
        registeredConfigs.add(config);
        PrimeCore.getInstance().getLogger().log(Level.INFO, "Config '" + config.getName() + "' geladen!");
    }

}
