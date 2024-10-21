package UA.RustFlex.buildings.GUI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class BuildingPlanManager {

    private static final int GUI_SIZE = 54;
    private static final String GUI_TITLE = "Building Plan";
    private final Inventory gui;

    public BuildingPlanManager() {
        gui = Bukkit.createInventory(null, GUI_SIZE, GUI_TITLE);
        initializeGUI();
    }

    private void initializeGUI() {
        List<ItemData> items = List.of(
                new ItemData(4, Material.GLASS_PANE, "Floor"),
                new ItemData(20, Material.GLASS_PANE, "Walls"),
                new ItemData(40, Material.GLASS_PANE, "Foundation"),
                new ItemData(24, Material.GLASS_PANE, "DoorWave")
        );

        for (ItemData item : items) {
            ItemStack itemStack = new ItemStack(item.material);
            ItemMeta meta = itemStack.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(item.name);
                itemStack.setItemMeta(meta);
            }
            gui.setItem(item.slot, itemStack);
        }
    }

    public void openGUI(Player player) {
        player.openInventory(gui);
    }

    private static class ItemData {
        int slot;
        Material material;
        String name;

        ItemData(int slot, Material material, String name) {
            this.slot = slot;
            this.material = material;
            this.name = name;
        }
    }
}
