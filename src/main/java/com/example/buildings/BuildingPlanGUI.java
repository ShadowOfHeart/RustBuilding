package com.example.buildings;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BuildingPlanGUI implements Listener {

    private Inventory gui;

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction().name().contains("RIGHT_CLICK")) {
            Player player = event.getPlayer();
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType() == Material.STICK && item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                if (meta.hasDisplayName() && meta.getDisplayName().equals("BuildingPlan")) {
                    openGUI(player);
                }
            }
        }
    }

    private void openGUI(Player player) {
        gui = Bukkit.createInventory(null, 54, "Building Plan");

        addGUIItem(gui, 4, Material.GLASS_PANE, "Floor");
        addGUIItem(gui, 20, Material.GLASS_PANE, "Walls");
        addGUIItem(gui, 40, Material.GLASS_PANE, "Foundation");
        addGUIItem(gui, 24, Material.GLASS_PANE, "DoorWave");

        player.openInventory(gui);
    }

    private void addGUIItem(Inventory inv, int slot, Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        inv.setItem(slot, item);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().equals(gui)) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.GLASS_PANE) {
                String itemName = event.getCurrentItem().getItemMeta().getDisplayName();
                Player player = (Player) event.getWhoClicked();
                if ("Foundation".equals(itemName)) {
                    player.closeInventory();
                    // Действие, когда игрок выбирает "Foundation", можно добавить здесь.
                }
            }
        }
    }
}
