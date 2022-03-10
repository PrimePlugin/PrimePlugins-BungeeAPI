package de.primeapi.primeplugins.bungeeapi.api;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public abstract class SubCommand {

	public String permission;

	public abstract boolean execute(PrimePlayer p, String[] args);


	public boolean checkPermission(PrimePlayer p) {
		if (permission == null) return true;
		if (!p.hasPermission(permission)) {
			p.sendNoPerm(getPermission());
			return false;
		}
		return true;
	}

}
