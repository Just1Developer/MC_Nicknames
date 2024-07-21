package net.justonedev.mc.nicknames.prefix;

import org.bukkit.entity.Player;

public interface PrefixHandler {

    /**
     * Called when a player joins, makes all prefixes visible to the given player.
     * @param p The player.
     */
    void setNickname(Player p);

    /**
     * Updates a given players nickname to the provided nickname for all players to see.
     * @param p The player.
     * @param nickname The nickname.
     */
    void setNickname(Player p, String nickname);
}
