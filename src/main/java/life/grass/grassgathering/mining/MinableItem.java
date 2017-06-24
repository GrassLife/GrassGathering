package life.grass.grassgathering.mining;

import life.grass.grassgathering.GrassGathering;
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

    private final String name;
    private final int modeHeight;
    private final double highestRatio;
    private final double vRate;
    private final int maxChain;

    MinableItem(String name, int modeHeight, double highestRatio, double vRate, int maxChain) {
        this.name = name;
        this.modeHeight = modeHeight;
        this.highestRatio = highestRatio;
        this.vRate = vRate;
        this.maxChain = maxChain;
    }

    public void dropItem(Player player, Location bLocation){

        double prob = Math.random();
        double ratio = (exp( -pow(((modeHeight - bLocation.getY()) * 2) / (modeHeight * vRate), 2))) * highestRatio;

        if (prob < ratio && prob > 0) {
            player.getWorld().dropItemNaturally(bLocation, getItemStack());

            int chain = (int) (Math.random() * (maxChain + 1));
            player.setMetadata(this.name + "chain" ,new FixedMetadataValue(GrassGathering.getInstance(), chain));
        }

    }

    public void chainItem(Player player, Location bLocation) {

        List<MetadataValue> chains = player.getMetadata(this.name + "chain");

        for (MetadataValue chain : chains) {
            if (chain.getOwningPlugin().getDescription().getName().equals(GrassGathering.getInstance().getDescription().getName())) {
                if ((int) chain.value() > 0) {
                    int chain1 = (int) chain.value();
                    player.getWorld().dropItemNaturally(bLocation, getItemStack());

                    player.setMetadata(this.name + "chain", new FixedMetadataValue(GrassGathering.getInstance(), chain1 - 1));
                }
            }
        }
    }

    public ItemStack getItemStack() {
       return new ItemStack(Material.getMaterial(this.name.toUpperCase()));
    }
}
