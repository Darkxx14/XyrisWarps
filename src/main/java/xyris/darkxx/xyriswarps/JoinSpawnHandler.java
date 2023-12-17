package xyris.darkxx.xyriswarps;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class JoinSpawnHandler implements Listener {

    private final JavaPlugin plugin;

    public JoinSpawnHandler(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        teleportToSpawn(event.getPlayer());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        teleportToSpawn(event.getPlayer());
    }

    private void teleportToSpawn(Player player) {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        File spawnFile = new File(plugin.getDataFolder(), "spawn.yml");

        FileConfiguration mainConfig = YamlConfiguration.loadConfiguration(configFile);
        FileConfiguration spawnConfig = YamlConfiguration.loadConfiguration(spawnFile);

        boolean onJoinSpawn = mainConfig.getBoolean("on-join-spawn");

        if (onJoinSpawn) {
            if (spawnConfig.contains("spawn.coords") && spawnConfig.contains("spawn.world")) {
                double x = spawnConfig.getDouble("spawn.coords.x");
                double y = spawnConfig.getDouble("spawn.coords.y");
                double z = spawnConfig.getDouble("spawn.coords.z");
                float yaw = (float) spawnConfig.getDouble("spawn.yaw");
                float pitch = (float) spawnConfig.getDouble("spawn.pitch");
                String worldName = spawnConfig.getString("spawn.world");

                World world = Bukkit.getWorld(worldName);
                if (world != null) {
                    Location spawnLoc = new Location(world, x, y, z, yaw, pitch);
                    player.teleport(spawnLoc);
                } else {
                    plugin.getLogger().warning("World '" + worldName + "' does not exist!");
                }
            } else {
                plugin.getLogger().warning("Spawn coordinates or world not found in spawn.yml!");
            }
        }
    }
}
