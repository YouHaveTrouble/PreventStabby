package me.youhavetrouble.preventstabby.players;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.api.event.PlayerLeaveCombatEvent;
import me.youhavetrouble.preventstabby.config.ConfigCache;
import me.youhavetrouble.preventstabby.hooks.WorldGuardHook;
import me.youhavetrouble.preventstabby.util.CombatTimer;
import me.youhavetrouble.preventstabby.util.PluginMessages;
import lombok.Getter;
import me.youhavetrouble.preventstabby.util.PvpState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nullable;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerManager {

    @Getter
    ConcurrentHashMap<UUID, PlayerData> playerList = new ConcurrentHashMap<>();

    private PvpState pvpForcedState = PvpState.NONE;

    public final BukkitTask combatTrackerTask;

    public PlayerManager() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            PlayerData playerData = PreventStabby.getPlugin().getSqLite().getPlayerInfo(p.getUniqueId());
            playerList.put(p.getUniqueId(), playerData);
        }

        combatTrackerTask = Bukkit.getScheduler().runTaskTimerAsynchronously(PreventStabby.getPlugin(), () -> {
            for (PlayerData playerData : playerList.values()) {
                UUID uuid = playerData.getPlayerUuid();
                if (!CombatTimer.isInCombat(uuid)) {
                    playerData.setLastCombatCheck(true);
                    continue;
                }
                if (!playerData.getLastCombatCheck()) continue;
                Player player = Bukkit.getPlayer(uuid);
                if (player == null) continue;

                Bukkit.getScheduler().runTask(PreventStabby.getPlugin(), () -> {
                    if (PlayerLeaveCombatEvent.getHandlerList().getRegisteredListeners().length > 0) {
                        PlayerLeaveCombatEvent playerLeaveCombatEvent = new PlayerLeaveCombatEvent(player);
                        Bukkit.getPluginManager().callEvent(playerLeaveCombatEvent);
                        if (playerLeaveCombatEvent.isCancelled()) {
                            playerData.refreshCombatTime();
                            return;
                        }
                    }
                    playerData.setLastCombatCheck(false);
                    playerData.setInCombat(false);
                    PluginMessages.sendActionBar(uuid, PreventStabby.getPlugin().getConfigCache().getLeaving_combat());
                });
            }
        }, 20, 20);


    }

    /**
     * Sets player in combat and sets combat time to the interval set in config.
     *
     * @see PlayerData#refreshCombatTime()
     */
    public void refreshPlayersCombatTime(UUID uuid) {
        PlayerData data = playerList.get(uuid);
        if (data == null) return;
        Player player = Bukkit.getPlayer(uuid);
        if (player == null || player.isDead()) return;
        data.refreshCombatTime();
        data.setInCombat(true);
    }

    /**
     * Gets player's PlayerData object. Returns null when player with provided UUID doesn't exist.
     *
     * @param uuid Player's UUID.
     * @return Player's PlayerData object or null if player doesn't exist.
     */
    public PlayerData getPlayer(UUID uuid) {
        return playerList.get(uuid);
    }

    protected void addPlayer(UUID uuid, PlayerData data) {
        playerList.put(uuid, data);
    }

    /**
     * Returns true if player has personal pvp enabled, false otherwise.
     *
     * @param uuid Player's UUID.
     * @return True if player has personal pvp enabled, false otherwise.
     * @see PlayerData#isPvpEnabled()
     */
    public boolean getPlayerPvPState(UUID uuid) {
        return PreventStabby.getPlugin().getSmartCache().getPlayerData(uuid).isPvpEnabled();
    }

    /**
     * Sets player's personal pvp state.
     *
     * @param uuid  Player's UUID.
     * @param state Pvp state to set.
     * @see PlayerData#setPvpEnabled(boolean)
     */
    public void setPlayerPvpState(UUID uuid, boolean state) {
        PreventStabby.getPlugin().getSmartCache().getPlayerData(uuid).setPvpEnabled(state);
    }

    /**
     * Toggles player's personal pvp state.
     *
     * @param uuid Player's UUID.
     * @return Player's personal pvp state after the change.
     */
    public boolean togglePlayerPvpState(UUID uuid) {
        PlayerData playerData = PreventStabby.getPlugin().getSmartCache().getPlayerData(uuid);
        boolean newState = !playerData.isPvpEnabled();
        playerData.setPvpEnabled(newState);
        return newState;
    }

    /**
     * Check if attacker can harm the victim. Both of them have to have personal pvp enabled and none of them can have
     * any kind of spawn or teleport protection.
     *
     * @param attacker        Atacker's UUID.
     * @param victim          Victim's UUID.
     * @param sendDenyMessage Should plugin send a message that there was attempt at damaging to both players?
     * @return Whenever attacker can harm the victim.
     */
    public boolean canDamage(UUID attacker, UUID victim, boolean sendDenyMessage) {
        return canDamage(attacker, victim, sendDenyMessage, true);
    }

    /**
     * Check if attacker can harm the victim.
     *
     * @param attacker                   Atacker's UUID.
     * @param victim                     Victim's UUID.
     * @param sendDenyMessage            Should plugin send a message that there was attempt at damaging to both players?
     * @param checkVictimSpawnProtection Should teleport and spawn protections be taken into account?
     * @return Whenever attacker can harm the victim.
     */
    public boolean canDamage(UUID attacker, UUID victim, boolean sendDenyMessage, boolean checkVictimSpawnProtection) {
        ConfigCache config = PreventStabby.getPlugin().getConfigCache();
        String attackerMessage = sendDenyMessage ? config.getCannot_attack_attacker() : null;
        String victimMessage = sendDenyMessage ? config.getCannot_attack_victim() : null;
        return canDamage(attacker, victim, attackerMessage, victimMessage, checkVictimSpawnProtection);
    }

    /**
     * Check if attacker can harm the victim.
     *
     * @param attacker                   Atacker's UUID.
     * @param victim                     Victim's UUID.
     * @param attackerDenyMessage        Message that action was denied to the attacker.
     * @param victimDenyMessage          Message that action was denied to the victim.
     * @param checkVictimSpawnProtection Should teleport and spawn protections be taken into account?
     * @return Whenever attacker can harm the victim.
     */
    public boolean canDamage(UUID attacker, UUID victim, @Nullable String attackerDenyMessage, @Nullable String victimDenyMessage, boolean checkVictimSpawnProtection) {

        if (hasLoginProtection(attacker) || hasTeleportProtection(attacker)) return false;
        if (checkVictimSpawnProtection && hasLoginProtection(victim)) return false;
        if (checkVictimSpawnProtection && hasTeleportProtection(victim)) return false;

        switch (pvpForcedState) {
            case ENABLED:
                return true;
            case DISABLED:
                PluginMessages.sendActionBar(attacker, PreventStabby.getPlugin().getConfigCache().getCannot_attack_pvp_force_off());
                return false;
            case NONE:
            default:
                break;
        }

        SmartCache smartCache = PreventStabby.getPlugin().getSmartCache();

        if (!smartCache.getPlayerData(attacker).isPvpEnabled()) {
            Player attackerPlayer = Bukkit.getPlayer(attacker);
            if (PreventStabby.worldGuardHookEnabled() && attackerPlayer != null && WorldGuardHook.isPlayerForcedToPvp(attackerPlayer))
                return true;

            if (attackerDenyMessage != null) {
                PluginMessages.sendActionBar(attacker, attackerDenyMessage);
            }
            return false;
        }
        if (!smartCache.getPlayerData(victim).isPvpEnabled()) {
            Player victimPlayer = Bukkit.getPlayer(victim);
            if (PreventStabby.worldGuardHookEnabled() && victimPlayer != null && WorldGuardHook.isPlayerForcedToPvp(victimPlayer))
                return true;

            if (victimDenyMessage != null) {
                ConfigCache config = PreventStabby.getPlugin().getConfigCache();
                PluginMessages.sendActionBar(attacker, config.getCannot_attack_victim());
            }
            return false;
        }
        return true;
    }

    /**
     * @param uuid Player UUIDs.
     * @return True if any of the provided UUIDs has spawn protection.
     */
    public boolean hasLoginProtection(UUID... uuid) {
        SmartCache smartCache = PreventStabby.getPlugin().getSmartCache();
        for (UUID checkedUuid : uuid) {
            if (Instant.now().getEpochSecond() < smartCache.getPlayerData(checkedUuid).getLoginTimestamp())
                return true;
        }
        return false;
    }

    /**
     * @param uuid Player UUID.
     * @return True if player tied to the uuid currently has teleport protection.
     */
    public boolean hasTeleportProtection(UUID uuid) {
        SmartCache smartCache = PreventStabby.getPlugin().getSmartCache();
        return Instant.now().getEpochSecond() < smartCache.getPlayerData(uuid).getTeleportTimestamp();
    }

    /**
     * Returns current forced pvp state.
     *
     * @return Current forced pvp state.
     */
    public PvpState getForcedPvpState() {
        return pvpForcedState;
    }

    /**
     * Sets current forced pvp state.
     *
     * @param forcedPvpState New forced pvp state.
     */
    public void setForcedPvpState(PvpState forcedPvpState) {
        this.pvpForcedState = forcedPvpState;
    }
}
