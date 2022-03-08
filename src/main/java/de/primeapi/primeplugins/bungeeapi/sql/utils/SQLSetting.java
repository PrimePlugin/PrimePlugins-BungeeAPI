package de.primeapi.primeplugins.bungeeapi.sql.utils;

import de.primeapi.primeplugins.bungeeapi.PrimeCore;
import de.primeapi.util.sql.queries.Retriever;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SQLSetting {
    WARTUNG("false"),
    MOTD_NORMAL_1("§b§lBungeeSystem §3by PrimeAPI §8[§71.8.x-1.16.x§8]"),
    MOTD_NORMAL_2("§e§lhttps://primeapi.de"),
    MOTD_WARTUNG_1("§b§lBungeeSystem §3by PrimeAPI §8[§71.8.x-1.16.x§8]"),
    MOTD_WARTUNG_2("§e§lhttps://primeapi.de §8x §c§lWartungen"),
    SLOTS("50"),
    ;
    String standardValue;

    public Retriever<String> getValue() {
        return PrimeCore.getInstance().getDatabase().select(
                                "SELECT value FROM prime_bungee_settings WHERE identifier = ?"
                                                           ).parameters(this.toString())
                        .execute(String.class)
                        .get()
                        .map(s -> s == null ? standardValue : s);
    }

    public void setValue(String value) {
        PrimeCore.getInstance().getThreadPoolExecutor().submit(() -> {
            String s = PrimeCore.getInstance()
                                .getDatabase()
                                .select(
                                        "SELECT value FROM prime_bungee_settings WHERE identifier = ?"
                                       )
                                .parameters(this.toString())
                                .execute(String.class)
                                .get()
                                .complete();
            if (s == null) {
                PrimeCore.getInstance().getDatabase().update(
                        "INSERT INTO prime_bungee_settings VALUES (id,?,?)"
                                                            ).parameters(this.toString(), value).execute();
            } else {
                PrimeCore.getInstance().getDatabase().update(
                        "UPDATE prime_bungee_settings SET value = ? WHERE identifier = ?"
                                                            ).parameters(value, this.toString()).execute();
            }
        });
    }

    public Retriever<Boolean> getAsBoolean() {
        return new Retriever<>(() -> {
            String value = getValue().complete();
            return Boolean.valueOf(value);
        });
    }

    public Retriever<Integer> getAsInteger() {
        return new Retriever<>(() -> {
            String value = getValue().complete();
            return Integer.parseInt(value);
        });
    }

}
