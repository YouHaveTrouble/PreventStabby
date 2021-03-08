package eu.endermite.togglepvp.api;

import eu.endermite.togglepvp.TogglePvp;
import eu.endermite.togglepvp.util.CombatTimer;
import org.bukkit.entity.Player;
import java.util.UUID;

public class TogglePvpAPI {

    /**
     * Sets players PvP state. This will always save to database.
     * @param player Player to set pvp state to
     * @param newState State to set
     */
    public static void setPvpEnabled(Player player, boolean newState) {
        TogglePvp.getPlugin().getSmartCache().setPlayerPvpState(player.getUniqueId(), newState);
    }

    /**
     * Sets players PvP state. This will always save to database.
     * @param uuid UUID of player to set pvp state to
     * @param newState State to set
     */
    public static void setPvpEnabled(UUID uuid, boolean newState) {
        TogglePvp.getPlugin().getSmartCache().setPlayerPvpState(uuid, newState);
    }

    /**
     * Gets player's PvP state. If player is not cached this will query the database.
     * @param uuid UUID of the player to get data from.
     * @return True if enabled, false if disabled
     */
    public static boolean getPvpEnabled(UUID uuid) {
        return TogglePvp.getPlugin().getSmartCache().getPlayerData(uuid).isPvpEnabled();
    }

    /**
     * Gets player's PvP state. If player is not cached this will query the database.
     * @param player Player to get data from.
     * @return True if enabled, false if disabled
     */
    public static boolean getPvpEnabled(Player player) {
        return TogglePvp.getPlugin().getSmartCache().getPlayerData(player.getUniqueId()).isPvpEnabled();
    }

    /**
     * Checks if player can be damaged by another. Providing UUID of entity other than player may result in exceptions
     * @param attackerUuid Attacker's UUID
     * @param victimUuid Victim's UUID
     * @param sendDenyMessage True if check should send deny message to attacker
     * @return True if victim can be attacked by attacker, false if not
     */
    public static boolean canDamage(UUID attackerUuid, UUID victimUuid, boolean sendDenyMessage) {
        return  TogglePvp.getPlugin().getPlayerManager().canDamage(attackerUuid, victimUuid, sendDenyMessage);
    }

    /**
     * Checks if player can be damaged by another.
     * @param attacker Attacker
     * @param victim Victim
     * @param sendDenyMessage True if check should send deny message to attacker
     * @return True if victim can be attacked by attacker, false if not
     */
    public static boolean canDamage(Player attacker, Player victim, boolean sendDenyMessage) {
        return TogglePvp.getPlugin().getPlayerManager().canDamage(attacker.getUniqueId(), victim.getUniqueId(), sendDenyMessage);
    }

    /**
     * Checks if player has login protection.
     * @param uuid UUID of player to check
     * @return True if player has login protection, false if not
     */
    public static boolean hasLoginProtection(UUID uuid) {
        return TogglePvp.getPlugin().getPlayerManager().hasLoginProtection(uuid);
    }

    /**
     * Checks if player has login protection.
     * @param player Player to check
     * @return True if player has login protection, false if not
     */
    public static boolean hasLoginProtection(Player player) {
        return TogglePvp.getPlugin().getPlayerManager().hasLoginProtection(player.getUniqueId());
    }

    /**
     * Checks if player is in combat.
     * @param uuid UUID of player to check
     * @return True if player is in combat, false if they are not or if they are offline
     */
    public static boolean isInCombat(UUID uuid) {
        return CombatTimer.isInCombat(uuid);
    }

    /**
     * Checks if player is in combat.
     * @param player Player to check
     * @return True if player is in combat, false if they are not or if they are offline
     */
    public static boolean isInCombat(Player player) {
        return CombatTimer.isInCombat(player.getUniqueId());
    }


}
