package utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

    public static void MakeLongToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void MakeLongToast(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
    }

    public static void MakeShortToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void MakeShortToast(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    public static void MakeToast(Context context, String msg, int duration) {
        Toast.makeText(context, msg, duration).show();
    }

    public static void MakeToast(Context context, int resId, int duration) {
        Toast.makeText(context, resId, duration).show();
    }
}
