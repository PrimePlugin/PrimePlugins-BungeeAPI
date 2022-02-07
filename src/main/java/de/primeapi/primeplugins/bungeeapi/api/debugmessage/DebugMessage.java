package de.primeapi.primeplugins.bungeeapi.api.debugmessage;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import de.primeapi.primeplugins.bungeeapi.PrimeCore;
import de.primeapi.primeplugins.bungeeapi.configs.AccessDataConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor @AllArgsConstructor @Getter
public class DebugMessage {

    final String secret;
    List<PrimePluginInfo> primePlugins;
    List<DebugPluginInfo> plugins;
    boolean mysql;
    String mysqlDatabase;
    boolean bungeeAPI;
    boolean clanAPI;
    boolean coinsAPI;
    boolean friendsAPI;
    boolean permsAPI;
    String ipAdress;
    String serverName;
    String serverVersion;
    String javaVersion;


    public static DebugMessage send(String secret, ProxiedPlayer sender){
        List<PrimePluginInfo> primePlugins = PrimePluginInfo.getPlugins();
        List<DebugPluginInfo> plugins = DebugPluginInfo.getPluginInfos();
        boolean mysql = PrimeCore.getInstance().getConnection() != null;
        String mysqlDatabase = AccessDataConfig.getInstance().getString("mysql.database");
        String ipAdress = null;
        try {
            ipAdress = InetAddress.getLocalHost().toString();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        String servername = "BungeeCord :cloud:";
        String serverVersion = ProxyServer.getInstance().getVersion();
        String javaVersion = System.getProperty("java.version");
        DebugMessage message = new DebugMessage(
                secret,
                primePlugins,
                plugins,
                mysql,
                mysqlDatabase,
                false,
                false,
                false,
                false,
                false,
                ipAdress,
                servername,
                serverVersion,
                javaVersion
        );
        Gson gson = new Gson();
        String json = gson.toJson(message);
        HttpResponse<String> s = null;
        try {
            s = Unirest.post("https://api.primeapi.de/debugs/" + secret)
                    .header("Authorization", sender.getUniqueId().toString())
                    .body(json)
                    .asString();


            Collection<File> files = new ArrayList<>();
            File file = new File("proxy.log.0");
            if(file.exists()){
                files.add(file);
            }else {
                files.add(new File("logs/latest.log"));
            }
            files.addAll(PrimeCore.getInstance().getConfigManager().getALLFiles());

            HttpResponse<String> fileResponse = Unirest.post("https://api.primeapi.de/debugs/" + secret + "/files")
                    .header("Authorization", sender.getUniqueId().toString()).field("files", files).asString();

            if(s.getStatus() == 200 && fileResponse.getStatus() == 200){
                sender.sendMessage("§aErfolgreich!");
            }else {
                sender.sendMessage("§4Fehler§7: §c" + s.getBody() + "§7 | §c" + fileResponse.getBody());
            }

        } catch (UnirestException e) {
            e.printStackTrace();
            sender.sendMessage("Fehler");
        }

        return message;
    }


}
