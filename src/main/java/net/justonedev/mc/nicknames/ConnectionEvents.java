package net.justonedev.mc.nicknames;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ConnectionEvents implements Listener {

    @EventHandler
    public void onConnect(PlayerJoinEvent event) {
        Nicknames.prefixHandler.setNickname(event.getPlayer());
    }

}
