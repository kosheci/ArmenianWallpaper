package am.arasce.wallpaper;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;

/* loaded from: classes.dex */
final class MapLayer {
    private static final float LON_C = 41.75f;
    private static final float COS = (float) Math.cos(Math.toRadians(37.75d));
    private static final float LAT_C = 37.75f;
    private static final float[] CORE = {38.5f, 39.9f, 39.3f, 40.4f, 40.2f, 40.8f, 41.2f, 41.25f, 42.5f, 41.55f, 43.6f, 41.25f, 44.6f, 41.3f, 45.8f, 41.0f, 46.6f, 40.85f, 47.4f, 40.6f, 48.4f, 40.2f, 49.3f, 39.75f, 49.0f, 39.2f, 48.2f, 38.85f, 47.0f, 38.95f, 46.1f, 38.8f, 45.4f, 38.45f, 45.35f, 37.6f, 44.6f, LAT_C, 43.6f, 37.95f, 42.6f, 38.0f, 41.9f, LAT_C, 41.2f, 37.6f, 40.2f, 37.85f, 39.4f, 38.0f, 38.9f, 38.25f, 38.4f, 38.8f, 38.4f, 39.4f};
    private static final float[][] OTHERS = {new float[]{41.9f, LAT_C, 42.6f, 38.0f, 43.6f, 37.95f, 44.6f, LAT_C, 45.35f, 37.6f, 44.9f, 37.2f, 44.2f, 37.15f, 43.2f, 37.25f, 42.3f, 37.45f}, new float[]{42.3f, 37.45f, 43.2f, 37.25f, 44.2f, 37.15f, 44.9f, 37.2f, 45.0f, 36.9f, 44.9f, 36.3f, 44.5f, 35.5f, 43.6f, 35.4f, 43.1f, 36.0f, 42.9f, 36.7f}, new float[]{45.35f, 37.6f, 45.4f, 38.45f, 46.1f, 38.8f, 47.0f, 38.95f, 48.2f, 38.85f, 49.0f, 39.2f, 48.9f, 38.4f, 49.0f, 37.6f, 49.5f, 37.45f, 48.6f, 36.0f, 47.4f, 35.7f, 46.3f, 35.9f, 45.6f, 36.5f, 45.0f, 36.9f, 44.9f, 37.2f}, new float[]{45.8f, 41.0f, 45.6f, 41.6f, 46.4f, 41.9f, 47.6f, 42.1f, 48.6f, 41.9f, 49.1f, 41.2f, 49.6f, 40.6f, 49.3f, 39.75f, 48.4f, 40.2f, 47.4f, 40.6f, 46.6f, 40.85f}, new float[]{41.6f, 41.6f, 41.6f, 42.4f, 43.0f, 42.9f, 44.6f, 42.7f, 45.6f, 42.3f, 45.6f, 41.6f, 45.8f, 41.0f, 44.6f, 41.3f, 43.6f, 41.25f, 42.5f, 41.55f, 41.2f, 41.25f}, new float[]{40.7f, 37.7f, 41.2f, 37.6f, 41.9f, LAT_C, 42.3f, 37.45f, 42.9f, 36.7f, 42.4f, 36.2f, 41.0f, 36.2f, 40.5f, 36.7f}, new float[]{38.2f, 37.5f, 39.4f, 38.0f, 40.2f, 37.85f, 40.7f, 37.7f, 40.5f, 36.7f, 41.0f, 36.2f, 40.0f, 35.8f, 39.0f, 35.9f, 38.1f, 36.5f, 38.0f, 37.0f}, new float[]{36.7f, 37.6f, 37.3f, 38.2f, 38.2f, 38.35f, 38.9f, 38.25f, 39.4f, 38.0f, 38.2f, 37.5f, 37.6f, 37.2f, 36.9f, 37.2f}, new float[]{34.0f, 36.3f, 33.9f, 37.0f, 34.5f, 37.45f, 35.6f, 37.6f, 36.4f, 37.55f, 36.7f, 37.6f, 36.9f, 37.2f, 36.3f, 36.7f, 36.1f, 36.6f, 35.8f, 36.75f, 35.3f, 36.6f, 34.6f, 36.8f}, new float[]{36.1f, 36.6f, 36.3f, 36.7f, 36.9f, 37.2f, 37.6f, 37.2f, 38.0f, 37.0f, 38.1f, 36.5f, 39.0f, 35.9f, 40.0f, 35.8f, 39.6f, 35.2f, 38.4f, 34.4f, 37.4f, 33.5f, 36.9f, 32.8f, 36.0f, 32.6f, 35.1f, 32.85f, 35.2f, 33.27f, 35.37f, 33.56f, 35.5f, 33.9f, 35.65f, 34.4f, 35.9f, 35.2f, 35.78f, 35.6f, 35.9f, 36.0f, 36.0f, 36.3f}};
    private static final float[] RAID = {33.5f, 38.2f, 33.8f, 39.4f, 34.8f, 40.0f, 36.6f, 40.2f, 38.2f, 39.9f, 38.5f, 39.9f, 38.4f, 39.4f, 38.4f, 38.8f, 38.9f, 38.25f, 38.2f, 38.35f, 37.3f, 38.2f, 36.7f, 37.6f, 35.6f, 37.6f, 34.5f, 37.45f, 33.9f, 37.0f};
    private static final String[] LABELS = {"ВЕЛИКАЯ АРМЕНИЯ", "СИРИЯ", "АТРОПАТЕНА", "КИЛИКИЯ", "ИВЕРИЯ", "АЛБАНИЯ"};
    private static final float[] LABEL_LL = {42.2f, 40.15f, 37.8f, 34.6f, 47.4f, 36.6f, 35.1f, 37.15f, 43.4f, 42.1f, 47.6f, 41.5f};
    private static final String[] CITIES = {"Арташат", "Тигранакерт", "Антиохия", "Дамаск", "Птолемаида"};
    private static final float[] CITY_LL = {44.55f, 39.95f, 41.0f, 38.1f, 36.16f, 36.2f, 36.3f, 33.51f, 35.07f, 32.93f};
    // Artistic marker positions, not surveyed historical borders.
    private static final String[] REGIONS = {"Բարձր Հայք","Ծոփք","Աղձնիք","Տուրուբերան","Մոկք","Կորճայք","Պարսկահայք","Վասպուրական","Սյունիք","Արցախ","Փայտակարան","Ուտիք","Գուգարք","Տայք","Այրարատ"};
    private static final float[] REGION_LL = {39.4f,40.0f, 38.6f,38.9f, 40.5f,37.9f, 42.2f,39.1f, 42.9f,38.3f, 43.8f,37.9f, 45.0f,38.2f, 43.8f,39.0f, 45.7f,39.8f, 46.8f,40.0f, 48.3f,39.4f, 47.4f,40.4f, 44.5f,41.1f, 41.1f,40.8f, 43.7f,40.2f};
    private final float[] regionXY = new float[REGION_LL.length];
    private final Path wall = new Path();
    private final Path terrainLight = new Path();
    private final Path terrainShadow = new Path();
    private final RectF terrainBounds = new RectF();
    private final Paint terrainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint wallPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path core = new Path();
    private final Path others = new Path();
    private final Path raid = new Path();
    private final float[] labelXY = new float[LABEL_LL.length];
    private final float[] cityXY = new float[CITY_LL.length];
    private final Paint fill = new Paint(1);
    private final Paint line = new Paint(1);
    private final Paint raidLine = new Paint(1);
    private final Paint label = new Paint(1);
    private final Paint dot = new Paint(1);
    private float k = 1.0f;
    private int lit = -2575773;
    private int light = -1522828;

    MapLayer() {
        this.fill.setStyle(Paint.Style.FILL);
        this.line.setStyle(Paint.Style.STROKE);
        this.line.setStrokeJoin(Paint.Join.ROUND);
        this.raidLine.setStyle(Paint.Style.STROKE);
        this.label.setTypeface(Typeface.create("serif", 1));
        this.label.setTextAlign(Paint.Align.CENTER);
        this.dot.setStyle(Paint.Style.FILL);
    }

    void setSize(float f) {
        this.k = (0.97f * f) / (16.4f * COS);
        this.core.reset();
        this.others.reset();
        this.raid.reset();
        addPoly(this.core, CORE);
        for (float[] fArr : OTHERS) {
            addPoly(this.others, fArr);
        }
        addPoly(this.raid, RAID);
        project(LABEL_LL, this.labelXY);
        project(CITY_LL, this.cityXY);
        project(REGION_LL, this.regionXY);
        rebuildTerrain();
        float f2 = this.k / 60.0f;
        this.line.setStrokeWidth(Math.max(1.5f, 1.6f * f2));
        this.raidLine.setStrokeWidth(Math.max(1.2f, 1.2f * f2));
        this.raidLine.setPathEffect(new DashPathEffect(new float[]{6.0f * f2, 6.0f * f2}, 0.0f));
        this.label.setTextSize(f2 * 13.0f);
        this.label.setLetterSpacing(0.18f);
    }

    void setColors(int i, int i2) {
        this.lit = i;
        this.light = i2;
    }

    private void addPoly(Path path, float[] fArr) {
        for (int i = 0; i < fArr.length; i += 2) {
            float f = (fArr[i] - LON_C) * COS * this.k;
            float f2 = (-(fArr[i + 1] - LAT_C)) * this.k;
            if (i == 0) {
                path.moveTo(f, f2);
            } else {
                path.lineTo(f, f2);
            }
        }
        path.close();
    }

    private void project(float[] fArr, float[] fArr2) {
        for (int i = 0; i < fArr.length; i += 2) {
            fArr2[i] = (fArr[i] - LON_C) * COS * this.k;
            fArr2[i + 1] = (-(fArr[i + 1] - LAT_C)) * this.k;
        }
    }

    void drawRelief(Canvas canvas, float tiltX, float tiltY, float scale) {
        float depth = Math.max(2f, Math.min(16f, 8f * scale));
        float dx = -tiltX * depth * 0.6f;
        float dy = depth * (0.65f + tiltY * 0.4f);
        wallPaint.setStyle(Paint.Style.FILL);
        int color = this.light & 0x00ffffff;
        for (int i = 0; i < CORE.length; i += 2) {
            int j = (i + 2) % CORE.length;
            float x1 = (CORE[i] - LON_C) * COS * k, y1 = -(CORE[i+1] - LAT_C) * k;
            float x2 = (CORE[j] - LON_C) * COS * k, y2 = -(CORE[j+1] - LAT_C) * k;
            wall.reset(); wall.moveTo(x1,y1); wall.lineTo(x2,y2);
            wall.lineTo(x2+dx,y2+dy); wall.lineTo(x1+dx,y1+dy); wall.close();
            float normal = (y2-y1) / Math.max(0.001f, (float)Math.hypot(x2-x1,y2-y1));
            wallPaint.setColor(color | (clamp((int)(85f + 45f * normal)) << 24));
            canvas.drawPath(wall, wallPaint);
        }
    }

    /** Stylised procedural relief; heights are illustrative, not geographic elevations. */
    private void rebuildTerrain() {
        this.terrainLight.reset(); this.terrainShadow.reset();
        this.core.computeBounds(this.terrainBounds, true);
        float width = this.terrainBounds.width(), height = this.terrainBounds.height();
        if (width <= 0f || height <= 0f) return;
        float dx = width / 10f, dy = height / 5f;
        for (int row=0; row<5; row++) {
            for (int col=0; col<10; col++) {
                float x = this.terrainBounds.left + col*dx, y = this.terrainBounds.top + row*dy;
                float peakHeight = (0.15f + ((col*17 + row*23) % 11)/16f) * dy;
                float peakX = x + dx*(0.35f + (row%3)*0.1f);
                float peakY = y + dy*0.35f - peakHeight * 0.45f;
                this.terrainLight.moveTo(x,y+dy); this.terrainLight.lineTo(peakX,peakY);
                this.terrainLight.lineTo(x+dx,y+dy); this.terrainLight.close();
                this.terrainShadow.moveTo(x+dx,y+dy); this.terrainShadow.lineTo(peakX,peakY);
                this.terrainShadow.lineTo(x+dx,y+dy*0.55f); this.terrainShadow.close();
            }
        }
    }

    void drawTerrain(Canvas canvas, float tiltX, float tiltY) {
        canvas.save();
        canvas.clipPath(this.core);
        this.terrainPaint.setStyle(Paint.Style.FILL);
        this.terrainPaint.setColor((this.light & 0x00ffffff) | (42 << 24));
        canvas.drawPath(this.terrainLight,this.terrainPaint);
        this.terrainPaint.setColor(0x88150e08);
        canvas.drawPath(this.terrainShadow,this.terrainPaint);
        canvas.restore();
    }

    void drawLines(Canvas canvas, float f, float f2) {
        int i = this.light & 16777215;
        this.fill.setColor((clamp((int) (10.0f + (26.0f * f))) << 24) | i);
        canvas.drawPath(this.core, this.fill);
        this.fill.setColor((clamp((int) (5.0f + (14.0f * f))) << 24) | i);
        canvas.drawPath(this.others, this.fill);
        this.line.setColor((clamp((int) ((34.0f + (8.0f * f2)) + (130.0f * f))) << 24) | i);
        canvas.drawPath(this.others, this.line);
        this.raidLine.setColor(13662816 | (clamp((int) (22.0f + (110.0f * f))) << 24));
        canvas.drawPath(this.raid, this.raidLine);
        float strokeWidth = this.line.getStrokeWidth();
        this.line.setStrokeWidth(1.7f * strokeWidth);
        this.line.setColor(i | (clamp((int) ((55.0f + (12.0f * f2)) + (150.0f * f))) << 24));
        canvas.drawPath(this.core, this.line);
        this.line.setStrokeWidth(strokeWidth);
    }

    void drawLabels(Canvas canvas, float f, boolean enhanced) {
        if (f >= 0.03f) {
            int iClamp = clamp((int) (230.0f * f));
            this.label.setColor((this.lit & 16777215) | (iClamp << 24));
            this.label.setShadowLayer(4.0f, 0.0f, 1.0f, clamp((int) (200.0f * f)) << 24);
            float textSize = this.label.getTextSize();
            int i = 0;
            while (i < LABELS.length) {
                this.label.setTextSize(i == 0 ? 1.25f * textSize : textSize);
                canvas.drawText(LABELS[i], this.labelXY[i * 2], this.labelXY[(i * 2) + 1], this.label);
                i++;
            }
            this.label.setTextSize(0.9f * textSize);
            this.label.setLetterSpacing(0.04f);
            this.dot.setColor((this.lit & 16777215) | (iClamp << 24));
            for (int i2 = 0; i2 < CITIES.length; i2++) {
                float f2 = this.cityXY[i2 * 2];
                float f3 = this.cityXY[(i2 * 2) + 1];
                canvas.drawCircle(f2, f3, 0.28f * textSize, this.dot);
                canvas.drawText(CITIES[i2], f2, f3 - (0.6f * textSize), this.label);
            }
            // Points are a conceptual arrangement: they are not historical boundaries.
            for (int j = 0; j < REGIONS.length; j++) {
                float rx = this.regionXY[j*2], ry = this.regionXY[j*2+1];
                this.dot.setColor((this.light & 0x00ffffff) | (clamp((int)(150f*f)) << 24));
                canvas.drawCircle(rx, ry, Math.max(4f, textSize * 0.32f), this.dot);
            }
            float araratX = (44.29833f - LON_C) * COS * this.k;
            float araratY = -(39.70194f - LAT_C) * this.k;
            this.label.setTextSize(textSize * 1.12f);
            this.dot.setColor((this.lit & 16777215) | (iClamp << 24));
            float mountain = Math.max(8f, textSize * (enhanced ? 1.2f : 0.85f));
            if (enhanced) {
                this.dot.setColor((this.light & 0x00ffffff) | (clamp((int)(65f*f)) << 24));
                canvas.drawCircle(araratX, araratY, mountain * 1.55f, this.dot);
            }
            Path peak = this.wall;
            peak.reset(); peak.moveTo(araratX - mountain, araratY + mountain * 0.55f);
            peak.lineTo(araratX, araratY - mountain);
            peak.lineTo(araratX + mountain, araratY + mountain * 0.55f); peak.close();
            this.dot.setColor((this.light & 0x00ffffff) | (iClamp << 24));
            canvas.drawPath(peak, this.dot);
            this.dot.setColor((this.lit & 0x00ffffff) | (iClamp << 24));
            canvas.drawCircle(araratX, araratY, Math.max(3f, textSize * 0.23f), this.dot);
            canvas.drawText("ԱՐԱՐԱՏ ▲", araratX, araratY - textSize, this.label);
            this.label.setTextSize(textSize);
            this.label.setLetterSpacing(0.18f);
        }
    }

    void drawSelection(Canvas canvas, String selected) {
        for (int i = 0; i < REGIONS.length; i++) {
            if (!selected.equals(REGIONS[i] + " · условно")) continue;
            float x = this.regionXY[i * 2], y = this.regionXY[i * 2 + 1];
            this.dot.setStyle(Paint.Style.STROKE);
            this.dot.setStrokeWidth(Math.max(2f, this.label.getTextSize() * 0.15f));
            this.dot.setColor((this.light & 0x00ffffff) | 0xdc000000);
            canvas.drawCircle(x, y, Math.max(12f, this.label.getTextSize() * 1.1f), this.dot);
            this.dot.setStyle(Paint.Style.FILL);
            break;
        }
    }

    String findCity(float x, float y, float zoom) {
        float limit = Math.max(52f / Math.max(1f, zoom), this.label.getTextSize() * 4f);
        float best = limit * limit;
        String name = "";
        for (int i = 0; i < CITIES.length; i++) {
            float dx = x - this.cityXY[i*2], dy = y - this.cityXY[i*2+1];
            float distance = dx*dx + dy*dy;
            if (distance < best) { best = distance; name = CITIES[i]; }
        }
        float ax = (44.29833f - LON_C) * COS * this.k;
        float ay = -(39.70194f - LAT_C) * this.k;
        float dx = x-ax, dy = y-ay;
        if (dx*dx + dy*dy < best) { best = dx*dx + dy*dy; name = "ԱՐԱՐԱՏ"; }
        for (int j = 0; j < REGIONS.length; j++) {
            float rx = x - this.regionXY[j*2], ry = y - this.regionXY[j*2+1];
            float distance = rx*rx + ry*ry;
            if (distance < best) { best = distance; name = REGIONS[j] + " · условно"; }
        }
        return name;
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
}
