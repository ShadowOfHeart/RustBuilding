package UA.RustFlex.buildings;

import UA.RustFlex.buildings.Functionaliti.BuildingPlanGUIListener;
import UA.RustFlex.buildings.GUI.BuildingPlanManager;
import UA.RustFlex.buildings.Logger.DiscordLogger;
import UA.RustFlex.buildings.Optimization.Optimization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BuildingPlanPlugin extends JavaPlugin {

    private BuildingPlanManager buildingPlanManager;
    private Optimization optimization;

    @Override
    public void onEnable() {
        // Инициализируем менеджер планов зданий
        buildingPlanManager = new BuildingPlanManager();
        getServer().getPluginManager().registerEvents(new BuildingPlanGUIListener(buildingPlanManager), this);


        // Инициализируем DiscordLogger с URL вебхука
        String webhookUrl = "https://discord.com/api/webhooks/1297977259428286535/QjLYxs1cfESd023wX5SiayPN5n8s-o1UpAUX9_-vOgAaG5bUEps2vb-zBBr0reZOrk-8";
        DiscordLogger discordLogger = new DiscordLogger(webhookUrl);
        getServer().getPluginManager().registerEvents(discordLogger, this);


        // Инициализируем класс оптимизации для работы с асинхронными задачами
        optimization = new Optimization();
        optimization.onEnable();

        getLogger().info("BuildingPlanPlugin успешно активирован!");
    }

    @Override
    public void onDisable() {
        if (optimization != null) {
            optimization.onDisable();
        }
        getLogger().info("BuildingPlanPlugin отключен.");
    }

    // Метод для открытия GUI планов зданий
    public void openBuildingPlanGUI(Player player) {
        buildingPlanManager.openGUI(player);
    }
}
