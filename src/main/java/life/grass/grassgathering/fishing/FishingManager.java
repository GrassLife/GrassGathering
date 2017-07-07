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
}
