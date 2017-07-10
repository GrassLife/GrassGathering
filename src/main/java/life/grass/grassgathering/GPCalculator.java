package life.grass.grassgathering;

public class GPCalculator {

    public static double toArcTanRate(int gPower, int xParam) {
        return (2 / Math.PI) * (Math.atan((gPower/50) - xParam)) + 1;
    }

    public static double toLogRate(int gPower) {
        return (0.5 * (Math.log((gPower/30) + 1.1))) / Math.log(2);
    }

}
