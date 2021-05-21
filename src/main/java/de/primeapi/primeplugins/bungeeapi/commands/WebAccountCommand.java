package de.primeapi.primeplugins.bungeeapi.commands;

import de.primeapi.primeplugins.bungeeapi.api.PrimePlayer;
import de.primeapi.primeplugins.bungeeapi.configs.CoreConfig;
import de.primeapi.primeplugins.bungeeapi.managers.messages.CoreMessage;
import de.primeapi.primeplugins.bungeeapi.sql.WebKey;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import javax.xml.soap.Text;

/**
 * @author Lukas S. PrimeAPI
 * created on 16.05.2021
 * crated for PrimePlugins
 */
public class WebAccountCommand extends Command {

    public WebAccountCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(!(commandSender instanceof ProxiedPlayer)) {
            return;
        }
        PrimePlayer p = new PrimePlayer((ProxiedPlayer) commandSender);
        WebKey.fromPlayer(p).submit(oldKey -> {
            if(oldKey != null) oldKey.delete();
            int rank;
            if(p.hasPermission("bungee.webadmin")){
                rank = 100;
            }else {
                rank = 0;
            }
            WebKey webKey = WebKey.createRandom(p, rank).complete();
            if(webKey == null){
                p.sendMessage(CoreMessage.WEBACCOUNT_ERROR);
                return;
            }
            TextComponent component = new TextComponent(
                    CoreMessage.WEBACCOUNT_SUCCESS
                            .replace("key", webKey.getKey().complete())
                    .getContent()
            );
            String url = CoreConfig.getInstance().getString("webinterface.link");
            url += "?key=" + webKey.getKey().complete();
            component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
            p.thePlayer().sendMessage(component);
        });
    }
}
