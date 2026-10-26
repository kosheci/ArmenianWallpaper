package am.arasce.wallpaper;

/** Animation policy for integration with the original ArasceRenderer. */
public final class VisualEffects14 {
    private VisualEffects14() {}
    public static float clamp(float x) { return Math.max(0f, Math.min(1f, x)); }
    public static float smooth(float x) { x=clamp(x); return x*x*(3f-2f*x); }
    public static float mapOpacity(boolean mapMode, long elapsedMs) {
        return mapMode ? smooth(elapsedMs/550f) : 0f;
    }
    public static float emblemOpacity(float mapOpacity) { return 1f-0.72f*clamp(mapOpacity); }
    public static float inscriptionGlow(long elapsedMs) {
        if (elapsedMs < 0 || elapsedMs > 2200) return 0f;
        return smooth(elapsedMs/420f) * (1f-smooth((elapsedMs-900f)/1300f));
    }
    public static float sparkGlow(long elapsedMs) {
        return 0.24f*smooth((elapsedMs-320f)/220f)*(1f-smooth((elapsedMs-650f)/600f));
    }
    public static float batteryArcSweep(int percent) { return 360f*clamp(percent/100f); }
    public static double moonAgeDays(long unixMs) {
        double referenceNewMoon=947182440000.0;
        double lunation=29.530588853;
        double age=((unixMs-referenceNewMoon)/86400000.0)%lunation;
        return age<0?age+lunation:age;
    }
    public static double moonIllumination(long unixMs) {
        return (1-Math.cos(2*Math.PI*moonAgeDays(unixMs)/29.530588853))/2;
    }
    public static final int NIGHT_BACKGROUND=0xFF090A0B;
    public static final int NIGHT_GOLD=0xFFB99A59;
    public static final int NIGHT_SILVER=0xFFB7B2A7;
}