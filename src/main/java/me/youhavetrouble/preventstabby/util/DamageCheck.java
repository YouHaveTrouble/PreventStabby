package me.youhavetrouble.preventstabby.util;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.config.ConfigCache;
import me.youhavetrouble.preventstabby.players.PlayerData;
import me.youhavetrouble.preventstabby.players.PlayerManager;
import org.bukkit.entity.*;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.UUID;

public class DamageCheck {

    private final ConfigCache config;
    private final PlayerManager playerManager;

    public DamageCheck(PreventStabby plugin) {
        this.config = plugin.getConfigCache();
        this.playerManager = plugin.getPlayerManager();
    }

    public DamageCheckResult canDamage(@NotNull Entity attacker, @NotNull Entity victim) {
        Target attackerData = getUuidOfActualPlayer(attacker);
        Target victimData = getUuidOfActualPlayer(victim);
        if (attackerData == null || victimData == null) return new DamageCheckResult(true, null, null);
        return canDamage(attackerData.playerUuid, victimData.playerUuid, victimData.classifier);
    }

    public DamageCheckResult canDamage(UUID attackerId, UUID victimId, EntityClassifier victimClassifier) {

        if (attackerId == null || victimId == null) return new DamageCheckResult(true, null, null);

        PlayerData attackerPlayerData = PreventStabby.getPlugin().getPlayerManager().getPlayer(attackerId);
        PlayerData victimPlayerData = PreventStabby.getPlugin().getPlayerManager().getPlayer(victimId);

        if (attackerPlayerData.isProtected()) {
            String message = switch (victimClassifier) {
                case PLAYER -> config.cannotAttackTeleportOrSpawnProtectionAttacker;
                case PET -> config.cannotAttackPetsTeleportOrSpawnProtectionAttacker;
                case MOUNT -> config.cannotAttackMountsTeleportOrSpawnProtectionAttacker;
                default -> null;
            };
            return new DamageCheckResult(false, message, null);
        }
        if (victimPlayerData.isProtected()) {
            String message = null;
            if (victimClassifier == EntityClassifier.PLAYER) {
                message = config.cannotAttackTeleportOrSpawnProtectionVictim;
            }
            return new DamageCheckResult(false, message, null);
        }

        return switch (playerManager.getForcedPvpState()) {
            case DISABLED -> new DamageCheckResult(false, config.cannotAttackForcedPvpOff, null);
            case ENABLED -> new DamageCheckResult(true, null, null);
            default -> new DamageCheckResult(true, null, null);
        };
    }

    /**
     * Get the UUID of the actual player, being owner of a pet, shooter of a projectile, etc.
     *
     * @param entity Base entity to get the player UUID from
     * @return UUID of the actual player, null if not found
     */
    @Nullable
    public Target getUuidOfActualPlayer(@NotNull Entity entity) {
        if (entity instanceof Player) return new Target(entity.getUniqueId(), EntityClassifier.PLAYER);

        // Get shooter of projectile
        if (entity instanceof Projectile projectile) {
            if (projectile.getShooter() instanceof Player shooter) {
                return new Target(shooter.getUniqueId(), EntityClassifier.PLAYER);
            }
        }

        // Get player riding mount
        if (!entity.getPassengers().isEmpty()) {
            Entity passenger = entity.getPassengers().get(0);
            if (passenger instanceof Player) {
                return new Target(passenger.getUniqueId(), EntityClassifier.MOUNT);
            }
        }

        // Get owner of tamed entity
        if (entity instanceof Tameable tameable) {
            if (tameable.getOwner() != null) {
                return new Target(tameable.getOwner().getUniqueId(), EntityClassifier.PET);
            }
        }

        return null;
    }


    public static class DamageCheckResult {
        public final boolean ableToDamage;
        public final String feedbackForAttacker, feedbackForVictim;

        private DamageCheckResult(
                boolean ableToDamage,
                @Nullable String feedbackForAttacker,
                @Nullable String feedbackForVictim
        ) {
            this.ableToDamage = ableToDamage;
            this.feedbackForAttacker = feedbackForAttacker;
            this.feedbackForVictim = feedbackForVictim;
        }
    }

    public static class Target {

        /**
         * The unique identifier for a player.
         */
        public final UUID playerUuid;

        /**
         * Represents the entity classifier of a target.
         * This indicates what type of entity player's id was assumed from
         */
        public final EntityClassifier classifier;

        private Target(UUID uuid, EntityClassifier classifier) {
            this.playerUuid = uuid;
            this.classifier = classifier;
        }
    }

    public enum EntityClassifier {
        PLAYER,
        PET,
        MOUNT,
        OTHER
    }

}
