package me.youhavetrouble.preventstabby.util;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.config.ConfigCache;
import me.youhavetrouble.preventstabby.players.PlayerManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Tameable;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.UUID;

public class DamageUtil {

    private final ConfigCache config;
    private final PlayerManager playerManager;

    public DamageUtil(PreventStabby plugin) {
        this.config = plugin.getConfigCache();
        this.playerManager = plugin.getPlayerManager();
    }

    public DamageCheckResult canDamage(Entity attacker, Entity victim) {

        Target attackerData = getUuidOfActualPlayer(attacker);
        Target victimData = getUuidOfActualPlayer(victim);
        if (attackerData == null || victimData == null) return new DamageCheckResult(true, null, null);

        if (playerManager.hasLoginProtection(attackerData.uuid) || playerManager.hasTeleportProtection(attackerData.uuid)) {
            String message = null;
            switch (victimData.classifier) {
                case PLAYER:
                    message = config.cannotAttackTeleportOrSpawnProtectionAttacker;
                    break;
                case PET:
                    message = config.cannotAttackPetsTeleportOrSpawnProtectionAttacker;
                    break;
                case MOUNT:
                    message = config.cannotAttackMountsTeleportOrSpawnProtectionAttacker;
                    break;
            }
            return new DamageCheckResult(false, message, null);
        }
        if (playerManager.hasLoginProtection(victimData.uuid) || playerManager.hasTeleportProtection(victimData.uuid)) {
            String message = null;
            if (victimData.classifier == EntityClassifier.PLAYER) {
                message = config.cannotAttackTeleportOrSpawnProtectionVictim;
            }
            return new DamageCheckResult(false, message, null);
        }

        switch (playerManager.getForcedPvpState()) {
            case NONE:
            default:
                break;
            case DISABLED:
                return new DamageCheckResult(false, config.cannotAttackForcedPvpOff, null);
            case ENABLED:
                return new DamageCheckResult(true, null, null);
        }

        return new DamageCheckResult(true, null, null);
    }

    /**
     * Get the UUID of the actual player, being owner of a pet, shooter of a projectile, etc.
     *
     * @param entity Base entity to get the player UUID from
     * @return UUID of the actual player, null if not found
     */
    @Nullable
    private Target getUuidOfActualPlayer(@NotNull Entity entity) {
        if (entity instanceof Player) return new Target(entity.getUniqueId(), EntityClassifier.PLAYER);

        // Get shooter of projectile
        if (entity instanceof Projectile) {
            Projectile projectile = (Projectile) entity;
            if (projectile.getShooter() instanceof Player) {
                Player shooter = (Player) projectile.getShooter();
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
        if (entity instanceof Tameable) {
            Tameable tameable = (Tameable) entity;
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
        public final UUID uuid;
        public final EntityClassifier classifier;

        private Target(UUID uuid, EntityClassifier classifier) {
            this.uuid = uuid;
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
