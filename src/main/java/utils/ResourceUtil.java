package utils;

import android.content.Context;
import android.os.Build;

/**
 * Created by Hugh on 2015/12/21.
 */
public class ResourceUtil {

    public static int getColor(Context context, int resId) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
           return context.getResources().getColor(resId);
        else
           return context.getResources().getColor(resId, null);
    }
}
