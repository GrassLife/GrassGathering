package life.grass.grassgathering.fishing;

import com.google.gson.JsonObject;
import life.grass.grassitem.ItemBuilder;
import life.grass.grassitem.JsonHandler;
import org.bukkit.Bukkit;
import org.bukkit.WeatherType;
import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.util.HashMap;

public abstract class FishableItem {

    private int defaultRatio;
    private HashMap<Biome, Double> bioRate = new HashMap<>();
    private HashMap<WeatherType, Double> weatherRate = new HashMap<>();
    protected ItemStack itemStack;

    FishableItem(String name, JsonObject config){
        this.defaultRatio = config.get("defaultRatio").getAsInt();
        this.itemStack = ItemBuilder.buildByUniqueName(name);
    }

    public double getRealratio(Biome b, WeatherType w) {

        double r1;
        double r2;

        r1 = bioRate.get(b) == null ? 1 : bioRate.get(b);

        r2 = weatherRate.get(w) == null ? 1 : weatherRate.get(w);

        return r1 * r2 * defaultRatio;
    }

    public abstract ItemStack getItemStack();

    public void setBioRate(HashMap<Biome, Double> bioRate) {
        this.bioRate = bioRate;
    }

    public void setWeatherRate(HashMap<WeatherType, Double> weatherRate) {
        this.weatherRate = weatherRate;
    }


}
