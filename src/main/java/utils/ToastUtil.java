package utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

public class ToastUtil {

    public static void makeLongToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void makeShortToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void makeToast(Context context, String msg, int duration) {
        Toast.makeText(context, msg, duration).show();
    }

    public static void makeToast(Context context, int resId, int duration) {
        Toast.makeText(context, resId, duration).show();
    }

    public static void makeLongSnackbar(View container, String msg) {
        Snackbar.make(container, msg, Snackbar.LENGTH_LONG).show();
    }


    public static void makeShortSnackbar(View container, String msg) {
        Snackbar.make(container, msg, Snackbar.LENGTH_SHORT).setActionTextColor(Color.WHITE).show();
    }


    public static void makeToast(View container, String msg, int duration) {
        Snackbar.make(container, msg, Snackbar.LENGTH_SHORT).show();
    }

}
