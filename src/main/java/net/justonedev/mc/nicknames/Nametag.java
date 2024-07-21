package net.justonedev.mc.nicknames;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.InternalStructure;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

/**
 * Minecraft version 1.18.2
 * @author <a href="https://gist.github.com/Ayouuuu">Ayouuuu</a>
 */
public class Nametag {

    PacketContainer packet;
    private final InternalStructure structure;
    private final Player player;

    public Nametag(Player player) {
        this.player = player;
        this.packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.SCOREBOARD_TEAM);
        String name = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        this.packet.getIntegers().write(0, 0);
        this.packet.getStrings().write(0, name);
        this.packet.getSpecificModifier(Collection.class).write(0, Collections.singletonList(player.getName()));
        this.structure = this.packet.getOptionalStructures().readSafely(0).get();
    }

    public Nametag setPrefix(String prefix) {
        this.structure.getChatComponents().write(1, WrappedChatComponent.fromText(ChatColor.translateAlternateColorCodes('&', prefix)));
        return this;
    }

    public Nametag color(ChatColor color) {
        this.structure.getEnumModifier(ChatColor.class, MinecraftReflection.getMinecraftClass("EnumChatFormat"))
                .write(0, color);
        return this;
    }

    public Nametag setSuffix(String suffix) {
        this.structure.getChatComponents().write(2, WrappedChatComponent.fromText(ChatColor.translateAlternateColorCodes('&', suffix)));
        return this;
    }

    public void build(Collection<? extends Player> players) {
        if (players.isEmpty()) return;
        this.structure.getIntegers().write(0, 3);
        this.packet.getOptionalStructures().write(0, Optional.of(structure));
        for (Player p : players) {
            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(p, packet);
            } catch (Exception e) {
                throw new RuntimeException("Cannot send packet " + packet, e);
            }
        }
    }

    public void build() {
        this.build(Bukkit.getOnlinePlayers());
    }
}
