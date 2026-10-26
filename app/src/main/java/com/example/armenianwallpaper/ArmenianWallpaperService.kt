package com.example.armenianwallpaper
import android.service.wallpaper.WallpaperService
import android.graphics.*
import android.os.Handler
import android.os.Looper
import java.time.LocalDate
import kotlin.math.*
class ArmenianWallpaperService:WallpaperService(){
 override fun onCreateEngine():Engine=ArtEngine()
 inner class ArtEngine:Engine(){
  private val handler=Handler(Looper.getMainLooper())
  private val prefs=getSharedPreferences("wallpaper",MODE_PRIVATE)
  private var visible=false
  private var rotation=0f
  private val paint=Paint(3)
  private val tick=object:Runnable{override fun run(){render();if(visible)handler.postDelayed(this,40)}}
  override fun onVisibilityChanged(v:Boolean){visible=v;handler.removeCallbacks(tick);if(v)handler.post(tick)}
  override fun onSurfaceDestroyed(holder:android.view.SurfaceHolder){visible=false;handler.removeCallbacks(tick);super.onSurfaceDestroyed(holder)}
  override fun onDestroy(){handler.removeCallbacks(tick);super.onDestroy()}
  private fun render(){
   var canvas:Canvas?=null
   try{
    canvas=surfaceHolder.lockCanvas()?:return
    val c=canvas
    val w=c.width.toFloat();val h=c.height.toFloat();val r=min(w,h)*.32f
    val date=LocalDate.now()
    val special=prefs.getBoolean("special",true)
    val dark=special&&date.monthValue==4&&date.dayOfMonth==24
    c.drawColor(if(dark)Color.rgb(16,16,22) else Color.rgb(9,23,38))
    val gold=if(dark)Color.rgb(175,155,145) else Color.rgb(238,185,100)
    paint.color=gold;paint.style=Paint.Style.STROKE;paint.strokeWidth=3f
    c.save();c.translate(w/2,h/2)
    c.drawCircle(0f,0f,r,paint);c.drawCircle(0f,0f,r*.73f,paint)
    if(prefs.getBoolean("animate",true))rotation+=.08f+prefs.getInt("speed",30)*.012f
    c.save();c.rotate(rotation)
    paint.strokeWidth=11f;paint.strokeCap=Paint.Cap.ROUND
    for(i in 0..7){c.save();c.rotate(i*45f);val path=Path();path.moveTo(0f,-r*.18f);path.lineTo(0f,-r*.49f);path.quadTo(r*.17f,-r*.65f,r*.29f,-r*.48f);c.drawPath(path,paint);c.restore()}
    c.restore()
    paint.style=Paint.Style.FILL;paint.textAlign=Paint.Align.CENTER;paint.typeface=Typeface.create("serif",Typeface.BOLD)
    paint.textSize=r*.11f
    val alphabet=ArmenianCore.ALPHABET
    for(i in alphabet.indices){c.save();c.rotate(i*360f/alphabet.length);c.drawText(alphabet[i].toString(),0f,-r*.86f,paint);c.restore()}
    c.restore()
    paint.color=gold;paint.textAlign=Paint.Align.CENTER;paint.typeface=Typeface.create("serif",Typeface.NORMAL)
    paint.textSize=min(w,h)*.037f
    if(prefs.getBoolean("calendar",true))c.drawText(ArmenianCore.calendar(date),w/2,h*.18f,paint)
    if(prefs.getBoolean("moon",true))c.drawText("☾  "+(ArmenianCore.moon(date)*100).toInt()+"%  •  ԼՈՒՍԻՆ",w/2,h*.77f,paint)
    if(prefs.getBoolean("quote",true)){paint.textSize=min(w,h)*.024f;c.drawText(ArmenianCore.quote(date),w/2,h*.86f,paint)}
    if(special&&date.monthValue==8&&date.dayOfMonth==11){paint.textSize=min(w,h)*.035f;c.drawText("ՆԱՎԱՍԱՐԴ",w/2,h*.92f,paint)}
   }catch(_:Exception){}finally{if(canvas!=null)try{surfaceHolder.unlockCanvasAndPost(canvas)}catch(_:Exception){}}
  }
 }
}