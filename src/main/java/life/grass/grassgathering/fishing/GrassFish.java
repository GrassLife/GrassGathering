package life.grass.grassgathering.fishing;

import com.google.gson.JsonObject;
import life.grass.grassitem.JsonHandler;
import org.bukkit.inventory.ItemStack;
import java.time.LocalDateTime;

public class GrassFish extends FishableItem {

    private int expireHours;
    private int sachi;
    private double minSizeRate;
    private double maxSizeRate;

    public GrassFish (String name, JsonObject config) {
        super(name, config);
        this.sachi = config.get("sachi").getAsInt();
        this.expireHours = config.get("expireHours").getAsInt();
        this.minSizeRate = config.has("minSizeRate") ? config.get("minSizeRate").getAsDouble() : 0.5;
        this.maxSizeRate = config.has("maxSizeRate") ? config.get("maxSizeRate").getAsDouble() : 1.5;
    }

    @Override
    public ItemStack getItemStack() {

        ItemStack item = super.itemStack;

        double rateParam = Math.random();
        double sizeRate = ((1 - rateParam) * minSizeRate) + (rateParam * maxSizeRate);

        if (expireHours != 0) item = JsonHandler.putDynamicData(item, "ExpireDate", LocalDateTime.now().plusHours(expireHours).toString());
        if (sachi != 0) item = JsonHandler.putDynamicData(item, "FoodElement/SACHI", -sachi);

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
}
