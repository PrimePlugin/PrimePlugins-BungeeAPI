package de.primeapi.primeplugins.bungeeapi.api;

import de.primeapi.primeplugins.bungeeapi.PrimeCore;
import de.primeapi.primeplugins.bungeeapi.sql.DatabaseTask;
import de.primeapi.primeplugins.bungeeapi.sql.permissions.SQLGroup;
import de.primeapi.primeplugins.bungeeapi.sql.permissions.SQLRanking;
import de.primeapi.primeplugins.bungeeapi.sql.permissions.SQLUserPermission;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

/**
 * @author Jonas D. Exceptionpilot
 * created on 29.01.2022
 * created for PrimePlugins-BungeeAPI
 */

public class PermsAPI {

    private static PermsAPI instance;

    public static PermsAPI getInstance() {
        return instance;
    }

    public boolean isOnline() {
        return online;
    }

    boolean online;
    public PermsAPI() {
        instance = this;
        online = false;
        try {
            DatabaseMetaData md = PrimeCore.getInstance().getConnection().getMetaData();
            ResultSet rs = md.getTables(null, null, "prime_perms_groups", null);
            online = rs.next();
            rs.close();
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        if (online) {
            PrimeCore.getInstance().getLogger().log(Level.INFO, " PermsAPI wurde geladen");
        } else {
            PrimeCore.getInstance().getLogger().log(Level.INFO, " PermsAPI wurde NICHT geladen");
        }
    }

    public DatabaseTask<SQLGroup> getHighestGroup(UUID uuid) {
        if (!online) throw new IllegalStateException("PermsAPI was not loaded");
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            List<SQLRanking> list = SQLRanking.fromUser(uuid).complete();
            if (list.size() == 0) {
                return SQLGroup.fromName("default").complete();
            }
            return list.get(0).getGroup().complete();
        }));
    }

    public DatabaseTask<List<String>> getPermissions(UUID uuid) {
        if (!online) throw new IllegalStateException("PermsAPI was not loaded");
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            SQLGroup defaultGroup = SQLGroup.fromName("default").complete();
            List<String> list;
            if (defaultGroup == null) {
                list = new ArrayList<>();
            } else {
                list = new ArrayList<>(defaultGroup.getPermissions().complete());
            }
            for (SQLRanking ranking :
                    SQLRanking.fromUser(uuid).complete()) {
                list.addAll(ranking.getGroup().complete().getPermissions().complete());
            }

            for (SQLUserPermission permission :
                    SQLUserPermission.fromUser(uuid).complete()) {
                if (permission.isNegative().complete()) {
                    list.remove(permission.getPermission().complete());
                } else {
                    list.add(permission.getPermission().complete());
                }
            }
            return list;
        }));
    }
}
