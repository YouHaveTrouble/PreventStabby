package me.youhavetrouble.preventstabby.api;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.util.CombatTimer;
import me.youhavetrouble.preventstabby.util.PvpState;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.UUID;

public class PreventStabbyAPI {

    /**
     * Sets players PvP state. This will always save to database.
     * @param player Player to set pvp state to
     * @param newState State to set
     */
    public static void setPvpEnabled(Player player, boolean newState) {
        PreventStabby.getPlugin().getSmartCache().setPlayerPvpState(player.getUniqueId(), newState);
    }

    /**
     * Sets players PvP state. This will always save to database.
     * @param uuid UUID of player to set pvp state to
     * @param newState State to set
     */
    public static void setPvpEnabled(UUID uuid, boolean newState) {
        PreventStabby.getPlugin().getSmartCache().setPlayerPvpState(uuid, newState);
    }

    /**
     * Gets player's PvP state. If player is not cached this will query the database.
     * @param uuid UUID of the player to get data from.
     * @return True if enabled, false if disabled
     */
    public static boolean getPvpEnabled(UUID uuid) {
        return PreventStabby.getPlugin().getSmartCache().getPlayerData(uuid).isPvpEnabled();
    }

    /**
     * Gets player's PvP state. If player is not cached this will query the database.
     * @param player Player to get data from.
     * @return True if enabled, false if disabled
     */
    public static boolean getPvpEnabled(Player player) {
        return PreventStabby.getPlugin().getSmartCache().getPlayerData(player.getUniqueId()).isPvpEnabled();
    }

    /**
     * Checks if player can be damaged by another. Providing UUID of entity other than player may result in exceptions
     * @param attackerUuid Attacker's UUID
     * @param victimUuid Victim's UUID
     * @param sendDenyMessage True if check should send deny message to attacker
     * @return True if victim can be attacked by attacker, false if not
     */
    public static boolean canDamage(UUID attackerUuid, UUID victimUuid, boolean sendDenyMessage) {
        return  PreventStabby.getPlugin().getPlayerManager().canDamage(attackerUuid, victimUuid, sendDenyMessage);
    }

    /**
     * Checks if player can be damaged by another.
     * @param attacker Attacker
     * @param victim Victim
     * @param sendDenyMessage True if check should send deny message to attacker
     * @return True if victim can be attacked by attacker, false if not
     */
    public static boolean canDamage(Player attacker, Player victim, boolean sendDenyMessage) {
        return PreventStabby.getPlugin().getPlayerManager().canDamage(attacker.getUniqueId(), victim.getUniqueId(), sendDenyMessage);
    }

    /**
     * Checks if player can be damaged by another. Providing UUID of entity other than player may result in exceptions
     * @param attackerUuid Attacker's UUID
     * @param victimUuid Victim's UUID
     * @return True if victim can be attacked by attacker, false if not
     */
    public static boolean canDamage(UUID attackerUuid, UUID victimUuid) {
        return  PreventStabby.getPlugin().getPlayerManager().canDamage(attackerUuid, victimUuid, false);
    }

    /**
     * Checks if player can be damaged by another.
     * @param attacker Attacker
     * @param victim Victim
     * @return True if victim can be attacked by attacker, false if not
     */
    public static boolean canDamage(Player attacker, Player victim) {
        return PreventStabby.getPlugin().getPlayerManager().canDamage(attacker.getUniqueId(), victim.getUniqueId(), false);
    }

    /**
     * Checks if player has login protection.
     * @param uuid UUID of player to check
     * @return True if player has login protection, false if not
     */
    public static boolean hasLoginProtection(UUID uuid) {
        return PreventStabby.getPlugin().getPlayerManager().hasLoginProtection(uuid);
    }

    /**
     * Checks if player has teleport protection.
     * @param player Player to check
     * @return True if player has teleport protection, false if not
     */
    public static boolean hasTeleportProtection(Player player) {
        return PreventStabby.getPlugin().getPlayerManager().hasLoginProtection(player.getUniqueId());
    }

    /**
     * Checks if player has teleport protection.
     * @param uuid UUID of player to check
     * @return True if player has teleport protection, false if not
     */
    public static boolean hasTeleportProtection(UUID uuid) {
        return PreventStabby.getPlugin().getPlayerManager().hasLoginProtection(uuid);
    }

    /**
     * Checks if player has login protection.
     * @param player Player to check
     * @return True if player has login protection, false if not
     */
    public static boolean hasLoginProtection(Player player) {
        return PreventStabby.getPlugin().getPlayerManager().hasLoginProtection(player.getUniqueId());
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

    /**
     * @return Current state of forced PvP
     */
    public static PvpState getForcedPvpState() {
        return PreventStabby.getPlugin().getPlayerManager().getForcedPvpState();
    }

    /**
     *
     * @param newForcedPvpState<br>
     * true - force enable PvP for every player<br>
     * false - force disable PvP for every player<br>
     * null - don't force PvP state<br>
     */
    public static void setForcedPvpState(PvpState newForcedPvpState) {
        PreventStabby.getPlugin().getPlayerManager().setForcedPvpState(newForcedPvpState);
    }

}

