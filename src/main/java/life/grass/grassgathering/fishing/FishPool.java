package life.grass.grassgathering.fishing;

import com.google.gson.JsonObject;
import life.grass.grassgathering.GPCalculator;
import life.grass.grassgathering.ResourceJsonContainer;
import life.grass.grassitem.GrassJson;
import life.grass.grassitem.JsonHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FishPool {

    private static FishPool fishPool = new FishPool();
    private static List<FishableItem> fishList = makeFishableItems();

    private FishPool() {

        fishList = makeFishableItems();
    }

    public static FishPool getInstance() {
        return fishPool;
    }

    private static List<FishableItem> makeFishableItems() {

        List<FishableItem> fishableItemList = new ArrayList<FishableItem>();
        Map<String, JsonObject> fishableItemJsonMap = ResourceJsonContainer.getInstance().getFishableItemJsonMap();

        fishableItemJsonMap.forEach((name, item) -> {
            String type = item.get("type").getAsString();
            if (type.equals("Fish")) {
                fishableItemList.add(new GrassFish(name, item));
            } else if (type.equals("Treasure")) {
                fishableItemList.add(new GrassTreasure(name, item));
            }
        });
        return fishableItemList;
    }

    /*
    釣りをしているときのバイオームと天候などを渡してgetRealratioにより比取得して対応するindexに入れていきます。
     */
    private ArrayList<Double> getRatioList(Player player) {
        ArrayList<Double> list = new ArrayList<>();

        fishList.forEach(fish -> list.add(fish.getRealRatio(player)));

        return list;
    }

    public ItemStack giveFishTo(Player player) {

        ItemStack itemStack = null;

        if (isFailed(player)) {
            player.sendTitle("", "魚が逃げてしまった!!", 10, 70, 20);
        } else {
            List<Double> ratioList = fishPool.getRatioList(player);
            List<Double> rsumList = FishingManager.makeSumList(ratioList);
            itemStack = fishList.get(FishingManager.probMaker(rsumList)).getItemStack();
        }
        return Optional.ofNullable(itemStack).orElse(new ItemStack(Material.AIR));
    }

    private List<Double> makeFailList(Player player){
        int gatheringPower;
        if (player.getInventory().getItemInMainHand().getType().equals(Material.FISHING_ROD)) {

            GrassJson grassJson = JsonHandler.getGrassJson(player.getInventory().getItemInMainHand());
            gatheringPower = grassJson.getJsonReader().getActualGatheringPower();

        } else if (player.getInventory().getItemInOffHand().getType().equals(Material.FISHING_ROD)) {

            GrassJson grassJson = JsonHandler.getGrassJson(player.getInventory().getItemInOffHand());
            gatheringPower = grassJson.getJsonReader().getActualGatheringPower();

        } else {

            gatheringPower = 0;

        }

        List<Double> list = new ArrayList<>();
        double successRate = ((GPCalculator.toArcTanRate(gatheringPower, 4) * 5) - 0.5);
        list.add(1.0);
        list.add(successRate < 0.1 ? 0.1 : successRate);
        return list;
    }

    private boolean isFailed(Player player) {
        return FishingManager.probMaker(FishingManager.makeSumList(makeFailList(player))) == 0;
    }

    public void releaseFishes() {
        fishList = makeFishableItems();
    }
}
