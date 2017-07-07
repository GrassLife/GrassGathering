package life.grass.grassgathering.harvesting.event;

import life.grass.grassgathering.harvesting.GrassCrop;
import life.grass.grassitem.JsonHandler;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class HarvestingEvent implements Listener {

    @EventHandler
    public void onDropItems(EntitySpawnEvent event) {
        if (event.getEntity() instanceof Item) {

            Item drop = (Item) event.getEntity();
            GrassCrop crop = new GrassCrop(drop.getItemStack());

            crop.dropGrassCrop(drop);
        }
    }

    @EventHandler
    public void onRightClickWithPotato(PlayerInteractEvent event) {
        System.out.println(event.getClickedBlock().getType().toString());


        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
                && event.getClickedBlock().getType().equals(Material.SOIL)
                && (event.getMaterial().equals(Material.POTATO_ITEM) || event.getMaterial().equals(Material.CARROT_ITEM))) {

            String uniqueName = JsonHandler.getGrassJson(event.getItem()).getUniqueName();
            System.out.println(uniqueName);
            if (!uniqueName.equals("seed_potato") && !uniqueName.equals("seed_carrot")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBreakExceptionalCrops(BlockBreakEvent event) {
        event.setDropItems(false);
        Material blockType = event.getBlock().getType();
        if (blockType.equals(Material.POTATO) || blockType.equals(Material.CARROT)) {
            GrassCrop crop;
            if (blockType.equals(Material.POTATO)) {
                crop = new GrassCrop(new ItemStack(Material.POTATO_ITEM));
            } else if (blockType.equals(Material.CARROT)) {
                crop = new GrassCrop(new ItemStack(Material.CARROT_ITEM));
            } else {
                crop = new GrassCrop(new ItemStack(Material.AIR));
            }
            crop.dropExceptionalCrop(event.getBlock());
        }
    }
}
