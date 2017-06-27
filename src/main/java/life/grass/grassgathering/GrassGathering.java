package life.grass.grassgathering;

import life.grass.grassgathering.fishing.event.PlayerFishingEvent;
import life.grass.grassgathering.mining.event.MiningEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class GrassGathering extends JavaPlugin {

    public static GrassGathering instance;

    public static GrassGathering getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;
        ResourceJsonContainer.getInstance();
        getServer().getPluginManager().registerEvents(new PlayerFishingEvent(), this);
        getServer().getPluginManager().registerEvents(new MiningEvent(), this);
        getCommand("grassgathering").setExecutor(new GrassGatheringCommandExecutor());
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

}
