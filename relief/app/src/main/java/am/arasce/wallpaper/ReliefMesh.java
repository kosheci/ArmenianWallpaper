package am.arasce.wallpaper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/** Actual closed three-dimensional meshes, projected in software onto the wallpaper canvas. */
final class ReliefMesh {
    private static final class Face {
        final float[] p = new float[12];
        final float light;
        float depth;
        Face(float[] a, float[] b, float[] c, float[] d, float light) {
            System.arraycopy(a,0,p,0,3); System.arraycopy(b,0,p,3,3);
            System.arraycopy(c,0,p,6,3); System.arraycopy(d,0,p,9,3); this.light=light;
        }
    }
    private final ArrayList<Face> faces = new ArrayList<>();
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path path = new Path();
    private final float[] projected = new float[12];
    ReliefMesh() {
        ring(285, 281, 0, 10, 72);
        ring(230, 226, 6, 14, 64);
        ring(201, 198, 6, 14, 64);
        ring(93, 88, 17, 27, 48);
        for (int i=0; i<8; i++) arm(i * Math.PI / 4.0);
        square(130, 0); square(130, Math.PI / 4);
        for (int i=0; i<8; i++) {
            double a=i*Math.PI/4.0; sphere(182*Math.cos(a),182*Math.sin(a),20,5.5);
        }
    }
    private static float[] v(double x,double y,double z) { return new float[]{(float)x,(float)y,(float)z}; }
    private void add(float[] a,float[] b,float[] c,float[] d,float light) { faces.add(new Face(a,b,c,d,light)); }
    private void ring(float outer,float inner,float back,float front,int steps) {
        for (int i=0;i<steps;i++) {
            double a=i*2*Math.PI/steps, b=(i+1)*2*Math.PI/steps;
            float[] ofa=v(outer*Math.cos(a),outer*Math.sin(a),front), ofb=v(outer*Math.cos(b),outer*Math.sin(b),front);
            float[] ifa=v(inner*Math.cos(a),inner*Math.sin(a),front), ifb=v(inner*Math.cos(b),inner*Math.sin(b),front);
            float[] oba=v(outer*Math.cos(a),outer*Math.sin(a),back), obb=v(outer*Math.cos(b),outer*Math.sin(b),back);
            float[] iba=v(inner*Math.cos(a),inner*Math.sin(a),back), ibb=v(inner*Math.cos(b),inner*Math.sin(b),back);
            add(ofa,ofb,ifb,ifa,1f); add(oba,obb,ofb,ofa,(float)(0.55+0.20*Math.cos(a-0.7)));
            add(iba,ifa,ifb,ibb,(float)(0.42+0.12*Math.cos(a))); add(oba,iba,ibb,obb,0.30f);
        }
    }
    private void square(double r, double rotation) {
        for (int i=0;i<4;i++) {
            double a=rotation+i*Math.PI/2, b=rotation+(i+1)*Math.PI/2;
            // The rotated square uses four short extruded rectangular bars.
            double x1=r*(Math.cos(a)-Math.sin(a)), y1=r*(Math.sin(a)+Math.cos(a));
            double x2=r*(Math.cos(b)-Math.sin(b)), y2=r*(Math.sin(b)+Math.cos(b));
            bar(x1,y1,x2,y2,2.1,10,17);
        }
    }
    private void bar(double x1,double y1,double x2,double y2,double width,double back,double front) {
        double len=Math.hypot(x2-x1,y2-y1),nx=-(y2-y1)/len*width,ny=(x2-x1)/len*width;
        float[] a=v(x1+nx,y1+ny,back),b=v(x2+nx,y2+ny,back),c=v(x2-nx,y2-ny,back),d=v(x1-nx,y1-ny,back);
        float[] e=v(x1+nx,y1+ny,front),f=v(x2+nx,y2+ny,front),g=v(x2-nx,y2-ny,front),h=v(x1-nx,y1-ny,front);
        add(e,f,g,h,1f);add(a,b,f,e,0.5f);add(d,h,g,c,0.7f);add(a,e,h,d,0.55f);add(b,c,g,f,0.6f);
    }
    private void sphere(double x,double y,double z,double r) {
        for(int i=0;i<8;i++) for(int j=0;j<5;j++) {
            double a=i*Math.PI/4,b=(i+1)*Math.PI/4,t=j*Math.PI/5,u=(j+1)*Math.PI/5;
            add(spherePoint(x,y,z,r,a,t),spherePoint(x,y,z,r,b,t),spherePoint(x,y,z,r,b,u),spherePoint(x,y,z,r,a,u),(float)(0.48+0.45*Math.sin((t+u)/2)));
        }
    }
    private float[] spherePoint(double x,double y,double z,double r,double a,double t) {
        return v(x+r*Math.sin(t)*Math.cos(a),y+r*Math.sin(t)*Math.sin(a),z+r*Math.cos(t));
    }
    private void arm(double rotation) {
        int n=20;
        for(int i=0;i<n;i++) {
            double t=i/(double)n,u=(i+1)/(double)n;
            float[][] a=armSection(t,rotation), b=armSection(u,rotation);
            add(a[2],b[2],b[3],a[3],0.97f);
            add(a[0],b[0],b[2],a[2],0.55f);
            add(a[1],a[3],b[3],b[1],0.72f);
            add(a[0],a[1],b[1],b[0],0.30f);
        }
    }
    private float[][] armSection(double t,double r) {
        double radius=7+70*t, angle=r+0.92*t;
        double x=radius*Math.sin(angle),y=-radius*Math.cos(angle);
        double half=(2.0+5.3*Math.sin(Math.PI*t))*0.5;
        double nx=Math.cos(angle),ny=Math.sin(angle);
        return new float[][] { v(x+nx*half,y+ny*half,20),v(x-nx*half,y-ny*half,20),v(x+nx*half,y+ny*half,31),v(x-nx*half,y-ny*half,31) };
    }
    void draw(Canvas c,float tiltX,float tiltY,int baseColor) {
        final double ry=-tiltX*0.24, rx=tiltY*0.24;
        final double sy=Math.sin(ry), cy=Math.cos(ry), sx=Math.sin(rx), cx=Math.cos(rx);
        for(Face f:faces) { f.depth=0; for(int i=0;i<4;i++) { float x=f.p[3*i],y=f.p[3*i+1],z=f.p[3*i+2]; f.depth += (float)((-x*sy+z*cy)*cx+y*sx); } }
        Collections.sort(faces, Comparator.comparingDouble(f -> f.depth));
        paint.setStyle(Paint.Style.FILL); paint.setShader(null);
        for(Face f:faces) {
            for(int i=0;i<4;i++) {
                float x=f.p[3*i],y=f.p[3*i+1],z=f.p[3*i+2];
                double xx=x*cy+z*sy, zz=-x*sy+z*cy, yy=y*cx-zz*sx, depth=y*sx+zz*cx;
                double scale=1200.0/(1200.0-depth);
                projected[2*i]=(float)(xx*scale); projected[2*i+1]=(float)(yy*scale);
            }
            float light=Math.max(0.2f,Math.min(1f,f.light));
            paint.setColor(Color.rgb((int)(Color.red(baseColor)*light),(int)(Color.green(baseColor)*light),(int)(Color.blue(baseColor)*light)));
            path.reset();path.moveTo(projected[0],projected[1]);for(int i=1;i<4;i++)path.lineTo(projected[i*2],projected[i*2+1]);path.close();c.drawPath(path,paint);
        }
    }
}
