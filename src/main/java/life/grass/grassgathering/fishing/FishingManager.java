package life.grass.grassgathering.fishing;

import life.grass.grassgathering.GrassGathering;
import org.bukkit.Material;
import org.bukkit.WeatherType;
import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FishingManager {

    private static List<FishableItem> fishList = makeFishableItems();
    private static List<Double> failList = makeFailList();

    /*
    実装時はコンフィグからアイテムのリストを作る予定。
     */
    private static List<FishableItem> makeFishableItems() {

        List<FishableItem> fishableItemList = new ArrayList<FishableItem>();
        ConfigurationSection items = GrassGathering.getInstance().getConfig().getConfigurationSection("fishableItems");

        for(String key : items.getKeys(false)) {
            ConfigurationSection item = items.getConfigurationSection(key);

            fishableItemList.add(new FishableItem(
                    Integer.parseInt(item.get("defaultRatio").toString()),
                    item.get("uniqueName").toString()
            ));

        }
        return fishableItemList;
    }

    public static List<Double> makeFailList(){
        List<Double> list = new ArrayList<>();
        list.add(5.0);
        list.add(5.0);
        return list;
    }

    /*
    釣りをしているときのバイオームと天候などを渡してgetRealratioにより比取得して対応するindexに入れていきます。
     */
    public static ArrayList<Double> makeRatioList(Biome b, WeatherType w) {
        ArrayList<Double> list = new ArrayList<>();
        for (int i = 0; i < fishList.size(); i++) {
            list.add(fishList.get(i).getRealratio(b, w));
        }
        return list;
    }

    /*
    リストを渡すと各々のインデックスまでの総和を要素に持つ比のリストを生成します
     */
    public static List<Double> makeSumList(List<Double> list) {
        List<Double> sumList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Double left = i == 0 ? 0.0 : sumList.get(i - 1);
            sumList.add(left + list.get(i));
        }
        return sumList;
    }

    /*
    比のリストを渡すとそれに従ってランダムなindexを返してくれます。
     */
    public static int probMaker(List<Double> list) {
        double random = Math.random() * list.get(list.size() - 1);
        int indexNumber = 0;
        for (int i = 0; i < list.size(); i++) {
            double left = i == 0 ? 0 : list.get(i - 1);
            if (random >= left && random < list.get(i)) {
                indexNumber = i;
            }
        }

        return indexNumber;
    }



    public static List<FishableItem> getFishList() {
        return fishList;
    }

    public static List<Double> getFailList() {
        return failList;
    }
}
