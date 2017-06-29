package life.grass.grassgathering.fishing;

import com.google.gson.JsonObject;
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
    private int expireHours;
    private int sachi;

    FishableItem(String name, JsonObject config){
        this.defaultRatio = config.get("defaultRatio").getAsInt();
        ItemStack item = ItemBuilder.buildByUniqueName(name);
        this.sachi = config.get("sachi").getAsInt();
        this.expireHours = config.get("expireHours").getAsInt();
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
        double rand = Math.random();
        if(rand <= 0.01) {
            item = JsonHandler.putDynamicData(item, "Enchant/Prefix", "fatness");
        } else if(rand <= 0.1) {
            item = JsonHandler.putDynamicData(item, "Enchant/Prefix", "child");
        }

        return item;
    }

    public void setBioRate(HashMap<Biome, Double> bioRate) {
        this.bioRate = bioRate;
    }

    public void setWeatherRate(HashMap<WeatherType, Double> weatherRate) {
        this.weatherRate = weatherRate;
    }


}
