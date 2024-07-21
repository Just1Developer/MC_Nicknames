package net.justonedev.mc.nicknames;

import net.justonedev.mc.nicknames.item.NicknameItem;
import net.justonedev.mc.nicknames.prefix.PrefixHandler;
import net.justonedev.mc.nicknames.prefix.scoreboard.ScoreboardPrefixHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

/**
 * @author JustOneDeveloper
 */
public final class Nicknames extends JavaPlugin implements Listener {

    public static Nicknames singleton;
    public static PrefixHandler prefixHandler;
    public static Material PLUGIN_MATERIAL = Material.NAME_TAG;

    /**
     * An immutable list of all nicknames.
     */
    public List<String> allNicknames;

    @Override
    public void onEnable() {
        singleton = this;
        allNicknames = List.of("§7[§cMiner§7] §f", "§7[§eExplorer§7] §f", "§7[§bGobbler§7] §f", "§7[§cG§6o§eb§ab§2l§3e§9d§be§5g§do§co§6k§7] §f");    // Todo properly do this, of course. And before making teams

        prefixHandler = new ScoreboardPrefixHandler();
        for (Player p : Bukkit.getOnlinePlayers()) {
            prefixHandler.setNickname(p);
        }

        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new ConnectionEvents(), this);

        NicknameItem nickNameItem = new NicknameItem();

        getCommand("createnickname").setExecutor(nickNameItem);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    int i = 0;
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        i %= allNicknames.size();

        prefixHandler.setNickname(event.getPlayer(), allNicknames.get(i));

        ++i;
    }
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        i %= allNicknames.size();

        prefixHandler.setNickname(event.getPlayer(), allNicknames.get(i));

        ++i;
    }
}
