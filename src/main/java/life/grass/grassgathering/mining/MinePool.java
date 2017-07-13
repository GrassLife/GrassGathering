package life.grass.grassgathering.mining;

import com.google.gson.JsonObject;
import life.grass.grassgathering.ResourceJsonContainer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MinePool {

    private static MinePool minePool = new MinePool();
    private static List<MinableItem> minableItems;

    private MinePool() {
        minableItems = makeMinableItems();
    }

    public static MinePool getInstance() {
        return minePool;
    }

    public void decideDrop(Player player, boolean isNormalStone, Location bLocation) {

        minableItems.forEach(item -> {
            item.mine(player, isNormalStone, bLocation);
        });
    }

    private List<MinableItem> makeMinableItems() {

        List<MinableItem> minableItemList = new ArrayList<>();

        Map<String, JsonObject> mineJsonMap = ResourceJsonContainer.getInstance().getMineJsonMap();

        mineJsonMap.forEach((name, item) -> {
            minableItemList.add(new MinableItem(name, item));
        });
        return minableItemList;
    }

    public void setMinableItems() {
        minableItems = makeMinableItems();
    }
}
