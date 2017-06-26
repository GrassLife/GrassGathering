package life.grass.grassgathering.fishing;

import life.grass.grassgathering.GrassGathering;
import life.grass.grassitem.GrassJson;
import life.grass.grassitem.ItemBuilder;
import life.grass.grassitem.JsonHandler;
import org.bukkit.WeatherType;
import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.util.HashMap;

public class FishableItem {

    private int defaultRatio;
    private HashMap<Biome, Double> bioRate = new HashMap<>();
    private HashMap<WeatherType, Double> weatherRate = new HashMap<>();
    private ItemStack itemStack;
    private long expireHours;
    private int sachi;

    FishableItem(ConfigurationSection config){
        this.defaultRatio = Integer.parseInt(config.get("defaultRatio").toString());
        ItemStack item = ItemBuilder.buildByUniqueName(config.get("uniqueName").toString());
        this.sachi = Integer.parseInt(config.get("Sachi").toString());
        this.expireHours = Long.parseLong(config.get("ExpireHours").toString());
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
        ItemStack item = this.itemStack;
        double sizeRate = 0.5 + Math.random();
        item = JsonHandler.putDynamicData(item, "ExpireDate", LocalDateTime.now().plusHours(expireHours).toString());
        item = JsonHandler.putDynamicData(item, "FoodElement/SACHI", -sachi);
        item = JsonHandler.putDynamicData(item, "Calorie", "*" + sizeRate);
        item = JsonHandler.putDynamicData(item, "Weight", "*" + sizeRate);
        return item;
    }

    public void setBioRate(HashMap<Biome, Double> bioRate) {
        this.bioRate = bioRate;
    }

    public void setWeatherRate(HashMap<WeatherType, Double> weatherRate) {
        this.weatherRate = weatherRate;
    }


}
