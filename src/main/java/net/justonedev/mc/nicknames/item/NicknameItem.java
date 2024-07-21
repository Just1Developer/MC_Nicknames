package net.justonedev.mc.nicknames.item;

import net.justonedev.mc.nicknames.Nicknames;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class NicknameItem implements CommandExecutor, Listener {

    public static final String itemName = "§7Empty Nickname";
    public static ItemStack nicknameItem, editBook;

    private static final int NICKNAME_MODEL_DATA = -2, BOOK_MODEL_DATA = -20;

    public static void init() {
        nicknameItem = new ItemStack(Nicknames.PLUGIN_MATERIAL, 1);
        ItemMeta meta = nicknameItem.getItemMeta();
        assert meta != null;
        meta.setCustomModelData(NICKNAME_MODEL_DATA);
        nicknameItem.setItemMeta(meta);

        editBook = new ItemStack(Material.WRITABLE_BOOK, 1);
        meta = editBook.getItemMeta();
        assert meta != null;
        meta.setDisplayName(itemName);
        meta.setCustomModelData(BOOK_MODEL_DATA);
        editBook.setItemMeta(meta);
    }

    public static boolean isNicknameItem(ItemStack item) {
        if (item == null || item.getType() != nicknameItem.getType()) return false;
        if (item.getItemMeta() == null) return false;
        if (!item.getItemMeta().hasCustomModelData()) return false;
        return item.getItemMeta().getCustomModelData() == NICKNAME_MODEL_DATA;
    }

    public static ItemStack newNickname(String nickname) {
        ItemStack item = new ItemStack(nicknameItem);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(nickname);
        item.setItemMeta(meta);
        return item;
    }

    public NicknameItem() {
        init();
    }

    private static void openNicknameEditor(Player p) {
        if (p.getInventory().getItemInMainHand().getType() != Material.AIR) {
            p.sendMessage("§cYour main hand must be empty!");
            return;
        }

        p.getInventory().setItemInMainHand(new ItemStack(editBook));
        //p.openBook(p.getInventory().getItemInMainHand());
        p.openBook(new ItemStack(editBook));
    }

    @EventHandler
    public void onBookSign(PlayerEditBookEvent e) {
        Player p = e.getPlayer();

        if (!e.getPreviousBookMeta().hasCustomModelData()) return;
        if (e.getPreviousBookMeta().getCustomModelData() != BOOK_MODEL_DATA) return;

        if (!e.isSigning() || e.isCancelled()) {
            p.getInventory().setItemInMainHand(null);
            return;
        }

        StringBuilder nicknameBuilder = new StringBuilder();
        for (String line : e.getNewBookMeta().getPages()) {
            nicknameBuilder.append(line);
        }
        String nickname = ChatColor.translateAlternateColorCodes('&', nicknameBuilder.toString().trim());

        p.getInventory().setItemInMainHand(newNickname(nickname));
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender.isOp() && !commandSender.hasPermission("nicknames.create")) {
            commandSender.sendMessage("§cUnknown or incomplete command, see below for error");
            commandSender.sendMessage("§c§ucreatenickname§r§c<--[HERE]");
            return false;
        }

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("§cThis command is for players only.");
            return false;
        }

        openNicknameEditor((Player) commandSender);
        return true;
    }
}
