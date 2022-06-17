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
                    if (playerData.getLastCombatCheck()) {
                        Player player = Bukkit.getPlayer(uuid);
                        if (player == null) continue;
                        PlayerLeaveCombatEvent playerLeaveCombatEvent = new PlayerLeaveCombatEvent(player);
                        Bukkit.getScheduler().runTask(PreventStabby.getPlugin(), () -> {
                            Bukkit.getPluginManager().callEvent(playerLeaveCombatEvent);
                            if (playerLeaveCombatEvent.isCancelled()) {
                                playerData.refreshCombatTime();
                                return;
                            }
                            playerData.setLastCombatCheck(false);
                            playerData.setInCombat(false);
                            PluginMessages.sendActionBar(uuid, PreventStabby.getPlugin().getConfigCache().getLeaving_combat());
                        });
                    }
                } else {
                    playerData.setLastCombatCheck(true);
                }
            }
        }, 20, 20);


    }

    public void refreshPlayersCacheTime(UUID uuid) {
        playerList.get(uuid).refreshCachetime();
    }

    public void refreshPlayersCombatTime(UUID uuid) {
        PlayerData data = playerList.get(uuid);
        if (data == null) return;
        Player player = Bukkit.getPlayer(uuid);
        if (player == null || player.isDead()) return;
        data.refreshCombatTime();
        data.setInCombat(true);

    }

    public PlayerData getPlayer(UUID uuid) {
        return playerList.get(uuid);
    }

    public void addPlayer(UUID uuid, PlayerData data) {
        playerList.put(uuid, data);
    }

    public boolean getPlayerPvPState(UUID uuid) {
        return PreventStabby.getPlugin().getSmartCache().getPlayerData(uuid).isPvpEnabled();
    }

    public void setPlayerPvpState(UUID uuid, boolean state) {
        PreventStabby.getPlugin().getSmartCache().getPlayerData(uuid).setPvpEnabled(state);
    }

    public boolean togglePlayerPvpState(UUID uuid) {
        SmartCache smartCache = PreventStabby.getPlugin().getSmartCache();
        if (smartCache.getPlayerData(uuid).isPvpEnabled()) {
            smartCache.getPlayerData(uuid).setPvpEnabled(false);
            return false;
        } else {
            smartCache.getPlayerData(uuid).setPvpEnabled(true);
            return true;
        }
    }

    public boolean canDamage(UUID attacker, UUID victim, boolean sendDenyMessage) {
        return canDamage(attacker, victim, sendDenyMessage, true);
    }

    public boolean canDamage(UUID attacker, UUID victim, boolean sendDenyMessage, boolean checkVictimSpawnProtection) {

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

            if (sendDenyMessage) {
                ConfigCache config = PreventStabby.getPlugin().getConfigCache();
                PluginMessages.sendActionBar(attacker, config.getCannot_attack_attacker());
            }
            return false;
        }
        if (!smartCache.getPlayerData(victim).isPvpEnabled()) {
            Player victimPlayer = Bukkit.getPlayer(victim);
            if (PreventStabby.worldGuardHookEnabled() && victimPlayer != null && WorldGuardHook.isPlayerForcedToPvp(victimPlayer))
                return true;

            if (sendDenyMessage) {
                ConfigCache config = PreventStabby.getPlugin().getConfigCache();
                PluginMessages.sendActionBar(attacker, config.getCannot_attack_victim());
            }
            return false;
        }
        return true;
    }

    /**
     * @param uuid Player UUIDs
     * @return true if any of the provided UUIDs has spawn protection
     */
    public boolean hasLoginProtection(UUID... uuid) {
        SmartCache smartCache = PreventStabby.getPlugin().getSmartCache();
        for (UUID checkedUuid : uuid) {
            if (Instant.now().getEpochSecond() < smartCache.getPlayerData(checkedUuid).getLoginTimestamp())
                return true;
        }
        return false;
    }

    public boolean hasTeleportProtection(UUID uuid) {
        SmartCache smartCache = PreventStabby.getPlugin().getSmartCache();
        return Instant.now().getEpochSecond() < smartCache.getPlayerData(uuid).getTeleportTimestamp();
    }

    public PvpState getForcedPvpState() {
        return pvpForcedState;
    }

    public void setForcedPvpState(PvpState forcedPvpState) {
        this.pvpForcedState = forcedPvpState;
    }
}
