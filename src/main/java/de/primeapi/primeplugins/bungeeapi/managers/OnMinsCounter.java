package de.primeapi.primeplugins.bungeeapi.managers;

import de.primeapi.primeplugins.bungeeapi.api.PrimePlayer;
import net.md_5.bungee.api.ProxyServer;

public class OnMinsCounter implements Runnable {

    @Override
    public void run() {
        ProxyServer.getInstance().getPlayers().forEach(proxiedPlayer -> {
            PrimePlayer p = new PrimePlayer(proxiedPlayer);
            p.retrieveOnMins().submit(integer -> p.setOnMins(integer + 1));
        });
    }
}
