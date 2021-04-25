package de.primeapi.primeplugins.bungeeapi.managers.messages;

import de.primeapi.primeplugins.bungeeapi.PrimeCore;
import lombok.SneakyThrows;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class MessageManager {

    private Configuration cfg;
    private File file;

    public MessageManager(){
        file = new File("plugins/primeplugin/core/messages.yml");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        reload();
    }


    public void reload(){
        try {
            cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        PrimeCore.getInstance().getThreadPoolExecutor().submit(() -> {
            int i = 0;
            for (CoreMessage message : CoreMessage.values()) {
                if (cfg.contains(message.getPath())) {
                    message.setContent(ChatColor.translateAlternateColorCodes('&', cfg.getString(message.getPath())).replaceAll("%prefix%", CoreMessage.PREFIX.getContent()).replaceAll("<br>", "\n"));
                } else {
                    String s = (message.getPrefix() ? "%prefix%" : "") + message.getContent().replaceAll("§", "&");
                    cfg.set(message.getPath(), s);
                    i++;
                    message.setContent(ChatColor.translateAlternateColorCodes('&', s.replaceAll("%prefix%", CoreMessage.PREFIX.getContent())));
                }
            }
            PrimeCore.getInstance().getLogger().log(Level.INFO, "Es wurde(n) " + i + " neue Nachricht(en) in die messages.yml eingefügt!");
            try {
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(cfg, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
