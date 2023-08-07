package com.icheered.minimumplayers;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/*
 * minimumplayers java plugin
 */
public class Plugin extends JavaPlugin {
    private static final Logger LOGGER = Logger.getLogger("minimumplayers");
    private int minimumNumberOfPlayers;

    @Override
    public void onEnable() {
        // Ensure that config.yml is present (either load it or create with defaults)
        saveDefaultConfig();

        // Load minimumNumberOfPlayers from the config file.
        // If not found, it will use the default value 3.
        minimumNumberOfPlayers = getConfig().getInt("minimumNumberOfPlayers", 3);

        LOGGER.info("minimumplayers enabled with minimum number of players: " + minimumNumberOfPlayers);

        // Register the player movement listener
        Bukkit.getPluginManager().registerEvents(new PlayerMovementListener(this), this);

    }

    @Override
    public void onDisable() {
        LOGGER.info("minimumplayers disabled");
    }

    public int getMinimumNumberOfPlayers() {
        return minimumNumberOfPlayers;
    }
}
