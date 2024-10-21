package UA.RustFlex.buildings.GUI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class BuildingPlanManager {

    private final Inventory gui;

    public BuildingPlanManager() {
        gui = Bukkit.createInventory(null, 54, "Building Plan");
        initializeGUI();
    }

    private void initializeGUI() {
        List<GUIItem> items = Arrays.asList(
                new GUIItem(4, Material.GLASS_PANE, "Floor"),
                new GUIItem(20, Material.GLASS_PANE, "Walls"),
                new GUIItem(40, Material.GLASS_PANE, "Foundation"),
                new GUIItem(24, Material.GLASS_PANE, "DoorWave")
        );

        for (GUIItem item : items) {
            addGUIItem(item.slot, item.material, item.name);
        }
    }

    public void openGUI(Player player) {
        player.openInventory(gui);
    }

    private void addGUIItem(int slot, Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }

        gui.setItem(slot, item);
    }

    public Inventory getGUI() {
        return gui;
    }

    private static class GUIItem {
        int slot;
        Material material;
        String name;

        GUIItem(int slot, Material material, String name) {
            this.slot = slot;
            this.material = material;
            this.name = name;
        }
    }
}
