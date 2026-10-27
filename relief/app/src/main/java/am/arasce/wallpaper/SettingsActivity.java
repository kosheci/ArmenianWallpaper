package am.arasce.wallpaper;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

/* loaded from: classes.dex */
public class SettingsActivity extends Activity {
    private static final int CARD = -15460061;
    private static final int GOLD = -2575773;
    private static final int GOLD_DIM = -7706068;
    private static final int TEXT = -1448744;
    private static final int TEXT_DIM = -7566716;
    private SharedPreferences prefs;
    private LinearLayout root;
    private LocationListener pendingLocation;

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.prefs = Settings.prefs(this);
        build();
        if (!this.prefs.getBoolean("flip_hint_shown", false)) {
            Toast.makeText(this, "Дважды коснитесь обоев, чтобы открыть карту", Toast.LENGTH_LONG).show();
            this.prefs.edit().putBoolean("flip_hint_shown", true).apply();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void build() {
        ScrollView scrollView = new ScrollView(this);
        scrollView.setBackgroundColor(-16118768);
        scrollView.setFillViewport(true);
        this.root = new LinearLayout(this);
        this.root.setOrientation(1);
        this.root.setPadding(dp(20), dp(36), dp(20), dp(32));
        scrollView.addView(this.root);
        TextView textViewText = text("Աստուած արասցէ", 30, GOLD);
        textViewText.setTypeface(Typeface.create("serif", 0));
        textViewText.setGravity(1);
        this.root.addView(textViewText);
        TextView textViewText2 = text("ԼՈՒՅՍ  ·  СВЕТ АРМЕНИИ  ·  3D", 15, TEXT_DIM);
        textViewText2.setGravity(1);
        textViewText2.setPadding(0, dp(4), 0, dp(24));
        this.root.addView(textViewText2);
        Button button = button("Установить как обои", true);
        button.setOnClickListener(new View.OnClickListener() { // from class: am.arasce.wallpaper.SettingsActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SettingsActivity.this.openWallpaperPicker();
            }
        });
        this.root.addView(button, fullWidth(dp(52)));
        section("Настроение");
        LinearLayout presets = card();
        TextView hint = text("Выберите готовый стиль или настройте детали ниже.", 14, TEXT_DIM);
        hint.setPadding(0, dp(8), 0, dp(12));
        presets.addView(hint);
        Button calm = button("Спокойный", false);
        calm.setOnClickListener(v -> applyPreset(false));
        presets.addView(calm, fullWidth(dp(48)));
        Button bright = button("Выразительный", false);
        bright.setOnClickListener(v -> applyPreset(true));
        LinearLayout.LayoutParams brightParams = fullWidth(dp(48));
        brightParams.topMargin = dp(8);
        presets.addView(bright, brightParams);
        Button classic = button("Классика", false);
        classic.setOnClickListener(v -> applyNamedPreset(0));
        presets.addView(classic, fullWidth(dp(48)));
        Button night = button("Ночное золото", false);
        night.setOnClickListener(v -> applyNamedPreset(1));
        presets.addView(night, fullWidth(dp(48)));
        Button matrixStyle = button("Матрица", false);
        matrixStyle.setOnClickListener(v -> applyNamedPreset(2));
        presets.addView(matrixStyle, fullWidth(dp(48)));
        Button saveStyle = button("Сохранить мой стиль", false);
        saveStyle.setOnClickListener(v -> saveMyStyle());
        presets.addView(saveStyle, fullWidth(dp(48)));
        Button loadStyle = button("Вернуть мой стиль", false);
        loadStyle.setOnClickListener(v -> loadMyStyle());
        presets.addView(loadStyle, fullWidth(dp(48)));
        section("Композиция");
        LinearLayout layout = card();
        slider(layout, "Размер медальона", "emblem_size", 100, 60, 120, "%");
        slider(layout, "Положение по горизонтали", "emblem_x", 50, 25, 75, "%");
        slider(layout, "Положение по вертикали", "emblem_y", 56, 30, 75, "%");
        section("Эффекты");
        LinearLayout effects = card();
        toggle(effects, "Визуальные эффекты", "Общий выключатель анимации, свечения, частиц и наклона", "effects_enabled", true);
        toggle(effects, "Центральная искра", "Небольшой блик в центре аревахача", "sparkle_enabled", true);
        toggle(effects, "Волны от касаний", "Круги света при касании и разблокировке", "ripples_enabled", true);
        toggle(effects, "Плавное пробуждение", "Мягкое появление при разблокировке", "wake_fade", true);
        toggle(effects, "Автоматическая экономия", "Около 30 кадров в секунду при заряде до 20% или системной экономии", "auto_energy", true);
        section("Движение");
        LinearLayout linearLayoutCard = card();
        toggle(linearLayoutCard, "Объёмный наклон 3D", "Эмблема поворачивается в перспективе при наклоне телефона", "perspective", true);
        slider(linearLayoutCard, "Сила наклона (гироскоп)", "parallax", 70, 0, 200, "%");
        slider(linearLayoutCard, "Скорость вращения", "speed", 65, 0, 300, "%");
        section("Свет");
        LinearLayout linearLayoutCard2 = card();
        slider(linearLayoutCard2, "Надпись в покое", "glow", 18, 0, 60, "%");
        slider(linearLayoutCard2, "Золотые пылинки", "motes", 40, 0, 150, "");
        toggle(linearLayoutCard2, "Цвет по времени суток", "Рассвет — розовое золото, день — золото, закат — янтарь, ночь — серебро", "daycycle", true);
        section("Армянская матрица");
        LinearLayout matrix = card();
        toggle(matrix, "Буквы Матрицы", "Золотой дождь из букв армянского алфавита в цвете обоев", "matrix_on", true);
        slider(matrix, "Яркость букв", "matrix_intensity", 55, 0, 100, "%");
        slider(matrix, "Плотность букв", "matrix_density", 100, 0, 100, "%");
        slider(matrix, "Скорость дождя", "matrix_speed", 100, 0, 200, "%");
        slider(matrix, "Размер букв", "matrix_size", 100, 60, 160, "%");
        toggle(matrix, "Алфавит по порядку", "Стабильная последовательность букв вместо случайной смены", "matrix_ordered", false);
        section("Арарат и календарь");
        LinearLayout heritage = card();
        toggle(heritage, "Армянский календарь", "Традиционный 365-дневный календарь, летосчисление с 552 года", "armenian_calendar", true);
        toggle(heritage, "Календарь под медальоном", "Выключено — сверху экрана", "calendar_below", false);
        TextView compassState = text(compassStatusText(), 13, TEXT_DIM);
        compassState.setPadding(dp(4), dp(6), dp(4), dp(6));
        heritage.addView(compassState);
        toggle(heritage, "Компас на Арарат", "Нужны компас телефона и недавно обновлённое местоположение", "ararat_compass", false);
        Button locate = button("Разрешить и обновить местоположение", false);
        locate.setOnClickListener(v -> requestAraratLocation());
        heritage.addView(locate, fullWidth(dp(52)));
        section("Небесный круг");
        LinearLayout sky = card();
        toggle(sky, "Небесная орбита и Луна", "Тонкий круг со звёздами и приблизительной фазой Луны", "celestial", true);
        toggle(sky, "Кольцо заряда", "Золотая дуга показывает заряд телефона на небесном круге", "battery", true);
        section("Графика");
        LinearLayout linearLayoutCard3 = card();
        toggle(linearLayoutCard3, "Блеск металла", "Блик скользит по золоту, когда вы наклоняете телефон", "sheen", true);
        toggle(linearLayoutCard3, "Лучи из центра", "Тонкие лучи света вокруг аревахача", "rays", false);
        toggle(linearLayoutCard3, "Туманность", "Глубокие облака синего, фиолетового и бирюзового на фоне", "nebula", true);
        toggle(linearLayoutCard3, "Карта державы Тиграна", "Карта за эмблемой. Показывается и скрывается двойным касанием.", "map", true);
        section("Касания");
        LinearLayout linearLayoutCard4 = card();
        toggle(linearLayoutCard4, "Жесты", "Тап — волна и свечение надписи. Двойной тап — показать или скрыть карту. Проведите по кругу, чтобы раскрутить кольца.", "gestures", true);
        toggle(linearLayoutCard4, "Вспышка при разблокировке", "Розетка вспыхивает и надпись проявляется каждый раз, когда вы открываете телефон", "unlock", true);
        TextView textViewText3 = text("Перетаскивание работает, только если лаунчер передаёт движения пальца обоям. На рабочем столе Samsung чаще доходят только тапы.", 12, TEXT_DIM);
        textViewText3.setPadding(dp(4), dp(16), dp(4), dp(16));
        this.root.addView(textViewText3);
        Button button2 = button("Сбросить настройки", false);
        button2.setOnClickListener(new View.OnClickListener() { // from class: am.arasce.wallpaper.SettingsActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Settings.reset(SettingsActivity.this);
                SettingsActivity.this.build();
            }
        });
        this.root.addView(button2, fullWidth(dp(48)));
        setContentView(scrollView);
    }

    /* JADX INFO: Access modifiers changed from: private */
    private String compassStatusText() {
        if (!prefs.getBoolean("ararat_compass", false)) return "Компас: выключен";
        android.hardware.SensorManager sm = (android.hardware.SensorManager)getSystemService(SENSOR_SERVICE);
        if (sm == null || sm.getDefaultSensor(android.hardware.Sensor.TYPE_ROTATION_VECTOR) == null) return "Компас: датчик недоступен";
        long t = prefs.getLong("ararat_location_time", 0L);
        return t > 0 && System.currentTimeMillis() - t <= 86400000L
                ? "Компас: местоположение получено; проверьте калибровку" : "Компас: обновите местоположение";
    }

    private void applyNamedPreset(int style) {
        prefs.edit().putBoolean("effects_enabled", true)
            .putBoolean("matrix_on", style == 2).putBoolean("daycycle", style != 1).putBoolean("night_gold", style == 1)
            .putBoolean("rays", style == 2).putBoolean("nebula", style != 0)
            .putBoolean("sheen", true).putInt("speed", style == 0 ? 45 : style == 1 ? 35 : 80)
            .putInt("motes", style == 0 ? 20 : style == 1 ? 30 : 45)
            .putInt("matrix_intensity", style == 2 ? 55 : 25).apply();
        build();
    }

    private static final String[] STYLE_KEYS = {"effects_enabled","sparkle_enabled","ripples_enabled","wake_fade","auto_energy",
        "emblem_size","emblem_x","emblem_y","parallax","speed","glow","motes","daycycle","night_gold","matrix_on","matrix_intensity",
        "matrix_density","matrix_speed","matrix_size","matrix_ordered","armenian_calendar","calendar_below","perspective",
        "celestial","battery","sheen","rays","nebula","map","gestures","unlock"};
    private void saveMyStyle() {
        try {
            org.json.JSONObject data = new org.json.JSONObject();
            java.util.Map<String, ?> all = prefs.getAll();
            for (String key : STYLE_KEYS) if (all.containsKey(key)) data.put(key, all.get(key));
            prefs.edit().putString("user_style", data.toString()).apply();
            Toast.makeText(this, "Ваш стиль сохранён", Toast.LENGTH_SHORT).show();
        } catch (org.json.JSONException e) {
            Toast.makeText(this, "Не удалось сохранить стиль", Toast.LENGTH_SHORT).show();
        }
    }
    private void loadMyStyle() {
        String raw = prefs.getString("user_style", null);
        if (raw == null) { Toast.makeText(this, "Сначала сохраните свой стиль", Toast.LENGTH_SHORT).show(); return; }
        try {
            org.json.JSONObject data = new org.json.JSONObject(raw);
            android.content.SharedPreferences.Editor e = prefs.edit();
            for (String key : STYLE_KEYS) {
                if (!data.has(key)) { e.remove(key); continue; }
                Object value = data.get(key);
                if (value instanceof Boolean) e.putBoolean(key, (Boolean)value);
                else if (value instanceof Number) e.putInt(key, ((Number)value).intValue());
            }
            e.apply(); build(); Toast.makeText(this, "Ваш стиль восстановлен", Toast.LENGTH_SHORT).show();
        } catch (org.json.JSONException e) {
            Toast.makeText(this, "Не удалось восстановить стиль", Toast.LENGTH_SHORT).show();
        }
    }

    private void applyPreset(boolean expressive) {
        prefs.edit().putInt("parallax", expressive ? 100 : 70)
            .putInt("speed", expressive ? 100 : 65)
            .putInt("glow", expressive ? 28 : 18)
            .putInt("motes", expressive ? 70 : 40)
            .putBoolean("rays", expressive)
            .putBoolean("nebula", true)
            .putBoolean("sheen", true).apply();
        build();
    }

    private void requestAraratLocation() {
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 420);
            return;
        }
        obtainAraratLocation();
    }

    @Override public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != 420) return;
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            obtainAraratLocation();
        } else {
            Toast.makeText(this, "Разрешение не предоставлено: компас недоступен", Toast.LENGTH_LONG).show();
        }
    }

    private void saveAraratLocation(Location location) {
        if (location == null || location.getTime() <= 0) return;
        prefs.edit().putFloat("ararat_lat", (float)location.getLatitude())
            .putFloat("ararat_lon", (float)location.getLongitude())
            .putLong("ararat_location_time", location.getTime())
            .putBoolean("ararat_compass", true).apply();
    }

    private void obtainAraratLocation() {
        LocationManager lm = (LocationManager)getSystemService(LOCATION_SERVICE);
        try {
            String provider = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ? LocationManager.NETWORK_PROVIDER
                    : (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                       && lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ? LocationManager.GPS_PROVIDER : null);
            if (provider == null) {
                Toast.makeText(this, "Включите геолокацию телефона и повторите", Toast.LENGTH_LONG).show();
                return;
            }
            Location last = lm.getLastKnownLocation(provider);
            if (last != null && System.currentTimeMillis() - last.getTime() < 86400000L) {
                saveAraratLocation(last);
                Toast.makeText(this, "Компас включён; уточняем местоположение", Toast.LENGTH_SHORT).show();
            } else {
                prefs.edit().putBoolean("ararat_compass", true).apply();
                Toast.makeText(this, "Ожидаем местоположение; оставьте экран настроек открытым", Toast.LENGTH_LONG).show();
            }
            if (pendingLocation != null) lm.removeUpdates(pendingLocation);
            pendingLocation = new LocationListener() {
                @Override public void onLocationChanged(Location location) {
                    saveAraratLocation(location);
                    Toast.makeText(SettingsActivity.this, "Местоположение обновлено", Toast.LENGTH_SHORT).show();
                    pendingLocation = null;
                }
                @Override public void onStatusChanged(String provider, int status, Bundle extras) {}
                @Override public void onProviderEnabled(String provider) {}
                @Override public void onProviderDisabled(String provider) {}
            };
            lm.requestSingleUpdate(provider, pendingLocation, getMainLooper());
            build();
        } catch (SecurityException | IllegalArgumentException e) {
            Toast.makeText(this, "Не удалось получить местоположение", Toast.LENGTH_LONG).show();
        }
    }

    @Override protected void onDestroy() {
        if (pendingLocation != null) {
            try { ((LocationManager)getSystemService(LOCATION_SERVICE)).removeUpdates(pendingLocation); }
            catch (SecurityException ignored) {}
        }
        super.onDestroy();
    }

    public void openWallpaperPicker() {
        Intent intent = new Intent("android.service.wallpaper.CHANGE_LIVE_WALLPAPER");
        intent.putExtra("android.service.wallpaper.extra.LIVE_WALLPAPER_COMPONENT", new ComponentName(this, (Class<?>) ArasceWallpaperService.class));
        try {
            startActivity(intent);
        } catch (Exception e) {
            try {
                startActivity(new Intent("android.service.wallpaper.LIVE_WALLPAPER_CHOOSER"));
            } catch (Exception e2) {
            }
        }
    }

    private void section(String str) {
        TextView textViewText = text(str.toUpperCase(), 12, GOLD_DIM);
        textViewText.setLetterSpacing(0.15f);
        textViewText.setPadding(dp(4), dp(24), 0, dp(8));
        this.root.addView(textViewText);
    }

    private LinearLayout card() {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(dp(16), dp(8), dp(16), dp(8));
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(CARD);
        gradientDrawable.setCornerRadius(dp(16));
        gradientDrawable.setStroke(dp(1), 869839459);
        linearLayout.setBackground(gradientDrawable);
        this.root.addView(linearLayout, fullWidth(-2));
        return linearLayout;
    }

    private void slider(LinearLayout linearLayout, String str, final String str2, int i, int i2, int i3, final String str3) {
        LinearLayout linearLayout2 = new LinearLayout(this);
        linearLayout2.setOrientation(0);
        linearLayout2.setPadding(0, dp(10), 0, 0);
        TextView textViewText = text(str, 15, TEXT);
        final TextView textViewText2 = text("", 15, GOLD);
        linearLayout2.addView(textViewText, new LinearLayout.LayoutParams(0, -2, 1.0f));
        linearLayout2.addView(textViewText2);
        linearLayout.addView(linearLayout2);
        SeekBar seekBar = new SeekBar(this);
        seekBar.setMin(i2);
        seekBar.setMax(i3);
        int i4 = this.prefs.getInt(str2, i);
        seekBar.setProgress(i4);
        textViewText2.setText(i4 + str3);
        seekBar.setProgressTintList(ColorStateList.valueOf(GOLD));
        seekBar.setThumbTintList(ColorStateList.valueOf(GOLD));
        seekBar.setProgressBackgroundTintList(ColorStateList.valueOf(-12959926));
        seekBar.setPadding(dp(4), dp(10), dp(4), dp(10));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: am.arasce.wallpaper.SettingsActivity.3
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar2, int i5, boolean z) {
                textViewText2.setText(i5 + str3);
                if (z) {
                    SettingsActivity.this.prefs.edit().putInt(str2, i5).apply();
                }
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar2) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar2) {
            }
        });
        linearLayout.addView(seekBar, fullWidth(-2));
    }

    private void toggle(LinearLayout linearLayout, String str, String str2, final String str3, boolean z) {
        LinearLayout linearLayout2 = new LinearLayout(this);
        linearLayout2.setOrientation(0);
        linearLayout2.setGravity(16);
        linearLayout2.setPadding(0, dp(12), 0, dp(12));
        LinearLayout linearLayout3 = new LinearLayout(this);
        linearLayout3.setOrientation(1);
        linearLayout3.addView(text(str, 15, TEXT));
        TextView textViewText = text(str2, 12, TEXT_DIM);
        textViewText.setPadding(0, dp(3), dp(12), 0);
        linearLayout3.addView(textViewText);
        linearLayout2.addView(linearLayout3, new LinearLayout.LayoutParams(0, -2, 1.0f));
        Switch r1 = new Switch(this);
        r1.setChecked(this.prefs.getBoolean(str3, z));
        int[][] iArr = {new int[]{android.R.attr.state_checked}, new int[0]};
        r1.setThumbTintList(new ColorStateList(iArr, new int[]{GOLD, -6645094}));
        r1.setTrackTintList(new ColorStateList(iArr, new int[]{GOLD_DIM, -12959926}));
        r1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: am.arasce.wallpaper.SettingsActivity.4
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z2) {
                SettingsActivity.this.prefs.edit().putBoolean(str3, z2).apply();
            }
        });
        linearLayout2.addView(r1);
        linearLayout.addView(linearLayout2);
    }

    private Button button(String str, boolean z) {
        Button button = new Button(this);
        button.setText(str);
        button.setAllCaps(false);
        button.setTextSize(2, 16.0f);
        button.setTextColor(z ? -16118768 : GOLD);
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(dp(26));
        if (z) {
            gradientDrawable.setColor(GOLD);
        } else {
            gradientDrawable.setColor(0);
            gradientDrawable.setStroke(dp(1), GOLD_DIM);
        }
        button.setBackground(gradientDrawable);
        button.setStateListAnimator(null);
        return button;
    }

    private TextView text(String str, int i, int i2) {
        TextView textView = new TextView(this);
        textView.setText(str);
        textView.setTextSize(2, i);
        textView.setTextColor(i2);
        return textView;
    }

    private LinearLayout.LayoutParams fullWidth(int i) {
        return new LinearLayout.LayoutParams(-1, i);
    }

    private int dp(int i) {
        return Math.round(i * getResources().getDisplayMetrics().density);
    }
}
