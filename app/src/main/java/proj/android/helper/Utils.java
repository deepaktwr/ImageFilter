package proj.android.helper;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import proj.android.gl_helper.GlSurfaceView;

/**
 * Created by deepak on 24/5/15.
 */
public class Utils {
    private static final String TAG = "FILTER";
    private static boolean isProgramReady = false;
    public static void logMessage(String message){
        Log.i(TAG, message);
    }
    public static void logError(String message){
        Log.e(TAG, message);
    }
    public static void showToast(Context context,String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    public float c(float val){
        return val/100f;
    }
    public static String formatMessage(String message, Object ... values){
        return String.format(message, values);
    }

    public static boolean isProgramReady() {
        return isProgramReady;
    }

    public static void setIsProgramReady(boolean isProgramReady) {
        Utils.isProgramReady = isProgramReady;
    }

    private static GlSurfaceView glSurfaceView;
    public static void setSurfaceViewObj(GlSurfaceView glSurfaceView1){
        glSurfaceView=glSurfaceView1;
    }
    public static GlSurfaceView getSurfaceView(){
        return glSurfaceView;
    }
}
