package util.color;

public class ColorUtil {
    public static int[] hexToRGB(String hex) {
        int r = Integer.parseInt(hex.substring(1, 3), 16);
        int g = Integer.parseInt(hex.substring(3, 5), 16);
        int b = Integer.parseInt(hex.substring(5, 7), 16);
        return new int[]{r, g, b};
    }
}
