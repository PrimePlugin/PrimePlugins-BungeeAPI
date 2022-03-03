package de.primeapi.primeplugins.bungeeapi.listeners;

import de.primeapi.primeplugins.bungeeapi.PrimeCore;
import de.primeapi.primeplugins.bungeeapi.api.PrimePlayer;
import de.primeapi.primeplugins.bungeeapi.api.RestPlugin;
import de.primeapi.primeplugins.bungeeapi.enums.PlayerData;
import de.primeapi.primeplugins.bungeeapi.sql.SQLPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.List;

public class PostLoginListener implements Listener {

    private boolean update = false;
    private String msg = "";

    @EventHandler
    public void onPostLogin(PostLoginEvent e) {
        {
            SQLPlayer.create(e.getPlayer().getUniqueId(), e.getPlayer().getName()).submit(player -> {
                player.updateName(e.getPlayer().getName());
            });
        }
        PrimePlayer p = new PrimePlayer(e.getPlayer());

        p.setData(PlayerData.LAST_LOGIN, String.valueOf(System.currentTimeMillis()));
        p.setData(PlayerData.IP_ADDRESS, String.valueOf(e.getPlayer().getAddress().getAddress().getHostAddress()));

        p.retrieveData(PlayerData.FIRST_LOGIN).submit(s -> {
            if (s == null) {
                p.setData(PlayerData.FIRST_LOGIN, String.valueOf(System.currentTimeMillis()));
            }
        });

        if (!PrimeCore.getInstance().getRestManager().isChecked()) {
            List<String> updates = new ArrayList<>();
            for (RestPlugin plugin : PrimeCore.getInstance().getRestManager().getPlugins()) {
                if(plugin != null){
                    if (plugin.isNewUpdateAvailable()) {
                        update = true;
                        updates.add(plugin.getName());
                    }
                }
            }

            if (updates.size() >= 1) {
                update = true;
                msg = "§8[§c§lCoreAPI§8] §eFür folgende Plugins ist ein update verfügbar: ";
                for (String s : updates) {
                    msg += "§b" + s + "§e, ";
                }
                msg += "\n" + "§8[§c§lCoreAPI§8] §7Verwende: /bungeeapi update <all|[pluginName]>";
            }
            PrimeCore.getInstance().getRestManager().setChecked(true);
        }

        if (update && p.hasPermission("primeplugins.update")) {
            e.getPlayer().sendMessage(msg);
        }
    }

}
