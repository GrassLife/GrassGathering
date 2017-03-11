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

    COAL(new ItemStack(Material.COAL), 60, 0.022, 2.8, 10, 1),
    IRON_ORE(new ItemStack(Material.IRON_ORE), 40, 0.0175, 2, 10, 2),
    GOLD_ORE(new ItemStack(Material.GOLD_ORE), 30, 0.0075, 2, 6, 4),
    LAPIS_LAZULI(new ItemStack(Material.INK_SACK, 1, (short)4), 35, 0.012, 0.5, 10, 3),
    EMERALD(new ItemStack(Material.EMERALD), 20, 0.015, 0.2, 5, 5),
    DIAMOND(new ItemStack(Material.DIAMOND), 13, 0.002, 1.5, 5, 10),
    REDSTONE(new ItemStack(Material.REDSTONE), 15, 0.02, 1.4, 10, 3);

    private final ItemStack material;
    private final int modeHeight;
    private final double highestRatio;
    private final double vRate;
    private final int maxChain;
    private final int knowledgePoint;

    EMinableItems(ItemStack material, int modeHeight, double highestRatio, double vRate, int maxChain, int knowledgePoint) {
        this.material = material;
        this.modeHeight = modeHeight;
        this.highestRatio = highestRatio;
        this.vRate = vRate;
        this.maxChain = maxChain;
        this.knowledgePoint = knowledgePoint;
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

    public int getKnowledgePoint() {
        return knowledgePoint;
    }

    public void dropItem(BlockBreakEvent event, Location bLocation){
        double prob = Math.random();
        double ratio = (exp( -pow(((modeHeight - bLocation.getY()) * 2) / (modeHeight * vRate), 2))) * highestRatio;
        if (prob < ratio && prob > 0) {
            event.getPlayer().getWorld().dropItemNaturally(bLocation, material);

            KnowledgeStats stats = new KnowledgeStats(event.getPlayer());
            increasePickaxeKnowledgePoint(stats);
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
                    increasePickaxeKnowledgePoint(stats);
                    player.setMetadata(this.name() + "chain", new FixedMetadataValue(GrassGathering.getInstance(), chain1 - 1));
                }
            }
        }
    }

    public void increasePickaxeKnowledgePoint(KnowledgeStats stats) {
        stats.increaseKnowledgePoint("PICKAXE", knowledgePoint);
        System.out.println(stats.getKnowledgePointString());
    }
}
