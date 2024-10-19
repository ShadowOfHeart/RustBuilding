package com.example.buildings;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import com.comphenix.protocol.wrappers.ChunkCoordIntPair;
import com.comphenix.protocol.wrappers.MultiBlockChangeInfo;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class BuildingPlanGUI extends JavaPlugin implements Listener {

    private Inventory gui;
    private final Map<Player, Location[]> phantomBlocks = new HashMap<>();
    private ProtocolManager protocolManager;

    @Override
    public void onEnable() {
        protocolManager = ProtocolLibrary.getProtocolManager();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction().name().contains("RIGHT_CLICK")) {
            Player player = event.getPlayer();
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType() == Material.STICK && item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                if (meta.hasDisplayName() && meta.getDisplayName().equals("BuildingPlan")) {
                    openGUI(player);
                }
            }
        }
    }

    private void openGUI(Player player) {
        gui = Bukkit.createInventory(null, 54, "Building Plan");

        addGUIItem(gui, 4, Material.GLASS_PANE, "Floor");
        addGUIItem(gui, 20, Material.GLASS_PANE, "Walls");
        addGUIItem(gui, 40, Material.GLASS_PANE, "Foundation");
        addGUIItem(gui, 24, Material.GLASS_PANE, "DoorWave");

        player.openInventory(gui);
    }

    private void addGUIItem(Inventory inv, int slot, Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        inv.setItem(slot, item);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().equals(gui)) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.GLASS_PANE) {
                String itemName = event.getCurrentItem().getItemMeta().getDisplayName();
                Player player = (Player) event.getWhoClicked();
                if ("Foundation".equals(itemName)) {
                    player.closeInventory();
                    spawnPhantomBlocks(player);
                    trackPlayerMovement(player);
                }
            }
        }
    }

    private void spawnPhantomBlocks(Player player) {
        Location playerLoc = player.getLocation();
        Vector direction = playerLoc.getDirection().normalize();
        Location spawnLocation = playerLoc.clone().add(direction.multiply(3));

        Location[] blockLocations = new Location[9];
        int index = 0;

        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                Location blockLocation = spawnLocation.clone().add(x, 0, z);
                blockLocations[index++] = blockLocation;
            }
        }

        phantomBlocks.put(player, blockLocations);
        updatePhantomBlocks(player, blockLocations);
    }

    private void trackPlayerMovement(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline() || !phantomBlocks.containsKey(player)) {
                    cancel();
                    return;
                }

                ItemStack itemInHand = player.getInventory().getItemInMainHand();
                if (itemInHand == null || !itemInHand.hasItemMeta() || !itemInHand.getItemMeta().hasDisplayName() ||
                        !itemInHand.getItemMeta().getDisplayName().equals("BuildingPlan")) {
                    removePhantomBlocks(player);
                    cancel();
                    return;
                }

                Location[] blockLocations = phantomBlocks.get(player);
                Location playerLoc = player.getLocation();
                Vector direction = playerLoc.getDirection().normalize();
                Location newSpawnLocation = playerLoc.clone().add(direction.multiply(3));

                int index = 0;
                for (int x = -1; x <= 1; x++) {
                    for (int z = -1; z <= 1; z++) {
                        Location blockLocation = newSpawnLocation.clone().add(x, 0, z);
                        blockLocations[index++] = blockLocation;
                    }
                }

                updatePhantomBlocks(player, blockLocations);
            }
        }.runTaskTimer(this, 0L, 1L);
    }

    private void updatePhantomBlocks(Player player, Location[] blockLocations) {
        Map<ChunkCoordIntPair, List<MultiBlockChangeInfo>> changesByChunk = new HashMap<>();

        for (Location loc : blockLocations) {
            ChunkCoordIntPair chunkCoords = new ChunkCoordIntPair(loc.getBlockX() >> 4, loc.getBlockZ() >> 4);
            Material phantomMaterial = loc.getBlock().getType() == Material.AIR ? Material.LIME_STAINED_GLASS : Material.RED_STAINED_GLASS;
            WrappedBlockData blockData = WrappedBlockData.createData(phantomMaterial);

            MultiBlockChangeInfo changeInfo = new MultiBlockChangeInfo(loc, blockData);

            changesByChunk.computeIfAbsent(chunkCoords, k -> new ArrayList<>()).add(changeInfo);
        }

        for (Map.Entry<ChunkCoordIntPair, List<MultiBlockChangeInfo>> entry : changesByChunk.entrySet()) {
            PacketContainer packet = new PacketContainer(PacketType.Play.Server.MULTI_BLOCK_CHANGE);
            packet.getChunkCoordIntPairs().write(0, (ChunkCoordIntPair) Collections.singletonList(entry.getKey()));
            packet.getMultiBlockChangeInfoArrays().write(0, entry.getValue().toArray(new MultiBlockChangeInfo[0]));

            try {
                protocolManager.sendServerPacket(player, packet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void removePhantomBlocks(Player player) {
        Location[] blockLocations = phantomBlocks.remove(player);
        if (blockLocations != null) {
            Map<ChunkCoordIntPair, List<MultiBlockChangeInfo>> changesByChunk = new HashMap<>();

            for (Location loc : blockLocations) {
                ChunkCoordIntPair chunkCoords = new ChunkCoordIntPair(loc.getBlockX() >> 4, loc.getBlockZ() >> 4);
                WrappedBlockData blockData = WrappedBlockData.createData(loc.getBlock().getType());

                MultiBlockChangeInfo changeInfo = new MultiBlockChangeInfo(loc, blockData);

                changesByChunk.computeIfAbsent(chunkCoords, k -> new ArrayList<>()).add(changeInfo);
            }

            for (Map.Entry<ChunkCoordIntPair, List<MultiBlockChangeInfo>> entry : changesByChunk.entrySet()) {
                PacketContainer packet = new PacketContainer(PacketType.Play.Server.MULTI_BLOCK_CHANGE);
                packet.getChunkCoordIntPairs().write(0, (ChunkCoordIntPair) Collections.singletonList(entry.getKey()));
                packet.getMultiBlockChangeInfoArrays().write(0, entry.getValue().toArray(new MultiBlockChangeInfo[0]));

                try {
                    protocolManager.sendServerPacket(player, packet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack newItem = player.getInventory().getItem(event.getNewSlot());

        if (phantomBlocks.containsKey(player) &&
                (newItem == null || !newItem.hasItemMeta() || !newItem.getItemMeta().hasDisplayName() ||
                        !newItem.getItemMeta().getDisplayName().equals("BuildingPlan"))) {
            removePhantomBlocks(player);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (phantomBlocks.containsKey(player)) {
            Location[] blockLocations = phantomBlocks.get(player);
            Location playerLoc = player.getLocation();
            Vector direction = playerLoc.getDirection().normalize();
            Location newSpawnLocation = playerLoc.clone().add(direction.multiply(3));

            int index = 0;
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    Location blockLocation = newSpawnLocation.clone().add(x, 0, z);
                    blockLocations[index++] = blockLocation;
                }
            }

            updatePhantomBlocks(player, blockLocations);
        }
    }
}
