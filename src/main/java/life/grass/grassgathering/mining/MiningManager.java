package life.grass.grassgathering.mining;

import com.google.gson.JsonObject;
import life.grass.grassgathering.ResourceJsonContainer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MiningManager {

    private static List<MinableItem> minableItems = makeMinableItems();

    public static void decideDrop(Player player, boolean isNormalStone, Location bLocation) {

        minableItems.forEach(item -> {
            item.mine(player, isNormalStone, bLocation);
        });
    }

    private static List<MinableItem> makeMinableItems() {

        List<MinableItem> minableItemList = new ArrayList<>();

        Map<String, JsonObject> mineJsonMap = ResourceJsonContainer.getInstance().getMineJsonMap();

        mineJsonMap.forEach((name, item) -> {
            minableItemList.add(new MinableItem(name, item));
        });
        return minableItemList;
    }

    public static void setMinableItems() {
        minableItems = makeMinableItems();
    }
}