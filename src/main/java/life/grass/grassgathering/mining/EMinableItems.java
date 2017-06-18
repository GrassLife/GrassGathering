package life.grass.grassgathering.mining;

import life.grass.grassgathering.GrassGathering;
import life.grass.grassknowledge.player.KnowledgeStats;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

import static java.lang.Math.exp;
import static java.lang.Math.pow;

/**
 * Created by takah on 2016/10/23.
 */
public enum EMinableItems {

    COAL(new ItemStack(Material.COAL),
            intConfig("coal.modeHeight"),
            doubleConfig("coal.highestRatio"),
            doubleConfig("coal.vRate"),
            intConfig("coal.maxChain")),
    IRON_ORE(new ItemStack(Material.IRON_ORE),
            intConfig("iron_ore.modeHeight"),
            doubleConfig("iron_ore.highestRatio"),
            doubleConfig("iron_ore.vRate"),
            intConfig("iron_ore.maxChain")),
    GOLD_ORE(new ItemStack(Material.GOLD_ORE),
            intConfig("gold_ore.modeHeight"),
            doubleConfig("gold_ore.highestRatio"),
            doubleConfig("gold_ore.vRate"),
            intConfig("gold_ore.maxChain")),
    LAPIS_LAZULI(new ItemStack(Material.INK_SACK, 1, (short)4),
            intConfig("lapis_lazuli.modeHeight"),
            doubleConfig("lapis_lazuli.highestRatio"),
            doubleConfig("lapis_lazuli.vRate"),
            intConfig("lapis_lazuli.maxChain")),
    EMERALD(new ItemStack(Material.EMERALD),
            intConfig("emerald.modeHeight"),
            doubleConfig("emerald.highestRatio"),
            doubleConfig("emerald.vRate"),
            intConfig("emerald.maxChain")),
    DIAMOND(new ItemStack(Material.DIAMOND),
            intConfig("diamond.modeHeight"),
            doubleConfig("diamond.highestRatio"),
            doubleConfig("diamond.vRate"),
            intConfig("diamond.maxChain")),
    REDSTONE(new ItemStack(Material.REDSTONE),
            intConfig("redstone.modeHeight"),
            doubleConfig("redstone.highestRatio"),
            doubleConfig("redstone.vRate"),
            intConfig("redstone.maxChain"));

    private final ItemStack material;
    private final int modeHeight;
    private final double highestRatio;
    private final double vRate;
    private final int maxChain;

    EMinableItems(ItemStack material, int modeHeight, double highestRatio, double vRate, int maxChain) {
        this.material = material;
        this.modeHeight = modeHeight;
        this.highestRatio = highestRatio;
        this.vRate = vRate;
        this.maxChain = maxChain;
    }

    public ItemStack getMaterial() {
        return material;
    }

    public int getModeHeight() {
        return modeHeight;
    }

    public double getHighestRatio() {
        return highestRatio;
    }

    public void dropItem(BlockBreakEvent event, Location bLocation){
        double prob = Math.random();
        double ratio = (exp( -pow(((modeHeight - bLocation.getY()) * 2) / (modeHeight * vRate), 2))) * highestRatio;
        if (prob < ratio && prob > 0) {
            event.getPlayer().getWorld().dropItemNaturally(bLocation, material);

            KnowledgeStats stats = new KnowledgeStats(event.getPlayer());
            int chain = (int) (Math.random() * (maxChain + 1));
            event.getPlayer().setMetadata(this.name() + "chain" ,new FixedMetadataValue(GrassGathering.getInstance(), chain));
        }

    }

    public void chainItem(BlockBreakEvent event, Location bLocation){
        Player player = event.getPlayer();
        List<MetadataValue> chains = player.getMetadata(this.name() + "chain");
        for(MetadataValue chain : chains){
            if (chain.getOwningPlugin().getDescription().getName().equals(GrassGathering.getInstance().getDescription().getName())){
                if ((int) chain.value() > 0){
                    int chain1 = (int) chain.value();
                    player.getWorld().dropItemNaturally(bLocation, material);

                    KnowledgeStats stats = new KnowledgeStats(event.getPlayer());
                    player.setMetadata(this.name() + "chain", new FixedMetadataValue(GrassGathering.getInstance(), chain1 - 1));
                }
            }
        }
    }

    private static int intConfig(String string) {
        return Integer.parseInt(GrassGathering.getInstance().getConfig().get(string).toString());
    }

    private static double doubleConfig(String string) {
        return Double.parseDouble(GrassGathering.getInstance().getConfig().get(string).toString());
    }

}
