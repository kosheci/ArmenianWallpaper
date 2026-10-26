package com.example.armenianwallpaper
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.math.*
object ArmenianCore {
 const val ALPHABET = "ԱԲԳԴԵԶԷԸԹԺԻԼԽԾԿՀՁՂՃՄՅՆՇՈՉՊՋՌՍՎՏՐՑՒՓՔ"
 private val u=arrayOf("","Ա","Բ","Գ","Դ","Ե","Զ","Է","Ը","Թ")
 private val t=arrayOf("","Ժ","Ի","Լ","Խ","Ծ","Կ","Հ","Ձ","Ղ")
 private val h=arrayOf("","Ճ","Մ","Յ","Ն","Շ","Ո","Չ","Պ","Ջ")
 private val k=arrayOf("","Ռ","Ս","Վ","Տ","Ր","Ց","Ւ","Փ","Ք")
 fun numeral(n:Int):String=if(n !in 1..9999)n.toString() else k[n/1000]+h[n%1000/100]+t[n%100/10]+u[n%10]
 fun calendar(d:LocalDate=LocalDate.now()):String="ԹՎ "+numeral(d.year-551)+" • "+numeral(d.dayOfMonth)
 fun moon(d:LocalDate=LocalDate.now()):Double {val p=(ChronoUnit.DAYS.between(LocalDate.of(2000,1,6),d)/29.530588853)%1.0;return if(p<0)p+1 else p}
 fun bearing(lat:Double,lon:Double):Float {val a=Math.toRadians(lat);val b=Math.toRadians(39.7024);val dl=Math.toRadians(44.2991-lon);return ((Math.toDegrees(atan2(sin(dl)*cos(b),cos(a)*sin(b)-sin(a)*cos(b)*cos(dl)))+360)%360).toFloat()}
 fun quote(d:LocalDate=LocalDate.now()):String=listOf("ՃԱՆԱՉԵԼ ԶԻՄԱՍՏՈՒԹԻՒՆ ԵՒ ԶԽՐԱՏ","ՄԱՀ ԻՄԱՑԵԱԼ՝ ԱՆՄԱՀՈՒԹԻՒՆ","ԼՈՅՍ Ի ԽԱՒԱՐԻ ԼՈՒՍԱՒՈՐԻ")[d.dayOfYear%3]
}