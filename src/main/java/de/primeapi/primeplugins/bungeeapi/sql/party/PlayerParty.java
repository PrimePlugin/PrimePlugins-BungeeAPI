package de.primeapi.primeplugins.bungeeapi.sql.party;

import de.primeapi.primeplugins.bungeeapi.PrimeCore;
import de.primeapi.primeplugins.bungeeapi.sql.DatabaseTask;
import de.primeapi.primeplugins.bungeeapi.sql.SQLPlayer;
import de.primeapi.primeplugins.bungeeapi.sql.utils.OnlineStats;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class PlayerParty {

    UUID owner;

    public DatabaseTask<List<SQLPlayer>> getPlayers(boolean inludeOwner) {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            List<String> stringList = PrimeCore.getInstance().getDatabase().select(
                    "SELECT uuid FROM prime_bungee_online WHERE party = ?"
            ).parameters(owner.toString())
                    .getAs(String.class)
                    .toList().toBlocking().singleOrDefault(new ArrayList<>());
            return stringList.stream()
                    .filter(s -> {
                        if (!inludeOwner) {
                            return !s.equals(owner.toString());
                        }
                        return true;
                    })
                    .map(s -> new SQLPlayer(UUID.fromString(s)))
                    .collect(Collectors.toList());
        }));
    }

    public void setOwner(UUID uuid) {
        getPlayers(true).submit(list -> list.forEach(primePlayer -> OnlineStats.setParty(primePlayer.retrieveUniqueId().complete(), uuid)));
        owner = uuid;
    }


}
