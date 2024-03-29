package de.primeapi.primeplugins.bungeeapi.commands.coins.subcommands;


import de.primeapi.primeplugins.bungeeapi.api.PrimePlayer;
import de.primeapi.primeplugins.bungeeapi.api.SubCommand;
import de.primeapi.primeplugins.bungeeapi.managers.messages.CoreMessage;
import de.primeapi.primeplugins.bungeeapi.sql.SQLPlayer;
import de.primeapi.primeplugins.bungeeapi.util.PrimeUtils;

public class SetSubCommand extends SubCommand {
	public SetSubCommand() {
		super("primecore.coins.set");
	}

	@Override
	public boolean execute(PrimePlayer p, String[] args) {
		if (!checkPermission(p)) {
			return true;
		}
		if (args.length != 3) {
			p.sendMessage(CoreMessage.COINS_SET_USAGE);
			return true;
		}
		int amount;
		try {
			amount = Integer.parseInt(args[2]);
		} catch (Exception ex) {
			p.sendMessage(CoreMessage.COINS_NONUMBER);
			return true;
		}
		SQLPlayer.loadPlayerByName(args[1]).submit(target -> {
			if (target == null) {
				p.sendMessage(CoreMessage.COINS_PLAYERNOTFOUND);
				return;
			}
			target.setCoins(amount);
			p.sendMessage(CoreMessage.COINS_SET_SUCCESS.replace("player", target.retrieveRealName().complete())
			                                           .replace("coins", PrimeUtils.formatInteger(amount)));
		});
		return true;
	}
}
