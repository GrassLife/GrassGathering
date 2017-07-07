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

        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {

            event.setExpToDrop(0);
            
            Item gottenFish = (Item) event.getCaught();
            ItemStack itemStack = FishPool.getInstance().giveFishTo(event.getPlayer());

            gottenFish.setItemStack(itemStack);
        }
    }
}
