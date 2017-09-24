package life.grass.grassgathering.fishing;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import life.grass.grassitem.GrassJson;
import life.grass.grassitem.ItemBuilder;
import life.grass.grassitem.JsonHandler;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class FishableItem {

    private int defaultRatio;
    private HashMap<String, Double> bioRate = new HashMap<>();
    private HashMap<String, Double> weatherRate = new HashMap<>();
    private List<FishableItemEnchant> enchantList = new ArrayList<>();
    private boolean isOnlyOcean;
    private int exp;
    protected ItemStack itemStack;

    FishableItem(String name, JsonObject config) {

        this.defaultRatio = config.get("defaultRatio").getAsInt();
        this.itemStack = ItemBuilder.buildByUniqueName(name);
        this.isOnlyOcean = config.has("isOnlyOcean") && config.get("isOnlyOcean").getAsBoolean();
        this.exp = config.has("exp") ? config.get("exp").getAsInt() : 3;

        if (config.has("bioRate")) {
            for (Map.Entry<String, JsonElement> entry : config.get("bioRate").getAsJsonObject().entrySet()) {
                bioRate.put(entry.getKey(), entry.getValue().getAsDouble());
            }
        }
        if (config.has("weatherRate")) {
            for (Map.Entry<String, JsonElement> entry : config.get("weatherRate").getAsJsonObject().entrySet()) {
                weatherRate.put(entry.getKey(), entry.getValue().getAsDouble());
            }
        }
        if (config.has("enchantRate")) {
            for (Map.Entry<String, JsonElement> entry : config.get("enchantRate").getAsJsonObject().entrySet()) {
                enchantList.add(new FishableItemEnchant(entry.getKey(), entry.getValue().getAsInt()));
            }
        }
    }

    public ItemStack addEnchant(ItemStack item) {
        ItemStack itemToReturn;
        if (this.enchantList.size() > 0) {
            String enchant = this.enchantList.get(FishingManager.probMaker(FishingManager.makeSumList(this.makeEnchantRateList()))).getName();
            itemToReturn = enchant.equals("none") ? item : JsonHandler.putDynamicData(item, "Enchant/Prefix",
                    this.enchantList.get(FishingManager.probMaker(FishingManager.makeSumList(this.makeEnchantRateList()))).getName());
        } else {
            itemToReturn = item;
        }
        return itemToReturn;
    }

    private List<Double> makeEnchantRateList() {
        List<Double> list = new ArrayList<>();
        this.enchantList.forEach(enchant -> list.add((double) enchant.getRate()));
        return list;
    }

    public double getRealRatio(Player player) {

        if (this.isOnlyOcean && (Math.abs(player.getLocation().getX()) < 2000 && Math.abs(player.getLocation().getZ()) < 2000)) {

            return 0;

        } else {

            double r1;
            double r2;

            String bString = player.getWorld().getBiome(player.getLocation().getBlockX(), player.getLocation().getBlockZ()).toString();
            String wString = player.getWorld().hasStorm() ? player.getWorld().isThundering() ? "ThunderStorm" : "Rain" : "Clear";

            r1 = bioRate.get(bString) != null ? bioRate.get(bString) : bioRate.get("others") != null ? bioRate.get("others") : 1;
            r2 = weatherRate.get(wString) != null ? weatherRate.get(wString) : weatherRate.get("others") != null ? weatherRate.get("others") : 1;

            return r1 * r2 * defaultRatio;
        }
    }

    public int getExp() {
        return this.exp;
    }

    public abstract ItemStack getItemStack();
}
