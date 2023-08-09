package com.icheered.minimumplayers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ServerEventsHandler implements Listener {
    private final Plugin plugin;

    public ServerEventsHandler(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        handlePlayerCountChange(event);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        handlePlayerCountChange(event);
    }

    private void handlePlayerCountChange(Object event) {
        int onlinePlayers = Bukkit.getOnlinePlayers().size();
        int requiredPlayers = plugin.getMinimumNumberOfPlayers();

        if (onlinePlayers > requiredPlayers) {
            return;
        }

        String message = plugin.getServerLockedMessage()
                .replace("%currentPlayers%", String.valueOf(onlinePlayers))
                .replace("%minimumPlayers%", String.valueOf(requiredPlayers));

        if (event instanceof PlayerQuitEvent && onlinePlayers < requiredPlayers) {
            // Only when a player quits and the count goes below the minimum
            if (!plugin.isServerFrozen()) {
                plugin.startFreezeTimer();
            }
        } else if (onlinePlayers == requiredPlayers) {
            // If the server is frozen, cancel the freeze timer to unfreeze it
            if (plugin.isServerFrozen()) {
                plugin.cancelFreezeTimer();
            }

            if (event instanceof PlayerJoinEvent) {
                message = plugin.getPlayersIncreasedToMinimumMessage();
            } else if (event instanceof PlayerQuitEvent) {
                message = plugin.getPlayersDecreasedToMinimumMessage();
            }
        } else if (event instanceof PlayerJoinEvent && onlinePlayers < requiredPlayers) {
            // If a player joins and the count is still below the required, freeze
            // immediately without timer
            plugin.isServerFrozen = true;
        }

        // Send the message to all online players
        for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(ChatColor.AQUA + message);
        }

        // Log the message to the server log
        plugin.getLogger().info(message);
    }

}
