package de.primeapi.primeplugins.bungeeapi.api.debugmessage;

import de.primeapi.primeplugins.bungeeapi.PrimeCore;
import de.primeapi.primeplugins.bungeeapi.configs.CoreConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@AllArgsConstructor @Getter @Setter
public class PrimePluginInfo {
    static {
        registeredPlugins = new ArrayList<>();
        registerPlugin(
                CompletableFuture.supplyAsync(() -> {
                    String name = "SpigotCore";
                    String version = PrimeCore.getInstance().getDescription().getVersion();
                    HashMap<String, Object> properties = new HashMap<>();
                    properties.put("Webinterface-Link", CoreConfig.getInstance().getString("webinterface.link"));
                    properties.put("Websocket Port", CoreConfig.getInstance().getInt("webinterface.websocket.port"));
                    return new PrimePluginInfo(name, null, version, properties);
                })
        );
    }

    private static ArrayList<CompletableFuture<PrimePluginInfo>> registeredPlugins;

    public static void registerPlugin(CompletableFuture<PrimePluginInfo> plugin){
        registeredPlugins.add(plugin);
    }

    public static List<PrimePluginInfo> getPlugins(){
        return registeredPlugins.stream().map(CompletableFuture::join).collect(Collectors.toList());
    }


    String name;
    String license;
    String version;
    HashMap<String, Object> properties;

}
