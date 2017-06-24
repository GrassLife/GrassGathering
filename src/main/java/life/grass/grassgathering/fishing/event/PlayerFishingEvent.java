package life.grass.grassgathering.fishing.event;

import life.grass.grassgathering.fishing.FishableItem;
import life.grass.grassgathering.fishing.FishingManager;
import life.grass.grassitem.JsonHandler;
import org.bukkit.WeatherType;
import org.bukkit.block.Biome;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
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
            List<Double> miss = FishingManager.makeSumList(FishingManager.getFailList());

            if(FishingManager.probMaker(miss) == 0){
                gottenFish.remove();
            } else {

                List<Double> ratioList = FishingManager.makeRatioList(playerBiome, playerWeather);
                List<Double> rsumList = FishingManager.makeSumList(ratioList);
                ItemStack harvest = FishingManager.getFishList().get(FishingManager.probMaker(rsumList)).getItemStack();
                gottenFish.setItemStack(harvest);

            }
        }
    }
}
