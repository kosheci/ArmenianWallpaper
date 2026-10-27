package am.arasce.wallpaper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
        private boolean baseSet;
        private float baseX;
        private float baseY;
        private boolean dayCycle;
        private float downX;
        private float downY;
        private boolean dragging;
        private float flare;
        private final Runnable frame;
        private boolean gestures;
        private volatile float gx;
        private volatile float gy;
        private final Handler handler;
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
            this.handler = new Handler(Looper.getMainLooper());
            this.frame = new Runnable() { // from class: am.arasce.wallpaper.ArasceWallpaperService.ArasceEngine.1
                @Override // java.lang.Runnable
                public void run() {
                    ArasceEngine.this.drawFrame();
                }
            };
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
                    if (ArasceEngine.this.unlockFlash) {
                        float fNow = ArasceEngine.this.now();
                        ArasceEngine.this.flare = 1.0f;
                        ArasceEngine.this.touchAt = fNow;
                        ArasceEngine.this.renderer.addRipple(ArasceEngine.this.renderer.centerX(), ArasceEngine.this.renderer.centerY(), fNow);
                        ArasceEngine.this.renderer.addRipple(ArasceEngine.this.renderer.centerX(), ArasceEngine.this.renderer.centerY(), fNow + 0.25f);
                    }
                }
            };
            this.sensorManager = (SensorManager) ArasceWallpaperService.this.getSystemService("sensor");
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
            this.renderer.parallaxMul = this.prefs.getInt("parallax", 70) / 100.0f;
            this.renderer.speedMul = this.prefs.getInt("speed", 65) / 100.0f;
            this.renderer.ambientBase = this.prefs.getInt("glow", 18) / 100.0f;
            this.renderer.moteCount = this.prefs.getInt("motes", 40);
            this.renderer.sheenOn = this.prefs.getBoolean("sheen", true);
            this.renderer.raysOn = this.prefs.getBoolean("rays", false);
            this.renderer.nebulaOn = this.prefs.getBoolean("nebula", true);
            this.renderer.mapOn = this.prefs.getBoolean("map", true);
            this.renderer.perspectiveOn = this.prefs.getBoolean("perspective", true);
            this.renderer.celestialOn = this.prefs.getBoolean("celestial", true);
            this.renderer.batteryOn = this.prefs.getBoolean("battery", true);
            updateBattery();
            this.gestures = this.prefs.getBoolean("gestures", true);
            this.unlockFlash = this.prefs.getBoolean("unlock", true);
            this.dayCycle = this.prefs.getBoolean("daycycle", true);
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
                this.baseSet = false;
                this.lastPaletteT = -100.0f;
                this.lastFrameT = -1.0f;
                this.handler.removeCallbacks(this.frame);
                this.handler.post(this.frame);
                return;
            }
            stop();
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public void onSurfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
            super.onSurfaceChanged(surfaceHolder, i, i2, i3);
            this.renderer.setSize(i2, i3);
            if (this.visibleNow) {
                this.handler.removeCallbacks(this.frame);
                this.handler.post(this.frame);
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
                    this.renderer.addRipple(f, f2, fNow);
                    if (z2 && this.renderer.mapOn) {
                        this.holdReveal = !this.holdReveal;
                        this.flare = 1.0f;
                        this.lastTapT = -100.0f;
                    }
                }
            }
        }

        private float angleAt(float f, float f2) {
            return (float) Math.toDegrees(Math.atan2(f2 - this.renderer.centerY(), f - this.renderer.centerX()));
        }

        @Override // android.hardware.SensorEventListener
        public void onSensorChanged(SensorEvent sensorEvent) {
            this.gx = sensorEvent.values[0];
            this.gy = sensorEvent.values[1];
            this.hasReading = true;
        }

        @Override // android.hardware.SensorEventListener
        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        private void stop() {
            this.handler.removeCallbacks(this.frame);
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
            float fNow = now();
            float fMin = this.lastFrameT < 0.0f ? 0.016f : Math.min(0.1f, fNow - this.lastFrameT);
            this.lastFrameT = fNow;
            if (this.hasReading) {
                if (!this.baseSet) {
                    this.baseX = this.gx;
                    this.baseY = this.gy;
                    this.baseSet = true;
                }
                this.baseX += (this.gx - this.baseX) * 0.005f;
                this.baseY += (this.gy - this.baseY) * 0.005f;
                float fClamp = clamp((this.gx - this.baseX) / 3.5f, -1.0f, 1.0f);
                float fClamp2 = clamp((-(this.gy - this.baseY)) / 3.5f, -1.0f, 1.0f);
                this.tiltX = ((fClamp - this.tiltX) * 0.12f) + this.tiltX;
                this.tiltY += (fClamp2 - this.tiltY) * 0.12f;
            }
            if (!this.dragging) {
                this.spin += this.spinVel * fMin;
                this.spinVel *= (float) Math.exp((-fMin) * 1.6f);
                if (Math.abs(this.spinVel) < 0.5f) {
                    this.spinVel = 0.0f;
                }
            }
            this.pageSpinShown += (this.pageSpin - this.pageSpinShown) * 0.15f;
            this.renderer.spin = this.spin + this.pageSpinShown;
            float f = this.holdReveal ? 1.0f : 0.0f;
            this.reveal = (0.12f * (f - this.reveal)) + this.reveal;
            this.flare = ((float) Math.exp((-fMin) * 1.3f)) * this.flare;
            this.renderer.flare = this.flare;
            if (fNow - this.lastPaletteT > 20.0f) {
                this.renderer.setPalette(this.dayCycle ? Palette.forNow() : Palette.DAY);
                if (this.renderer.batteryOn) updateBattery();
                this.lastPaletteT = fNow;
            }
            SurfaceHolder surfaceHolder = getSurfaceHolder();
            Canvas canvasLockHardwareCanvas = null;
            try {
                canvasLockHardwareCanvas = surfaceHolder.lockHardwareCanvas();
                if (canvasLockHardwareCanvas != null) {
                    float age = fNow - this.touchAt;
                    float inscription = age >= 0f && age < 2.6f ? Math.min(1f, age / 0.35f) * Math.min(1f, (2.6f - age) / 1.1f) : 0f;
                    this.renderer.draw(canvasLockHardwareCanvas, fNow, this.tiltX, this.tiltY, this.reveal, inscription);
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
            this.handler.removeCallbacks(this.frame);
            if (this.visibleNow) {
                this.handler.postDelayed(this.frame, 33L);
            }
        }
    }
}
