package proj.android.helper;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by deepak on 24/5/15.
 */
public class Utils {
    private static final String TAG = "FILTER";
    public static void logMessage(String message){
        Log.e(TAG, message);
    }
    public static void showToast(Context context,String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
