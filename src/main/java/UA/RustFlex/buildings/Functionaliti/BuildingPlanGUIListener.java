package UA.RustFlex.buildings.Functionaliti;

import UA.RustFlex.buildings.GUI.BuildingPlanManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class BuildingPlanGUIListener implements Listener {

    private final BuildingPlanManager buildingPlanManager;

    public BuildingPlanGUIListener(BuildingPlanManager buildingPlanManager) {
        this.buildingPlanManager = buildingPlanManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().equals(buildingPlanManager.getGUI()) && event.getCurrentItem() != null) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            player.closeInventory();
        }
    }
}
