package de.primeapi.primeplugins.bungeeapi.sql;

import de.primeapi.primeplugins.bungeeapi.PrimeCore;
import de.primeapi.primeplugins.bungeeapi.util.RandomString;
import de.primeapi.util.sql.queries.Retriever;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.security.SecureRandom;

/**
 * @author Lukas S. PrimeAPI
 * created on 16.05.2021
 * crated for PrimePlugins
 */
@RequiredArgsConstructor
@Getter
public class WebKey {
	final int id;


	public static Retriever<WebKey> createRandom(SQLPlayer player, Integer rank) {
		String key = new RandomString(8, new SecureRandom(), RandomString.upper).nextString();
		return create(player, key, rank);
	}

	public static Retriever<WebKey> fromPlayer(SQLPlayer player) {
		return PrimeCore.getInstance().getDatabase().select("SELECT id FROM core_web_keys WHERE player = ?")
		                .parameters(player.retrieveUniqueId().complete().toString())
		                .execute(Integer.class)
		                .get()
		                .map(integer -> integer == null ? null : new WebKey(integer));
	}

	public static Retriever<WebKey> create(SQLPlayer player, String key, Integer rank) {
		return PrimeCore.getInstance().getDatabase().update("INSERT INTO core_web_keys values (id,?,?,?)")
		                .parameters(player.retrieveUniqueId().complete().toString(), key, rank)
		                .returnGeneratedKeys(Integer.class)
		                .get()
		                .map(integer -> integer == null ? null : new WebKey(integer));
	}

	public Retriever<String> getKey() {
		return PrimeCore.getInstance()
		                .getDatabase()
		                .select("SELECT `key` FROM core_web_keys WHERE id=?")
		                .parameters(id)
		                .execute(String.class)
		                .get();
	}

	public void delete() {
		PrimeCore.getInstance().getDatabase().update("DELETE FROM core_web_keys WHERE id =?")
		         .parameters(id)
		         .execute();
	}
}
