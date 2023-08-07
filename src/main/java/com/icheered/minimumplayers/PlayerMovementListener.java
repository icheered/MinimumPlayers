package com.icheered.minimumplayers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.Location;

public class PlayerMovementListener implements Listener {

    private final Plugin plugin;

    public PlayerMovementListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (Bukkit.getOnlinePlayers().size() < plugin.getMinimumNumberOfPlayers()) {
            if (hasMoved(event.getFrom(), event.getTo())) {
                event.setCancelled(true);
            }
        }
    }

    private boolean hasMoved(Location from, Location to) {
        return from.getBlockX() != to.getBlockX()
                || from.getBlockY() != to.getBlockY()
                || from.getBlockZ() != to.getBlockZ();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        int onlinePlayers = Bukkit.getOnlinePlayers().size();
        int requiredPlayers = plugin.getMinimumNumberOfPlayers();

        if (onlinePlayers <= requiredPlayers) {
            String message;

            // Case: Players are less than minimum
            if (onlinePlayers < requiredPlayers) {
                message = ChatColor.AQUA + "There are " + onlinePlayers + " players online. "
                        + requiredPlayers + " players are required to unlock the server.";
            }
            // Case: Server just got unlocked
            else if (onlinePlayers == requiredPlayers && onlinePlayers - 1 == requiredPlayers - 1) {
                message = ChatColor.GREEN + "The server is now unlocked!";
            }
            // Case: More people leaving will lock the server
            else if (onlinePlayers == requiredPlayers && onlinePlayers + 1 == requiredPlayers + 1) {
                message = ChatColor.YELLOW + "If more people leave, the server will be locked.";
            } else {
                return; // Exit the method early for other cases.
            }

            // Send the message to all online players
            for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(message);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        int onlinePlayers = Bukkit.getOnlinePlayers().size() - 1; // Subtracting one for the leaving player.
        int requiredPlayers = plugin.getMinimumNumberOfPlayers();

        if (onlinePlayers <= requiredPlayers) {
            String message;

            // Case: Players are less than minimum after the current player quits
            if (onlinePlayers < requiredPlayers) {
                message = ChatColor.AQUA + "There are " + onlinePlayers + " players online. "
                        + requiredPlayers + " players are required to unlock the server.";
            }
            // Case: Server will be locked if another player quits
            else if (onlinePlayers == requiredPlayers) {
                message = ChatColor.YELLOW + "If more people leave, the server will be locked.";
            } else {
                return; // Exit the method early for other cases.
            }

            // Send the message to all online players
            for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(message);
            }
        }
    }

}
