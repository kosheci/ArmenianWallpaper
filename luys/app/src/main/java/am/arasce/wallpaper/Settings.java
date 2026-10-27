package am.arasce.wallpaper;

import android.content.Context;
import android.content.SharedPreferences;

/* loaded from: classes.dex */
final class Settings {
    static final String DAYCYCLE = "daycycle";
    static final boolean D_DAYCYCLE = true;
    static final boolean D_GESTURES = true;
    static final int D_GLOW = 10;
    static final boolean D_MAP = true;
    static final int D_MOTES = 70;
    static final boolean D_NEBULA = true;
    static final int D_PARALLAX = 100;
    static final boolean D_RAYS = true;
    static final boolean D_SHEEN = true;
    static final int D_SPEED = 100;
    static final boolean D_UNLOCK = true;
    static final String GESTURES = "gestures";
    static final String GLOW = "glow";
    static final String MAP = "map";
    static final String MOTES = "motes";
    static final String NEBULA = "nebula";
    static final String PARALLAX = "parallax";
    static final String PREFS = "arasce";
    static final String RAYS = "rays";
    static final String SHEEN = "sheen";
    static final String SPEED = "speed";
    static final String UNLOCK = "unlock";

    private Settings() {
    }

    static SharedPreferences prefs(Context context) {
        return context.getSharedPreferences(PREFS, 0);
    }

    static void reset(Context context) {
        prefs(context).edit().clear().apply();
    }
}
