package com.nations.discordlink;

import com.nations.discordlink.commands.BanD;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public final class DiscordLink extends JavaPlugin implements CommandExecutor, TabCompleter {

    Logger log;

    public FileConfiguration playerlinks;
    public Bot bot;

    private File linksFile = new File(this.getDataFolder(), "playerlinks.yml");

    // Key: In-Game Name ,
    // Value: Discord User ID
    public HashMap<String, String> links;

    @Override
    public void onEnable() {
        // Plugin startup logic
        log = this.getServer().getLogger();
        getConfig().options().copyDefaults(true);
        saveConfig();
        loadLinks();
        this.bot = new Bot(this);
        this.getServer().getPluginCommand("band").setExecutor(new BanD(this));
        this.getServer().getPluginCommand("discordlink").setExecutor(this);
        if(getConfig().getBoolean("general-settings.auto-save")) {
            Long delay = Long.parseLong(String.valueOf(60 * 60 * 20 * getConfig().getInt("general-settings.saving-interval-hours")));
            BukkitTask task = Bukkit.getScheduler().runTaskLater(this, new Runnable() {
                @Override
                public void run() {
                    saveLinks();
                }
            }, delay);
        }
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
        saveLinks();
    }

    public void displayHelp(CommandSender sender){
        //
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        switch(args.length){
            case 0:
                displayHelp(sender);
            case 1:
                switch(args[0].toLowerCase()){
                    case "reloadconfig":
                        reloadConfig();
                // TODO: removeuser
                }
        }

        return true;

    }
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(command.getPermission().equals("discordlink.discordlink")){
            ArrayList<String> toReturn = new ArrayList<>();
            switch(args.length){
                case 0:
                    toReturn.add("reloadconfig");
            }
        }
        return null;
    }
}
