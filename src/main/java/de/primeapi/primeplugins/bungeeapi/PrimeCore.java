package de.primeapi.primeplugins.bungeeapi;

import de.primeapi.primeplugins.bungeeapi.api.BungeeAPI;
import de.primeapi.primeplugins.bungeeapi.api.PermsAPI;
import de.primeapi.primeplugins.bungeeapi.commands.PayCommand;
import de.primeapi.primeplugins.bungeeapi.commands.PrimeCoreCommand;
import de.primeapi.primeplugins.bungeeapi.configs.AccessDataConfig;
import de.primeapi.primeplugins.bungeeapi.configs.CoreConfig;
import de.primeapi.primeplugins.bungeeapi.listeners.PluginMessagingListener;
import de.primeapi.primeplugins.bungeeapi.listeners.PostLoginListener;
import de.primeapi.primeplugins.bungeeapi.managers.OnMinsCounter;
import de.primeapi.primeplugins.bungeeapi.managers.commands.CommandsManager;
import de.primeapi.primeplugins.bungeeapi.managers.config.ConfigManager;
import de.primeapi.primeplugins.bungeeapi.managers.messages.MessageManager;
import de.primeapi.primeplugins.bungeeapi.managers.rest.RestManager;
import de.primeapi.primeplugins.bungeeapi.websocket.SocketProvider;
import de.primeapi.primeplugins.bungeeapi.websocket.commands.KickWebsocketCommand;
import de.primeapi.primeplugins.bungeeapi.websocket.commands.SudoWebsocketCommand;
import de.primeapi.util.sql.Database;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

@Getter
public class PrimeCore extends Plugin {

    private static PrimeCore instance;
    public static PrimeCore getInstance() {
        return instance;
    }

    private static boolean loaded = false;

    Connection connection;
    ThreadPoolExecutor threadPoolExecutor;
    ConfigManager configManager;
    CommandsManager commandsManager;
    Database database;
    SocketProvider socketProvider;
    RestManager restManager;
    boolean coins;


    @Override
    public void onEnable() {
        if(loaded){
            getLogger().warning("Abort loading, already loaded");
            return;
        }
        loaded = true;
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
        coins = CoreConfig.getInstance().getBoolean("coins.pay");
        commandsManager = new CommandsManager();
        new MessageManager();
        registerListeners();
        registerCommands();
        ProxyServer.getInstance().getScheduler().schedule(this, new OnMinsCounter(), 1, 1, TimeUnit.MINUTES);
        getProxy().registerChannel("prime:primemessaging");

        try {
            getLogger().info("Starting WebSocket....");
            socketProvider = new SocketProvider();
            socketProvider.registerCommand(new SudoWebsocketCommand());
            socketProvider.registerCommand(new KickWebsocketCommand());
            getLogger().info("Websocket started!");
        }catch (Exception ex){
            getLogger().warning("Error starting Websocket: " + ex.getMessage());
        }
        restManager = new RestManager();
        restManager.registerPlugin(new RestCore(this));

        new BungeeAPI();
        new PermsAPI();
    }

    private void registerListeners() {
        ProxyServer.getInstance().getPluginManager().registerListener(this, new PostLoginListener());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new PluginMessagingListener());
    }

    private void registerCommands() {
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new PrimeCoreCommand("primecorebungee"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new PrimeCoreCommand("bungeeapi"));
        if(coins) {
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new PayCommand("pay"));
        }
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
                connection.prepareStatement("CREATE TABLE IF NOT EXISTS `core_players` (`id` INT NOT NULL AUTO_INCREMENT UNIQUE,`uuid` VARCHAR(36) NOT NULL UNIQUE,`name` VARCHAR(16) NOT NULL UNIQUE,`realname` VARCHAR(16) NOT NULL UNIQUE,`coins` INT NOT NULL,`playtime` INT NOT NULL,PRIMARY KEY (`id`));").execute();
                connection.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS `core_settings` (" +
                                "`id` INT NOT NULL AUTO_INCREMENT UNIQUE," +
                                "`uuid` VARCHAR(36) NOT NULL," +
                                "`setting` VARCHAR(36) NOT NULL," +
                                "`value` INT," +
                                "PRIMARY KEY (`id`));"
                ).execute();
                connection.prepareStatement("CREATE TABLE IF NOT EXISTS `core_web_keys` (`id` INT NOT NULL AUTO_INCREMENT UNIQUE, `player` VARCHAR(36) NOT NULL UNIQUE, `key` VARCHAR(8) NOT NULL, `rank` INT NOT NULL, primary key (`id`))").execute();
                connection.prepareStatement("CREATE TABLE IF NOT EXISTS `core_web_accounts` (`id` INT NOT NULL AUTO_INCREMENT UNIQUE, `player` VARCHAR(36) NOT NULL UNIQUE, `password` VARCHAR(255) NOT NULL, `rank` INT NOT NULL, primary key (`id`))").execute();
                connection.prepareStatement("CREATE TABLE IF NOT EXISTS `core_web_unban` (`id` INT NOT NULL AUTO_INCREMENT UNIQUE, `player` VARCHAR(36) NOT NULL UNIQUE, `type` VARCHAR(64) NOT NULL, `reason` VARCHAR(64) NOT NULL, `lenght` VARCHAR(64) NOT NULL, `message` VARCHAR(1000) NOT NULL, primary key (`id`))").execute();
                connection.prepareStatement(
                                  "CREATE TABLE IF NOT EXISTS `core_playerdata` (`id` INT NOT NULL AUTO_INCREMENT " +
		                                  "UNIQUE, `uuid` VARCHAR(36) NOT NULL, `type` VARCHAR(32) NOT NULL, `value` " +
		                                  "VARCHAR(64), primary key (`id`))")
                          .execute();
                database = new Database(connection);
                getLogger().log(Level.INFO, "Asynchronous MySQL-Connection established");
            } catch (SQLException throwables) {
                getLogger().log(Level.WARNING, "MySQL-Connection failed: " + throwables.getMessage());
            }
    }
}
