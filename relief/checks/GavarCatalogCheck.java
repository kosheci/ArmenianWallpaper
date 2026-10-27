package am.arasce.wallpaper;
public class GavarCatalogCheck {
    public static void main(String[] args) {
        if (GavarCatalog.REGIONS.length != 15 || GavarCatalog.GAVARS.length != 15) throw new AssertionError("regions");
        for (int i=0; i<15; i++) {
            if (GavarCatalog.GAVAR_ARRAYS[i].length < 5) throw new AssertionError("empty list " + i);
            if (GavarCatalog.regionIndex(GavarCatalog.REGIONS[i] + " · условно") != i) throw new AssertionError("selection " + i);
        }
        if (GavarCatalog.regionIndex("ԱՐԱՐԱՏ") != -1) throw new AssertionError("nonregion");
        System.out.println("Region catalog checks passed");
    }
}
