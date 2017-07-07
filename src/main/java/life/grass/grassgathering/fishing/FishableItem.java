package life.grass.grassgathering.fishing;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import life.grass.grassitem.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public abstract class FishableItem {

    private int defaultRatio;
    private HashMap<String, Double> bioRate = new HashMap<>();
    private HashMap<String, Double> weatherRate = new HashMap<>();
    protected ItemStack itemStack;

    FishableItem(String name, JsonObject config){
        Gson gson = new Gson();
        this.defaultRatio = config.get("defaultRatio").getAsInt();
        this.itemStack = ItemBuilder.buildByUniqueName(name);
        if (config.has("bioRate")) {
            for (Map.Entry<String, JsonElement> entry : config.get("bioRate").getAsJsonObject().entrySet()) {
                bioRate.put(entry.getKey(), entry.getValue().getAsDouble());
            }
        }
          // this.bioRate = gson.fromJson(config.get("bioRate").getAsJsonObject(), new TypeToken<Map<String, Double>>(){}.getType());
        if (config.has("weatherRate")) {
            for (Map.Entry<String, JsonElement> entry : config.get("weatherRate").getAsJsonObject().entrySet()) {
                weatherRate.put(entry.getKey(), entry.getValue().getAsDouble());
            }
        }
    }

    public double getRealratio(Player player) {

        double r1;
        double r2;

        String bString = player.getWorld().getBiome(player.getLocation().getBlockX(), player.getLocation().getBlockZ()).toString();
        String wString = player.getWorld().hasStorm() ? player.getWorld().isThundering() ? "ThunderStorm" : "Rain" : "Clear";

        r1 = bioRate.get(bString) != null ? bioRate.get(bString) :  bioRate.get("others") != null ? bioRate.get("others") : 1;
        r2 = weatherRate.get(wString) != null ? weatherRate.get(wString) : weatherRate.get("others") != null ? weatherRate.get("others") : 1;

        return r1 * r2 * defaultRatio;
    }

    public abstract ItemStack getItemStack();
}
