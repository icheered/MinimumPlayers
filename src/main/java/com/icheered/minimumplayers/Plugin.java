package com.icheered.minimumplayers;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.ChatColor;

public class Plugin extends JavaPlugin {
    private static final Logger LOGGER = Logger.getLogger("minimumplayers");
    private static final int DEFAULT_MINIMUM_PLAYERS = 3;

    private BukkitTask freezeTask;
    public boolean isServerFrozen = true;

    private int minimumNumberOfPlayers;

    // Newly added fields
    private boolean allowGracePeriod;
    private int gracePeriodMinutes;
    private boolean sendGracePeriodMessage;
    private String gracePeriodMessage;
    private boolean sendMessageWhenFrozen;
    private String frozenMessage;
    private int frozenMessageIntervalSeconds;
    private String serverLockedMessage;
    private String playersIncreasedToMinimumMessage;
    private String playersDecreasedToMinimumMessage;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        minimumNumberOfPlayers = getConfig().getInt("minimumNumberOfPlayers", DEFAULT_MINIMUM_PLAYERS);

        // Load new configuration values
        allowGracePeriod = getConfig().getBoolean("allowGracePeriod", true);
        gracePeriodMinutes = getConfig().getInt("gracePeriodMinutes", 5);
        sendGracePeriodMessage = getConfig().getBoolean("sendGracePeriodMessage", true);
        gracePeriodMessage = getConfig().getString("gracePeriodMessage");
        sendMessageWhenFrozen = getConfig().getBoolean("sendMessageWhenFrozen", true);
        frozenMessage = getConfig().getString("frozenMessage");
        frozenMessageIntervalSeconds = getConfig().getInt("frozenMessageIntervalSeconds", 5);
        serverLockedMessage = getConfig().getString("serverLockedMessage");
        playersIncreasedToMinimumMessage = getConfig().getString("playersIncreasedToMinimumMessage");
        playersDecreasedToMinimumMessage = getConfig().getString("playersDecreasedToMinimumMessage");

        LOGGER.info("minimumplayers enabled with minimum number of players: " + minimumNumberOfPlayers);

        Bukkit.getPluginManager().registerEvents(new PlayerMovementHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new ServerEventsHandler(this), this);
    }

    @Override
    public void onDisable() {
        LOGGER.info("minimumplayers disabled");
    }

    public int getMinimumNumberOfPlayers() {
        return minimumNumberOfPlayers;
    }

    // Newly added getters
    public boolean isAllowGracePeriod() {
        return allowGracePeriod;
    }

    public int getGracePeriodMinutes() {
        return gracePeriodMinutes;
    }

    public boolean isSendGracePeriodMessage() {
        return sendGracePeriodMessage;
    }

    public String getGracePeriodMessage() {
        return gracePeriodMessage;
    }

    public boolean isSendMessageWhenFrozen() {
        return sendMessageWhenFrozen;
    }

    public String getFrozenMessage() {
        return frozenMessage;
    }

    public int getFrozenMessageIntervalSeconds() {
        return frozenMessageIntervalSeconds;
    }

    public String getServerLockedMessage() {
        return serverLockedMessage;
    }

    public String getPlayersIncreasedToMinimumMessage() {
        return playersIncreasedToMinimumMessage;
    }

    public String getPlayersDecreasedToMinimumMessage() {
        return playersDecreasedToMinimumMessage;
    }

    public void startFreezeTimer() {
        if (!isAllowGracePeriod()) {
            // If grace period isn't allowed, freeze the server immediately
            isServerFrozen = true;
            return;
        }

        if (freezeTask != null) {
            freezeTask.cancel();
        }

        if (isSendGracePeriodMessage()) {
            String message = getGracePeriodMessage().replace("%minutes%", String.valueOf(getGracePeriodMinutes()));
            for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(ChatColor.YELLOW + message);
            }
        }

        freezeTask = Bukkit.getScheduler().runTaskLater(this, new Runnable() {
            @Override
            public void run() {
                isServerFrozen = true;
            }
        }, 20L * 60 * getGracePeriodMinutes()); // Using the grace period from config
    }

    public void cancelFreezeTimer() {
        if (freezeTask != null) {
            freezeTask.cancel();
            freezeTask = null;
        }
        isServerFrozen = false;
    }

    public boolean isServerFrozen() {
        return isServerFrozen;
    }

}
