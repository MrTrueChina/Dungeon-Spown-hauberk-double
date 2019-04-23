package random;

public class Random {
    public static int Range(int min, int max) {
        if (max < min) {
            int temp = min;
            min = max;
            max = temp;
        }

        return min + (int) (Math.random() * (max - min));
    }

    public static int weightedRandom(int[] probability) {
        int allProbability = 0;

        for (int i : probability)
            allProbability += i;

        int target = Range(0, allProbability);

        for (int i = 0; i < probability.length; i++)
            if ((target -= probability[i]) < 0)
                return i;

        return 0;
    }
}
