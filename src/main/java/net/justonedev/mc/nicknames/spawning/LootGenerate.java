package net.justonedev.mc.nicknames.spawning;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.LootGenerateEvent;

import java.util.Objects;

public class LootGenerate implements Listener {

    @EventHandler
    public void onLootGenerate(LootGenerateEvent e) {

    }

    private static void load() {
        YamlConfiguration cfg = null;
        Objects.requireNonNull(cfg.getConfigurationSection("")).getKeys(true);
    }

}
