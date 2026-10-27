import am.arasce.wallpaper.VisualEffects14;
public class Check { public static void main(String[] args) {
  if (VisualEffects14.batteryArcSweep(-1) != 0 || VisualEffects14.batteryArcSweep(50) != 180 || VisualEffects14.batteryArcSweep(101) != 360) throw new AssertionError("battery");
  if (VisualEffects14.moonIllumination(947182440000L) > 0.001 || VisualEffects14.moonIllumination(947182440000L + (long)(29.530588853 * 86400000 / 2)) < 0.999) throw new AssertionError("moon");
  System.out.println("Battery and lunar phase checks passed");
}}
