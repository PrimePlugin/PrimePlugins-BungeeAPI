package de.primeapi.primeplugins.bungeeapi.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import de.primeapi.primeplugins.bungeeapi.PrimeCore;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Lukas S. PrimeAPI
 * created on 14.05.2021
 * crated for PrimePlugins
 */
public class PluginMessagingListener implements Listener {
    List<String> prev = new ArrayList<>();

    @EventHandler
    public void onPluginMessage(PluginMessageEvent e){
        try {
            String tag = e.getTag();
            if (!tag.equalsIgnoreCase("primemessaging")) {
                return;
            }
            ByteArrayDataInput in = ByteStreams.newDataInput(e.getData());
            String subChannel = in.readUTF();
            if (subChannel.equalsIgnoreCase("sudoPlayer")) {
                String name = in.readUTF();
                String command = in.readUTF();

                ProxiedPlayer t = ProxyServer.getInstance().getPlayer(name);
                if(prev.contains(name+command)) return;
                if (t != null){
                    ProxyServer.getInstance().getPluginManager().dispatchCommand(t, command);
                    prev.add(name + command);
                    ProxyServer.getInstance().getScheduler().schedule(PrimeCore.getInstance(), () -> {
                        prev.remove(name+command);
                    }, 3, TimeUnit.SECONDS);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
