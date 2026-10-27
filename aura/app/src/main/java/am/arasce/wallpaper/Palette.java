package am.arasce.wallpaper;

import java.util.Calendar;

/* loaded from: classes.dex */
final class Palette {
    final int bg;
    final int dark;
    final int dot;
    final int light;
    final int lit;
    final int mid;
    static final Palette DAY = new Palette(-1522828, -5537227, -10204905, -2575773, -666227, -15460061);
    static final Palette DAWN = new Palette(-867936, -4621730, -9814474, -1197926, -469056, -15068136);
    static final Palette DUSK = new Palette(-1005472, -5216722, -10604012, -1861035, -540022, -15068656);
    static final Palette NIGHT = new Palette(-3549464, -8416336, -12891549, -4666906, -2234121, -15723233);
    private static final float[] HOURS = {0.0f, 5.0f, 6.5f, 9.0f, 17.0f, 19.5f, 22.0f, 24.0f};
    private static final Palette[] KEYS = {NIGHT, NIGHT, DAWN, DAY, DAY, DUSK, NIGHT, NIGHT};

    Palette(int i, int i2, int i3, int i4, int i5, int i6) {
        this.light = i;
        this.mid = i2;
        this.dark = i3;
        this.lit = i4;
        this.dot = i5;
        this.bg = i6;
    }

    static Palette forNow() {
        Calendar calendar = Calendar.getInstance();
        return forHour((calendar.get(12) / 60.0f) + calendar.get(11));
    }

    static Palette forHour(float f) {
        for (int i = 0; i < HOURS.length - 1; i++) {
            if (f >= HOURS[i] && f <= HOURS[i + 1]) {
                float f2 = (f - HOURS[i]) / (HOURS[i + 1] - HOURS[i]);
                return lerp(KEYS[i], KEYS[i + 1], (3.0f - (f2 * 2.0f)) * f2 * f2);
            }
        }
        return DAY;
    }

    static Palette lerp(Palette palette, Palette palette2, float f) {
        return new Palette(mix(palette.light, palette2.light, f), mix(palette.mid, palette2.mid, f), mix(palette.dark, palette2.dark, f), mix(palette.lit, palette2.lit, f), mix(palette.dot, palette2.dot, f), mix(palette.bg, palette2.bg, f));
    }

    boolean sameAs(Palette palette) {
        return palette != null && this.light == palette.light && this.mid == palette.mid && this.dark == palette.dark && this.lit == palette.lit && this.dot == palette.dot && this.bg == palette.bg;
    }

    static int mix(int i, int i2, float f) {
        if (f < 0.0f) {
            f = 0.0f;
        } else if (f > 1.0f) {
            f = 1.0f;
        }
        return (((int) (((i >> 16) & 255) + ((((i2 >> 16) & 255) - ((i >> 16) & 255)) * f))) << 16) | (-16777216) | (((int) (((i >> 8) & 255) + ((((i2 >> 8) & 255) - ((i >> 8) & 255)) * f))) << 8) | ((int) ((i & 255) + (((i2 & 255) - (i & 255)) * f)));
    }
}
