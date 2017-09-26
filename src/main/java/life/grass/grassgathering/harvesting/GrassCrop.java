package life.grass.grassgathering.harvesting;

import life.grass.grassitem.GrassJson;
import life.grass.grassitem.ItemBuilder;
import life.grass.grassitem.JsonHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
public class GrassCrop {

    ItemStack itemStack;

    public GrassCrop(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public GrassJson getGrassJson() {
        return JsonHandler.getGrassJson(itemStack);
    }

    public void dropGrassCrop(Item drop) {
        GrassJson json = this.getGrassJson();
        if (json == null) return;
        if (!json.hasDynamicValueInItem("ExpireDate") && json.hasItemTag("Ingredient")) {
            this.itemStack = JsonHandler.putExpireDateHours(this.itemStack, 12);
            this.itemStack = putSpeciality(this.itemStack, drop.getLocation());
            drop.setItemStack(this.itemStack);
            int dropExp = Math.random() <= 0.02 ? 1 : 0;
            if (!(drop.getItemStack().getType() == Material.BROWN_MUSHROOM) && !(drop.getItemStack().getType() == Material.RED_MUSHROOM)) {
                drop.getWorld().spawn(drop.getLocation(), ExperienceOrb.class).setExperience(dropExp);
            }
        }
    }

    public void dropExceptionalCrop(Block block) {
        if (this.itemStack.getType().equals(Material.AIR)) return;
        if (block.getData() < 7) {
            if (block.getType().equals(Material.POTATO)) {
                block.getWorld().dropItemNaturally(block.getLocation(), ItemBuilder.buildByUniqueName("seed_potato"));
            } else if (block.getType().equals(Material.CARROT)) {
                block.getWorld().dropItemNaturally(block.getLocation(), ItemBuilder.buildByUniqueName("seed_carrot"));
            }
        } else {
            int seedAmount = (int) Math.ceil(Math.random() * 2);
            int edibleAmount = (int) Math.ceil(Math.random() * 3);
            ItemStack seedItem;
            ItemStack edibleItem;
            if (block.getType().equals(Material.POTATO)) {
                seedItem = ItemBuilder.buildByUniqueName("seed_potato");
                edibleItem = ItemBuilder.buildByUniqueName("Vanilla_POTATO_ITEM");
            } else if (block.getType().equals(Material.CARROT)) {
                seedItem = ItemBuilder.buildByUniqueName("seed_carrot");
                edibleItem = ItemBuilder.buildByConfigString("Vanilla_CARROT_ITEM");
            } else {
                seedItem = new ItemStack(Material.AIR);
                edibleItem = new ItemStack(Material.AIR);
            }
            edibleItem = putSpeciality(edibleItem, block.getLocation());
            seedItem.setAmount(seedAmount);
            edibleItem.setAmount(edibleAmount);
            block.getWorld().dropItemNaturally(block.getLocation(), seedItem);
            block.getWorld().dropItemNaturally(block.getLocation(), edibleItem);
        }
    }

    private ItemStack putSpeciality(ItemStack itemStack, Location location) {
        String enchant = specialityConverter(location);
        return enchant == null ? itemStack : JsonHandler.putDynamicData(itemStack, "Enchant/Prefix", enchant);

    }

    private String specialityConverter(Location location) {

        String biomeName = location.getWorld().getBiome(location.getBlockX(), location.getBlockZ()).toString();

        if (location.getBlockY() < 50) {
            return "underground";
        } else if (location.getBlock().getLightFromSky() < 12) {
            return "lanky";
        } else if (biomeName.contains("DESERT")) {
            return "deserts";
        } else if (biomeName.contains("ICE") || biomeName.contains("COLD")) {
            return "snowLands";
        } else if (biomeName.contains("OCEAN") || biomeName.contains("BEACH")) {
            return "malnourished";
        } else if (biomeName.contains("EXTREME_HILL")) {
            return "hills";
        } else if (biomeName.contains("SWAMPLAND")) {
            return "fresh";
        } else if (biomeName.contains("JUNGLE")) {
            return "strong";
        } else {
            return null;
        }

    }

}
