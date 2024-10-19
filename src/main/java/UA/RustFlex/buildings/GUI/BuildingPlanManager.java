package UA.RustFlex.buildings.GUI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BuildingPlanManager {

    private final Inventory gui;

    public BuildingPlanManager() {
        gui = Bukkit.createInventory(null, 54, "Building Plan");
        addGUIItem(4, Material.GLASS_PANE, "Floor");
        addGUIItem(20, Material.GLASS_PANE, "Walls");
        addGUIItem(40, Material.GLASS_PANE, "Foundation");
        addGUIItem(24, Material.GLASS_PANE, "DoorWave");
    }

    public void openGUI(Player player) {
        player.openInventory(gui);
    }

    private void addGUIItem(int slot, Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        gui.setItem(slot, item);
    }

    public Inventory getGUI() {
        return gui;
    }
}
