package com.nations.discordlink;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;

public final class DiscordLink extends JavaPlugin {

    Logger log = this.getServer().getLogger();

    public FileConfiguration playerlinks;

    public HashMap<String, String> links;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getConfig().options().copyDefaults(true);
        saveConfig();
        loadLinks();
    }

    public void loadLinks(){
        File file = new File(this.getDataFolder(), "playerlinks.yml");
        playerlinks = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection sec = playerlinks.getConfigurationSection("links.map")
        if(sec.equals(null))
        {
            playerlinks.createSection("links.map");saveConfig();
        }else{
            links = new HashMap<String,String> playerlinks.get("links.map");
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
