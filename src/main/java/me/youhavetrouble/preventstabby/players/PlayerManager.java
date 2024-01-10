package me.youhavetrouble.preventstabby.players;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.util.DamageCheck;
import me.youhavetrouble.preventstabby.util.PvpState;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class PlayerManager {

    private final PreventStabby plugin;
    private final ConcurrentHashMap<UUID, PlayerData> playerList = new ConcurrentHashMap<>();
    private PvpState pvpForcedState = PvpState.NONE;

    public PlayerManager(PreventStabby plugin) {
        this.plugin = plugin;
        Bukkit.getAsyncScheduler().runAtFixedRate(plugin, (task) -> {
            // Refresh cache timer if player is online
            for (PlayerData playerData : playerList.values()) {
                if (playerData == null) continue;
                Player player = Bukkit.getPlayer(playerData.getPlayerUuid());
                if (player != null && player.isOnline()) {
                    playerData.refreshCacheTime();
                }
            }
            // Check for entries that should be invalidated
            playerList.values().removeIf(PlayerData::isCacheExpired);
        },250, 250, TimeUnit.MILLISECONDS);
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
     * Determines whether the given attacker can damage the victim.
     *
     * @param attacker The attacking entity.
     * @param victim   The victim entity.
     * @return A {@link DamageCheck.DamageCheckResult} object containing the result of the damage check.
     */
    public DamageCheck.DamageCheckResult canDamage(Entity attacker, Entity victim) {
        DamageCheck damageCheck = PreventStabby.getPlugin().getDamageUtil();
        return damageCheck.canDamage(attacker, victim);
    }

    /**
     * Determines whether the given players have any form of protection enabled, including login and teleport protection.
     *
     * @param players The players to check for protection.
     * @return true if any of the players have protection enabled, false otherwise.
     * @see PlayerData#isProtected()
     */
    public boolean hasProtection(OfflinePlayer... players) {
        for (OfflinePlayer offlinePlayer : players) {
            UUID uuid = offlinePlayer.getUniqueId();
            PlayerData playerData = playerList.get(uuid);
            if (playerData == null) continue;
            if (playerData.isProtected()) return true;
        }
        return false;
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

    public CompletableFuture<PlayerData> getPlayerData(UUID uuid) {
        // Try to get data from cache and refresh it
        PlayerData data = getPlayer(uuid);
        if (data != null) {
            data.refreshCacheTime();
            return CompletableFuture.completedFuture(data);
        }
        // Get data from database or provide default
        return CompletableFuture.supplyAsync(() -> {
            PlayerData playerData = PreventStabby.getPlugin().getSqLite().getPlayerInfo(uuid);
            if (playerData == null) {
                playerData = new PlayerData(uuid, false);
            }
            PreventStabby.getPlugin().getPlayerManager().addPlayer(uuid, playerData);
            return playerData;
        });


    }

    public void setPlayerPvpState(UUID uuid, boolean state) {
        // If player is in cache update that
        if (getPlayer(uuid) != null) {
            getPlayer(uuid).setPvpEnabled(state);
        }
        // Update the database aswell
        PreventStabby.getPlugin().getSqLite().updatePlayerInfo(uuid, new PlayerData(uuid, state));
    }
}
