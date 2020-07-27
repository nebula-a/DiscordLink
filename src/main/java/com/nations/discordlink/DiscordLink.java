package com.nations.discordlink;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public final class DiscordLink extends JavaPlugin {

    Logger log = this.getServer().getLogger();

    public FileConfiguration playerlinks;

    private File linksFile = new File(this.getDataFolder(), "playerlinks.yml");

    public HashMap<String, String> links;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getConfig().options().copyDefaults(true);
        saveConfig();
        loadLinks();
    }

    public String getStr(String path){
        return getConfig().getString(path);
    }

    public void loadLinks(){
        playerlinks = YamlConfiguration.loadConfiguration(linksFile);
        ConfigurationSection sec = playerlinks.getConfigurationSection("links.map");
        if(sec.equals(null))
        {
            playerlinks.createSection("links.map");saveConfig();
            links = new HashMap<>();
        }else{
            Map map = playerlinks.getMapList("links").get(0);
            links = (map instanceof HashMap)
                    ? (HashMap) map
                            : new HashMap<>();
        }
    }
    public void saveLinks(){
        for(String key : links.keySet()) playerlinks.set("links.map."+key, links.get(key));
        try {
            playerlinks.save(linksFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
