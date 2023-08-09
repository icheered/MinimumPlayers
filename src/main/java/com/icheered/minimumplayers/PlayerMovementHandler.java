package com.icheered.minimumplayers;

import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import java.util.HashMap;
import java.util.UUID;

public class PlayerMovementHandler implements Listener {
    private final Plugin plugin;
    private final HashMap<UUID, Long> lastMessageTime = new HashMap<>();

    public PlayerMovementHandler(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        long currentTimeMillis = System.currentTimeMillis();

        // Check if the server is frozen, instead of the number of online players
        if (plugin.isServerFrozen() && hasMoved(event.getFrom(), event.getTo())) {
            event.setCancelled(true);

            if (plugin.isSendMessageWhenFrozen() && (!lastMessageTime.containsKey(playerId)
                    || (currentTimeMillis - lastMessageTime.get(playerId) > plugin.getFrozenMessageIntervalSeconds()
                            * 1000))) {
                int onlinePlayers = Bukkit.getOnlinePlayers().size();
                int requiredPlayers = plugin.getMinimumNumberOfPlayers();

                String message = plugin.getFrozenMessage()
                        .replace("%currentPlayers%", String.valueOf(onlinePlayers))
                        .replace("%minimumPlayers%", String.valueOf(requiredPlayers));

                player.sendMessage(ChatColor.RED + message);

                lastMessageTime.put(playerId, currentTimeMillis);
            }
        }
    }

    private boolean hasMoved(Location from, Location to) {
        return from.getBlockX() != to.getBlockX()
                || from.getBlockY() != to.getBlockY()
                || from.getBlockZ() != to.getBlockZ();
    }
}
