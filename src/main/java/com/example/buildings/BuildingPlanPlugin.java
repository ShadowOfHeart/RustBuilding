package com.example.buildings;

import org.bukkit.plugin.java.JavaPlugin;

public class BuildingPlanPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new BuildingPlanGUI(), this);
    }

    @Override
    public void onDisable() {
        // Здесь можно добавить логику при отключении плагина, если нужно.
    }
}
