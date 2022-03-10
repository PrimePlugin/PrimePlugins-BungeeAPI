package de.primeapi.primeplugins.bungeeapi.sql.utils;

import de.primeapi.primeplugins.bungeeapi.PrimeCore;
import de.primeapi.primeplugins.bungeeapi.sql.party.PlayerParty;
import de.primeapi.util.sql.queries.Retriever;
import lombok.NonNull;

import java.util.UUID;

public class OnlineStats {

	public static void flush() {
		PrimeCore.getInstance().getDatabase().update("DELETE FROM `prime_bungee_online`").execute();
	}

	public static void insertPlayer(@NonNull UUID uuid) {
		PrimeCore.getInstance().getDatabase().update("INSERT INTO `prime_bungee_online` VALUES (?,?,?,?)")
		         .parameters(uuid.toString(), null, 0, null)
		         .execute();
	}

	public static void removePlayer(@NonNull UUID uuid) {
		PrimeCore.getInstance().getDatabase().update("DELETE FROM prime_bungee_online WHERE uuid = ?")
		         .parameters(uuid.toString())
		         .execute();
	}

	public static void setServer(@NonNull UUID uuid, String s) {
		PrimeCore.getInstance().getDatabase().update("UPDATE prime_bungee_online SET server = ? WHERE uuid = ?")
		         .parameters(s, uuid.toString())
		         .execute();
	}

	public static void setAFK(@NonNull UUID uuid, boolean b) {
		PrimeCore.getInstance().getDatabase().update("UPDATE prime_bungee_online SET afk = ? WHERE uuid = ?")
		         .parameters((b ? 1 : 0), uuid.toString())
		         .execute();
	}

	public static void setParty(@NonNull UUID uuid, UUID party) {
		if (party == null) {
			PrimeCore.getInstance().getDatabase().update("UPDATE prime_bungee_online SET party = null WHERE uuid = ?")
			         .parameters(uuid.toString())
			         .execute();
		} else {
			PrimeCore.getInstance().getDatabase().update("UPDATE prime_bungee_online SET party = ? WHERE uuid = ?")
			         .parameters(party.toString(), uuid.toString())
			         .execute();
		}
	}

	public static Retriever<String> getServer(@NonNull UUID uuid) {
		return PrimeCore.getInstance().getDatabase().select("SELECT server FROM prime_bungee_online WHERE uuid = ?")
		                .parameters(uuid.toString())
		                .execute(String.class).get();
	}

	public static Retriever<Boolean> getAFK(@NonNull UUID uuid) {
		return PrimeCore.getInstance().getDatabase().select("SELECT afk FROM prime_bungee_online WHERE uuid = ?")
		                .parameters(uuid.toString())
		                .execute(Integer.class).get().map(integer -> integer == 1);
	}

	public static Retriever<PlayerParty> getParty(@NonNull UUID uuid) {

		return PrimeCore.getInstance().getDatabase().select("SELECT party FROM prime_bungee_online WHERE uuid = ?")
		                .parameters(uuid.toString())
		                .execute(String.class)
		                .get()
		                .map(s -> new PlayerParty(UUID.fromString(s)));
	}


}
