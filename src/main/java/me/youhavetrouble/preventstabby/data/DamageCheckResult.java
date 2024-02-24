package me.youhavetrouble.preventstabby.data;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * @param ableToDamage  Result of the damage check
 * @param feedbackForAttacker Feedback to send to the attacker
 * @param feedbackForVictim Feedback to send to the victim
 * @param attackerId UUID of attacker player
 * @param victimId UUID of victim player
 */
public record DamageCheckResult(
        boolean ableToDamage,
        @Nullable UUID attackerId,
        @Nullable UUID victimId,
        @Nullable String feedbackForAttacker,
        @Nullable String feedbackForVictim
) {

    public static DamageCheckResult positive() {
        return new DamageCheckResult(
                true,
                null,
                null,
                null,
                null
        );
    }

    public static DamageCheckResult positive(UUID attackerId, UUID victimId) {
        return new DamageCheckResult(
                true,
                attackerId,
                victimId,
                null,
                null
        );
    }

}