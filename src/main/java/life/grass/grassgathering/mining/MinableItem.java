package life.grass.grassgathering.mining;

import com.google.gson.JsonObject;
import life.grass.grassgathering.GPCalculator;
import life.grass.grassgathering.GrassGathering;
import life.grass.grassitem.GrassJson;
import life.grass.grassitem.ItemBuilder;
import life.grass.grassitem.JsonHandler;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.List;

import static java.lang.Math.exp;
import static java.lang.Math.pow;

public class MinableItem {

    private final String uniqueName;
    private final int modeHeight;
    private final double highestRatio;
    private final double vRate;
    private final int maxChain;
    private final int exp;

    private final String CHAIN_KEY;
    private final String BBCHAIN_KEY;
    private final String NOWBB_KEY;
    private final ChatColor CHAT_COLOR;
    private final boolean HAS_BB;

    MinableItem(String name, JsonObject jsonObject) {
        this.uniqueName = name;
        this.modeHeight = jsonObject.get("modeHeight").getAsInt();
        this.highestRatio = jsonObject.get("highestRatio").getAsDouble();
        this.vRate = jsonObject.get("vRate").getAsDouble();
        this.maxChain = jsonObject.get("maxChain").getAsInt();
        this.exp = jsonObject.get("exp").getAsInt();
        
        this.CHAIN_KEY = this.uniqueName + "chain";
        this.BBCHAIN_KEY = this.uniqueName + "BBChain";
        this.NOWBB_KEY = this.uniqueName + "nowBB";
        this.CHAT_COLOR = getChatColorByConfig(jsonObject.get("chatColor").getAsString());
        this.HAS_BB = jsonObject.has("hasBB") && jsonObject.get("hasBB").getAsBoolean();
    }

    public void mine(Player player, boolean isNormalStone, Location bLocation) {

        int bbChain = 0;
        boolean isNowBB = false;

        for (MetadataValue a : player.getMetadata(this.BBCHAIN_KEY)) {
            bbChain = (int) a.value();
        }

        for (MetadataValue b : player.getMetadata(this.NOWBB_KEY)) {
            isNowBB = (boolean) b.value();
        }

        if (bbChain > 0 && this.HAS_BB) {

            bbChain(player, bLocation, isNowBB);

        } else {
            dropItem(player, isNormalStone, bLocation, bbChain);
            chainItem(player, bLocation);
        }
    }

    public void dropItem(Player player, boolean isNormalStone, Location bLocation, int bbChain) {

        if (bbChain == 0) {

            double prob = Math.random();
            double gRate;
            GrassJson grassJson = JsonHandler.getGrassJson(player.getInventory().getItemInMainHand());
            if (grassJson != null) {
                gRate = GPCalculator.toLogRate(grassJson.getJsonReader().getActualGatheringPower());
            } else {
                gRate = GPCalculator.toLogRate(0);
            }

            if (gRate > 0.2 && !isNormalStone) gRate = 0.2;

            double ratio = (exp(-pow(((modeHeight - bLocation.getY()) * 2) / (modeHeight * vRate), 2)))
                    * highestRatio * gRate;

            if (prob < ratio && prob > 0) {
                player.getWorld().dropItem(bLocation.add(0.5, 0, 0.5), getItemStack());
                player.giveExp(exp);

                int chain = (int) (Math.random() * (maxChain + 1));
                player.setMetadata(this.CHAIN_KEY, new FixedMetadataValue(GrassGathering.getInstance(), chain));

                if (judgeChanceTime()) {

                    player.setMetadata(this.BBCHAIN_KEY, new FixedMetadataValue(GrassGathering.getInstance(), 5));
                    player.sendTitle("", "おや?" + this.CHAT_COLOR + "大鉱脈" + ChatColor.WHITE + "の予感が...", 25, 10, 10);

                }
            }
        }
    }

    public void chainItem(Player player, Location bLocation) {

        List<MetadataValue> chains = player.getMetadata(this.CHAIN_KEY);

        for (MetadataValue chain : chains) {
            if (chain.getOwningPlugin().getDescription().getName().equals(GrassGathering.getInstance().getDescription().getName())) {
                if ((int) chain.value() > 0) {
                    int chain1 = (int) chain.value();
                    player.getWorld().dropItem(bLocation.add(0.5, 0, 0.5), getItemStack());

                    player.setMetadata(this.CHAIN_KEY, new FixedMetadataValue(GrassGathering.getInstance(), chain1 - 1));
                    player.giveExp(this.exp);
                }
            }
        }
    }

    public void bbChain(Player player, Location bLocation, boolean isNowBB) {

        List<MetadataValue> bbChains = player.getMetadata(this.BBCHAIN_KEY);

        if (isNowBB) {

            ItemStack itemToDrop = getItemStack();
            itemToDrop.setAmount(2);
            player.getWorld().dropItem(bLocation.add(0.5, 0, 0.5), itemToDrop);

            bLocation.getWorld().spawnParticle(Particle.CRIT, bLocation, 25);
            if (itemToDrop.getType().equals(Material.DIAMOND)) {
                bLocation.getWorld().spawnParticle(Particle.LAVA, bLocation, 10);
            }

            for (MetadataValue bbChain : bbChains) {
                if (bbChain.getOwningPlugin().getDescription().getName()
                        .equals(GrassGathering.getInstance().getDescription().getName())) {

                    player.setMetadata(this.BBCHAIN_KEY,
                            new FixedMetadataValue(GrassGathering.getInstance(), (int) bbChain.value() - 1));

                    if ((int) bbChain.value() == 1) {
                        player.setMetadata(this.NOWBB_KEY,
                                new FixedMetadataValue(GrassGathering.getInstance(), false));
                    }
                }
            }
        } else {

            for (MetadataValue bbChain : bbChains) {
                if (bbChain.getOwningPlugin().getDescription().getName().equals(GrassGathering.getInstance().getDescription().getName())) {
                    if ((int) bbChain.value() > 0) {
                        player.setMetadata(this.BBCHAIN_KEY,
                                new FixedMetadataValue(GrassGathering.getInstance(), (int) bbChain.value() - 1));
                        if ((int) bbChain.value() == 1) {

                            boolean isBB = judgeBigBonus();
                            player.setMetadata(this.NOWBB_KEY,
                                    new FixedMetadataValue(GrassGathering.getInstance(), isBB));
                            if (isBB) {
                                if (this.uniqueName.equals("Vanilla_DIAMOND")) {

                                    specialBBEffect(player, bLocation);

                                } else {

                                    normalBBEffect(player, bLocation);

                                }

                            } else {
                                player.sendTitle("", "残念...ただの鉱脈だった...", 10, 40, 10);


                                player.getWorld().dropItem(bLocation.add(0.5, 0, 0.5), getItemStack());
                                player.setMetadata(this.CHAIN_KEY, new FixedMetadataValue(GrassGathering.getInstance(), (int) (Math.random() * (maxChain + 1))));
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean judgeChanceTime() {
        return Math.random() < 0.01;
    }

    public boolean judgeBigBonus() {
        return Math.random() < 0.5;
    }

    public ItemStack getItemStack() {
       return ItemBuilder.buildByUniqueName(this.uniqueName);
    }

    private void normalBBEffect(Player player, Location bLocation) {

        BukkitScheduler scheduler = GrassGathering.getInstance().getServer().getScheduler();

        player.playSound(bLocation, Sound.BLOCK_ANVIL_PLACE, 1, 1);
        scheduler.scheduleSyncDelayedTask(GrassGathering.getInstance(), new Runnable() {
            @Override
            public void run() {
                player.playSound(bLocation, Sound.BLOCK_ANVIL_PLACE, 1, 1);
            }
        }, 5L);
        scheduler.scheduleSyncDelayedTask(GrassGathering.getInstance(), new Runnable() {
            @Override
            public void run() {
                player.playSound(bLocation, Sound.BLOCK_ANVIL_PLACE, 1, 1);
            }
        }, 10L);
        player.sendTitle("やった!!" + CHAT_COLOR + "大鉱脈" + ChatColor.WHITE + "だ!!!!", "", 5, 70, 10);
        player.setMetadata(this.BBCHAIN_KEY, new FixedMetadataValue(GrassGathering.getInstance(), 10));
        Bukkit.broadcastMessage(ChatColor.GREEN + "[大鉱脈当選情報!!] " + ChatColor.WHITE + player.getName() + "さんが" + this.CHAT_COLOR + this.getItemStack().getType().toString() + ChatColor.WHITE + "の大鉱脈を開拓しました!");
    }

    private void specialBBEffect(Player player, Location bLocation) {

        BukkitScheduler scheduler = GrassGathering.getInstance().getServer().getScheduler();

        player.playSound(bLocation, Sound.BLOCK_ANVIL_PLACE, 1, 1);
        scheduler.scheduleSyncDelayedTask(GrassGathering.getInstance(), new Runnable() {
            @Override
            public void run() {
                player.playSound(bLocation, Sound.BLOCK_ANVIL_PLACE, 1, 1);
            }
        }, 5L);
        scheduler.scheduleSyncDelayedTask(GrassGathering.getInstance(), new Runnable() {
            @Override
            public void run() {
                player.playSound(bLocation, Sound.BLOCK_ANVIL_PLACE, 1, 1);
            }
        }, 10L);
        scheduler.scheduleSyncDelayedTask(GrassGathering.getInstance(), new Runnable() {
            @Override
            public void run() {
                player.playSound(bLocation, Sound.BLOCK_ANVIL_PLACE, 1, 1);
            }
        }, 15L);
        scheduler.scheduleSyncDelayedTask(GrassGathering.getInstance(), new Runnable() {
            @Override
            public void run() {
                player.playSound(bLocation, Sound.BLOCK_ANVIL_PLACE, 1, 1);
            }
        }, 20L);

        player.sendTitle("やった!!!!" + CHAT_COLOR + "大鉱脈" + ChatColor.WHITE + "だ!!!!", "", 5, 70, 10);
        player.setMetadata(this.BBCHAIN_KEY, new FixedMetadataValue(GrassGathering.getInstance(), 10));
        Bukkit.broadcastMessage(ChatColor.AQUA + "[大鉱脈当選情報!!] " + ChatColor.WHITE + player.getName() + "さんが" + this.CHAT_COLOR + this.getItemStack().getType().toString() + ChatColor.WHITE + "の大鉱脈を開拓しました!");
    }

    public static ChatColor getChatColorByConfig(String string) {
        if (string == null) {
            return ChatColor.GOLD;
        } else switch (string) {
            case "coal":
                return ChatColor.BLACK;
            case "iron":
                return ChatColor.GRAY;
            case "gold":
                return ChatColor.YELLOW;
            case "emerald":
                return ChatColor.GREEN;
            case "redstone":
                return ChatColor.DARK_RED;
            case "diamond":
                return ChatColor.AQUA;
            default:
                return ChatColor.GOLD;
        }
    }
}
