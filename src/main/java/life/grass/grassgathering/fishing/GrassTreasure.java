package life.grass.grassgathering.fishing;

import com.google.gson.JsonObject;
import org.bukkit.inventory.ItemStack;

public class GrassTreasure extends FishableItem {

    public GrassTreasure(String name, JsonObject config) {
        super(name, config);
    }

    @Override
    public ItemStack getItemStack() {
        return super.itemStack;
    }

}
