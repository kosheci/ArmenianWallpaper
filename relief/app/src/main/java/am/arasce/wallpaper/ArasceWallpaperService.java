package am.arasce.wallpaper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.hardware.GeomagneticField;
import android.location.Location;
import android.view.Surface;
import android.view.WindowManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Choreographer;
import android.os.Build;
import android.os.SystemClock;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

/* loaded from: classes.dex */
public class ArasceWallpaperService extends WallpaperService {
    @Override // android.service.wallpaper.WallpaperService
    public WallpaperService.Engine onCreateEngine() {
        return new ArasceEngine();
    }

    private final class ArasceEngine extends WallpaperService.Engine implements SensorEventListener, SharedPreferences.OnSharedPreferenceChangeListener {
        private Sensor rotationSensor;
        private final float[] rotationMatrix = new float[9];
        private final float[] remappedMatrix = new float[9];
        private final float[] orientation = new float[3];
        private boolean baseSet;
        private float baseX;
        private float baseY;
        private boolean dayCycle;
        private boolean nightGold;
        private float downX;
        private float downY;
        private boolean dragging;
        private float flare;
        private final Choreographer.FrameCallback frame;
        private boolean gestures;
        private boolean effectsEnabled = true;
        private boolean autoEnergy = true;
        private long lastEnergyFrame = 0;
        private boolean ripplesEnabled = true;
        private volatile float gx;
        private volatile float gy;
        private final Choreographer choreographer;
        private volatile boolean hasReading;
        private boolean holdReveal;
        private float lastAngle;
        private float lastFrameT;
        private float lastMoveT;
        private float lastPaletteT;
        private float lastTapT;
        private float lastTapX;
        private float lastTapY;
        private float pageSpin;
        private float pageSpinShown;
        private final SharedPreferences prefs;
        private boolean receiverRegistered;
        private final ArasceRenderer renderer;
        private float reveal;
        private final Sensor sensor;
        private final SensorManager sensorManager;
        private float spin;
        private float spinVel;
        private final long t0;
        private float tiltX;
        private float tiltY;
        private float touchAt;
        private boolean unlockFlash;
        private final BroadcastReceiver unlockReceiver;
        private boolean visibleNow;

        ArasceEngine() {
            super();
            this.renderer = new ArasceRenderer();
            this.choreographer = Choreographer.getInstance();
            this.frame = frameTimeNanos -> ArasceEngine.this.drawFrame();
            this.t0 = SystemClock.elapsedRealtime();
            this.lastFrameT = -1.0f;
            this.gestures = true;
            this.unlockFlash = true;
            this.dayCycle = true;
            this.lastPaletteT = -100.0f;
            this.touchAt = -100.0f;
            this.lastTapT = -100.0f;
            this.unlockReceiver = new BroadcastReceiver() { // from class: am.arasce.wallpaper.ArasceWallpaperService.ArasceEngine.2
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    if (ArasceEngine.this.unlockFlash && ArasceEngine.this.effectsEnabled) {
                        float fNow = ArasceEngine.this.now();
                        ArasceEngine.this.flare = 1.0f;
                        if (ArasceEngine.this.prefs.getBoolean("wake_fade", true)) ArasceEngine.this.renderer.wakeAt = fNow;
                        ArasceEngine.this.touchAt = fNow;
                        if (ArasceEngine.this.ripplesEnabled) ArasceEngine.this.renderer.addRipple(ArasceEngine.this.renderer.centerX(), ArasceEngine.this.renderer.centerY(), fNow);
                        if (ArasceEngine.this.ripplesEnabled) ArasceEngine.this.renderer.addRipple(ArasceEngine.this.renderer.centerX(), ArasceEngine.this.renderer.centerY(), fNow + 0.25f);
                    }
                }
            };
            this.sensorManager = (SensorManager) ArasceWallpaperService.this.getSystemService("sensor");
            this.rotationSensor = this.sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            Sensor defaultSensor = this.sensorManager.getDefaultSensor(9);
            this.sensor = defaultSensor == null ? this.sensorManager.getDefaultSensor(1) : defaultSensor;
            this.prefs = Settings.prefs(ArasceWallpaperService.this);
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            setTouchEventsEnabled(true);
            this.prefs.registerOnSharedPreferenceChangeListener(this);
            applySettings();
            try {
                ArasceWallpaperService.this.registerReceiver(this.unlockReceiver, new IntentFilter("android.intent.action.USER_PRESENT"));
                this.receiverRegistered = true;
            } catch (Exception e) {
            }
        }

        @Override // android.content.SharedPreferences.OnSharedPreferenceChangeListener
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
            applySettings();
        }

        private void applySettings() {
            this.effectsEnabled = this.prefs.getBoolean("effects_enabled", true);
            this.ripplesEnabled = this.effectsEnabled && this.prefs.getBoolean("ripples_enabled", true);
            this.renderer.effectsOn = this.effectsEnabled;
            this.renderer.sparkleOn = this.prefs.getBoolean("sparkle_enabled", true);
            this.renderer.parallaxMul = this.effectsEnabled ? this.prefs.getInt("parallax", 70) / 100.0f : 0f;
            this.renderer.speedMul = this.effectsEnabled ? this.prefs.getInt("speed", 65) / 100.0f : 0f;
            this.renderer.ambientBase = this.effectsEnabled ? this.prefs.getInt("glow", 18) / 100.0f : 0f;
            this.renderer.moteCount = this.effectsEnabled ? this.prefs.getInt("motes", 40) : 0;
            this.renderer.sheenOn = this.effectsEnabled && this.prefs.getBoolean("sheen", true);
            this.renderer.raysOn = this.effectsEnabled && this.prefs.getBoolean("rays", false);
            this.renderer.nebulaOn = this.effectsEnabled && this.prefs.getBoolean("nebula", true);
            this.renderer.mapOn = this.prefs.getBoolean("map", true);
            this.renderer.matrixOn = this.effectsEnabled && this.prefs.getBoolean("matrix_on", true);
            this.renderer.matrixIntensity = this.prefs.getInt("matrix_intensity", 55);
            this.renderer.matrixDensity = this.prefs.getInt("matrix_density", 100);
            this.renderer.matrixSpeed = this.prefs.getInt("matrix_speed", 100);
            this.renderer.matrixSize = this.prefs.getInt("matrix_size", 100);
            this.renderer.matrixOrdered = this.prefs.getBoolean("matrix_ordered", false);
            this.renderer.calendarPosition = this.prefs.getBoolean("calendar_below", false) ? 1 : 0;
            this.renderer.setLayout(this.prefs.getInt("emblem_size", 100) / 100f, this.prefs.getInt("emblem_x", 50) / 100f, this.prefs.getInt("emblem_y", 56) / 100f);
            this.autoEnergy = this.prefs.getBoolean("auto_energy", true);
            this.renderer.calendarOn = this.prefs.getBoolean("armenian_calendar", true);
            this.renderer.mountainOn = this.prefs.getBoolean("ararat_mountain", true);
            this.renderer.letterHaloOn = this.prefs.getBoolean("letter_halo", false);
            this.renderer.northOn = this.prefs.getBoolean("north_marker", true);
            this.renderer.festivalOn = this.prefs.getBoolean("navasard_festival", false);
            this.renderer.mapTouchOn = this.prefs.getBoolean("map_touch", true);
            this.renderer.compassOn = this.prefs.getBoolean("ararat_compass", false);
            if (!this.renderer.compassOn) this.renderer.compassReady = false;
            this.renderer.compassStatus = this.rotationSensor == null ? "датчик недоступен" : "обновите местоположение";
            if (this.visibleNow && this.renderer.compassOn && this.rotationSensor != null) this.sensorManager.registerListener(this, this.rotationSensor, SensorManager.SENSOR_DELAY_UI);
            this.renderer.perspectiveOn = this.effectsEnabled && this.prefs.getBoolean("perspective", true);
            this.renderer.celestialOn = this.effectsEnabled && this.prefs.getBoolean("celestial", true);
            this.renderer.batteryOn = this.prefs.getBoolean("battery", true);
            updateBattery();
            this.gestures = this.prefs.getBoolean("gestures", true);
            this.unlockFlash = this.prefs.getBoolean("unlock", true);
            this.dayCycle = this.prefs.getBoolean("daycycle", true);
            this.nightGold = this.prefs.getBoolean("night_gold", false);
            if (!this.gestures) {
                this.holdReveal = false;
                this.spinVel = 0.0f;
            }
            this.lastPaletteT = -100.0f;
        }

        private void updateBattery() {
            Intent battery = ArasceWallpaperService.this.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            if (battery != null) {
                int level = battery.getIntExtra("level", -1);
                int scale = battery.getIntExtra("scale", -1);
                this.renderer.batteryPercent = level >= 0 && scale > 0 ? Math.max(0, Math.min(100, level * 100 / scale)) : -1;
            }
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public void onVisibilityChanged(boolean z) {
            this.visibleNow = z;
            if (z) {
                if (this.sensor != null) {
                    this.sensorManager.registerListener(this, this.sensor, 1);
                }
                if (this.renderer.compassOn && this.rotationSensor != null) this.sensorManager.registerListener(this, this.rotationSensor, SensorManager.SENSOR_DELAY_UI);
                this.baseSet = false;
                this.lastPaletteT = -100.0f;
                this.lastFrameT = -1.0f;
                this.choreographer.removeFrameCallback(this.frame);
                this.choreographer.postFrameCallback(this.frame);
                return;
            }
            stop();
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public void onSurfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
            super.onSurfaceChanged(surfaceHolder, i, i2, i3);
            this.renderer.setSize(i2, i3);
            if (Build.VERSION.SDK_INT >= 30 && surfaceHolder.getSurface().isValid()) {
                try { surfaceHolder.getSurface().setFrameRate(120f, Surface.FRAME_RATE_COMPATIBILITY_DEFAULT); }
                catch (IllegalArgumentException | IllegalStateException ignored) {}
            }
            if (this.visibleNow) {
                this.choreographer.removeFrameCallback(this.frame);
                this.choreographer.postFrameCallback(this.frame);
            }
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public void onSurfaceDestroyed(SurfaceHolder surfaceHolder) {
            this.visibleNow = false;
            stop();
            super.onSurfaceDestroyed(surfaceHolder);
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public void onDestroy() {
            this.visibleNow = false;
            stop();
            this.prefs.unregisterOnSharedPreferenceChangeListener(this);
            if (this.receiverRegistered) {
                try {
                    ArasceWallpaperService.this.unregisterReceiver(this.unlockReceiver);
                } catch (Exception e) {
                }
                this.receiverRegistered = false;
            }
            super.onDestroy();
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public void onTouchEvent(MotionEvent motionEvent) {
            float fNow = now();
            switch (motionEvent.getActionMasked()) {
                case 0:
                    this.downX = motionEvent.getX();
                    this.downY = motionEvent.getY();
                    this.lastAngle = angleAt(this.downX, this.downY);
                    this.lastMoveT = fNow;
                    this.dragging = false;
                    break;
                case 1:
                    if (!this.dragging) {
                        handleTap(motionEvent.getX(), motionEvent.getY());
                    }
                    this.dragging = false;
                    break;
                case 2:
                    if (this.gestures) {
                        float x = motionEvent.getX() - this.downX;
                        float y = motionEvent.getY() - this.downY;
                        if (!this.dragging && (x * x) + (y * y) > 900.0f) {
                            this.dragging = true;
                        }
                        if (this.dragging) {
                            float fAngleAt = angleAt(motionEvent.getX(), motionEvent.getY());
                            float f = fAngleAt - this.lastAngle;
                            if (f > 180.0f) {
                                f -= 360.0f;
                            }
                            if (f < -180.0f) {
                                f += 360.0f;
                            }
                            this.spin += f;
                            this.spinVel = clamp(f / Math.max(0.008f, fNow - this.lastMoveT), -720.0f, 720.0f);
                            this.lastAngle = fAngleAt;
                            this.lastMoveT = fNow;
                            break;
                        }
                    }
                    break;
                case 3:
                    this.dragging = false;
                    break;
            }
            super.onTouchEvent(motionEvent);
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public Bundle onCommand(String str, int i, int i2, int i3, Bundle bundle, boolean z) {
            if ("android.wallpaper.tap".equals(str)) {
                handleTap(i, i2);
            }
            return super.onCommand(str, i, i2, i3, bundle, z);
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public void onOffsetsChanged(float f, float f2, float f3, float f4, int i, int i2) {
            this.pageSpin = this.gestures ? 120.0f * f : 0.0f;
        }

        private void handleTap(float f, float f2) {
            float fNow = now();
            float f3 = f - this.lastTapX;
            float f4 = f2 - this.lastTapY;
            boolean z = (f3 * f3) + (f4 * f4) < 14400.0f;
            if (fNow - this.lastTapT >= 0.08f || !z) {
                boolean z2 = this.gestures && fNow - this.lastTapT < 0.35f && z;
                this.lastTapT = fNow;
                this.lastTapX = f;
                this.lastTapY = f2;
                this.touchAt = fNow;
                if (this.gestures) {
                    if (!z2 && this.holdReveal && this.reveal > 0.97f && this.renderer.mapOn)
                        this.renderer.selectMapAt(f, f2, this.tiltX, this.tiltY, fNow);
                    if (!z2 && this.ripplesEnabled) this.renderer.addRipple(f, f2, fNow);
                    if (z2 && this.renderer.mapOn) {
                        this.holdReveal = !this.holdReveal;
                        this.flare = this.effectsEnabled ? 0.25f : 0f;
                        this.lastTapT = -100.0f;
                    }
                }
            }
        }

        private float angleAt(float f, float f2) {
            return (float) Math.toDegrees(Math.atan2(f2 - this.renderer.centerY(), f - this.renderer.centerX()));
        }

        @Override // android.hardware.SensorEventListener
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() != Sensor.TYPE_ROTATION_VECTOR) {
                this.gx = event.values[0]; this.gy = event.values[1]; this.hasReading = true; return;
            }
            if (!this.renderer.compassOn) return;
            long at = this.prefs.getLong("ararat_location_time", 0L);
            if (at <= 0 || System.currentTimeMillis() - at > 86400000L) {
                this.renderer.compassReady = false; return;
            }
            float lat = this.prefs.getFloat("ararat_lat", 0f), lon = this.prefs.getFloat("ararat_lon", 0f);
            float[] result = new float[3];
            Location.distanceBetween(lat, lon, 39.70194, 44.29833, result);
            SensorManager.getRotationMatrixFromVector(this.rotationMatrix, event.values);
            int rotation = ((WindowManager) ArasceWallpaperService.this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
            int axisX = SensorManager.AXIS_X, axisY = SensorManager.AXIS_Y;
            if (rotation == Surface.ROTATION_90) { axisX = SensorManager.AXIS_Y; axisY = SensorManager.AXIS_MINUS_X; }
            else if (rotation == Surface.ROTATION_180) { axisX = SensorManager.AXIS_MINUS_X; axisY = SensorManager.AXIS_MINUS_Y; }
            else if (rotation == Surface.ROTATION_270) { axisX = SensorManager.AXIS_MINUS_Y; axisY = SensorManager.AXIS_X; }
            if (!SensorManager.remapCoordinateSystem(this.rotationMatrix, axisX, axisY, this.remappedMatrix)) {
                this.renderer.compassReady = false; return;
            }
            SensorManager.getOrientation(this.remappedMatrix, this.orientation);
            GeomagneticField field = new GeomagneticField(lat, lon, 0f, System.currentTimeMillis());
            float trueHeading = (float)Math.toDegrees(this.orientation[0]) + field.getDeclination();
            this.renderer.compassAngle = result[1] - trueHeading;
            this.renderer.northAngle = -trueHeading;
            this.renderer.compassReady = true;
        }

        @Override // android.hardware.SensorEventListener
        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        private void stop() {
            this.choreographer.removeFrameCallback(this.frame);
            this.sensorManager.unregisterListener(this);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public float now() {
            return (SystemClock.elapsedRealtime() - this.t0) / 1000.0f;
        }

        private float clamp(float f, float f2, float f3) {
            return f < f2 ? f2 : f > f3 ? f3 : f;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void drawFrame() {
            long wall = SystemClock.elapsedRealtime();
            android.os.PowerManager power = (android.os.PowerManager) ArasceWallpaperService.this.getSystemService(Context.POWER_SERVICE);
            int hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY);
            this.renderer.quietActive = this.effectsEnabled && this.prefs.getBoolean("quiet_night", false) && (hour >= 22 || hour < 7);
            boolean economy = this.renderer.quietActive || (this.autoEnergy && ((power != null && power.isPowerSaveMode()) || (this.renderer.batteryPercent >= 0 && this.renderer.batteryPercent <= 20)));
            if (economy && wall - this.lastEnergyFrame < 33) {
                if (this.visibleNow) this.choreographer.postFrameCallback(this.frame);
                return;
            }
            this.lastEnergyFrame = wall;
            float fNow = now();
            float fMin = this.lastFrameT < 0.0f ? 0.008f : Math.max(0f, Math.min(0.1f, fNow - this.lastFrameT));
            float smoothTilt = 1f - (float)Math.pow(1f - 0.12f, fMin / 0.033f);
            float smoothBase = 1f - (float)Math.pow(1f - 0.005f, fMin / 0.033f);
            float smoothPage = 1f - (float)Math.pow(1f - 0.15f, fMin / 0.033f);
            this.lastFrameT = fNow;
            if (this.hasReading) {
                if (!this.baseSet) {
                    this.baseX = this.gx;
                    this.baseY = this.gy;
                    this.baseSet = true;
                }
                this.baseX += (this.gx - this.baseX) * smoothBase;
                this.baseY += (this.gy - this.baseY) * smoothBase;
                float fClamp = clamp((this.gx - this.baseX) / 3.5f, -1.0f, 1.0f);
                float fClamp2 = clamp((-(this.gy - this.baseY)) / 3.5f, -1.0f, 1.0f);
                this.tiltX = ((fClamp - this.tiltX) * smoothTilt) + this.tiltX;
                this.tiltY += (fClamp2 - this.tiltY) * smoothTilt;
            }
            if (!this.dragging) {
                this.spin += this.spinVel * fMin;
                this.spinVel *= (float) Math.exp((-fMin) * 1.6f);
                if (Math.abs(this.spinVel) < 0.5f) {
                    this.spinVel = 0.0f;
                }
            }
            this.pageSpinShown += (this.pageSpin - this.pageSpinShown) * smoothPage;
            this.renderer.spin = this.spin + this.pageSpinShown;
            float f = this.holdReveal ? 1.0f : 0.0f;
            this.reveal = (smoothTilt * (f - this.reveal)) + this.reveal;
            this.flare = ((float) Math.exp((-fMin) * 1.3f)) * this.flare;
            this.renderer.flare = this.effectsEnabled ? this.flare : 0f;
            if (fNow - this.lastPaletteT > 20.0f) {
                this.renderer.setPalette(this.dayCycle ? Palette.forNow() : (this.nightGold ? new Palette(Palette.DAY.light, Palette.DAY.mid, Palette.DAY.dark, Palette.DAY.lit, Palette.DAY.dot, Palette.NIGHT.bg) : Palette.DAY));
                this.renderer.updateCalendar();
                updateBattery();
                this.lastPaletteT = fNow;
            }
            SurfaceHolder surfaceHolder = getSurfaceHolder();
            Canvas canvasLockHardwareCanvas = null;
            try {
                canvasLockHardwareCanvas = surfaceHolder.lockHardwareCanvas();
                if (canvasLockHardwareCanvas != null) {
                    float age = fNow - this.touchAt;
                    float inscription = age >= 0f && age < 2.6f ? Math.min(1f, age / 0.35f) * Math.min(1f, (2.6f - age) / 1.1f) : 0f;
                    float flip = this.reveal * this.reveal * (3f - 2f * this.reveal);
                    this.renderer.draw(canvasLockHardwareCanvas, fNow, this.tiltX, this.tiltY, flip, this.effectsEnabled ? inscription : 0f);
                }
                if (canvasLockHardwareCanvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvasLockHardwareCanvas);
                    } catch (Exception e) {
                    }
                }
            } catch (Exception e2) {
                if (canvasLockHardwareCanvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvasLockHardwareCanvas);
                    } catch (Exception e3) {
                    }
                }
            } catch (Throwable th) {
                if (canvasLockHardwareCanvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvasLockHardwareCanvas);
                    } catch (Exception e4) {
                    }
                }
                throw th;
            }
            this.choreographer.removeFrameCallback(this.frame);
            if (this.visibleNow) {
                this.choreographer.postFrameCallback(this.frame);
            }
        }
    }
}
