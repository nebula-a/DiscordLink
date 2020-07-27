package com.nations.discordlink;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.configuration.file.FileConfiguration;

import javax.annotation.Nonnull;

public class BotEvents<validationFromChannel> extends ListenerAdapter {

    private Bot bot;
    private DiscordLink plugin;
    private TextChannel vfc;

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent e) {
        super.onGuildMessageReceived(e);

        if(e.getChannel().getId().equals(vfc))
        {
            User user = e.getAuthor();
            String content = e.getMessage().getContentRaw();
            String prefix = plugin.getStr("validation.prefix");
            if(content.startsWith(prefix))
            {
                plugin.links.put(content.substring(prefix.length()), user.getId());
            }
        }

    }

    public BotEvents(Bot bot){
        this.bot = bot;
        this.plugin = bot.plugin;
        vfc = bot.guild.getTextChannelById(bot.config.getString("validation.receive-in-channel-id"));
    }

}
