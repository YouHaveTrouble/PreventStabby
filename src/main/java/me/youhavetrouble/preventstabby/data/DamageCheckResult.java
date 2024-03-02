package me.youhavetrouble.preventstabby.data;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * @param ableToDamage  Result of the damage check
 * @param feedbackForAttacker Feedback to send to the attacker
 * @param attackerId UUID of attacker player
 * @param victimId UUID of victim player
 */
public record DamageCheckResult(
        boolean ableToDamage,
        @Nullable UUID attackerId,
        @Nullable UUID victimId,
        @Nullable String feedbackForAttacker,
        boolean shouldVictimBePutInCombat
) {

    public static DamageCheckResult positive() {
        return new DamageCheckResult(
                true,
                null,
                null,
                null,
                true
        );
    }

    public static DamageCheckResult positive(UUID attackerId, UUID victimId, boolean shouldVictimBePutInCombat) {
        return new DamageCheckResult(
                true,
                attackerId,
                victimId,
                null,
                shouldVictimBePutInCombat
        );
    }

}