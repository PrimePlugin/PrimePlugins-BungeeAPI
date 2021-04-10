package de.primeapi.primeplugins.bungeeapi;

import de.primeapi.primeplugins.bungeeapi.commands.PrimeCoreCommand;
import de.primeapi.primeplugins.bungeeapi.commands.coins.CoinsCommand;
import de.primeapi.primeplugins.bungeeapi.configs.AccessDataConfig;
import de.primeapi.primeplugins.bungeeapi.configs.CoreConfig;
import de.primeapi.primeplugins.bungeeapi.listeners.PostLoginListener;
import de.primeapi.primeplugins.bungeeapi.managers.commands.CommandsManager;
import de.primeapi.primeplugins.bungeeapi.managers.config.ConfigManager;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;

@Getter
public class PrimeCore extends Plugin {

    private static PrimeCore instance;
    public static PrimeCore getInstance() {
        return instance;
    }

    Connection connection;
    ThreadPoolExecutor threadPoolExecutor;
    ConfigManager configManager;
    CommandsManager commandsManager;


    @Override
    public void onEnable() {
        instance = this;
        getLogger().log(Level.INFO, "---------------[ PrimeAPI | core ]---------------");
        getLogger().log(Level.INFO, "Plugin: PrimeCore");
        getLogger().log(Level.INFO, "Author: PrimeAPI");
        getLogger().log(Level.INFO, "Version: " + getDescription().getVersion());
        getLogger().log(Level.INFO, "---------------[ PrimeAPI | core ]---------------");


        threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        configManager = new ConfigManager();
        registerConfigs();

        registerSQL();
        commandsManager = new CommandsManager();
        registerListeners();
    }

    private void registerListeners() {
        ProxyServer.getInstance().getPluginManager().registerListener(this, new PostLoginListener());
    }

    private void registerCommands() {
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new PrimeCoreCommand("primecorebungee"));
    }


    private void registerConfigs(){
        File ord = new File("plugins/primeplugin/core");
        if(!ord.exists()) ord.mkdir();
        configManager.register(new AccessDataConfig());
        configManager.register(new CoreConfig());
    }

    private void registerSQL(){
            try {
                connection = DriverManager.getConnection("jdbc:mysql://" + AccessDataConfig.getInstance().getString("mysql.host") + "/" + AccessDataConfig.getInstance().getString("mysql.database") + "?autoReconnect=true", AccessDataConfig.getInstance().getString("mysql.username"), AccessDataConfig.getInstance().getString("mysql.password"));
                getLogger().log(Level.INFO, "MySQL-Connection established");
                connection.prepareStatement("CREATE TABLE IF NOT EXISTS `core_players` (`id` INT NOT NULL AUTO_INCREMENT UNIQUE,`uuid` VARCHAR(36) NOT NULL UNIQUE,`name` VARCHAR(16) NOT NULL UNIQUE,`realname` VARCHAR(16) NOT NULL UNIQUE,`coins` INT NOT NULL,PRIMARY KEY (`id`));").execute();
                connection.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS `core_settings` (" +
                                "`id` INT NOT NULL AUTO_INCREMENT UNIQUE," +
                                "`uuid` VARCHAR(36) NOT NULL," +
                                "`setting` VARCHAR(36) NOT NULL," +
                                "`value` INT," +
                                "PRIMARY KEY (`id`));"
                ).execute();
            } catch (SQLException throwables) {
                getLogger().log(Level.WARNING, "MySQL-Connection failed: " + throwables.getMessage());
            }
    }
}
