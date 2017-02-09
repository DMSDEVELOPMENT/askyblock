package com.wasteofplastic.askyblock.dms;

import com.wasteofplastic.askyblock.panels.ControlPanel;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import ru.luvas.rmcs.utils.KitManager;
import ru.luvas.rmcs.utils.RListener;
import ru.luvas.rmcs.utils.Task;
import ru.luvas.rmcs.utils.inventory.InventoryManager;
import ru.luvas.rmcs.utils.inventory.SimpleItemStack;
import ru.luvas.rmcs.utils.items.ActionType;
import ru.luvas.rmcs.utils.items.UsableItem;

import java.util.Iterator;

/**
 * Created by RINES on 09.02.17.
 */
public class DmsInitializer {

    public static void load() {
        KitManager.initialize("cooldowns_skyblock");
        handleNinthSlot();
    }

    private static void handleNinthSlot() {
        final ItemStack menu = new SimpleItemStack(Material.NETHER_STAR, "&a&lМеню", "&7Нажми, чтобы открыть меню (:");
        new UsableItem(menu, ActionType.RIGHT) {
            @Override
            public void onUse(Player p, ActionType actionType) {
                p.openInventory(ControlPanel.controlPanel.get(ControlPanel.getDefaultPanelName()));
            }
        };
        final int slot = 8;
        InventoryManager.addBlockedSlot(slot);
        new RListener() {
            @EventHandler
            public void onDeath(PlayerDeathEvent e) {
                for(Iterator<ItemStack> iterator = e.getDrops().iterator(); iterator.hasNext();) {
                    ItemStack item = iterator.next();
                    if(item.getType() == menu.getType() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equals(menu.getItemMeta().getDisplayName())) {
                        iterator.remove();
                        break;
                    }
                }
            }

            @EventHandler
            public void onPlayerRespawn(PlayerRespawnEvent e) {
                Player p = e.getPlayer();
                Task.schedule(() -> {
                    if(p.isOnline())
                        p.getInventory().setItem(slot, menu);
                }, 1l);
            }

            @EventHandler(priority = EventPriority.HIGHEST)
            public void onPlayerJoin(PlayerJoinEvent e) {
                e.getPlayer().getInventory().setItem(slot, menu);
            }
        };
    }

}
