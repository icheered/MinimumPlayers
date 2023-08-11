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

        // Player quit event is fired before the player is removed from the online
        // players list
        if (event instanceof PlayerQuitEvent) {
            onlinePlayers--;
        }

        plugin.setCurrentNumberOfPlayers(onlinePlayers);

        int requiredPlayers = plugin.getMinimumNumberOfPlayers();

        if (onlinePlayers > requiredPlayers) {
            plugin.unlockServer();
            return;
        }

        String message = "";

        if (event instanceof PlayerJoinEvent) {
            if (onlinePlayers < requiredPlayers) {
                message = ChatColor.RED + plugin.getServerLockedMessage()
                        .replace("%currentPlayers%", String.valueOf(onlinePlayers))
                        .replace("%minimumPlayers%", String.valueOf(requiredPlayers));
            } else if (onlinePlayers == requiredPlayers) {
                message = ChatColor.AQUA + plugin.getPlayersIncreasedToMinimumMessage();
                plugin.unlockServer();
            }
        } else if (event instanceof PlayerQuitEvent) {
            if (onlinePlayers == requiredPlayers) {
                message = ChatColor.YELLOW + plugin.getPlayersDecreasedToMinimumMessage();
            } else if (onlinePlayers < requiredPlayers) {
                if (plugin.isServerFrozen()) {
                    message = ChatColor.RED + plugin.getServerLockedMessage()
                            .replace("%currentPlayers%", String.valueOf(onlinePlayers))
                            .replace("%minimumPlayers%", String.valueOf(requiredPlayers));
                } else {
                    plugin.lockServer();
                }
            }
        }

        // Send the message to all online players if there's a message to send
        if (!message.isEmpty()) {
            for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(message);
            }
        }
    }
}
