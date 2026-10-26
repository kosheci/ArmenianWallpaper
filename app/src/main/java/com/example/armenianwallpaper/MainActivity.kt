package com.example.armenianwallpaper
import android.app.Activity
import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.widget.*
class MainActivity:Activity(){
 override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState)
 val prefs=getSharedPreferences("wallpaper",MODE_PRIVATE)
 val layout=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setPadding(28,48,28,28);setBackgroundColor(Color.rgb(13,24,39))}
 fun title(s:String){layout.addView(TextView(this).apply{text=s;textSize=23f;setTextColor(Color.rgb(236,189,107));gravity=Gravity.CENTER;setPadding(0,12,0,20)})}
 title("ԱՐԵՒԱԽԱՉ • Живая Печать")
 for(pair in listOf("animate" to "Вращение","moon" to "Фаза Луны","calendar" to "Календарь","quote" to "Цитата дня","special" to "Памятные даты")){val key=pair.first;layout.addView(Switch(this).apply{text=pair.second;setTextColor(Color.WHITE);isChecked=prefs.getBoolean(key,true);setOnCheckedChangeListener{_,v->prefs.edit().putBoolean(key,v).apply()}})}
 title("Скорость вращения")
 layout.addView(SeekBar(this).apply{max=100;progress=prefs.getInt("speed",30);setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{override fun onProgressChanged(s:SeekBar?,p:Int,user:Boolean){if(user)prefs.edit().putInt("speed",p).apply()};override fun onStartTrackingTouch(s:SeekBar?){};override fun onStopTrackingTouch(s:SeekBar?){} })})
 layout.addView(Button(this).apply{text="УСТАНОВИТЬ ЖИВЫЕ ОБОИ";setOnClickListener{startActivity(Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER).putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,ComponentName(this@MainActivity,ArmenianWallpaperService::class.java)))}})
 setContentView(layout)
 }
}