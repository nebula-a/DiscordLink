package com.nations.discordlink;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.configuration.file.FileConfiguration;

import javax.security.auth.login.LoginException;
import java.util.Objects;

public class Bot {

    public DiscordLink plugin;
    public FileConfiguration config;
    public JDA jda;
    public Guild guild;

    public Bot(DiscordLink plugin){
        this.plugin = plugin;
        this.config = plugin.getConfig();
        try {
            JDABuilder jb = JDABuilder.createDefault(plugin.getStr("general-settings.discord-bot-token"));
            jb.enableIntents(GatewayIntent.getIntents(GatewayIntent.DEFAULT));
            BotEvents botEvents = new BotEvents(this);
            jda = jb.build();
            jda.addEventListener(botEvents);
            jda = jda.awaitReady();
            this.guild = jda.getGuildById(Objects.requireNonNull(config.getString("general-settings.guild-id")));
        } catch (LoginException | InterruptedException | NullPointerException e) {
            e.printStackTrace();
        }
    }
    public void banUser(String userId, int delDays){
        guild.ban(Objects.requireNonNull(jda.getUserById(userId)), delDays).queue();
    }
    public boolean banOnDiscord(String ign, int delDays){
        if(plugin.links.containsKey(ign)){
            banUser(plugin.links.get(ign), delDays);
            return true;
        }
        return false;
    }



}
