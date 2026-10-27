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

/* loaded from: classes.dex */
public class SettingsActivity extends Activity {
    private static final int CARD = -15460061;
    private static final int GOLD = -2575773;
    private static final int GOLD_DIM = -7706068;
    private static final int TEXT = -1448744;
    private static final int TEXT_DIM = -7566716;
    private SharedPreferences prefs;
    private LinearLayout root;

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.prefs = Settings.prefs(this);
        build();
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
        TextView textViewText2 = text("Да сотворит Бог  ·  Живые обои", 15, TEXT_DIM);
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
        section("Движение");
        LinearLayout linearLayoutCard = card();
        slider(linearLayoutCard, "Сила наклона (гироскоп)", "parallax", 70, 0, 200, "%");
        slider(linearLayoutCard, "Скорость вращения", "speed", 65, 0, 300, "%");
        section("Свет");
        LinearLayout linearLayoutCard2 = card();
        slider(linearLayoutCard2, "Надпись в покое", "glow", 18, 0, 60, "%");
        slider(linearLayoutCard2, "Золотые пылинки", "motes", 40, 0, 150, "");
        toggle(linearLayoutCard2, "Цвет по времени суток", "Рассвет — розовое золото, день — золото, закат — янтарь, ночь — серебро", "daycycle", true);
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
