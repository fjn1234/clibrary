package compatbility;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;

public class DrawableRevise {

    public static Drawable getDrawable(Resources resources, int id, Resources.Theme theme) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {    //API 1
            //noinspection deprecation
            return resources.getDrawable(id);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//API 22
            return resources.getDrawable(id, theme);
        }
        return null;
    }

}
