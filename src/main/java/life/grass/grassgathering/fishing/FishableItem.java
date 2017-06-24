package life.grass.grassgathering.fishing;

import life.grass.grassgathering.GrassGathering;
import life.grass.grassitem.GrassJson;
import life.grass.grassitem.ItemBuilder;
import life.grass.grassitem.JsonHandler;
import org.bukkit.WeatherType;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.util.HashMap;

public class FishableItem {

    private int defaultRatio;
    private HashMap<Biome, Double> bioRate = new HashMap<>();
    private HashMap<WeatherType, Double> weatherRate = new HashMap<>();
    private ItemStack itemStack;

    FishableItem(int defaultRatio, String itemUniqueName){
        this.defaultRatio = defaultRatio;
        ItemStack item = ItemBuilder.buildByUniqueName(itemUniqueName);
        this.itemStack = item;
    }

    public double getRealratio(Biome b, WeatherType w) {

        double r1;
        double r2;
        double realRatio;

        r1 = bioRate.get(b) == null ? 1 : bioRate.get(b);

        r2 = weatherRate.get(w) == null ? 1 : weatherRate.get(w);

        realRatio = r1 * r2 * defaultRatio;
        return realRatio;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setBioRate(HashMap<Biome, Double> bioRate) {
        this.bioRate = bioRate;
    }

    public void setWeatherRate(HashMap<WeatherType, Double> weatherRate) {
        this.weatherRate = weatherRate;
    }


}
