package com.nations.discordlink;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.Objects;

public class BotEvents extends ListenerAdapter {

    //private Bot bot;
    private final DiscordLink plugin;
    private TextChannel vfc;

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent e) {
        super.onGuildMessageReceived(e);

        if(e.getChannel().getId().equals(vfc.getId()))
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

    public BotEvents(Bot bot) {
        this.plugin = bot.plugin;
        try {
            vfc = bot.guild.getTextChannelById(Objects.requireNonNull(bot.config.getString("validation.receive-in-channel-id")));
        }catch(NullPointerException e){
            plugin.log.warning("Config setting validation.receive-in-channel-id is null!");
        }
    }
}
