package de.primeapi.primeplugins.bungeeapi.commands;

import de.primeapi.primeplugins.bungeeapi.api.PrimePlayer;
import de.primeapi.primeplugins.bungeeapi.managers.messages.CoreMessage;
import de.primeapi.primeplugins.bungeeapi.sql.SQLPlayer;
import de.primeapi.primeplugins.bungeeapi.util.PrimeUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

/**
 * @author Jonas D. Exceptionpilot
 * created on 18.11.2021
 * created for PrimePlugins-BungeeAPI
 */

public class PayCommand extends Command {
	public PayCommand(String name) {
		super(name);
	}

	@Override
	public void execute(CommandSender commandSender, String[] args) {
		if (!(commandSender instanceof ProxiedPlayer)) return;
		PrimePlayer primePlayer = new PrimePlayer((ProxiedPlayer) commandSender);

		if (args.length == 0 || args.length == 1 || args.length >= 3) {
			primePlayer.sendMessage(CoreMessage.PAY_USAGE);
			return;
		}
		if (!PrimeUtils.isNumber(args[1])) {
			primePlayer.sendMessage(CoreMessage.PAY_NOT_NUMBER);
			return;
		}

		int amount = Integer.parseInt(args[1]);
		ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);


		if (target == null) {
			primePlayer.sendMessage(CoreMessage.PAY_NOT_ONLINE);
			return;
		}

		if (target.getUniqueId().equals(primePlayer.getUniqueId())) {
			primePlayer.sendMessage(CoreMessage.PAY_NOT_SELF);
			return;
		}

		SQLPlayer.loadPlayerByName(primePlayer.getName()).submit(sqlPlayer -> {
			if (sqlPlayer.retrieveCoins().complete() >= amount) {
				SQLPlayer sqlTarget = SQLPlayer.loadPlayerByName(target.getName()).complete();
				sqlPlayer.removeCoins(amount);
				sqlTarget.addCoins(amount);

				new PrimePlayer(target).sendMessage(CoreMessage.PAY_SUCCESSFULLY_RECEIVER
						                                    .replace("%c%", amount)
						                                    .replace("%p%", primePlayer.getPlayer().getName())
				                                   );
				primePlayer.sendMessage(CoreMessage.PAY_SUCCESSFULLY
						                        .replace("%c%", amount)
						                        .replace("%p%", target.getName())
				                       );
			} else {
				primePlayer.sendMessage(CoreMessage.PAY_NOT_ENOUGH);
			}
		});
	}
}
