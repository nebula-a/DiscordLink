package com.nations.discordlink.commands;

import com.nations.discordlink.DiscordLink;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class BanD implements CommandExecutor {

    private DiscordLink plugin;

    private static final String PREFIX = ChatColor.WHITE+"["+ChatColor.RED+"/!\\"+ChatColor.WHITE;
    private static final String FAIL = PREFIX+ChatColor.GREEN+" That member is not linked.";

    public BanD(DiscordLink plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length < 1) return false;
        try{
            int delDays = Integer.parseInt(args[1]);
            boolean success = plugin.bot.banOnDiscord(args[0], delDays);
            if(!success) sender.sendMessage(FAIL);
            else{
                String user = plugin.bot.jda.getUserById(plugin.links.get(args[0])).getName();
                sender.sendMessage(ChatColor.GREEN+"Banned "+user+".");
            }
        }
        catch(NumberFormatException e){
        }
        return true;
    }
}
