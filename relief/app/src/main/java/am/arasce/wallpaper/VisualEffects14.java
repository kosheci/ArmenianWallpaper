package am.arasce.wallpaper;

/** Standalone animation policy for integration into ArasceRenderer. No Android dependencies. */
public final class VisualEffects14 {
    private VisualEffects14() {}
    public static float clamp(float x) { return Math.max(0f, Math.min(1f, x)); }
    public static float smooth(float x) { x=clamp(x); return x*x*(3f-2f*x); }
    /** Map remains invisible until explicitly toggled by a double tap. */
    public static float mapOpacity(boolean mapMode, long elapsedMs) {
        return mapMode ? smooth(elapsedMs/550f) : 0f;
    }
    /** Emblem remains prominent unless the map is deliberately visible. */
    public static float emblemOpacity(float mapOpacity) { return 1f-0.72f*clamp(mapOpacity); }
    /** Single tap gives text priority; no automatic map flash. */
    public static float inscriptionGlow(long elapsedMs) {
        if (elapsedMs < 0 || elapsedMs > 2200) return 0f;
        return smooth(elapsedMs/420f) * (1f-smooth((elapsedMs-900f)/1300f));
    }
    /** Subtle spark, delayed until the inscription has appeared. */
    public static float sparkGlow(long elapsedMs) {
        return 0.24f*smooth((elapsedMs-320f)/220f)*(1f-smooth((elapsedMs-650f)/600f));
    }
    /** A restrained golden battery ring: angle in degrees. */
    public static float batteryArcSweep(int percent) { return 360f*clamp(percent/100f); }
    /** Moon age in days (approximate astronomical synodic period). */
    public static double moonAgeDays(long unixMs) {
        double referenceNewMoon=947182440000.0; // 2000-01-06 18:14 UTC
        double lunation=29.530588853;
        double age=((unixMs-referenceNewMoon)/86400000.0)%lunation;
        return age<0?age+lunation:age;
    }
    public static double moonIllumination(long unixMs) {
        return (1-Math.cos(2*Math.PI*moonAgeDays(unixMs)/29.530588853))/2;
    }
    /** Warm night palette, ARGB. */
    public static final int NIGHT_BACKGROUND=0xFF090A0B;
    public static final int NIGHT_GOLD=0xFFB99A59;
    public static final int NIGHT_SILVER=0xFFB7B2A7;
}
