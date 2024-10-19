package UA.RustFlex.buildings.GUI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BuildingPlanManager {

    private Inventory gui;

    public BuildingPlanManager() {
        createGUI();
    }

    // Метод для создания GUI
    private void createGUI() {
        gui = Bukkit.createInventory(null, 54, "Building Plan");

        addGUIItem(gui, 4, Material.GLASS_PANE, "Floor");
        addGUIItem(gui, 20, Material.GLASS_PANE, "Walls");
        addGUIItem(gui, 40, Material.GLASS_PANE, "Foundation");
        addGUIItem(gui, 24, Material.GLASS_PANE, "DoorWave");
    }

    // Метод для открытия GUI
    public void openGUI(Player player) {
        player.openInventory(gui);
    }

    // Вспомогательный метод для добавления предметов в GUI
    private void addGUIItem(Inventory inv, int slot, Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        inv.setItem(slot, item);
    }

    public Inventory getGUI() {
        return gui;
    }
}
