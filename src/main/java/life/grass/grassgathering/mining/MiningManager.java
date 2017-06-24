package life.grass.grassgathering.mining;

import life.grass.grassgathering.GrassGathering;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MiningManager {

    private static List<MinableItem> minableItems = makeMinableItems();

    public static void decideDrop(Player player, Location bLocation) {

        minableItems.forEach(item -> {
            item.dropItem(player, bLocation);
            item.chainItem(player, bLocation);
        });
    }

    private static List<MinableItem> makeMinableItems() {

        List<MinableItem> minableItemList = new ArrayList<MinableItem>();
        ConfigurationSection items = GrassGathering.getInstance().getConfig().getConfigurationSection("items");

        for(String key : items.getKeys(false)) {
            ConfigurationSection item = items.getConfigurationSection(key);

            minableItemList.add(new MinableItem(
                    key,
                    Integer.parseInt(item.get("modeHeight").toString()),
                    Double.parseDouble(item.get("highestRatio").toString()),
                    Double.parseDouble(item.get("vRate").toString()),
                    Integer.parseInt(item.get("maxChain").toString())
            ));

        }
        return minableItemList;
    }
}