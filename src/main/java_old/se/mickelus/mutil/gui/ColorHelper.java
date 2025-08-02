package se.mickelus.mutil.gui;

import net.minecraft.util.Mth;

import java.awt.*;

public class ColorHelper {
    private static int withSaturation(int color, double saturation) {
        float[] hsl = new float[3];
        Color.RGBtoHSB((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, hsl);
        return Color.HSBtoRGB(hsl[0], (float) saturation, hsl[2]);
    }

    public static int multiplyBrightness(int color, double multiplier) {
        float[] hsl = new float[3];
        Color.RGBtoHSB((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, hsl);
        return Color.HSBtoRGB(hsl[0], hsl[1], Mth.clamp((float) multiplier * hsl[2], 0, 1));
    }

    public static int withBrightness(int color, double brightness) {
        float[] hsl = new float[3];
        Color.RGBtoHSB((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, hsl);
        return Color.HSBtoRGB(hsl[0], hsl[1], (float) brightness);
    }

    public static int withAlpha(int color, double alpha) {
        return ((int) Math.round(alpha * 255) << 24) | ((color) & 0x00FFFFFF);
    }

    public static int blend(int colorA, int colorB, float ratio) {
        int a1 = (colorA >> 24 & 0xff);
        int r1 = ((colorA & 0xff0000) >> 16);
        int g1 = ((colorA & 0xff00) >> 8);
        int b1 = (colorA & 0xff);

        int a2 = (colorB >> 24 & 0xff);
        int r2 = ((colorB & 0xff0000) >> 16);
        int g2 = ((colorB & 0xff00) >> 8);
        int b2 = (colorB & 0xff);

        int a = (int) ((a1 * (1 - ratio)) + (a2 * ratio));
        int r = (int) ((r1 * (1 - ratio)) + (r2 * ratio));
        int g = (int) ((g1 * (1 - ratio)) + (g2 * ratio));
        int b = (int) ((b1 * (1 - ratio)) + (b2 * ratio));

        return a << 24
                | r << 16
                | g << 8
                | b;
    }
}
