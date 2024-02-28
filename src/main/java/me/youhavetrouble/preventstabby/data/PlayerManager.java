package me.youhavetrouble.preventstabby.data;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.api.event.PlayerEnterCombatEvent;
import me.youhavetrouble.preventstabby.api.event.PlayerLeaveCombatEvent;
import me.youhavetrouble.preventstabby.util.PluginMessages;
import me.youhavetrouble.preventstabby.util.PvpState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
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

        Bukkit.getScheduler().runTaskTimer(plugin, (task -> {
            List<LivingEntity> entities = new ArrayList<>();
            Bukkit.getWorlds().forEach((world -> entities.addAll(world.getLivingEntities())));
            Bukkit.getAsyncScheduler().runNow(plugin, (asyncTask) -> entities.forEach(livingEntity -> {
                if (!(livingEntity instanceof Tameable tameable)) return;
                UUID ownerId = tameable.getOwnerUniqueId();
                if (ownerId == null) return;
                getPlayerData(ownerId);
            }));
        }), 0, 20 * 15);

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

    public void handleDamageCheck(@NotNull DamageCheckResult damageCheckResult) {
        if (damageCheckResult.attackerId() == null) return;
        if (damageCheckResult.victimId() == null) return;
        PluginMessages.sendOutMessages(damageCheckResult);
        PlayerData attacker = getPlayer(damageCheckResult.attackerId());
        PlayerData victim = getPlayer(damageCheckResult.victimId());
        if (attacker == null || victim == null) return;
        if (!damageCheckResult.ableToDamage()) return;
        attacker.markInCombat();
        victim.markInCombat();
    }


    /**
     * Determines whether the given attacker can damage the victim.
     *
     * @param attacker The attacking entity.
     * @param victim The victim entity.
     * @return A {@link DamageCheckResult} object containing the result of the damage check.
     */
    public DamageCheckResult canDamage(@NotNull Entity attacker, @NotNull Entity victim) {
        Target attackerData = Target.getTarget(attacker);
        Target victimData = Target.getTarget(victim);
        if (attackerData == null || victimData == null) return DamageCheckResult.positive();
        return canDamage(attackerData.playerUuid, victimData.playerUuid, victimData.classifier);
    }

    public DamageCheckResult canDamage(UUID attackerId, UUID victimId, Target.EntityClassifier victimClassifier) {

        if (attackerId == null || victimId == null) return DamageCheckResult.positive();

        PlayerData attackerPlayerData = getPlayer(attackerId);
        PlayerData victimPlayerData = getPlayer(victimId);

        if (attackerPlayerData == null || victimPlayerData == null) {
            return DamageCheckResult.positive();
        }

        if (attackerPlayerData.isProtected()) {
            String message = switch (victimClassifier) {
                case PLAYER -> plugin.getConfigCache().cannotAttackTeleportOrSpawnProtectionAttacker;
                case PET -> plugin.getConfigCache().cannotAttackPetsTeleportOrSpawnProtectionAttacker;
                case MOUNT -> plugin.getConfigCache().cannotAttackMountsTeleportOrSpawnProtectionAttacker;
                default -> null;
            };
            return new DamageCheckResult(false, attackerId, victimId, message, null);
        }
        if (victimPlayerData.isProtected()) {
            String message = null;
            if (victimClassifier == Target.EntityClassifier.PLAYER) {
                message = plugin.getConfigCache().cannotAttackTeleportOrSpawnProtectionVictim;
            }
            return new DamageCheckResult(false, attackerId, victimId, message, null);
        }

        return switch (getForcedPvpState()) {
            case DISABLED -> new DamageCheckResult(false, attackerId, victimId, plugin.getConfigCache().cannotAttackForcedPvpOff, null);
            case ENABLED -> DamageCheckResult.positive(attackerId, victimId);
            default -> DamageCheckResult.positive(attackerId, victimId);
        };
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