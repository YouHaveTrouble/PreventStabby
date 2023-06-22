package me.youhavetrouble.preventstabby.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.util.PluginMessages;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlaceholderApiHook extends PlaceholderExpansion {

    private final PreventStabby plugin;
    private final LegacyComponentSerializer legacyComponentSerializer;

    public PlaceholderApiHook(PreventStabby preventStabby) {
        plugin = preventStabby;
        this.legacyComponentSerializer = LegacyComponentSerializer.legacyAmpersand();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "preventstabby";
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().get(0);
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        switch (params) {
            case "pvp":
                if (!player.isOnline()) return String.valueOf(false);
                return String.valueOf(plugin.getPlayerManager().getPlayer(player.getUniqueId()).isPvpEnabled());
            case "combat_time":
                return getCombatTimePlaceholder(player.getUniqueId(), player);
            case "in_combat":
                if (!player.isOnline()) return String.valueOf(false);
                return String.valueOf(plugin.getPlayerManager().getPlayer(player.getUniqueId()).isInCombat());
            case "pvp_forced":
                switch (PreventStabby.getPlugin().getPlayerManager().getForcedPvpState()) {
                    case NONE:
                        return legacyComponentSerializer.serialize(PluginMessages.parseMessage(plugin.getConfigCache().getPlaceholder_pvp_forced_none()));
                    case ENABLED:
                        return legacyComponentSerializer.serialize(PluginMessages.parseMessage(plugin.getConfigCache().getPlaceholder_pvp_forced_true()));
                    case DISABLED:
                        return legacyComponentSerializer.serialize(PluginMessages.parseMessage(plugin.getConfigCache().getPlaceholder_pvp_forced_false()));
                }
            default:
                return null;
        }
    }

    private String getCombatTimePlaceholder(UUID uuid, OfflinePlayer player) {
        if (!player.isOnline()) {
            return legacyComponentSerializer.serialize(PluginMessages.parseMessage(plugin.getConfigCache().getPlaceholder_not_in_combat()));
        }
        long seconds = plugin.getPlayerManager().getPlayer(uuid).getCombatTime();
        if (seconds > 0) {
            String msg = plugin.getConfigCache().getPlaceholder_combat_time();
            msg = msg.replaceAll("%time%", String.valueOf(seconds));
            return legacyComponentSerializer.serialize(PluginMessages.parseMessage(msg));
        }
        return legacyComponentSerializer.serialize(PluginMessages.parseMessage(plugin.getConfigCache().getPlaceholder_not_in_combat()));
    }

}
