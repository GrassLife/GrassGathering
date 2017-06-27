package life.grass.grassgathering.fishing;

import com.google.gson.JsonObject;
import life.grass.grassgathering.ResourceJsonContainer;
import org.bukkit.WeatherType;
import org.bukkit.block.Biome;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FishPool {

    private static FishPool fishPool = new FishPool();
    private static List<FishableItem> fishList = makeFishableItems();

    private FishPool() {

        fishList = makeFishableItems();

    }

    private static List<FishableItem> makeFishableItems() {

        List<FishableItem> fishableItemList = new ArrayList<FishableItem>();
        Map<String, JsonObject> fishJsonMap = ResourceJsonContainer.getInstance().getFishJsonMap();

        fishJsonMap.forEach((name, item) -> {
            fishableItemList.add(new FishableItem(name, item));
        });
        return fishableItemList;
    }

    public static FishPool getInstance() {
        return fishPool;
    }

    /*
    釣りをしているときのバイオームと天候などを渡してgetRealratioにより比取得して対応するindexに入れていきます。
     */
    public ArrayList<Double> getRatioList(Biome b, WeatherType w) {
        ArrayList<Double> list = new ArrayList<>();
        for (int i = 0; i < fishList.size(); i++) {
            list.add(fishList.get(i).getRealratio(b, w));
        }
        return list;
    }


    public List<FishableItem> getFishList() {
        return fishList;
    }
}
