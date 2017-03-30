package life.grass.grassgathering.fishing.event;

import life.grass.grassgathering.fishing.FishableItem;
import life.grass.grassgathering.fishing.FishingManager;
import life.grass.grassknowledge.player.KnowledgeStats;
import org.bukkit.WeatherType;
import org.bukkit.block.Biome;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.List;

public class PlayerFishingEvent implements Listener {
    @EventHandler
    public void onPlayerFishing(PlayerFishEvent event) {
        Biome playerBiome = event.getPlayer().getLocation().getWorld()
                .getBiome(event.getPlayer().getLocation().getBlockX(), event.getPlayer().getLocation().getBlockZ());
        WeatherType playerWeather = event.getPlayer().getPlayerWeather();
        event.setExpToDrop(0);

        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            Item gottenFish = (Item) event.getCaught();
            KnowledgeStats stats = new KnowledgeStats(event.getPlayer());
            List<Double> miss = FishingManager.makeSumList(FishingManager.getFailList());
            if(FishingManager.probMaker(miss) == 0){
                gottenFish.remove();
                stats.increaseKnowledgePoint("FISHING", 1);
            } else {
                List<Double> ratioList = FishingManager.makeRatioList(playerBiome, playerWeather);
                List<Double> rsumList = FishingManager.makeSumList(ratioList);
                FishableItem harvest = FishingManager.getFishList().get(FishingManager.probMaker(rsumList));
                gottenFish.setItemStack(FishingManager.getFishItemMap().get(harvest));
                stats.increaseKnowledgePoint("FISHING", 5);
            }
        }
    }
}
