package xyris.darkxx.xyriswarps;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class Main extends JavaPlugin implements Listener {
    private JoinSpawnHandler configManager;
    @Override
    public void onEnable() {
        createConfig();
        new JoinSpawnHandler(this);
        getLogger().info("XyrisWarps has been enabled!");

        getCommand("createwarp").setExecutor(this);
        getCommand("deletewarp").setExecutor(this);
        getCommand("warp").setExecutor(this);
        getCommand("setspawn").setExecutor(this);

        getServer().getPluginManager().registerEvents(this, this);

        saveDefaultWarpsConfig();
        saveDefaultSpawnConfig();
    }
    private void createConfig() {
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            saveDefaultConfig();
        }
    }
    @Override
    public void onDisable() {
    }
    private void saveDefaultWarpsConfig() {
        File warpsFile = new File(getDataFolder(), "warps.yml");
        if (!warpsFile.exists()) {
            saveResource("warps.yml", false);
        }
    }

    private void saveDefaultSpawnConfig() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
        File spawnFile = new File(getDataFolder(), "spawn.yml");
        if (!spawnFile.exists()) {
            saveResource("spawn.yml", false);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("createwarp")) {
            if (!sender.hasPermission("xyriswarps.createwarp")) {
                sender.sendMessage("§cYou don't have permission to use this command.");
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cThis command can only be used by players.");
                return true;
            }

            Player player = (Player) sender;

            if (args.length != 1) {
                sender.sendMessage("§c/createwarp <arena_name>");
                return true;
            }

            String arenaName = args[0];

            FileConfiguration config = getConfig("warps.yml");
            config.set("warps." + arenaName + ".world", player.getLocation().getWorld().getName()); // Save the world name
            config.set("warps." + arenaName + ".coords.x", player.getLocation().getX());
            config.set("warps." + arenaName + ".coords.y", player.getLocation().getY());
            config.set("warps." + arenaName + ".coords.z", player.getLocation().getZ());
            config.set("warps." + arenaName + ".yaw", player.getLocation().getYaw());
            config.set("warps." + arenaName + ".pitch", player.getLocation().getPitch());
            saveConfig(config, "warps.yml");

            sender.sendMessage("§aWarp '" + arenaName + "' created successfully!");
            return true;
        } else if (cmd.getName().equalsIgnoreCase("deletewarp")) {
            if (!sender.hasPermission("xyriswarps.deletewarp")) {
                sender.sendMessage("§cYou don't have permission to use this command.");
                return true;
            }
            if (args.length != 1) {
                sender.sendMessage("§c/deletewarp <arena_name>");
                return true;
            }

            String arenaName = args[0];
            FileConfiguration config = getConfig("warps.yml");
            if (config.contains("warps." + arenaName)) {
                config.set("warps." + arenaName, null);
                try {
                    config.save(new File(getDataFolder(), "warps.yml"));
                    sender.sendMessage("§aWarp '" + arenaName + "' deleted successfully!");
                } catch (IOException e) {
                    sender.sendMessage("§cFailed to delete warp '" + arenaName + "'. Please check console for details.");
                    e.printStackTrace();
                }
            } else {
                sender.sendMessage("§cWarp '" + arenaName + "' does not exist!");
            }
            return true;
        } else if (cmd.getName().equalsIgnoreCase("warp")) {
            if (!sender.hasPermission("xyriswarps.usewarp")) {
                sender.sendMessage("§cYou don't have permission to use this command.");
            }
            if (args.length < 1 || args.length > 2) {
                sender.sendMessage("§c/warp <arena_name> [player_name]");
                return true;
            }

            String arenaName = args[0];
            String playerName = (args.length == 2) ? args[1] : sender.getName();

            FileConfiguration config = getConfig("warps.yml");
            if (config.contains("warps." + arenaName)) {
                double x = config.getDouble("warps." + arenaName + ".coords.x");
                double y = config.getDouble("warps." + arenaName + ".coords.y");
                double z = config.getDouble("warps." + arenaName + ".coords.z");
                float yaw = (float) config.getDouble("warps." + arenaName + ".yaw");
                float pitch = (float) config.getDouble("warps." + arenaName + ".pitch");
                String worldName = config.getString("warps." + arenaName + ".world");

                World targetWorld = getServer().getWorld(worldName);
                if (targetWorld != null) {
                    Player targetPlayer = getServer().getPlayer(playerName);
                    if (targetPlayer != null) {
                        targetPlayer.teleport(new Location(targetWorld, x, y, z, yaw, pitch));
                        sender.sendMessage("§aTeleported " + targetPlayer.getName() + " to warp '" + arenaName + "'!");
                    } else {
                        sender.sendMessage("§cPlayer '" + playerName + "' is not online.");
                    }
                } else {
                    sender.sendMessage("§cWorld '" + worldName + "' for warp '" + arenaName + "' does not exist!");
                }
            } else {
                sender.sendMessage("§cWarp '" + arenaName + "' does not exist!");
            }
            return true;


        } else if (cmd.getName().equalsIgnoreCase("setspawn")) {
            if (!sender.hasPermission("xyriswarps.setspawn")) {
                sender.sendMessage("§cYou don't have permission to use this command.");
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cThis command can only be used by players.");
                return true;
            }

            Player player = (Player) sender;

            FileConfiguration spawnConfig = getConfig("spawn.yml");
            spawnConfig.set("spawn.world", player.getLocation().getWorld().getName());
            spawnConfig.set("spawn.coords.x", player.getLocation().getX());
            spawnConfig.set("spawn.coords.y", player.getLocation().getY());
            spawnConfig.set("spawn.coords.z", player.getLocation().getZ());
            spawnConfig.set("spawn.yaw", player.getLocation().getYaw());
            spawnConfig.set("spawn.pitch", player.getLocation().getPitch());
            saveConfig(spawnConfig, "spawn.yml");

            sender.sendMessage("§aSpawn point set successfully!");
            return true;
        }
        return false;
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        FileConfiguration spawnConfig = getConfig("spawn.yml");
        if (spawnConfig.contains("spawn.coords") && spawnConfig.contains("spawn.world")) {
            double x = spawnConfig.getDouble("spawn.coords.x");
            double y = spawnConfig.getDouble("spawn.coords.y");
            double z = spawnConfig.getDouble("spawn.coords.z");
            float yaw = (float) spawnConfig.getDouble("spawn.yaw");
            float pitch = (float) spawnConfig.getDouble("spawn.pitch");

            String worldName = spawnConfig.getString("spawn.world");
            World world = Bukkit.getWorld(worldName);

            if (world != null) {
                Location respawnLoc = new Location(world, x, y, z, yaw, pitch);
                event.setRespawnLocation(respawnLoc);
            } else {
                getLogger().warning("§cWorld '" + worldName + "' does not exist!");
            }
        } else {
            getLogger().warning("§cSpawn coordinates or world not found in configuration!");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> completions = new ArrayList<>();

        if (cmd.getName().equalsIgnoreCase("deletewarp")) {
            if (args.length == 1) {
                FileConfiguration config = getConfig("warps.yml");
                if (config.contains("warps")) {
                    Set<String> warpNames = config.getConfigurationSection("warps").getKeys(false);
                    completions.addAll(warpNames);
                }
            }
        } else if (cmd.getName().equalsIgnoreCase("warp")) {
            if (args.length == 1 && sender instanceof Player) {
                FileConfiguration config = getConfig("warps.yml");
                if (config.contains("warps")) {
                    Set<String> warpNames = config.getConfigurationSection("warps").getKeys(false);
                    completions.addAll(warpNames);
                }
            } else if (args.length == 2) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    completions.add(player.getName());
                }
            }
        }

        Collections.sort(completions);
        return completions;
    }



    FileConfiguration getConfig(String fileName) {
        File configFile = new File(getDataFolder(), fileName);
        return org.bukkit.configuration.file.YamlConfiguration.loadConfiguration(configFile);
    }

    private void saveConfig(FileConfiguration config, String fileName) {
        try {
            config.save(new File(getDataFolder(), fileName));
        } catch (IOException e) {
            getLogger().warning("§cCould not save " + fileName + " to " + getDataFolder());
        }
    }
}
