package me.youhavetrouble.preventstabby.api;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.players.PlayerData;
import me.youhavetrouble.preventstabby.util.CombatTimer;
import me.youhavetrouble.preventstabby.util.PvpState;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.UUID;

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

}

