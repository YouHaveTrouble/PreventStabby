package me.youhavetrouble.preventstabby.api;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.data.DamageCheckResult;
import me.youhavetrouble.preventstabby.util.PvpState;
import org.bukkit.entity.Entity;

public class PreventStabbyAPI {

    /**
     * @return Current state of forced PvP
     */
    public static PvpState getForcedPvpState() {
        return PreventStabby.getPlugin().getPlayerManager().getForcedPvpState();
    }

    /**
     * Sets forced PvP state until server restart or changed with override command or this method.
     */
    public static void setForcedPvpState(PvpState newForcedPvpState) {
        PreventStabby.getPlugin().getPlayerManager().setForcedPvpState(newForcedPvpState);
    }

    public static DamageCheckResult canDamage(Entity attacker, Entity victim) {
        return PreventStabby.getPlugin().getPlayerManager().canDamage(attacker, victim);
    }

}

