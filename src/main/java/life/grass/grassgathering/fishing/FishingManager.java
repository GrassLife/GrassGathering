package life.grass.grassgathering.fishing;

import java.util.ArrayList;
import java.util.List;

public class FishingManager {

    /*
    リストを渡すと各々のインデックスまでの総和を要素に持つ比のリストを生成します
     */
    public static List<Double> makeSumList(List<Double> list) {
        List<Double> sumList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Double left = i == 0 ? 0.0 : sumList.get(i - 1);
            sumList.add(left + list.get(i));
        }
        return sumList;
    }

    /*
    比のリストを渡すとそれに従ってランダムなindexを返してくれます。
     */
    public static int probMaker(List<Double> list) {
        double random = Math.random() * list.get(list.size() - 1);
        int indexNumber = 0;
        for (int i = 0; i < list.size(); i++) {
            double left = i == 0 ? 0 : list.get(i - 1);
            if (random >= left && random < list.get(i)) {
                indexNumber = i;
            }
        }

        return indexNumber;
    }

    public static String releaseMessage() {

        double param = Math.random() * 4.5;

        if (param < 1) {
            return "ありがとう...ありがとう...";
        } else if (param < 2) {
            return "これでみんなのところにかえれる...!!";
        } else if (param < 3) {
            return "このご恩はいつか必ず...!";
        } else if (param < 4) {
            return "助かった...";
        } else {
            return "フン!!逃すくらいなら最初っから釣るなってんだ!!";
        }

    }
}
