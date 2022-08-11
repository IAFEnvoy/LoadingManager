package net.iafenvoy.loadingmgr.progress;

public enum LoadingStats {
    None("ERROR", "ERROR", 0),
    AddBlocks("1/3 - Adding Models", "1/2 - Adding Block Models - %d/%d", 1.0 / 3),
    AddItems("1/3 - Adding Models", "2/2 - Adding Item Models - %d/%d", 1.0 / 3),
    PreparingStitchingTextures("2/3 - Stitching Textures", "1/4 - Preparing - %d/%d", 2.0 / 3),
    ExtractingTextures("2/3 - Stitching Textures", "2/4 - Extracting Textures - %d/%d", 2.0 / 3),
    StitchingTextures("2/3 - Stitching Textures", "3/4 - Stitching Textures - %d/%d", 2.0 / 3),
    LoadingTextures("2/3 - Stitching Textures", "4/4 - Loading Textures - %d/%d", 2.0 / 3),
    Atlas("3/3 - Baking Models", "1/2 - Atlas - %d/%d", 3.0 / 3),
    Baking("3/3 - Baking Models", "2/2 - Baking Models - %d/%d", 3.0 / 3);

    public static LoadingStats now = LoadingStats.None;
    private static int v1 = 0, v2 = -1;
    private final String text1, text2;
    private final float value1;

    LoadingStats(String text1, String text2, double value1) {
        this.text1 = text1;
        this.text2 = text2;
        this.value1 = (float) value1;
    }

    public static void setValue(int a1, int a2) {
        v1 = a1;
        v2 = a2;
    }

    public static float getValue() {
        return v2 != 0 ? (float) v1 / v2 : 0;
    }

    public float getValue1() {
        return value1;
    }

    public String getText1() {
        return text1;
    }

    public String getText2() {
        return String.format(text2, v1, v2);
    }
}
