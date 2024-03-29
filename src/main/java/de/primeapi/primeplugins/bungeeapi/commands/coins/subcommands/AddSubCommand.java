package de.primeapi.primeplugins.bungeeapi.commands.coins.subcommands;

import de.primeapi.primeplugins.bungeeapi.api.PrimePlayer;
import de.primeapi.primeplugins.bungeeapi.api.SubCommand;
import de.primeapi.primeplugins.bungeeapi.managers.messages.CoreMessage;
import de.primeapi.primeplugins.bungeeapi.sql.SQLPlayer;
import de.primeapi.primeplugins.bungeeapi.util.PrimeUtils;


public class AddSubCommand extends SubCommand {

	public AddSubCommand() {
		super("primecore.coins.add");
	}

	@Override
	public boolean execute(PrimePlayer p, String[] args) {

		if (!checkPermission(p)) {
			return true;
		}
		if (args.length != 3) {
			p.sendMessage(CoreMessage.COINS_ADD_USAGE);
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
			target.addCoins(amount);
			p.sendMessage(CoreMessage.COINS_ADD_SUCCESS.replace("player", target.retrieveRealName().complete())
			                                           .replace("coins", PrimeUtils.formatInteger(amount)));
		});
		return true;
	}
}
