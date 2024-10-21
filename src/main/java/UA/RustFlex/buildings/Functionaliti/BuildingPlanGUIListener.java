package UA.RustFlex.buildings.Functionaliti;

import UA.RustFlex.buildings.GUI.BuildingPlanManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BuildingPlanGUIListener implements Listener {

    private static final String GUI_TITLE = "Building Plan";
    private final BuildingPlanManager buildingPlanManager;

    public BuildingPlanGUIListener(BuildingPlanManager buildingPlanManager) {
        this.buildingPlanManager = buildingPlanManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();
        ItemStack clickedItem = event.getCurrentItem();


        if (clickedInventory != null && event.getView().getTitle().equals(GUI_TITLE) && clickedItem != null) {
            event.setCancelled(true);
            handlePlayerInteraction((Player) event.getWhoClicked(), clickedItem);
        }
    }

    private void handlePlayerInteraction(Player player, ItemStack clickedItem) {
        player.closeInventory();

        switch (clickedItem.getType()) {
            case GLASS_PANE:
                player.sendMessage("You clicked on " + clickedItem.getItemMeta().getDisplayName());
                break;
            default:
                player.sendMessage("Invalid item clicked.");
                break;
        }
    }
}
