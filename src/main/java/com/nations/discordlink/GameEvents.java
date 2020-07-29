package com.nations.discordlink;

import okhttp3.EventListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class GameEvents implements Listener {

    private DiscordLink discordLink;

    public GameEvents(DiscordLink mainPlugin)
    {
        this.discordLink = mainPlugin;
    }

    // CHECK FOR USERNAME CHANGE & UPDATE W/ NEW USERNAME
    @EventHandler
    public void onAsyncPlayerJoinEvent(AsyncPlayerChatEvent e)
    {
        if(discordLink.useUuid)
        {
            String username = e.getPlayer().getName();
            String uuid = e.getPlayer().getUniqueId().toString();
            if (discordLink.uuidlinks.containsKey(uuid)
                    && !(discordLink.links.containsKey(username)))
            {
                discordLink.links.put(username, discordLink.uuidlinks.get(uuid));
            }
        }
    }

}
