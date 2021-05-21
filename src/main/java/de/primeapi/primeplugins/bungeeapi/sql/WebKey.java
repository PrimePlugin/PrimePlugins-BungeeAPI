package de.primeapi.primeplugins.bungeeapi.sql;

import de.primeapi.primeplugins.bungeeapi.PrimeCore;
import de.primeapi.primeplugins.bungeeapi.api.PrimePlayer;
import de.primeapi.primeplugins.bungeeapi.util.RandomString;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.security.SecureRandom;
import java.util.concurrent.CompletableFuture;

/**
 * @author Lukas S. PrimeAPI
 * created on 16.05.2021
 * crated for PrimePlugins
 */
@RequiredArgsConstructor @Getter
public class WebKey {
    final int id;


    public static DatabaseTask<WebKey> createRandom(SQLPlayer player, Integer rank){
        String key = new RandomString(8, new SecureRandom(), RandomString.upper).nextString();
        return create(player, key, rank);
    }

    public static DatabaseTask<WebKey> fromPlayer(SQLPlayer player){
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() ->
                PrimeCore.getInstance().getDatabase().select("SELECT id FROM core_web_keys WHERE player = ?")
                .parameters(player.retrieveUniqueId().complete().toString())
                .getAs(Integer.class)
                .map(integer -> integer == null ? null : new WebKey(integer))
                .toBlocking().singleOrDefault(null)));
    }

    public static DatabaseTask<WebKey> create(SQLPlayer player, String key, Integer rank){
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() ->
                PrimeCore.getInstance().getDatabase().update("INSERT INTO core_web_keys values (id,?,?,?)")
                .parameters(player.retrieveUniqueId().complete().toString(), key, rank)
                .returnGeneratedKeys()
                .getAs(Integer.class)
                .map(integer -> integer == null ? null : new WebKey(integer))
                .toBlocking().singleOrDefault(null)));
    }

    public DatabaseTask<String> getKey(){
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() ->
                PrimeCore.getInstance().getDatabase().select("SELECT `key` FROM core_web_keys WHERE id=?")
                .parameters(id)
                .getAs(String.class)
                .toBlocking()
                .singleOrDefault(null)
        ));
    }

    public void delete(){
        PrimeCore.getInstance().getDatabase().update("DELETE FROM core_web_keys WHERE id =?")
                .parameters(id)
                .execute();
    }
}
