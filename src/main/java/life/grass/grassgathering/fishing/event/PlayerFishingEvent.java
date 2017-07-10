package life.grass.grassgathering.fishing.event;

import life.grass.grassgathering.fishing.FishPool;
import life.grass.grassgathering.fishing.FishingManager;
import org.bukkit.Material;
import org.bukkit.WeatherType;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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

    @EventHandler
    public void onReleaseFish(PlayerInteractEvent event) {

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            List<Block> lineOfSight = event.getPlayer().getLineOfSight(null, 5);
            for (Block b : lineOfSight) {
                if (b.getType() == Material.STATIONARY_WATER && event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.RAW_FISH)) {
                    event.getPlayer().getInventory().getItemInMainHand().setAmount(0);
                    event.getPlayer().sendTitle("", FishingManager.releaseMessage(), 40, 10, 10);
                }
            }
        }
    }
}
