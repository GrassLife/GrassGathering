package life.grass.grassgathering;

import life.grass.grassgathering.fishing.event.PlayerFishingEvent;
import life.grass.grassgathering.mining.event.MiningEvent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class GrassGathering extends JavaPlugin {

    public static GrassGathering instance;

    public static GrassGathering getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;
        getServer().getPluginManager().registerEvents(new PlayerFishingEvent(), this);
        getServer().getPluginManager().registerEvents(new MiningEvent(), this);
        createFile();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }


    private void createFile() {
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            File file = new File(getDataFolder(), "config.yml");
            if (!file.exists()) {
                getLogger().info("config.yml not found, creating...");
                saveDefaultConfig();
            } else {
                getLogger().info("config.yml found, loading...");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
