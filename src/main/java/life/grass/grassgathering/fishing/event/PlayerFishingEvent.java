package life.grass.grassgathering.fishing.event;

import life.grass.grassgathering.fishing.FishPool;
import life.grass.grassgathering.fishing.FishingManager;
import org.bukkit.WeatherType;
import org.bukkit.block.Biome;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PlayerFishingEvent implements Listener {
    @EventHandler
    public void onPlayerFishing(PlayerFishEvent event) {

        FishPool fishPool = FishPool.getInstance();

        Biome playerBiome = event.getPlayer().getLocation().getWorld()
                .getBiome(event.getPlayer().getLocation().getBlockX(), event.getPlayer().getLocation().getBlockZ());

        WeatherType playerWeather = event.getPlayer().getPlayerWeather();

        event.setExpToDrop(0);

        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {

            Item gottenFish = (Item) event.getCaught();
            List<Double> miss = FishingManager.makeSumList(FishingManager.getFailList());

            if(FishingManager.probMaker(miss) == 0){
                gottenFish.remove();
                event.getPlayer().sendTitle("", "魚が逃げてしまった!!", 10, 70, 20);
            } else {

                List<Double> ratioList = fishPool.getRatioList(playerBiome, playerWeather);
                List<Double> rsumList = FishingManager.makeSumList(ratioList);
                ItemStack harvest = fishPool.getFishList().get(FishingManager.probMaker(rsumList)).getItemStack();
                gottenFish.setItemStack(harvest);

            }
        }
    }
}
