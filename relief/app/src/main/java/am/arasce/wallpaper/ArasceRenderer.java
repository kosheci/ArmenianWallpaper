package am.arasce.wallpaper;

import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Camera;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import java.util.Random;

/* loaded from: classes.dex */
final class ArasceRenderer {
    private static final float BAKE = 0.5f;
    static final int MAX_MOTES = 150;
    private static final int MAX_RIPPLES = 6;
    private static final float NEB = 3.0f;
    private static final float RIPPLE_LIFE = 1.6f;
    private final Bitmap bokehSprite;
    private float cx;
    private float cy;
    private final Bitmap dotSprite;
    float flare;
    private Bitmap glowRings;
    private Bitmap glowRosette;
    private Bitmap glowStar;
    private Bitmap glowStatic;
    private Bitmap glowText;
    private float h;
    private LinearGradient metal;
    private LinearGradient metalFlat;
    private Bitmap nebula;
    private Palette palette;
    private float parallax;
    private int rippleNext;
    private float rotTime;
    private float sheenAngle;
    private float sheenOffset;
    private final Bitmap sparkle;
    float spin;
    private final String text;
    private final Paint textSheen;
    private float w;
    float parallaxMul = 1.0f;
    float speedMul = 1.0f;
    float ambientBase = 0.1f;
    int moteCount = 70;
    boolean sheenOn = true;
    boolean raysOn = true;
    boolean nebulaOn = true;
    boolean mapOn = true;
    boolean effectsOn = true;
    boolean sparkleOn = true;
    boolean matrixOn = true;
    int matrixIntensity = 55;
    private final Paint matrixPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final String MATRIX_LETTERS = "ԱԲԳԴԵԶԷԸԹԺԻԼԽԾԿՀՁՂՃՄՅՆՇՈՉՊՋՌՍՎՏՐՑՒՓՔՕՖ";
    boolean calendarOn = true;
    boolean compassOn = false;
    volatile boolean compassReady = false;
    volatile float compassAngle = 0f;
    String compassStatus = "Обновите местоположение";
    private String calendarText = "";
    private final Paint infoPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path compassPath = new Path();
    void updateCalendar() { calendarText = ArmenianCalendar.format(java.time.LocalDate.now()); }
    boolean perspectiveOn = true;
    private final Camera perspectiveCamera = new Camera();
    private final Matrix perspectiveMatrix = new Matrix();
    boolean celestialOn = true;
    boolean batteryOn = true;
    int batteryPercent = -1;
    private final Paint celestialPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint flipPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF celestialArc = new RectF(-375f, -375f, 375f, 375f);
    private float s = 1.0f;
    private float lastT = -1.0f;
    private int textLit = -2575773;
    private int textRest = -15130580;
    private final Matrix sheenMatrix = new Matrix();
    private final Paint bgPaint = new Paint(5);
    private final Paint vignettePaint = new Paint(4);
    private final Paint nebulaPaint = new Paint(MAX_RIPPLES);
    private final Paint stroke = new Paint(1);
    private final Paint jewelPaint = new Paint(1);
    private final Paint dotPaint = new Paint(1);
    private final Paint textPaint = new Paint(1);
    private final Paint glowPaint = new Paint(2);
    private final Paint textGlowPaint = new Paint(2);
    private final Paint shadowPaint = new Paint(2);
    private final Paint spritePaint = new Paint(2);
    private final Paint sparklePaint = new Paint(2);
    private final Paint raysPaint = new Paint(5);
    private final Paint medallionPaint = new Paint(5);
    private final Paint ripplePaint = new Paint(1);
    private final Paint rippleGlow = new Paint(1);
    private final DashPathEffect dash2x6 = new DashPathEffect(new float[]{2.0f, 6.0f}, 0.0f);
    private final DashPathEffect dash1x4 = new DashPathEffect(new float[]{1.0f, 4.0f}, 0.0f);
    private final DashPathEffect dashRing = new DashPathEffect(new float[]{40.0f, 10.0f, 5.0f, 10.0f}, 0.0f);
    private final DashPathEffect dash2x12 = new DashPathEffect(new float[]{2.0f, 12.0f}, 0.0f);
    private final DashPathEffect dash3x3 = new DashPathEffect(new float[]{NEB, NEB}, 0.0f);
    private final RectF squareRect = new RectF(-130.0f, -130.0f, 130.0f, 130.0f);
    private final Path textCircle = new Path();
    private final Path arms = new Path();
    private final Path armsInner = new Path();
    private final Path ornament = new Path();
    private final Path ornamentDots = new Path();
    private final Path dialTicks = new Path();
    private final Path jewels = new Path();
    private final Path rays = new Path();
    private final RectF tmpRect = new RectF();
    private final MapLayer map = new MapLayer();
    private final ReliefMesh relief = new ReliefMesh();
    private final float[] mx = new float[MAX_MOTES];
    private final float[] my = new float[MAX_MOTES];
    private final float[] mSize = new float[MAX_MOTES];
    private final float[] mSpeed = new float[MAX_MOTES];
    private final float[] mPhase = new float[MAX_MOTES];
    private final float[] mDepth = new float[MAX_MOTES];
    private final boolean[] mBokeh = new boolean[MAX_MOTES];
    private final float[] rx = new float[MAX_RIPPLES];
    private final float[] ry = new float[MAX_RIPPLES];
    private final float[] rt = new float[MAX_RIPPLES];

    ArasceRenderer() {
        this.stroke.setStyle(Paint.Style.STROKE);
        this.stroke.setStrokeCap(Paint.Cap.ROUND);
        this.jewelPaint.setStyle(Paint.Style.FILL);
        this.dotPaint.setStyle(Paint.Style.FILL);
        this.textPaint.setStyle(Paint.Style.FILL);
        this.textPaint.setTypeface(Typeface.create("serif", 0));
        this.textPaint.setTextSize(13.5f);
        this.raysPaint.setStyle(Paint.Style.FILL);
        this.medallionPaint.setStyle(Paint.Style.FILL);
        this.ripplePaint.setStyle(Paint.Style.STROKE);
        this.rippleGlow.setStyle(Paint.Style.STROKE);
        for (int i = 0; i < MAX_RIPPLES; i++) {
            this.rt[i] = -100.0f;
        }
        buildGeometry();
        this.textPaint.setLetterSpacing(0.55f);
        int iMax = Math.max(1, Math.round(1539.3804f / this.textPaint.measureText("Աստուած արասցէ • ")));
        this.textPaint.setLetterSpacing(0.0f);
        this.textPaint.setLetterSpacing((((1539.3804f / iMax) - this.textPaint.measureText("Աստուած արասցէ • ")) / "Աստուած արասցէ • ".length()) / this.textPaint.getTextSize());
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < iMax; i2++) {
            sb.append("Աստուած արասցէ • ");
        }
        this.text = sb.toString();
        this.textSheen = new Paint(this.textPaint);
        Random random = new Random(1054L);
        for (int i3 = 0; i3 < MAX_MOTES; i3++) {
            this.mx[i3] = random.nextFloat();
            this.my[i3] = random.nextFloat();
            this.mPhase[i3] = random.nextFloat() * 6.283f;
            this.mBokeh[i3] = i3 % 7 == 3;
            if (this.mBokeh[i3]) {
                this.mSize[i3] = 14.0f + (random.nextFloat() * 26.0f);
                this.mSpeed[i3] = 0.002f + (random.nextFloat() * 0.004f);
                this.mDepth[i3] = 2.2f + (random.nextFloat() * 0.8f);
            } else {
                this.mSize[i3] = 0.6f + (random.nextFloat() * RIPPLE_LIFE);
                this.mSpeed[i3] = 0.004f + (random.nextFloat() * 0.012f);
                this.mDepth[i3] = 1.2f + (random.nextFloat() * 1.0f);
            }
        }
        this.dotSprite = bakeSprite(32, new int[]{-1, -2130706433, 16777215}, new float[]{0.0f, 0.3f, 1.0f});
        this.bokehSprite = bakeSprite(64, new int[]{1090519039, 1442840575, -1711276033, 16777215}, new float[]{0.0f, 0.7f, 0.88f, 1.0f});
        this.sparkle = bakeSparkle();
        setPalette(Palette.DAY);
    }

    private void buildGeometry() {
        RectF rectF = new RectF(-245.0f, -245.0f, 245.0f, 245.0f);
        this.textCircle.moveTo(-245.0f, 0.0f);
        this.textCircle.arcTo(rectF, 180.0f, 180.0f, false);
        this.textCircle.arcTo(rectF, 0.0f, 180.0f, false);
        Path path = new Path();
        path.moveTo(0.0f, 0.0f);
        path.cubicTo(0.0f, -40.0f, 30.0f, -60.0f, 50.0f, -60.0f);
        path.cubicTo(66.0f, -60.0f, 74.0f, -48.0f, 68.0f, -40.0f);
        path.cubicTo(63.0f, -34.0f, 55.0f, -37.0f, 57.0f, -44.0f);
        Path path2 = new Path();
        path2.moveTo(0.0f, -14.0f);
        path2.cubicTo(2.0f, -36.0f, 24.0f, -51.0f, 42.0f, -52.0f);
        Matrix matrix = new Matrix();
        for (int i = 0; i < 8; i++) {
            matrix.setRotate(i * 45.0f);
            this.arms.addPath(path, matrix);
            this.armsInner.addPath(path2, matrix);
        }
        for (int i2 = 0; i2 < 48; i2++) {
            double radians = Math.toRadians((i2 * 7.5d) - 90.0d);
            float fCos = (float) Math.cos(radians);
            float fSin = (float) Math.sin(radians);
            float f = 302.0f * fCos;
            float f2 = 302.0f * fSin;
            if (i2 % MAX_RIPPLES == 0) {
                this.ornament.moveTo((10.0f * fCos) + f, (10.0f * fSin) + f2);
                this.ornament.lineTo(f - (fSin * 6.5f), (fCos * 6.5f) + f2);
                this.ornament.lineTo(f - (10.0f * fCos), f2 - (10.0f * fSin));
                this.ornament.lineTo((fSin * 6.5f) + f, f2 - (fCos * 6.5f));
                this.ornament.close();
            } else {
                this.ornament.addCircle(f, f2, 5.0f, Path.Direction.CW);
            }
            double radians2 = Math.toRadians(((i2 + 0.5d) * 7.5d) - 90.0d);
            this.ornamentDots.addCircle(((float) Math.cos(radians2)) * 302.0f, ((float) Math.sin(radians2)) * 302.0f, RIPPLE_LIFE, Path.Direction.CW);
        }
        for (int i3 = 0; i3 < 72; i3++) {
            double radians3 = Math.toRadians(i3 * 5.0d);
            float fCos2 = (float) Math.cos(radians3);
            float fSin2 = (float) Math.sin(radians3);
            float f3 = i3 % MAX_RIPPLES == 0 ? 193.0f : 198.0f;
            float f4 = i3 % MAX_RIPPLES == 0 ? 208.0f : 204.0f;
            this.dialTicks.moveTo(fCos2 * f3, f3 * fSin2);
            this.dialTicks.lineTo(fCos2 * f4, f4 * fSin2);
        }
        for (int i4 = 0; i4 < 8; i4++) {
            double radians4 = Math.toRadians((i4 * 45.0d) + 45.0d);
            this.jewels.addCircle(182.0f * ((float) Math.cos(radians4)), ((float) Math.sin(radians4)) * 182.0f, 3.6f, Path.Direction.CW);
        }
        for (int i5 = 0; i5 < 16; i5++) {
            double radians5 = Math.toRadians(i5 * 22.5d);
            double radians6 = Math.toRadians(1.1d);
            this.rays.moveTo(((float) Math.cos(radians5 - radians6)) * 30.0f, ((float) Math.sin(radians5 - radians6)) * 30.0f);
            this.rays.lineTo(420.0f * ((float) Math.cos(radians5 - (radians6 * 2.2d))), 420.0f * ((float) Math.sin(radians5 - (radians6 * 2.2d))));
            this.rays.lineTo(420.0f * ((float) Math.cos((radians6 * 2.2d) + radians5)), 420.0f * ((float) Math.sin((radians6 * 2.2d) + radians5)));
            this.rays.lineTo(((float) Math.cos(radians5 + radians6)) * 30.0f, ((float) Math.sin(radians5 + radians6)) * 30.0f);
            this.rays.close();
        }
    }

    void setSize(int i, int i2) {
        this.w = i;
        this.h = i2;
        this.s = (Math.min(this.w, this.h) / 800.0f) * 0.98f;
        this.cx = this.w / 2.0f;
        this.cy = this.h * 0.56f;
        this.parallax = 26.0f * this.s;
        this.vignettePaint.setShader(new RadialGradient(this.cx, this.h * BAKE, Math.max(this.w, this.h) * 0.75f, new int[]{0, 0, -1207959552}, new float[]{0.0f, BAKE, 1.0f}, Shader.TileMode.CLAMP));
        rebuildBackground();
        this.map.setSize(this.w);
        bakeAll();
    }

    float centerX() {
        return this.cx;
    }

    float centerY() {
        return this.cy;
    }

    void setPalette(Palette palette) {
        if (!palette.sameAs(this.palette)) {
            this.palette = palette;
            this.metal = new LinearGradient(-360.0f, 0.0f, 360.0f, 0.0f, new int[]{palette.dark, palette.mid, palette.light, Palette.mix(palette.light, -1, 0.55f), palette.light, palette.mid, palette.dark}, new float[]{0.0f, 0.22f, 0.4f, BAKE, 0.6f, 0.78f, 1.0f}, Shader.TileMode.CLAMP);
            this.metalFlat = new LinearGradient(-320.0f, 0.0f, 320.0f, 0.0f, new int[]{palette.light, palette.mid, palette.dark}, new float[]{0.0f, BAKE, 1.0f}, Shader.TileMode.CLAMP);
            this.dotPaint.setColor(palette.dot);
            this.jewelPaint.setColor(palette.dot);
            this.textLit = palette.lit;
            this.textRest = Palette.mix(palette.bg, -14011322, 0.45f);
            this.ripplePaint.setColor(palette.lit);
            this.map.setColors(palette.lit, palette.light);
            this.rippleGlow.setColor(palette.light);
            this.glowPaint.setColorFilter(new PorterDuffColorFilter(palette.light, PorterDuff.Mode.SRC_IN));
            this.textGlowPaint.setColorFilter(new PorterDuffColorFilter(palette.lit, PorterDuff.Mode.SRC_IN));
            this.shadowPaint.setColorFilter(new PorterDuffColorFilter(-16777216, PorterDuff.Mode.SRC_IN));
            this.spritePaint.setColorFilter(new PorterDuffColorFilter(palette.light, PorterDuff.Mode.SRC_IN));
            this.sparklePaint.setColorFilter(new PorterDuffColorFilter(Palette.mix(palette.dot, -1, 0.4f), PorterDuff.Mode.SRC_IN));
            int i = palette.lit & 16777215;
            this.raysPaint.setShader(new RadialGradient(0.0f, 0.0f, 420.0f, new int[]{939524096 | i, 301989888 | i, i}, new float[]{0.0f, 0.45f, 1.0f}, Shader.TileMode.CLAMP));
            this.medallionPaint.setShader(new RadialGradient(0.0f, 0.0f, 110.0f, new int[]{805306368 | i, 268435456 | i, i}, new float[]{0.0f, 0.6f, 1.0f}, Shader.TileMode.CLAMP));
            rebuildBackground();
        }
    }

    private void rebuildBackground() {
        if (this.w != 0.0f && this.palette != null) {
            int i = this.palette.bg;
            this.bgPaint.setShader(new RadialGradient(this.cx, this.cy, Math.max(this.w, this.h) * 0.62f, new int[]{i, Palette.mix(i, -16777216, BAKE), -16513785}, new float[]{0.0f, 0.6f, 1.0f}, Shader.TileMode.CLAMP));
        }
    }

    private void bakeAll() {
        recycle(this.glowStatic);
        recycle(this.glowRings);
        recycle(this.glowStar);
        recycle(this.glowRosette);
        recycle(this.glowText);
        recycle(this.nebula);
        try {
            this.glowStatic = bakeLayer(0, 362.0f);
            this.glowRings = bakeLayer(1, 242.0f);
            this.glowStar = bakeLayer(2, 202.0f);
            this.glowRosette = bakeLayer(3, 112.0f);
            this.glowText = bakeLayer(4, 272.0f);
            this.nebula = bakeNebula();
        } catch (OutOfMemoryError e) {
            this.nebula = null;
            this.glowText = null;
            this.glowRosette = null;
            this.glowStar = null;
            this.glowRings = null;
            this.glowStatic = null;
        }
    }

    private static void recycle(Bitmap bitmap) {
        if (bitmap != null) {
            bitmap.recycle();
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private Bitmap bakeLayer(int i, float f) {
        float f2 = BAKE * this.s;
        int iCeil = ((int) Math.ceil(2.0f * f * f2)) + 4;
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(iCeil, iCeil, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapCreateBitmap);
        canvas.translate(iCeil / 2.0f, iCeil / 2.0f);
        canvas.scale(f2, f2);
        Paint paint = new Paint(1);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(-1);
        paint.setMaskFilter(new BlurMaskFilter(7.0f, BlurMaskFilter.Blur.NORMAL));
        switch (i) {
            case 0:
                geomStatic(canvas, paint, false);
                return bitmapCreateBitmap;
            case 1:
                geomRings(canvas, paint, false, 0);
                return bitmapCreateBitmap;
            case 2:
                geomStar(canvas, paint, false, 0);
                return bitmapCreateBitmap;
            case 3:
                geomRosette(canvas, paint, false);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(0.0f, 0.0f, 9.0f, paint);
                return bitmapCreateBitmap;
            case 4:
                Paint paint2 = new Paint(this.textPaint);
                paint2.setColor(-1);
                paint2.setStyle(Paint.Style.FILL_AND_STROKE);
                paint2.setStrokeWidth(1.4f);
                paint2.setMaskFilter(new BlurMaskFilter(4.0f, BlurMaskFilter.Blur.NORMAL));
                canvas.drawTextOnPath(this.text, this.textCircle, 0.0f, 0.0f, paint2);
                return bitmapCreateBitmap;
            default:
                return bitmapCreateBitmap;
        }
    }

    private Bitmap bakeNebula() {
        float f = this.parallax;
        int iMax = Math.max(8, (int) ((this.w + (4.0f * f)) / NEB));
        int iMax2 = Math.max(8, (int) (((f * 4.0f) + this.h) / NEB));
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(iMax, iMax2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapCreateBitmap);
        Paint paint = new Paint(5);
        for (float[] fArr : new float[][]{new float[]{0.15f, 0.22f, 0.75f, 2768762.0f, 52.0f}, new float[]{0.9f, 0.38f, 0.8f, 4927086.0f, 48.0f}, new float[]{0.25f, 0.82f, 0.9f, 1990246.0f, 44.0f}, new float[]{0.78f, 0.92f, 0.6f, 7227934.0f, 38.0f}, new float[]{BAKE, 0.56f, 0.55f, 2768762.0f, 30.0f}, new float[]{0.65f, 0.08f, BAKE, 3810640.0f, 34.0f}}) {
            float f2 = fArr[0] * iMax;
            float f3 = fArr[1] * iMax2;
            float f4 = fArr[2] * iMax;
            int i = (int) fArr[3];
            int i2 = (int) fArr[4];
            paint.setShader(new RadialGradient(f2, f3, f4, new int[]{(i2 << 24) | i, ((i2 / 3) << 24) | i, i}, new float[]{0.0f, BAKE, 1.0f}, Shader.TileMode.CLAMP));
            canvas.drawCircle(f2, f3, f4, paint);
        }
        return bitmapCreateBitmap;
    }

    private static Bitmap bakeSprite(int i, int[] iArr, float[] fArr) {
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(i, i, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapCreateBitmap);
        Paint paint = new Paint(1);
        float f = i / 2.0f;
        paint.setShader(new RadialGradient(f, f, f, iArr, fArr, Shader.TileMode.CLAMP));
        canvas.drawCircle(f, f, f, paint);
        return bitmapCreateBitmap;
    }

    private static Bitmap bakeSparkle() {
        float f = 256 / 2.0f;
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapCreateBitmap);
        Paint paint = new Paint(1);
        paint.setShader(new RadialGradient(f, f, 46.0f, new int[]{-1, 1442840575, 16777215}, new float[]{0.0f, 0.25f, 1.0f}, Shader.TileMode.CLAMP));
        canvas.drawCircle(f, f, 46.0f, paint);
        paint.setShader(new LinearGradient(0.0f, f, 256, f, new int[]{16777215, -1, 16777215}, new float[]{0.0f, BAKE, 1.0f}, Shader.TileMode.CLAMP));
        canvas.drawRect(0.0f, f - 1.5f, 256, f + 1.5f, paint);
        canvas.save();
        canvas.rotate(90.0f, f, f);
        canvas.drawRect(0.0f, f - 1.5f, 256, f + 1.5f, paint);
        canvas.restore();
        canvas.save();
        canvas.rotate(45.0f, f, f);
        canvas.scale(BAKE, 1.0f, f, f);
        paint.setAlpha(120);
        canvas.drawRect(0.0f, f - 1.0f, 256, f + 1.0f, paint);
        canvas.rotate(90.0f, f, f);
        canvas.drawRect(0.0f, f - 1.0f, 256, f + 1.0f, paint);
        canvas.restore();
        return bitmapCreateBitmap;
    }

    void addRipple(float f, float f2, float f3) {
        this.rx[this.rippleNext] = f;
        this.ry[this.rippleNext] = f2;
        this.rt[this.rippleNext] = f3;
        this.rippleNext = (this.rippleNext + 1) % MAX_RIPPLES;
    }

    private static void alpha(Paint paint, boolean z, int i) {
        if (!z) {
            i = 255;
        } else if (i < 0) {
            i = 0;
        } else if (i > 255) {
            i = 255;
        }
        paint.setAlpha(i);
    }

    private void geomStatic(Canvas canvas, Paint paint, boolean z) {
        float f = z ? 1.0f : NEB;
        alpha(paint, z, 70);
        paint.setStrokeWidth(0.8f * f);
        paint.setPathEffect(this.dash2x6);
        canvas.drawCircle(0.0f, 0.0f, 320.0f, paint);
        paint.setPathEffect(null);
        canvas.drawCircle(0.0f, 0.0f, 285.0f, paint);
        paint.setPathEffect(this.dash1x4);
        canvas.drawCircle(0.0f, 0.0f, 160.0f, paint);
        paint.setPathEffect(null);
        canvas.drawCircle(0.0f, 0.0f, 95.0f, paint);
        alpha(paint, z, 120);
        paint.setStrokeWidth(0.9f * f);
        canvas.drawPath(this.ornament, paint);
        Paint.Style style = paint.getStyle();
        paint.setStyle(Paint.Style.FILL);
        alpha(paint, z, MAX_MOTES);
        canvas.drawPath(this.ornamentDots, paint);
        paint.setStyle(style);
        alpha(paint, z, 85);
        paint.setStrokeWidth(f * 1.0f);
        canvas.drawLine(0.0f, -350.0f, 0.0f, -330.0f, paint);
        canvas.drawLine(0.0f, 330.0f, 0.0f, 350.0f, paint);
        canvas.drawLine(-350.0f, 0.0f, -330.0f, 0.0f, paint);
        canvas.drawLine(330.0f, 0.0f, 350.0f, 0.0f, paint);
    }

    private void geomRings(Canvas canvas, Paint paint, boolean z, int i) {
        float f = z ? 1.0f : NEB;
        alpha(paint, z, i + 120);
        paint.setStrokeWidth(1.0f * f);
        paint.setPathEffect(this.dashRing);
        canvas.drawCircle(0.0f, 0.0f, 215.0f, paint);
        paint.setPathEffect(this.dash2x12);
        canvas.drawCircle(0.0f, 0.0f, 230.0f, paint);
        paint.setPathEffect(null);
        alpha(paint, z, i + 95);
        paint.setStrokeWidth(0.8f * f);
        canvas.drawPath(this.dialTicks, paint);
        alpha(paint, z, (i / 2) + 55);
        paint.setStrokeWidth(f * BAKE);
        canvas.drawCircle(0.0f, 0.0f, 201.0f, paint);
    }

    private void geomStar(Canvas canvas, Paint paint, boolean z, int i) {
        float f = z ? 1.0f : NEB;
        alpha(paint, z, i + 180);
        paint.setStrokeWidth(f * 1.3f);
        canvas.drawRoundRect(this.squareRect, 4.0f, 4.0f, paint);
        canvas.rotate(45.0f);
        canvas.drawRoundRect(this.squareRect, 4.0f, 4.0f, paint);
        canvas.rotate(-45.0f);
        if (!z) {
            Paint.Style style = paint.getStyle();
            paint.setStyle(Paint.Style.FILL);
            canvas.drawPath(this.jewels, paint);
            paint.setStyle(style);
        }
    }

    private void geomRosette(Canvas canvas, Paint paint, boolean z) {
        float f = z ? 1.0f : NEB;
        alpha(paint, z, 255);
        paint.setStrokeWidth(2.4f * f);
        canvas.drawPath(this.arms, paint);
        alpha(paint, z, 190);
        paint.setStrokeWidth(1.1f * f);
        canvas.drawPath(this.armsInner, paint);
        alpha(paint, z, 255);
        paint.setStrokeWidth(f * 0.8f);
        paint.setPathEffect(this.dash3x3);
        canvas.drawCircle(0.0f, 0.0f, 14.0f, paint);
        paint.setPathEffect(null);
    }

    void draw(Canvas canvas, float f, float f2, float f3, float f4, float inscription) {
        float fRippleBoost;
        float f5;
        if (this.w != 0.0f) {
            float fMin = this.lastT < 0.0f ? 0.0f : Math.min(0.1f, f - this.lastT);
            this.lastT = f;
            this.rotTime = (fMin * this.speedMul) + this.rotTime;
            float f6 = f2 * this.parallaxMul;
            float f7 = f3 * this.parallaxMul;
            float f8 = this.parallax;
            float fMax = Math.max(f4, this.flare);
            float fSin = this.effectsOn ? BAKE + (BAKE * ((float) Math.sin(1.2f * f))) : 0f;
            this.sheenAngle = 35.0f + (55.0f * f6);
            this.sheenOffset = (260.0f * f7) + (((float) Math.sin(0.15f * f)) * 80.0f);
            canvas.save();
            canvas.translate((-f6) * f8 * 0.2f, (-f7) * f8 * 0.2f);
            canvas.drawRect((-2.0f) * f8, (-2.0f) * f8, (2.0f * f8) + this.w, (2.0f * f8) + this.h, this.bgPaint);
            canvas.restore();
            if (this.nebulaOn && this.nebula != null) {
                canvas.save();
                canvas.translate(((-2.0f) * f8) - ((f6 * f8) * 0.35f), ((-2.0f) * f8) - ((f7 * f8) * 0.35f));
                canvas.scale(NEB, NEB);
                this.nebulaPaint.setAlpha(clamp((int) (205.0d + (45.0d * Math.sin(0.1f * f)))));
                canvas.drawBitmap(this.nebula, 0.0f, 0.0f, this.nebulaPaint);
                canvas.restore();
            }
            drawFlipDisc(canvas, f4);
            if (this.matrixOn) drawArmenianMatrix(canvas, f);
            if (this.mapOn && f4 >= 0.5f) {
                canvas.save();
                canvas.translate(this.cx - ((f6 * f8) * 0.15f), this.cy - ((f7 * f8) * 0.15f));
                canvas.scale(Math.max(0.001f, -(float)Math.cos(Math.PI * f4)), 1f);
                applyMapPerspective(canvas, f6, f7);
                this.map.drawLines(canvas, 1f, fSin);
                this.map.drawLabels(canvas, 1f);
                canvas.restore();
            }
            if (f4 < 0.5f) {
                canvas.save();
                canvas.translate(this.cx, this.cy);
                canvas.scale(Math.max(0.001f, (float)Math.cos(Math.PI * f4)), 1f);
                canvas.translate(-this.cx, -this.cy);
            if (this.celestialOn) drawCelestial(canvas, f, f6, f7);
            if (this.perspectiveOn) {
                canvas.save();
                canvas.translate(this.cx, this.cy);
                canvas.scale(this.s, this.s);
                this.relief.draw(canvas, f6, f7, this.palette.light);
                canvas.restore();
            }
            if (this.raysOn) {
                layer(canvas, f6, f7, 1.0f);
                canvas.rotate((this.rotTime * 1.5f) + (this.spin * 0.2f));
                float f9 = 1.0f + (0.08f * fMax) + (0.03f * fSin);
                canvas.scale(f9, f9);
                this.raysPaint.setAlpha(clamp((int) ((0.45f + (0.55f * fMax)) * 255.0f)));
                canvas.drawPath(this.rays, this.raysPaint);
                canvas.restore();
            }
            layer(canvas, f6, f7, 0.25f);
            shadowAndGlow(canvas, this.glowStatic, 0.0f, f6, f7, 70, (int) (55.0f + (60.0f * fMax)));
            applySheen(0.0f);
            geomStatic(canvas, this.stroke, true);
            canvas.restore();
            float f10 = ((-this.rotTime) * 4.0f) - this.spin;
            layer(canvas, f6, f7, 0.45f);
            shadowAndGlow(canvas, this.glowRings, f10, f6, f7, 80, (int) (50.0f + (90.0f * fMax)));
            canvas.rotate(f10);
            applySheen(f10);
            geomRings(canvas, this.stroke, true, (int) (80.0f * fMax));
            canvas.restore();
            float f11 = (this.rotTime * 2.0f) + (this.spin * BAKE);
            float fMax2 = Math.max(this.ambientBase * (1.0f + (0.3f * ((float) Math.sin(0.6f * f)))), inscription);
            layer(canvas, f6, f7, 0.6f);
            if (this.glowText != null && fMax2 > 0.02f) {
                canvas.save();
                canvas.rotate(f11);
                this.textGlowPaint.setAlpha(clamp((int) ((120.0f * inscription) + (65.0f * fMax2))));
                drawBaked(canvas, this.glowText, this.textGlowPaint);
                canvas.restore();
            }
            canvas.rotate(f11);
            // Draw an embossed edge behind the inscription before its lit face.
            if (this.perspectiveOn) {
                this.textPaint.setColor(this.palette.dark);
                for (int depth = 5; depth >= 1; depth--) {
                    canvas.save();
                    canvas.translate(-f6 * depth * 0.8f, -f7 * depth * 0.8f + depth * 0.35f);
                    canvas.drawTextOnPath(this.text, this.textCircle, 0.0f, 0.0f, this.textPaint);
                    canvas.restore();
                }
            }
            this.textPaint.setColor(this.textRest);
            canvas.drawTextOnPath(this.text, this.textCircle, 0.0f, 0.0f, this.textPaint);
            applySheen(f11);
            this.textSheen.setShader(this.sheenOn ? this.metal : null);
            this.textSheen.setColor(this.textLit);
            this.textSheen.setAlpha(clamp((int) (255.0f * fMax2)));
            canvas.drawTextOnPath(this.text, this.textCircle, 0.0f, 0.0f, this.textSheen);
            canvas.restore();
            float f12 = (this.rotTime * NEB) + (this.spin * 0.8f);
            layer(canvas, f6, f7, 0.8f);
            shadowAndGlow(canvas, this.glowStar, f12, f6, f7, 90, (int) (55.0f + (80.0f * fMax)));
            canvas.rotate(f12);
            applySheen(f12);
            geomStar(canvas, this.stroke, true, (int) (60.0f * fMax));
            this.jewelPaint.setAlpha(clamp((int) (170.0f + (50.0f * fSin) + (35.0f * fMax))));
            canvas.drawPath(this.jewels, this.jewelPaint);
            canvas.restore();
            layer(canvas, f6, f7, 1.0f);
            float f13 = 1.0f + (0.04f * f4) + (0.1f * this.flare);
            canvas.scale(f13, f13);
            this.medallionPaint.setAlpha(clamp((int) ((BAKE + (BAKE * fMax)) * 255.0f)));
            canvas.drawCircle(0.0f, 0.0f, 110.0f, this.medallionPaint);
            shadowAndGlow(canvas, this.glowRosette, 0.0f, f6, f7, 100, (int) (110.0f + (60.0f * fSin) + (85.0f * fMax)));
            applySheen(0.0f);
            geomRosette(canvas, this.stroke, true);
            canvas.drawCircle(0.0f, 0.0f, 5.0f, this.dotPaint);
            canvas.restore();
                canvas.restore();
            }
            float f14 = this.cx - (f6 * f8);
            float f15 = this.cy - (f7 * f8);
            float f16 = (90.0f + (40.0f * fSin) + (170.0f * fMax)) * this.s * BAKE;
            this.sparklePaint.setAlpha(clamp((int) ((0.12f + (0.12f * fSin) + (0.26f * fMax)) * 255.0f)));
            this.tmpRect.set(f14 - f16, f15 - f16, f14 + f16, f15 + f16);
            if (this.effectsOn && this.sparkleOn && f4 < 0.5f) canvas.drawBitmap(this.sparkle, (Rect) null, this.tmpRect, this.sparklePaint);
            for (int i = 0; i < MAX_RIPPLES; i++) {
                float f17 = f - this.rt[i];
                if (f17 >= 0.0f && f17 <= RIPPLE_LIFE) {
                    float f18 = f17 / RIPPLE_LIFE;
                    float f19 = (1.0f - ((1.0f - f18) * (1.0f - f18))) * 520.0f * this.s;
                    float f20 = (1.0f - f18) * (1.0f - f18);
                    this.rippleGlow.setStrokeWidth((10.0f * this.s * (1.0f - f18)) + 2.0f);
                    this.rippleGlow.setAlpha(clamp((int) (70.0f * f20)));
                    canvas.drawCircle(this.rx[i], this.ry[i], f19, this.rippleGlow);
                    this.ripplePaint.setStrokeWidth(RIPPLE_LIFE * this.s);
                    this.ripplePaint.setAlpha(clamp((int) (200.0f * f20)));
                    canvas.drawCircle(this.rx[i], this.ry[i], f19, this.ripplePaint);
                    if (f18 < 0.6f) {
                        this.ripplePaint.setAlpha(clamp((int) (f20 * 110.0f)));
                        canvas.drawCircle(this.rx[i], this.ry[i], f19 * 0.62f, this.ripplePaint);
                    }
                }
            }
            int iMin = Math.min(this.moteCount, MAX_MOTES);
            for (int i2 = 0; i2 < iMin; i2++) {
                float f21 = this.mDepth[i2];
                float fWrap = wrap(this.my[i2] - (this.mSpeed[i2] * f)) * this.h;
                float fSin2 = ((this.mx[i2] * this.w) + ((((float) Math.sin((0.3f * f) + this.mPhase[i2])) * 10.0f) * this.s)) - ((f6 * f8) * f21);
                float f22 = fWrap - (f21 * (f7 * f8));
                float fSin3 = BAKE + (BAKE * ((float) Math.sin(((0.8f + (this.mSpeed[i2] * 40.0f)) * f) + this.mPhase[i2])));
                if (this.mBokeh[i2]) {
                    fRippleBoost = (rippleBoost(fSin2, f22, f) * 0.4f) + (((fSin3 * 0.12f) + 0.08f) * (0.7f + (BAKE * fMax)));
                    f5 = this.mSize[i2] * this.s * BAKE;
                } else {
                    fRippleBoost = rippleBoost(fSin2, f22, f) + (((fSin3 * 0.33f) + 0.12f) * (0.65f + (0.35f * fMax)));
                    f5 = this.mSize[i2] * 3.2f * this.s * BAKE;
                }
                this.spritePaint.setAlpha(clamp((int) (Math.min(1.0f, fRippleBoost) * 255.0f)));
                this.tmpRect.set(fSin2 - f5, f22 - f5, fSin2 + f5, f5 + f22);
                canvas.drawBitmap(this.mBokeh[i2] ? this.bokehSprite : this.dotSprite, (Rect) null, this.tmpRect, this.spritePaint);
            }
            canvas.drawRect(0.0f, 0.0f, this.w, this.h, this.vignettePaint);
            drawInfo(canvas);
        }
    }

    private void drawArmenianMatrix(Canvas canvas, float time) {
        Paint p = this.matrixPaint;
        float spacing = Math.max(20f, this.w / 19f);
        float height = this.h + spacing * 14f;
        p.setTypeface(Typeface.create("monospace", Typeface.BOLD));
        p.setTextAlign(Paint.Align.CENTER);
        p.setTextSize(spacing * 0.68f);
        p.setStyle(Paint.Style.FILL);
        p.setShader(null);
        int strength = Math.max(0, Math.min(100, this.matrixIntensity));
        int cols = (int)Math.ceil(this.w / spacing);
        int ticks = (int)(time * 3.0f);
        for (int col = 0; col < cols; col++) {
            float speed = spacing * (0.72f + (col * 17 % 13) * 0.045f);
            float seed = ((col * 97) % 173) * spacing;
            float y = ((time * speed + seed) % height) - spacing * 2f;
            for (int row = 0; row < 11; row++) {
                float py = y - row * spacing;
                if (py < -spacing || py > this.h + spacing) continue;
                int index = Math.floorMod(col * 97 + row * 31 + ticks / 3, MATRIX_LETTERS.length());
                float fade = (11f - row) / 11f;
                int alpha = Math.min(255, (int)((row == 0 ? 230f : 125f * fade) * strength / 100f));
                p.setColor(row == 0 ? this.palette.light : this.palette.lit);
                p.setAlpha(alpha);
                canvas.drawText(MATRIX_LETTERS, index, index + 1, (col + 0.5f) * spacing, py, p);
            }
        }
    }

    private void drawInfo(Canvas canvas) {
        Paint p = this.infoPaint;
        p.setShader(null); p.setStyle(Paint.Style.FILL); p.setColor(this.palette.light);
        p.setTextAlign(Paint.Align.CENTER); p.setTypeface(Typeface.create("serif", Typeface.NORMAL));
        p.setShadowLayer(4f, 0f, 1f, 0xaa000000);
        float unit = Math.min(this.w, this.h);
        if (this.calendarOn && !this.calendarText.isEmpty()) {
            p.setAlpha(225); p.setTextSize(Math.max(17f, unit * 0.032f));
            canvas.drawText(this.calendarText, this.cx, this.h * 0.15f, p);
        }
        if (this.compassOn) {
            float y = this.h * 0.86f;
            p.setTextSize(Math.max(16f, unit * 0.025f));
            if (!this.compassReady) {
                p.setAlpha(190);
                canvas.drawText("Компас: " + this.compassStatus, this.cx, y, p);
            } else {
                canvas.save(); canvas.translate(this.cx, y - unit * 0.038f); canvas.rotate(this.compassAngle);
                float size = unit * 0.033f;
                this.compassPath.reset();
                this.compassPath.moveTo(0, -size);
                this.compassPath.lineTo(-size * 0.45f, size * 0.65f);
                this.compassPath.lineTo(0, size * 0.34f);
                this.compassPath.lineTo(size * 0.45f, size * 0.65f);
                this.compassPath.close();
                p.setAlpha(240); canvas.drawPath(this.compassPath, p); canvas.restore();
                p.setAlpha(220); canvas.drawText("ԱՐԱՐԱՏ", this.cx, y + unit * 0.035f, p);
            }
        }
        p.clearShadowLayer();
    }

    private void drawFlipDisc(Canvas canvas, float progress) {
        float p = Math.max(0f, Math.min(1f, progress));
        float turn = (float) Math.sin(Math.PI * p);
        float face = Math.abs((float) Math.cos(Math.PI * p));
        float radius = 350f * this.s;
        float thickness = 8f * this.s * turn;
        Paint pnt = this.flipPaint;
        pnt.setShader(null);
        pnt.setStyle(Paint.Style.FILL);
        pnt.setColor(this.palette.dark);
        pnt.setAlpha(235);
        canvas.drawOval(this.cx - radius * face - thickness, this.cy - radius,
                this.cx + radius * face + thickness, this.cy + radius, pnt);
        pnt.setColor(this.palette.bg);
        pnt.setAlpha(245);
        canvas.drawOval(this.cx - radius * face, this.cy - radius,
                this.cx + radius * face, this.cy + radius, pnt);
        pnt.setStyle(Paint.Style.STROKE);
        pnt.setStrokeWidth(Math.max(1f, 2.2f * this.s));
        pnt.setColor(this.palette.light);
        pnt.setAlpha((int)(95 + 100 * turn));
        canvas.drawOval(this.cx - radius * face - thickness * 0.55f, this.cy - radius,
                this.cx + radius * face + thickness * 0.55f, this.cy + radius, pnt);
        pnt.setStyle(Paint.Style.FILL);
        if (turn > 0.02f) {
            pnt.setColor(this.palette.lit);
            pnt.setAlpha((int)(45 * turn));
            canvas.drawOval(this.cx - thickness * 0.32f, this.cy - radius * 0.75f,
                    this.cx + thickness * 0.32f, this.cy + radius * 0.75f, pnt);
        }
    }

    private void applyMapPerspective(Canvas canvas, float tiltX, float tiltY) {
        if (!this.perspectiveOn) return;
        this.perspectiveCamera.save();
        this.perspectiveCamera.setLocation(0f, 0f, -16f);
        this.perspectiveCamera.rotateX(tiltY * 8f);
        this.perspectiveCamera.rotateY(-tiltX * 8f);
        this.perspectiveCamera.getMatrix(this.perspectiveMatrix);
        this.perspectiveCamera.restore();
        canvas.concat(this.perspectiveMatrix);
    }

    private void drawCelestial(Canvas canvas, float time, float tiltX, float tiltY) {
        layer(canvas, tiltX, tiltY, 0.12f);
        Paint p = this.celestialPaint;
        p.setShader(null);
        p.setColor(this.palette.light);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(0.7f);
        p.setAlpha(55);
        canvas.drawCircle(0, 0, 375f, p);
        for (int i = 0; i < 60; i++) {
            canvas.save();
            canvas.rotate(i * 6f);
            p.setAlpha(i % 5 == 0 ? 120 : 48);
            p.setStrokeWidth(i % 5 == 0 ? 1.5f : 0.7f);
            canvas.drawLine(0, -375f, 0, i % 5 == 0 ? -365f : -370f, p);
            canvas.restore();
        }
        if (this.batteryOn && this.batteryPercent >= 0) {
            p.setStrokeWidth(2.3f);
            p.setStrokeCap(Paint.Cap.ROUND);
            p.setAlpha(170);
            canvas.drawArc(this.celestialArc, 95f, VisualEffects14.batteryArcSweep(this.batteryPercent), false, p);
        }
        // The illuminated part of the moon changes with the approximate lunar cycle.
        float phase = (float) (VisualEffects14.moonAgeDays(System.currentTimeMillis()) / 29.530588853);
        float illuminated = (float) VisualEffects14.moonIllumination(System.currentTimeMillis());
        canvas.save();
        canvas.rotate(-32f);
        canvas.translate(0, -375f);
        p.setStyle(Paint.Style.FILL);
        p.setColor(this.palette.bg);
        p.setAlpha(255);
        canvas.drawCircle(0, 0, 12f, p);
        p.setColor(this.palette.light);
        p.setAlpha(190);
        canvas.drawCircle(0, 0, 8f, p);
        p.setColor(this.palette.bg);
        p.setAlpha(255);
        // Move the dark disc across the lit disc; the result is a stylized phase.
        if (illuminated < 0.98f) canvas.drawCircle((phase < 0.5f ? -1f : 1f) * illuminated * 16f, 0, 8f, p);
        canvas.restore();
        p.setStyle(Paint.Style.FILL);
        p.setColor(this.palette.lit);
        for (int i = 0; i < 3; i++) {
            double angle = time * 0.035 + i * 2.0943951;
            float x = (float) Math.cos(angle) * 375f;
            float y = (float) Math.sin(angle) * 375f;
            p.setAlpha(100 + (int)(50 * (0.5 + 0.5 * Math.sin(time + i))));
            canvas.drawCircle(x, y, i == 0 ? 3f : 1.6f, p);
        }
        canvas.restore();
    }

    private void applySheen(float f) {
        if (this.sheenOn) {
            this.sheenMatrix.setTranslate(this.sheenOffset, 0.0f);
            this.sheenMatrix.postRotate(this.sheenAngle - f);
            this.metal.setLocalMatrix(this.sheenMatrix);
            this.stroke.setShader(this.metal);
            return;
        }
        this.sheenMatrix.setRotate(45.0f - f);
        this.metalFlat.setLocalMatrix(this.sheenMatrix);
        this.stroke.setShader(this.metalFlat);
    }

    private void shadowAndGlow(Canvas canvas, Bitmap bitmap, float f, float f2, float f3, int i, int i2) {
        if (bitmap != null) {
            canvas.save();
            canvas.translate(5.0f + (f2 * 8.0f), 7.0f + (8.0f * f3));
            canvas.rotate(f);
            this.shadowPaint.setAlpha(clamp(i));
            drawBaked(canvas, bitmap, this.shadowPaint);
            canvas.restore();
            canvas.save();
            canvas.rotate(f);
            this.glowPaint.setAlpha(clamp(i2));
            drawBaked(canvas, bitmap, this.glowPaint);
            canvas.restore();
        }
    }

    private void drawBaked(Canvas canvas, Bitmap bitmap, Paint paint) {
        float f = this.s * BAKE;
        canvas.save();
        canvas.scale(1.0f / f, 1.0f / f);
        canvas.drawBitmap(bitmap, (-bitmap.getWidth()) / 2.0f, (-bitmap.getHeight()) / 2.0f, paint);
        canvas.restore();
    }

    private float rippleBoost(float f, float f2, float f3) {
        float fMax = 0.0f;
        for (int i = 0; i < MAX_RIPPLES; i++) {
            float f4 = f3 - this.rt[i];
            if (f4 >= 0.0f && f4 <= RIPPLE_LIFE) {
                float f5 = f4 / RIPPLE_LIFE;
                float f6 = (1.0f - ((1.0f - f5) * (1.0f - f5))) * 520.0f * this.s;
                float f7 = f - this.rx[i];
                float f8 = f2 - this.ry[i];
                float fAbs = 1.0f - (Math.abs(((float) Math.sqrt((f7 * f7) + (f8 * f8))) - f6) / (40.0f * this.s));
                if (fAbs > 0.0f) {
                    fMax = Math.max(fMax, (1.0f - f5) * fAbs * 0.8f);
                }
            }
        }
        return fMax;
    }

    private void layer(Canvas canvas, float f, float f2, float f3) {
        canvas.save();
        canvas.translate(this.cx - ((this.parallax * f) * f3), this.cy - ((this.parallax * f2) * f3));
        if (this.perspectiveOn) {
            this.perspectiveCamera.save();
            this.perspectiveCamera.setLocation(0f, 0f, -16f);
            this.perspectiveCamera.rotateX(f2 * 12f);
            this.perspectiveCamera.rotateY(-f * 12f);
            this.perspectiveCamera.getMatrix(this.perspectiveMatrix);
            this.perspectiveCamera.restore();
            canvas.concat(this.perspectiveMatrix);
        }
        canvas.scale(this.s, this.s);
    }

    private static int clamp(int i) {
        if (i < 0) {
            return 0;
        }
        if (i > 255) {
            return 255;
        }
        return i;
    }

    private static float wrap(float f) {
        return ((f % 1.0f) + 1.0f) % 1.0f;
    }
}
