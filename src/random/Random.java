package random;

public class Random {
    public static int Range(int min, int max) {
        if(max < min) {
            int temp = min;
            min = max;
            max = temp;
        }
        
        return min + (int) (Math.random() * (max - min));
    }
}
