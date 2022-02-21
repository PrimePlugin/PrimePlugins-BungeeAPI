package de.primeapi.primeplugins.bungeeapi.api;

import de.primeapi.primeplugins.bungeeapi.PrimeCore;
import de.primeapi.primeplugins.bungeeapi.managers.rest.PluginInfo;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * @author Lukas S. PrimeAPI
 * created on 31.05.2021
 * crated for PrimePlugins
 */
@Getter
public class RestPlugin {

    private final String name;
    public Plugin plugin;
    @Setter
    public String license = "";

    public RestPlugin(String name, Plugin plugin) {
        this.name = name;
        this.plugin = plugin;
        PrimeCore.getInstance().getRestManager().registerPlugin(this);
    }

    public boolean isNewUpdateAvailable(){
        try {
            PluginInfo pluginInfo = PrimeCore
                    .getInstance()
                    .getRestManager()
                    .getPlugininfo(name);
            return pluginInfo
                    .isNeverVersion(
                            plugin.getDescription().getVersion()
                                   );
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    public void downloadLatestVersion(String path){
        PrimeCore.getInstance().getRestManager().downloadPlugin(getPluginInfo(), license, path);
    }

    public PluginInfo getPluginInfo(){
        return PrimeCore.getInstance().getRestManager().getPlugininfo(name);
    }


}
