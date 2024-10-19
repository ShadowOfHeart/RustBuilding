package UA.RustFlex.buildings;

import UA.RustFlex.buildings.Functionaliti.BuildingPlanGUIListener;
import UA.RustFlex.buildings.GUI.BuildingPlanManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BuildingPlanPlugin extends JavaPlugin {

    private BuildingPlanManager buildingPlanManager;

    @Override
    public void onEnable() {
        buildingPlanManager = new BuildingPlanManager();
        getServer().getPluginManager().registerEvents(new BuildingPlanGUIListener(buildingPlanManager), this);
    }

    @Override
    public void onDisable() {
        // Логика при отключении плагина (если необходимо)
    }
}
