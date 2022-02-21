package de.primeapi.primeplugins.bungeeapi.managers.messages;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum CoreMessage {

    PREFIX("§bSystem §7●", false),
    NO_PERMS("§c Die fehlt die Berechtigung §8'§e%permission%§8'§c!", true),

    COINS_AMOUNT("§7 Du hast §e%coins% Coins§7!", true),
    COINS_USAGE("§7 Verwendung: §e/coins <set/add/remove/see>", true),
    COINS_NONUMBER("§4 Fehler: §cDer angegebene Wert ist keine Zahl!", true),
    COINS_PLAYERNOTFOUND("§4 Fehler: §cDer angegebene Spieler wurde nicht in der Datenbank gefunden!", true),
    COINS_ADD_USAGE("§7 Verwendung: §e/coins add <Spieler> <coins>", true),
    COINS_ADD_SUCCESS("§7 Du hast den Spieler §e%player% §aerfolgreich §6%coins% §7hinzugefügt!", true),
    COINS_SET_USAGE("§7 Verwendung: §e/coins set <Spieler> <coins>", true),
    COINS_SET_SUCCESS("§7 Du hast die Coins von §e%player% §aerfolgreich §7auf §6%coins% Coins §7gesetzt!", true),
    COINS_REMOVE_USAGE("§7 Verwendung: §e/coins remove <Spieler> <coins>", true),
    COINS_REMOVE_SUCCESS("§7 Du hast den Spieler §e%player% §aerfolgreich §6%coins% §7entfernt!", true),
    COINS_SEE_USAGE("§7 Verwendung: §e/coins see <Spieler>", true),
    COINS_SEE_SUCCESS("§7 Der Spieler §e%player% §7hat §6%coins% Coins§7!", true),

    WEBACCOUNT_ERROR("§c Es ist ein Fehler aufgetreten!", true),
    WEBACCOUNT_SUCCESS("§7 Dein Key wurde §aerfolgreich §7erstellt: §8'§e%key%§8' <br>%prefix%§7 Klicke §ehier §7um zur Registration zu gelangen!", true),

    KICK_WEB("§8» §b§lWebinterface §8«\n\n§c§lDu wurdest gekickt!\n§7Gekickt von %name%", false),

    PAY_USAGE("§eCoins §7●§7 Verwendung: §e/pay <Spieler> <Betrag>", false),
    PAY_NOT_SELF("§eCoins §7●§c Du kannst dir selbst kein Geld senden!", false),
    PAY_SUCCESSFULLY("§eCoins §7●§7 Du hast erfolgreich §e%c%$§7 an §e%p%§7 überwiesen!", false),
    PAY_SUCCESSFULLY_RECEIVER("§eCoins §7●§7 Du hast §e%c%$§7 von §a%p% §7erhalten!", false),
    PAY_NOT_ENOUGH("§eCoins §7●§c Du hast zu wenig Coins!", false),
    PAY_NOT_ONLINE("§eCoins §7●§c Dieser Spieler wurde nicht gefunden!", false),
    PAY_NOT_NUMBER("§eCoins §7●§c Bitte gebe eine Zahl als Betrag an!", false),

    PLACEHOLDER("DO NOT TOUCH", false);

    String path;
    @Setter
    String content;
    Boolean prefix;

    CoreMessage(String content, Boolean prefix) {
        this.content = content;
        this.prefix = prefix;
        this.path = this.toString().replaceAll("_", ".").toLowerCase();
    }

    CoreMessage(String path, String content, Boolean prefix) {
        this.content = content;
        this.prefix = prefix;
        this.path = path;
    }


    public CoreMessage replace(String key, String value) {
        if (!key.startsWith("%")) {
            key = "%" + key + "%";
        }
        String s = getContent().replaceAll(key, value);
        PLACEHOLDER.setContent(s);
        return PLACEHOLDER;
    }

    public CoreMessage replace(String key, Object value) {
        return replace(key, String.valueOf(value));
    }
}
