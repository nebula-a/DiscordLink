package com.nations.discordlink;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.configuration.file.FileConfiguration;

import javax.security.auth.login.LoginException;

public class Bot {

    public DiscordLink plugin;
    public FileConfiguration config;
    public JDA jda;
    public Guild guild;
    private BotEvents botEvents;

    public Bot(DiscordLink plugin){
        this.plugin = plugin;
        this.config = plugin.getConfig();
        try {
            JDABuilder jb = JDABuilder.createDefault("");
            jb.enableIntents(GatewayIntent.getIntents(GatewayIntent.DEFAULT));
            botEvents = new BotEvents(this);
            jda = jb.build();
            jda.addEventListener(botEvents);
            jda = jda.awaitReady();
            this.guild = jda.getGuildById(config.getString("general-settings.guild-id"));
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }
    }



}
