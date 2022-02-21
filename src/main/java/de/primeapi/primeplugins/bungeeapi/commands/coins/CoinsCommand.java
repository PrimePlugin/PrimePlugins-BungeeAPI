package de.primeapi.primeplugins.bungeeapi.commands.coins;

import de.primeapi.primeplugins.bungeeapi.api.PrimePlayer;
import de.primeapi.primeplugins.bungeeapi.commands.coins.subcommands.AddSubCommand;
import de.primeapi.primeplugins.bungeeapi.commands.coins.subcommands.RemoveSubCommand;
import de.primeapi.primeplugins.bungeeapi.commands.coins.subcommands.SeeSubCommand;
import de.primeapi.primeplugins.bungeeapi.commands.coins.subcommands.SetSubCommand;
import de.primeapi.primeplugins.bungeeapi.managers.messages.CoreMessage;
import de.primeapi.primeplugins.bungeeapi.util.PrimeUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CoinsCommand extends Command {
    public CoinsCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof ProxiedPlayer)) {
            return;
        }
        PrimePlayer p = new PrimePlayer((ProxiedPlayer) commandSender);
        if (args.length == 0) {
            p.retrieveCoins().submit(integer -> p.sendMessage(CoreMessage.COINS_AMOUNT.replace("coins", PrimeUtils.formatInteger(integer))));
            return;
        }
        switch (args[0].toLowerCase()) {
            case "add": {
                new AddSubCommand().execute(p, args);
                return;
            }
            case "set": {
                new SetSubCommand().execute(p, args);
                return;
            }
            case "remove": {
                new RemoveSubCommand().execute(p, args);
                return;
            }
            case "see":
            case "get": {
                new SeeSubCommand().execute(p, args);
                return;
            }
            default:
                p.sendMessage(CoreMessage.COINS_USAGE);
        }
        return;
    }
}
