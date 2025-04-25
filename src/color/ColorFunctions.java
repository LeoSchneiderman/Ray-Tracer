package color;

import main.*;

public class ColorFunctions {

    public static float[] getComponents(int color) {
        float r = Main.instance.red(color) / 255f;
        float g = Main.instance.green(color) / 255f;
        float b = Main.instance.blue(color) / 255f;
        return new float[] { r, g, b };
    }

}
