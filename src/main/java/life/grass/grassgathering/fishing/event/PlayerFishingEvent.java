package life.grass.grassgathering.fishing.event;

import life.grass.grassgathering.fishing.FishableItem;
import life.grass.grassgathering.fishing.FishingManager;
import org.bukkit.WeatherType;
import org.bukkit.block.Biome;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.List;

/**
 * Created by cyclicester on 2016/09/03.
 */
public class PlayerFishingEvent implements Listener {
    @EventHandler
    public void onPlayerFishing(PlayerFishEvent event) {
        Biome playerBiome = event.getPlayer().getLocation().getWorld()
                .getBiome(event.getPlayer().getLocation().getBlockX(), event.getPlayer().getLocation().getBlockZ());
        WeatherType playerWeather = event.getPlayer().getPlayerWeather();
        event.setExpToDrop(0);

        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            Item gottenFish = (Item) event.getCaught();
            List<Double> miss = FishingManager.makeSumList(FishingManager.getFailList());
            if(FishingManager.probMaker(miss) == 0){
                gottenFish.remove();
            } else {
                List<Double> ratioList = FishingManager.makeRatioList(playerBiome, playerWeather);
                List<Double> rsumList = FishingManager.makeSumList(ratioList);
                FishableItem harvest = FishingManager.getFishList().get(FishingManager.probMaker(rsumList));
                        System.out.println("You gotta" + harvest);
                        gottenFish.setItemStack(FishingManager.getFitemMap().get(harvest));
                    }
                }

    }

}
