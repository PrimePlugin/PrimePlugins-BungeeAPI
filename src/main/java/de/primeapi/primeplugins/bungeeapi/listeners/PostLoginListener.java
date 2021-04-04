package de.primeapi.primeplugins.bungeeapi.listeners;

import de.primeapi.primeplugins.bungeeapi.api.PrimePlayer;
import de.primeapi.primeplugins.bungeeapi.sql.SQLPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PostLoginListener implements Listener {

    @EventHandler
    public void onPostLogin(PostLoginEvent e){
        {
            SQLPlayer sqlPlayer = SQLPlayer.create(e.getPlayer().getUniqueId(), e.getPlayer().getName());
            sqlPlayer.updateName(e.getPlayer().getName());
        }
        PrimePlayer p = new PrimePlayer(e.getPlayer());
    }

}
