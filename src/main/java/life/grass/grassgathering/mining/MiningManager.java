package life.grass.grassgathering.mining;

import org.bukkit.Location;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * Created by takah on 2016/10/04.
 */
public  class MiningManager {

    public static void decideDrop(BlockBreakEvent event, Location bLocation) {

        for (EMinableItems item : EMinableItems.values()) {
            item.dropItem(event, bLocation);
            item.chainItem(event, bLocation);
        }

    }
}