package de.primeapi.primeplugins.bungeeapi.api;


import de.primeapi.primeplugins.bungeeapi.sql.DatabaseTask;
import de.primeapi.primeplugins.bungeeapi.sql.permissions.SQLGroup;
import de.primeapi.primeplugins.bungeeapi.sql.permissions.SQLRanking;
import de.primeapi.primeplugins.bungeeapi.sql.permissions.SQLUserPermission;
import de.primeapi.util.sql.queries.Retriever;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PermissionsPlayer extends PrimePlayer {
	public PermissionsPlayer(ProxiedPlayer player) {
		super(player);
	}


	public Retriever<SQLGroup> getHighestGroup() {
		return new Retriever<>(() -> {
			List<SQLRanking> list = SQLRanking.fromUser(getUniqueId()).complete();
			if (list.size() == 0) {
				return SQLGroup.fromName("default").complete();
			}
			return list.get(0).getGroup().complete();
		});
	}

	public Retriever<List<String>> getPermissions() {
		return new Retriever<>(() -> {
			SQLGroup defaultGroup = SQLGroup.fromName("default").complete();
			List<String> list;
			if (defaultGroup == null) {
				list = new ArrayList<>();
			} else {
				list = new ArrayList<>(defaultGroup.getPermissions().complete());
			}
			for (SQLRanking ranking :
					SQLRanking.fromUser(getUniqueId()).complete()) {
				list.addAll(ranking.getGroup().complete().getPermissions().complete());
			}

			for (SQLUserPermission permission :
					SQLUserPermission.fromUser(getUniqueId()).complete()) {
				if (permission.isNegative().complete()) {
					list.remove(permission.getPermission().complete());
				} else {
					list.add(permission.getPermission().complete());
				}
			}
			return list;
		});
	}

	public Retriever<Boolean> hasSelfPermission(String permission) {
		return new Retriever<>(() -> {
			List<String> list = getPermissions().complete();
			if (list.contains("*")) {
				return true;
			}

			if (list.contains(permission)) {
				return true;
			}

			if (!permission.contains(".")) {
				return list.contains(permission);
			}


			String perm[] = permission.toLowerCase().split("\\.");
			String splitted = perm[0];
			for (int i = 1; i < perm.length; i++) {
				if (list.contains(splitted + ".*")) {
					return true;
				}
				splitted += "." + perm[i];
			}


			return false;
		});
	}


}
