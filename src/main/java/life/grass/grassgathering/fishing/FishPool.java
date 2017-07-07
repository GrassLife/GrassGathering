package life.grass.grassgathering.fishing;

import com.google.gson.JsonObject;
import life.grass.grassgathering.ResourceJsonContainer;
import org.bukkit.Material;
import org.bukkit.WeatherType;
import org.bukkit.block.Biome;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FishPool {

    private static FishPool fishPool = new FishPool();
    private static List<FishableItem> fishList = makeFishableItems();
    private static List<Double> failList = makeFailList();

    private FishPool() {

        fishList = makeFishableItems();

    }

    public static FishPool getInstance() {
        return fishPool;
    }

    private static List<FishableItem> makeFishableItems() {

        List<FishableItem> fishableItemList = new ArrayList<FishableItem>();
        Map<String, JsonObject> fishableItemJsonMap = ResourceJsonContainer.getInstance().getFishableItemJsonMap();

        fishableItemJsonMap.forEach((name, item) -> {
            String type = item.get("type").getAsString();
            if (type.equals("Fish")) {
                fishableItemList.add(new GrassFish(name, item));
            } else if (type.equals("Treasure")) {
                fishableItemList.add(new GrassTreasure(name, item));
            }
        });
        return fishableItemList;
    }

    /*
    釣りをしているときのバイオームと天候などを渡してgetRealratioにより比取得して対応するindexに入れていきます。
     */
    private ArrayList<Double> getRatioList(Biome b, WeatherType w) {
        ArrayList<Double> list = new ArrayList<>();

        fishList.forEach(fish -> list.add(fish.getRealratio(b, w)));

        return list;
    }

    public ItemStack giveFishTo(Player player) {

        ItemStack itemStack = null;

        if (isFailed()) {
            player.sendTitle("", "魚が逃げてしまった!!", 10, 70, 20);
        } else {
            List<Double> ratioList = fishPool.getRatioList(player.getLocation().getWorld()
                    .getBiome(player.getLocation().getBlockX(), player.getLocation().getBlockZ()), player.getPlayerWeather());
            List<Double> rsumList = FishingManager.makeSumList(ratioList);
            itemStack = fishList.get(FishingManager.probMaker(rsumList)).getItemStack();
        }
        return Optional.ofNullable(itemStack).orElse(new ItemStack(Material.AIR));
    }

    private static List<Double> makeFailList(){
        List<Double> list = new ArrayList<>();
        list.add(5.0);
        list.add(5.0);
        return list;
    }

    private static boolean isFailed() {
        return FishingManager.probMaker(FishingManager.makeSumList(failList)) == 0;
    }

    public void releaseFishes() {
        fishList = makeFishableItems();
    }
}
