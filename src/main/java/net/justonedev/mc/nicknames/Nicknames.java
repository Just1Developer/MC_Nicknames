package net.justonedev.mc.nicknames;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author JustOneDeveloper
 */
public final class Nicknames extends JavaPlugin implements Listener {

    private ProtocolManager protocolManager;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        protocolManager = ProtocolLibrary.getProtocolManager();
        //registerPacketListeners();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Nametag tag = new Nametag(player);
        tag.setPrefix("§7[§cAdmin§7] ").build();
    }

    /*
    private void registerPacketListeners() {
        protocolManager.addPacketListener(new PacketAdapter(this, PacketType.Play.Server.PLAYER_INFO) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if (event.getPacket().getPlayerInfoDataLists().size() > 0) {
                    List<PlayerInfoData> playerInfoDataList = event.getPacket().getPlayerInfoDataLists().read(0);
                    List<PlayerInfoData> modifiedPlayerInfoDataList = playerInfoDataList.stream()
                            .map(data -> {
                                Player player = getServer().getPlayer(data.getProfile().getName());
                                String prefix = (player != null) ? getPrefix(player) : "";
                                return new PlayerInfoData(
                                        data.getProfile(),
                                        data.getLatency(),
                                        data.getGameMode(),
                                        WrappedChatComponent.fromText(prefix + data.getProfile().getName())
                                );
                            }).collect(Collectors.toList());

                    event.getPacket().getPlayerInfoDataLists().write(0, modifiedPlayerInfoDataList);
                }
            }
        });

        protocolManager.addPacketListener(new PacketAdapter(this, PacketType.Play.Server.SCOREBOARD_TEAM) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if (event.getPacket().getStringArrays().size() > 0) {
                    String[] entries = event.getPacket().getStringArrays().read(0);
                    String[] modifiedEntries = new String[entries.length];
                    for (int i = 0; i < entries.length; i++) {
                        Player player = getServer().getPlayer(entries[i]);
                        String prefix = (player != null) ? getPrefix(player) : "";
                        modifiedEntries[i] = prefix + entries[i];
                    }
                    event.getPacket().getStringArrays().write(0, modifiedEntries);
                }
            }
        });

        protocolManager.addPacketListener(new PacketAdapter(this, PacketType.Play.Server.CHAT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                Player player = event.getPlayer();
                String prefix = getPrefix(player);
                WrappedChatComponent chatComponent = event.getPacket().getChatComponents().read(0);
                String message = chatComponent.getJson();
                event.getPacket().getChatComponents().write(0, WrappedChatComponent.fromText(prefix + message));
            }
        });
    }

    private String getPrefix(Player player) {
        // Replace this with your own logic to get the player's prefix
        return "[Prefix] ";
    }

     */

/* v2
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        setPlayerPrefix(player, "§6[Prefix]§r ");
    }

    private void setPlayerPrefix(Player player, String prefix) {
        String displayName = prefix + player.getName();

        // Update tab list name
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            sendTabListPacket(onlinePlayer, player, displayName);
            sendNameTagPacket(onlinePlayer, player, displayName);
        }
    }

    private void sendTabListPacket(Player recipient, Player target, String displayName) {
        WrappedGameProfile profile = WrappedGameProfile.fromPlayer(target);
        WrappedChatComponent displayNameComponent = WrappedChatComponent.fromText(displayName);

        PlayerInfoData playerInfoData = new PlayerInfoData(
                profile,
                0,
                EnumWrappers.NativeGameMode.SURVIVAL,
                displayNameComponent
        );

        PacketContainer tabPacket = protocolManager.createPacket(PacketType.Play.Server.PLAYER_INFO);
        tabPacket.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.UPDATE_DISPLAY_NAME);
        tabPacket.getPlayerInfoDataLists().write(0, Collections.singletonList(playerInfoData));

        try {
            protocolManager.sendServerPacket(recipient, tabPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendNameTagPacket(Player recipient, Player target, String displayName) {
        PacketContainer entityPacket = protocolManager.createPacket(PacketType.Play.Server.ENTITY_METADATA);
        entityPacket.getIntegers().write(0, target.getEntityId());

        WrappedDataWatcher watcher = new WrappedDataWatcher(target);
        WrappedDataWatcher.WrappedDataWatcherObject displayNameObject = new WrappedDataWatcher.WrappedDataWatcherObject(2, WrappedDataWatcher.Registry.getChatComponentSerializer(true));
        watcher.setObject(displayNameObject, Optional.of(WrappedChatComponent.fromText(displayName).getHandle()));

        entityPacket.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());

        try {
            protocolManager.sendServerPacket(recipient, entityPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

 */

    /*
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        setPlayerPrefix(player, "§6[Prefix]§r ");
    }

    private void setPlayerPrefix(Player player, String prefix) {
        String displayName = prefix + player.getName();

        // Update tab list name
        PacketContainer tabPacket = protocolManager.createPacket(PacketType.Play.Server.PLAYER_INFO);
        tabPacket.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.ADD_PLAYER);
        WrappedGameProfile profile = WrappedGameProfile.fromPlayer(player);
        profile.getProperties().put("displayName", new WrappedSignedProperty("displayName", displayName));
        tabPacket.getPlayerInfoDataLists().write(0, List.of(new PlayerInfoData(profile, 0, EnumWrappers.NativeGameMode.SURVIVAL, WrappedChatComponent.fromText(displayName))));
        protocolManager.sendServerPacket(player, tabPacket);

        // Update nametag above head
        PacketContainer entityPacket = protocolManager.createPacket(PacketType.Play.Server.NAMED_ENTITY_SPAWN);
        entityPacket.getIntegers().write(0, player.getEntityId());
        entityPacket.getUUIDs().write(0, player.getUniqueId());
        entityPacket.getDoubles().write(0, player.getLocation().getX());
        entityPacket.getDoubles().write(1, player.getLocation().getY());
        entityPacket.getDoubles().write(2, player.getLocation().getZ());
        entityPacket.getBytes().write(0, (byte) ((int) (player.getLocation().getYaw() * 256.0F / 360.0F)));
        entityPacket.getBytes().write(1, (byte) ((int) (player.getLocation().getPitch() * 256.0F / 360.0F)));
        entityPacket.getDataWatcherModifier().write(0, player.getDataWatcher());
        protocolManager.sendServerPacket(player, entityPacket);
    }*/

}
