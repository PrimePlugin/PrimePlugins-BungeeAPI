package de.primeapi.primeplugins.bungeeapi.managers.commands;


import de.primeapi.primeplugins.bungeeapi.PrimeCore;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

public class CommandsManager {


    public ArrayList<Command> registeredCommands = new ArrayList<>();

    public CommandsManager() {
        File file = new File("plugins/primeplugin/core", "commands.yml");
        file.getParentFile().mkdirs();
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Configuration cfg = null;
        try {
            cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        String active = "";
        int activeCount = 0;
        String deactive = "";
        int deactiveCount = 0;

        for (Command c : Command.values()) {
            boolean b;

            if (cfg.contains("commands." + c.getName())) {
                b = cfg.getBoolean("commands." + c.getName());
            } else {
                b = true;
                PrimeCore.getInstance().getLogger().log(Level.INFO, "Command '" + c.getName() + "' wurde eingetragen!");
                cfg.set("commands." + c.getName(), true);
            }

            if (b) {
                ProxyServer.getInstance().getPluginManager().registerCommand(PrimeCore.getInstance(), c.getCommand());
                registeredCommands.add(c);
                active += c.getName() + ", ";
                activeCount++;
            } else {
                deactive += c.getName() + ", ";
                deactiveCount++;
            }
        }
        PrimeCore.getInstance().getLogger().log(Level.INFO, "Aktivierte Commands (" + activeCount + "): " + active);
        PrimeCore.getInstance().getLogger().log(Level.INFO, "Deaktivierte Commands (" + deactiveCount + "): " + deactive);
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(cfg, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
