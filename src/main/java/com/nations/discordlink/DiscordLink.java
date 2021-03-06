package com.nations.discordlink;

import com.nations.discordlink.commands.BanD;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import java.util.*;
import java.util.logging.Logger;

public final class DiscordLink extends JavaPlugin implements CommandExecutor, TabCompleter
{

    Logger log;
    private BukkitTask task;


    public Bot bot;
    public FileConfiguration playerlinks;
    private final File linksFile = new File(this.getDataFolder(), "playerlinks.yml");
    public boolean useUuid;
    // Key: In-Game Name , Value: Discord User ID
    public HashMap<String, String> links;
    // Key: Game UUID , Value: Discord User ID
    public HashMap<String, String> uuidlinks;


    public String RED = ChatColor.RED+"";
    public String GREEN = ChatColor.GREEN+"";
    public String BLUE = ChatColor.BLUE+"";
    public String YELLOW = ChatColor.YELLOW+"";
    public String GOLD = ChatColor.GOLD+"";
    public String AQUA = ChatColor.AQUA+"";
    public String DARKBLUE = ChatColor.DARK_BLUE+"";
    public String ULINE = ChatColor.UNDERLINE+"";
    public String RSET = ChatColor.RESET+"";

    private final String MESSAGE_LINKSSAVED = GREEN+"Links saved.";

    @Override
    public void onEnable()
    {
        // Plugin startup logic
        log = this.getServer().getLogger();
        getConfig().options().copyDefaults(true);
        saveConfig();
        loadLinks();
        this.bot = new Bot(this);
        Objects.requireNonNull(this.getServer().getPluginCommand("band")).setExecutor(new BanD(this));
        Objects.requireNonNull(this.getServer().getPluginCommand("discordlink")).setExecutor(this);
        if(getConfig().getBoolean("general-settings.auto-save"))
        {
            long delay = Long.parseLong(String.valueOf(60 * 60 * 20 * getConfig().getInt("general-settings.saving-interval-hours")));
            task = Bukkit.getScheduler().runTaskLater(this, this::saveLinks, delay);
            task.cancel();
        }
        Bukkit.getServer().getPluginManager().registerEvents(new GameEvents(this), this);
    }

    public String getStr(String path)
    {
        return getConfig().getString(path);
    }
    public boolean getBool(String path)
    {
        return getConfig().getBoolean(path);
    }
    public UUID getUUIDAsString(String uuid)
    {
        return UUID.fromString(uuid);
    }
    public void loadLinks()
    {
        useUuid = getBool("general-settings.use-uuids");
        playerlinks = YamlConfiguration.loadConfiguration(linksFile);
        if(useUuid)
        {
            ConfigurationSection sec = playerlinks.getConfigurationSection("links.uuid");
            if (sec == null)
            {
                playerlinks.createSection("links.uuid");
                saveConfig();
                uuidlinks = new HashMap<>();
            } else {
                for (String key : playerlinks.getKeys(false)) {
                    uuidlinks.put(key, playerlinks.getString("links.uuid." + key));
                }
            }
        }
        ConfigurationSection sec = playerlinks.getConfigurationSection("links.map");
        if (sec == null)
        {
            playerlinks.createSection("links.map");
            saveConfig();
            links = new HashMap<>();
        } else {
            for (String key : playerlinks.getKeys(false)) {
                links.put(key, playerlinks.getString("links.map." + key));
            }
        }
    }
    public void saveLinks()
    {
        if(useUuid){
            for(String key : uuidlinks.keySet()) playerlinks.set("links.uuid."+key, uuidlinks.get(key));
        }
        for(String key : links.keySet()) playerlinks.set("links.map."+key, links.get(key));
        try {
            playerlinks.save(linksFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable()
    {
        // Plugin shutdown logic
        task.cancel();
        saveLinks();
    }

    public void displayHelp(CommandSender sender)
    {
        String[] lines = {
                RED+"|"+AQUA+"--"+BLUE+"--"+DARKBLUE+ULINE+" Help "+RSET+BLUE+"--"+AQUA+"--"+RED+"|",
                RED+"|"+YELLOW+" /discordlink [option] <value1> <value2>",
                RED+"|"+GOLD+" ( /discordlink -h for more help ).",
                RED+"|"+YELLOW+" /band [ign] [days of msg history to delete]",
                RED+"|"+GOLD+" Ban a player's linked discord account from the Discord server.",
                RED+"|"+AQUA+"--"+BLUE+"--"+DARKBLUE+"------"+RSET+BLUE+"--"+AQUA+"--"+RED+"|"
        };
        for(String line : lines){
            sender.sendMessage(line);
        }
    }
    public void displayDLHelp(CommandSender sender)
    {
        String[] lines = {
                RED+"|"+AQUA+"--"+BLUE+"--"+DARKBLUE+ULINE+" Help "+RSET+BLUE+"--"+AQUA+"--"+RED+"|",
                RED+"|"+YELLOW+" /discordlink [option] <value1> <value2>",
                RED+"|"+GOLD+"  Option: -h Function: Displays this message.",
                RED+"|"+YELLOW+" /discordlink linkuser [ign] [discord id]",
                RED+"|"+GOLD+" Manually link a Discord user and their MC username.",
                RED+"|"+YELLOW+" /discordlink savelinks",
                RED+"|"+GOLD+" Manually save the links.",
                RED+"|"+YELLOW+" /discordlink remuser [ign]",
                RED+"|"+GOLD+" Manually remove a user link.",
                RED+"|"+AQUA+"--"+BLUE+"--"+DARKBLUE+"------"+RSET+BLUE+"--"+AQUA+"--"+RED+"|"
        };
        for(String line : lines)
        {
            sender.sendMessage(line);
        }

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {

        switch(args.length)
        {
            case 0:// NO ARGUMENTS PROVIDED
                displayHelp(sender);
            case 1:// 1 ARGUMENT PROVIDED (/dl [1])
                if(args[0].startsWith("-"))
                {
                    char opt = args[0].toLowerCase().charAt(1);
                    if(opt == 'h')
                    {
                        displayDLHelp(sender);
                    }// convert to switch statement if adding more
                }
                switch(args[0].toLowerCase())
                {
                    case "help":
                        displayHelp(sender);
                        break;
                    case "savelinks":
                        saveLinks();
                        sender.sendMessage(MESSAGE_LINKSSAVED);
                        break;
                    case "reloadconfig":
                        reloadConfig();
                        break;
                    default:
                        sender.sendMessage("Unknown argument: " + args[0].toLowerCase());
                        break;
                }
            case 2:// 2 ARGUMENTS PROVIDED (/dl [1] [2])
                switch(args[0].toLowerCase())
                {
                    case "remuser":
                        links.remove(args[1]);
                        break;
                    default:
                        sender.sendMessage("Unknown argument: " + args[0].toLowerCase());
                        break;
                }
            case 3:// 3 ARGUMENTS PROVIDED (/dl [1] [2] [3])
                switch(args[0].toLowerCase())
                {
                    case "linkuser":
                        // /discordlink linkuser [ign] [discordid]
                        links.put(args[1], args[2]);
                }
        }

        return true;

    }
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args)
    {
            try
            {
                ArrayList<String> toReturn = new ArrayList<>();
                if (Objects.equals(command.getPermission(), "discordlink.discordlink"))
                {
                    if (args.length == 0) {
                        toReturn.add("reloadconfig");
                    }// convert to switch statement if adding more
                }
                return toReturn;
            }catch(NullPointerException ignore){}
        return null;
    }

    public UUID convertToUuid(String username)
    {
        return Objects.requireNonNull(Bukkit.getPlayer(username)).getUniqueId();
    }
    public String convertFromUuid(UUID uuid)
    {
        return Bukkit.getOfflinePlayer(uuid).getName();
    }
}
