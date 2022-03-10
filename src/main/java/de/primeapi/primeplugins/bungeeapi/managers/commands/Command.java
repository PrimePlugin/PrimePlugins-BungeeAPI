package de.primeapi.primeplugins.bungeeapi.managers.commands;

import de.primeapi.primeplugins.bungeeapi.commands.WebAccountCommand;
import de.primeapi.primeplugins.bungeeapi.commands.coins.CoinsCommand;
import lombok.Getter;

@Getter
public enum Command {
	COINS("coins", new CoinsCommand("coins")),
	WEBACCOUNT("webaccount", new WebAccountCommand("webaccount")),
	;

	String name;
	net.md_5.bungee.api.plugin.Command command;

	Command(String name, net.md_5.bungee.api.plugin.Command command) {
		this.name = name;
		this.command = command;
	}
}
