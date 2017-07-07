package life.grass.grassgathering.fishing;

public class FishableItemEnchant {
    private String name;
    private int rate;

    FishableItemEnchant(String name, int rate) {
        this.name = name;
        this.rate = rate;
    }

    public int getRate() {
        return rate;
    }

    public String getName() {
        return name;
    }
}
