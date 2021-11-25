package de.primeapi.primeplugins.bungeeapi.commands;

import de.primeapi.primeplugins.bungeeapi.PrimeCore;
import de.primeapi.primeplugins.bungeeapi.api.PrimePlayer;
import de.primeapi.primeplugins.bungeeapi.api.RestPlugin;
import de.primeapi.primeplugins.bungeeapi.managers.rest.PluginInfo;
import net.md_5.bungee.api.CommandSender;
import de.primeapi.primeplugins.bungeeapi.api.debugmessage.DebugMessage;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.io.File;
import java.net.URISyntaxException;

public class PrimeCoreCommand extends Command {
    public PrimeCoreCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(args.length == 0) {
            commandSender.sendMessage("PrimeCore(Bungee) v" + PrimeCore.getInstance().getDescription().getVersion() + "!");
            commandSender.sendMessage("Author: PrimeAPI (https://primeapi.de)");
            commandSender.sendMessage("Verwende: /bungeeapi update");
        }else {
            PrimePlayer p = new PrimePlayer((ProxiedPlayer) commandSender);
            if(args[0].equalsIgnoreCase("debug")){
                // /priemcore debug <secret>
                if(args.length < 2){
                    p.thePlayer().sendMessage("§7Verwende: §e/primecore debug <Secret>");
                    return;
                }
                p.thePlayer().sendMessage("§7Sende Daten...");
                DebugMessage.send(args[1], p.thePlayer());
                return;
            }
            if (args[0].equalsIgnoreCase("update")) {
                if(!p.checkPermission("primeplugins.update")){
                    return;
                }
                if (args.length < 2) {
                    p.thePlayer().sendMessage("§8[§cBungeeAPI§8] §7Benutze: §e/spigotapi update <all/[PluginName]>");
                    p.thePlayer().sendMessage("§8[§cBungeeAPI§8] §ePluginName §8| §7Aktuelle Version §8| §bNeueste Version ");
                    for (RestPlugin plugin : PrimeCore.getInstance().getRestManager().getPlugins()) {
                        PluginInfo info = plugin.getPluginInfo();
                        String currVersion = plugin.getPlugin().getDescription().getVersion();
                        if (info.isNeverVersion(currVersion)) {
                            p.thePlayer().sendMessage("§8[§cBungeeAPI§8] §e" + plugin.getName() + " §8| §7" + currVersion + "§8 | §b" + info.getVersion() + " §c§l✖");
                        } else {
                            p.thePlayer().sendMessage("§8[§cBungeeAPI§8] §e" + plugin.getName() + " §8| §7" + currVersion + "§8 | §b" + info.getVersion() + " §a§l✔");
                        }
                    }
                    return;
                }
                if(args[1].equalsIgnoreCase("all")){
                    for (RestPlugin plugin : PrimeCore.getInstance().getRestManager().getPlugins()) {
                        p.thePlayer().sendMessage("§8[§cBungeeAPI§8] §7Installiere §e" + plugin.getName() + "§7...");
                        try {
                            File f = new File(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
                            plugin.downloadLatestVersion(f.getPath());
                            p.thePlayer().sendMessage("§8[§cBungeeAPI§8] §e" + plugin.getName() + "§7 wurde §aerfolgreich §7in die Datein §e" + f.getName() + "§7 runtergeladen!");
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                            p.thePlayer().sendMessage("§8[§cBungeeAPI§8] §e" + plugin.getName() + "§7 konnte §cnicht §7herruntergeladen werden: §c" + e.getMessage());
                        }
                    }
                    p.thePlayer().sendMessage("§8[§cSpigotAPI§8] §7Das Updaten aller Plugins wurde §aabgeschlossen!");

                    return;
                }else {
                    PrimeCore.getInstance().getRestManager().getPlugins().stream().filter(plugin -> plugin.getName().equalsIgnoreCase(args[1]))
                            .forEach(plugin -> {
                                p.thePlayer().sendMessage("§8[§cBungeeAPI§8] §7Installiere §e" + plugin.getName() + "§7...");
                                try {
                                    File f = new File(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
                                    plugin.downloadLatestVersion(f.getPath());
                                    p.thePlayer().sendMessage("§8[§cBungeeAPI§8] §e" + plugin.getName() + "§7 wurde §aerfolgreich §7in die Datein §e" + f.getName() + "§7 runtergeladen!");
                                } catch (URISyntaxException e) {
                                    e.printStackTrace();
                                    p.thePlayer().sendMessage("§8[§cBungeeAPI§8] §e" + plugin.getName() + "§7 konnte §cnicht §7herruntergeladen werden: §c" + e.getMessage());
                                }
                            });
                }
            }
        }
    }
}
