package me.youhavetrouble.preventstabby.players;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.api.event.PlayerLeaveCombatEvent;
import me.youhavetrouble.preventstabby.config.ConfigCache;
import me.youhavetrouble.preventstabby.util.CombatTimer;
import me.youhavetrouble.preventstabby.util.PluginMessages;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.time.Instant;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class PlayerManager {

    @Getter
    HashMap<UUID, PlayerData> playerList = new HashMap<>();

    public final BukkitTask combatTrackerTask;

    public PlayerManager() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            PlayerData playerData = PreventStabby.getPlugin().getSqLite().getPlayerInfo(p.getUniqueId());
            playerList.put(p.getUniqueId(), playerData);
        }

        combatTrackerTask = Bukkit.getScheduler().runTaskTimerAsynchronously(PreventStabby.getPlugin(), () -> {
            Iterator<PlayerData> iterator = playerList.values().iterator();
            while (iterator.hasNext()) {
                PlayerData playerData = iterator.next();
                UUID uuid = playerData.getPlayerUuid();
                if (!CombatTimer.isInCombat(uuid)) {
                    if (playerData.getLastCombatCheck()) {
                        Player player = Bukkit.getPlayer(uuid);
                        if (player == null)
                            return;
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
        try {
            PlayerData data = playerList.get(uuid);
            if (data == null)
                return;
            Player player = Bukkit.getPlayer(uuid);
            if (player == null || player.isDead())
                return;

            data.refreshCombatTime();
            data.setInCombat(true);
        } catch (Exception ignored) {
        }
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

        if (hasLoginProtection(attacker) || hasTeleportProtection(attacker))
            return false;

        if (checkVictimSpawnProtection && hasLoginProtection(victim))
            return false;

        if (checkVictimSpawnProtection && hasTeleportProtection(victim))
            return false;

        SmartCache smartCache = PreventStabby.getPlugin().getSmartCache();

        if (!smartCache.getPlayerData(attacker).isPvpEnabled()) {
            if (sendDenyMessage) {
                ConfigCache config = PreventStabby.getPlugin().getConfigCache();
                PluginMessages.sendActionBar(attacker, config.getCannot_attack_attacker());
            }
            return false;
        }
        if (!smartCache.getPlayerData(victim).isPvpEnabled()) {
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

}
