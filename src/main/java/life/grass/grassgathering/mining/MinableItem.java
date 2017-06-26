package life.grass.grassgathering.mining;

import com.google.gson.JsonObject;
import life.grass.grassgathering.GrassGathering;
import life.grass.grassitem.ItemBuilder;
import life.grass.grassitem.JsonHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

import static java.lang.Math.exp;
import static java.lang.Math.pow;

public class MinableItem {

    private final String uniqueName;
    private final int modeHeight;
    private final double highestRatio;
    private final double vRate;
    private final int maxChain;

    MinableItem(String name, JsonObject jsonObject) {
        this.uniqueName = name;
        this.modeHeight = Integer.parseInt(jsonObject.get("modeHeight").toString());
        this.highestRatio = Double.parseDouble(jsonObject.get("highestRatio").toString());
        this.vRate = Double.parseDouble(jsonObject.get("vRate").toString());
        this.maxChain = Integer.parseInt(jsonObject.get("maxChain").toString());
    }

    public void dropItem(Player player, Location bLocation){

        double prob = Math.random();
        double ratio = (exp( -pow(((modeHeight - bLocation.getY()) * 2) / (modeHeight * vRate), 2))) * highestRatio;

        if (prob < ratio && prob > 0) {
            player.getWorld().dropItemNaturally(bLocation, getItemStack());

            int chain = (int) (Math.random() * (maxChain + 1));
            player.setMetadata(this.uniqueName + "chain" ,new FixedMetadataValue(GrassGathering.getInstance(), chain));
        }

    }

    public void chainItem(Player player, Location bLocation) {

        List<MetadataValue> chains = player.getMetadata(this.uniqueName + "chain");

        for (MetadataValue chain : chains) {
            if (chain.getOwningPlugin().getDescription().getName().equals(GrassGathering.getInstance().getDescription().getName())) {
                if ((int) chain.value() > 0) {
                    int chain1 = (int) chain.value();
                    player.getWorld().dropItemNaturally(bLocation, getItemStack());

                    player.setMetadata(this.uniqueName + "chain", new FixedMetadataValue(GrassGathering.getInstance(), chain1 - 1));
                }
            }
        }
    }

    public ItemStack getItemStack() {
       return ItemBuilder.buildByUniqueName(uniqueName);
    }
}
