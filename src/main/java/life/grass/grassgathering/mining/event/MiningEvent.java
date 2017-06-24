package life.grass.grassgathering.mining.event;

import life.grass.grassgathering.GrassGathering;
import life.grass.grassgathering.mining.MiningManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitScheduler;

public class MiningEvent implements Listener {
    @EventHandler
    public void onMining(BlockBreakEvent event){
        Location bLocation = event.getBlock().getLocation();
        if(event.getBlock().getType() == Material.STONE) {
            BukkitScheduler scheduler = GrassGathering.getInstance().getServer().getScheduler();
            scheduler.scheduleSyncDelayedTask(GrassGathering.getInstance(), new Runnable() {
                @Override
                public void run() {
                    MiningManager.decideDrop(event.getPlayer(), bLocation);
                }
            }, 5L);

        }
    }
}