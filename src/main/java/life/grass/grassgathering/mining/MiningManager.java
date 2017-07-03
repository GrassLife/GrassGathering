package life.grass.grassgathering.mining;

import com.google.gson.JsonObject;
import life.grass.grassgathering.ResourceJsonContainer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MiningManager {

    private static List<MinableItem> minableItems = makeMinableItems();

    public static void decideDrop(Player player, Location bLocation) {

        minableItems.forEach(item -> {
            item.mine(player, bLocation);
        });
    }

    private static List<MinableItem> makeMinableItems() {

        List<MinableItem> minableItemList = new ArrayList<>();

        Map<String, JsonObject> mineJsonMap = ResourceJsonContainer.getInstance().getMineJsonMap();

        mineJsonMap.forEach((name, item) -> {
            System.out.println(item.get("chatColor").getAsString());
            minableItemList.add(new MinableItem(name, item));
        });
        return minableItemList;
    }

    public static void setMinableItems() {
        minableItems = makeMinableItems();
    }
}