package de.primeapi.primeplugins.bungeeapi.managers;

import de.primeapi.primeplugins.bungeeapi.api.BungeeAPI;
import de.primeapi.primeplugins.bungeeapi.api.PrimePlayer;
import de.primeapi.primeplugins.bungeeapi.sql.utils.OnlineStats;
import net.md_5.bungee.api.ProxyServer;

public class OnMinsCounter implements Runnable {

    @Override
    public void run() {
        ProxyServer.getInstance().getPlayers().forEach(proxiedPlayer -> {
            PrimePlayer p = new PrimePlayer(proxiedPlayer);
            if (BungeeAPI.getInstance().isOnline()) {
                OnlineStats.getAFK(p.getUniqueId()).submit(aBoolean -> {
                    if (!aBoolean) {
                        p.retrieveOnMins().submit(integer -> p.setOnMins(integer + 1));
                    }
                });
            } else {
                p.retrieveOnMins().submit(integer -> p.setOnMins(integer + 1));
            }
        });
    }
}
