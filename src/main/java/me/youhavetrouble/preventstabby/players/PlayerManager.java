package me.youhavetrouble.preventstabby.players;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.api.event.PlayerEnterCombatEvent;
import me.youhavetrouble.preventstabby.api.event.PlayerLeaveCombatEvent;
import me.youhavetrouble.preventstabby.util.DamageCheck;
import me.youhavetrouble.preventstabby.util.PluginMessages;
import me.youhavetrouble.preventstabby.util.PvpState;
import org.bukkit.Bukkit;
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
            // Check for entries that should be invalidated
            playerList.values().removeIf(PlayerData::isCacheExpired);
        }, 250, 250, TimeUnit.MILLISECONDS);

        Bukkit.getGlobalRegionScheduler().runAtFixedRate(plugin, (task) -> {
            for (PlayerData playerData : playerList.values()) {
                if (playerData == null) continue;
                Player player = Bukkit.getPlayer(playerData.getPlayerUuid());
                if (player == null || !player.isOnline()) continue;
                playerData.refreshCacheTime(); // Refresh cache timer if player is online
                // leaving combat logic
                if (playerData.getLastCombatCheckState() && !playerData.isInCombat()) {
                    PlayerLeaveCombatEvent leaveCombatEvent = null;
                    if (PlayerLeaveCombatEvent.getHandlerList().getRegisteredListeners().length > 0) {
                        leaveCombatEvent = new PlayerLeaveCombatEvent(player);
                        Bukkit.getPluginManager().callEvent(leaveCombatEvent);
                    }
                    if (leaveCombatEvent != null && leaveCombatEvent.isCancelled()) {
                        playerData.markInCombat();
                        playerData.setLastCombatCheckState(playerData.isInCombat());
                        continue;
                    }
                    PluginMessages.sendActionBar(player, plugin.getConfigCache().leaving_combat);
                    playerData.setLastCombatCheckState(playerData.isInCombat());
                    continue;
                }
                // entering combat logic
                if (!playerData.getLastCombatCheckState() && playerData.isInCombat()) {
                    PlayerEnterCombatEvent enterCombatEvent = null;
                    if (PlayerEnterCombatEvent.getHandlerList().getRegisteredListeners().length > 0) {
                        enterCombatEvent = new PlayerEnterCombatEvent(player);
                        Bukkit.getPluginManager().callEvent(enterCombatEvent);
                    }
                    if (enterCombatEvent != null && enterCombatEvent.isCancelled()) {
                        playerData.markNotInCombat();
                        playerData.setLastCombatCheckState(playerData.isInCombat());
                        continue;
                    }
                    PluginMessages.sendActionBar(player, plugin.getConfigCache().entering_combat);
                    playerData.setLastCombatCheckState(playerData.isInCombat());
                    continue;
                }
            }
        }, 1, 1);
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
        DamageCheck damageCheck = plugin.getDamageUtil();
        return damageCheck.canDamage(attacker, victim);
    }

    /**
     * Returns current forced pvp state.
     * @return Current forced pvp state.
     */
    public PvpState getForcedPvpState() {
        return pvpForcedState;
    }

    /**
     * Sets current forced pvp state.
     * @param forcedPvpState New forced pvp state.
     */
    public void setForcedPvpState(PvpState forcedPvpState) {
        this.pvpForcedState = forcedPvpState;
    }

    /**
     * Retrieves the PlayerData object for the player with the provided UUID. Returns new default if there isn't data.
     *
     * @param uuid The UUID of the player.
     * @return The PlayerData object associated with the player.
     */
    public CompletableFuture<PlayerData> getPlayerData(UUID uuid) {
        // Try to get data from cache and refresh it
        PlayerData data = getPlayer(uuid);
        if (data != null) {
            data.refreshCacheTime();
            return CompletableFuture.completedFuture(data);
        }
        // Get data from database or provide default
        return CompletableFuture.supplyAsync(() -> {
            PlayerData playerData = plugin.getSqLite().getPlayerInfo(uuid);
            if (playerData == null) {
                playerData = new PlayerData(uuid, false);
            }
            plugin.getPlayerManager().addPlayer(uuid, playerData);
            return playerData;
        });
    }

    public void setPlayerPvpState(UUID uuid, boolean state) {
        // If player is in cache update that
        if (getPlayer(uuid) != null) {
            getPlayer(uuid).setPvpEnabled(state);
        }
        // Update the database aswell
        plugin.getSqLite().updatePlayerInfo(uuid, new PlayerData(uuid, state));
    }

    public CompletableFuture<Boolean> togglePlayerPvpState(UUID uuid) {
        return getPlayerData(uuid).thenApply(playerData -> {
            playerData.setPvpEnabled(!playerData.isPvpEnabled());
            return playerData.isPvpEnabled();
        });
    }
}
