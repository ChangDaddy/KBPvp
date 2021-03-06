package cf.strafe.commands;

import cf.strafe.KnockBackFFA;
import cf.strafe.data.DataManager;
import cf.strafe.data.PlayerData;
import cf.strafe.shop.Item;
import cf.strafe.shop.ItemManager;
import cf.strafe.shop.nodes.BlockItem;
import cf.strafe.shop.nodes.StickItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KitEditCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            PlayerData data = DataManager.INSTANCE.getPlayer(((Player) sender).getPlayer());

            if (data.isInArena()) {
                data.getPlayer().sendMessage(ChatColor.RED + "You need to be in spawn to use this command!");
                return false;
            }
            if (data.isEditing()) {
                KnockBackFFA.INSTANCE.getExecutorService().execute(() -> {
                    for (int i = 0; i < 9; i++) {
                        ItemStack itemStack = data.getPlayer().getInventory().getItem(i);
                        if (itemStack == null) continue;
                        Item item = ItemManager.INSTANCE.findItem(ChatColor.stripColor(itemStack.getItemMeta().getDisplayName()));
                        if (item != null) {
                            if (item instanceof BlockItem) {
                                data.setBlockSlot(i);
                            } else if (item instanceof StickItem) {
                                data.setStickSlot(i);
                            }
                        } else {
                            switch (itemStack.getType()) {
                                case WEB: {
                                    data.setWebSlot(i);
                                    break;
                                }
                                case ENDER_PEARL: {
                                    data.setPearlSlot(i);
                                    break;
                                }
                            }
                        }
                    }
                    data.getPlayer().sendMessage(ChatColor.GREEN + "Saved!");
                    data.clearInventory();
                    data.setEditing(false);
                });
                return false;
            }


            data.getPlayer().getInventory().setItem(data.getStickSlot(), data.getStickItem().getStick());
            data.getPlayer().getInventory().setItem(data.getBlockSlot(), data.getBlockItem().getBlock());
            data.getPlayer().getInventory().setItem(data.getWebSlot(), new ItemStack(Material.WEB));
            data.getPlayer().getInventory().setItem(data.getPearlSlot(), new ItemStack(Material.ENDER_PEARL));
            data.getPlayer().updateInventory();
            data.setEditing(true);

            data.getPlayer().sendMessage(ChatColor.GREEN + "Move your items around to configure.");
            data.getPlayer().sendMessage(ChatColor.AQUA + "Run /kitedit when you're done!");

        }
        return false;
    }
}
