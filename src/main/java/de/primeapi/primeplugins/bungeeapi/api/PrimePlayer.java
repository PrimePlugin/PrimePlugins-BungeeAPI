package de.primeapi.primeplugins.bungeeapi.api;


import de.primeapi.primeplugins.bungeeapi.managers.messages.CoreMessage;
import de.primeapi.primeplugins.bungeeapi.sql.SQLPlayer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.awt.*;
import java.util.UUID;

public class PrimePlayer extends SQLPlayer {

    final ProxiedPlayer p;

    public PrimePlayer(ProxiedPlayer player) {
        super(player.getUniqueId());
        this.p = player;
    }

    public ProxiedPlayer getPlayer(){ return p;}
    public ProxiedPlayer thePlayer(){ return p;}

    public void sendMessage(CoreMessage message){
        thePlayer().sendMessage(TextComponent.fromLegacyText(message.getContent()));
    }

    public void sendNoPerm(String permission){
        sendMessage(CoreMessage.NO_PERMS.replace("permission", permission.toLowerCase()));
    }


    public UUID getUniqueId(){
        return thePlayer().getUniqueId();
    }

    public String getName() {
        return thePlayer().getName();
    }

    public String getRealName() {
        return thePlayer().getName();
    }

    public boolean hasPermission(String permission){
        return thePlayer().hasPermission(permission);
    }

    public boolean checkPermission(String permission){
        if(!thePlayer().hasPermission(permission)){
            sendNoPerm(permission);
            return false;
        }else{
            return true;
        }
    }

}
