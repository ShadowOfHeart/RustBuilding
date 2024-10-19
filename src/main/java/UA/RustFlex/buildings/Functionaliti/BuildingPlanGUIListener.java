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

    // Обработчик события клика в GUI
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Проверка, что игрок кликает в нашем GUI
        if (event.getInventory().equals(buildingPlanManager.getGUI())) {
            event.setCancelled(true);  // Отключаем возможность забирать предметы из GUI
            Player player = (Player) event.getWhoClicked();
            player.closeInventory();   // Закрываем инвентарь после клика
        }
    }
}
