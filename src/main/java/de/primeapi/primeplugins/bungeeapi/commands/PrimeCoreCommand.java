package de.primeapi.primeplugins.bungeeapi.commands;

import de.primeapi.primeplugins.bungeeapi.PrimeCore;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class PrimeCoreCommand extends Command {
    public PrimeCoreCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        commandSender.sendMessage("PrimeCore(Bungee) v" + PrimeCore.getInstance().getDescription().getVersion());
        commandSender.sendMessage("Author: PrimeAPI (https://primeapi.de)");
    }
}
