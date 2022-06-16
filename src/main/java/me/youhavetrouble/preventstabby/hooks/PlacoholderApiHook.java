package me.youhavetrouble.preventstabby.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.util.PluginMessages;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.UUID;

public class PlacoholderApiHook extends PlaceholderExpansion {

    private final PreventStabby plugin;
    private final LegacyComponentSerializer legacyComponentSerializer;

    public PlacoholderApiHook(PreventStabby preventStabby) {
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
        if (params.equalsIgnoreCase("combat_time")) {
            return getCombatTimePlaceholder(player.getUniqueId());
        }
        if (params.equalsIgnoreCase("in_combat")) {
            return String.valueOf(plugin.getPlayerManager().getPlayer(player.getUniqueId()).isInCombat());
        }
        return null;
    }

    private String getCombatTimePlaceholder(UUID uuid) {
        long seconds = plugin.getPlayerManager().getPlayer(uuid).getCombattime() - Instant.now().getEpochSecond();
        if (seconds > 0) {
            String msg = plugin.getConfigCache().getPlaceholder_combat_time();
            msg = msg.replaceAll("%time%", String.valueOf(seconds));
            return legacyComponentSerializer.serialize(PluginMessages.parseMessage(msg));
        }
        return legacyComponentSerializer.serialize(PluginMessages.parseMessage(plugin.getConfigCache().getPlaceholder_not_in_combat()));
    }

}
