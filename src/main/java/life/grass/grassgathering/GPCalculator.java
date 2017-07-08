package life.grass.grassgathering;

public class GPCalculator {

    public static double toStandardRate(int gPower) {
        return (0.5 * (Math.log((gPower/35) + 1.1))) / Math.log(2);
    }

}
