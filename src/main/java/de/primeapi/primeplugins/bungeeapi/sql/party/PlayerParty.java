package de.primeapi.primeplugins.bungeeapi.sql.party;

import de.primeapi.primeplugins.bungeeapi.PrimeCore;
import de.primeapi.primeplugins.bungeeapi.sql.SQLPlayer;
import de.primeapi.primeplugins.bungeeapi.sql.utils.OnlineStats;
import de.primeapi.util.sql.queries.Retriever;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class PlayerParty {

    UUID owner;

    public Retriever<List<SQLPlayer>> getPlayers(boolean inludeOwner) {
        return PrimeCore.getInstance().getDatabase().select(
                                "SELECT uuid FROM prime_bungee_online WHERE party = ?"
                                                           ).parameters(owner.toString())
                        .execute(String.class)
                        .getAsSet()
                        .map(strings -> strings.stream().map(s -> new SQLPlayer(UUID.fromString(s))).collect(
                                Collectors.toList()));
    }

    public void setOwner(UUID uuid) {
        getPlayers(true).submit(list -> list.forEach(
                primePlayer -> OnlineStats.setParty(primePlayer.retrieveUniqueId().complete(), uuid)));
        owner = uuid;
    }


}
