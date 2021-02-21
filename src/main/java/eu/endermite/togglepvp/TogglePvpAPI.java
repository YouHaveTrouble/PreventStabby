package eu.endermite.togglepvp;

import java.util.UUID;

public class TogglePvpAPI {

    /**
     * Sets player PvP state. This will always save to database.
     * @param uuid uuid of player to set
     * @param newState State to set
     */
    public static void setPvpEnabled(UUID uuid, boolean newState) {
        TogglePvp.getPlugin().getSmartCache().setPlayerPvpState(uuid, newState);
    }

    /**
     * Gets player's PvP state. If player is not cached this will query the database.
     * @param uuid uuid of player to get data from.
     * @return true if enabled, false if disabled
     */
    public static boolean getPvpEnabled(UUID uuid) {
        return TogglePvp.getPlugin().getSmartCache().getPlayerData(uuid).isPvpEnabled();
    }


}
