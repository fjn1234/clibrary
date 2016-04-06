package utils;

import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

/**
 * Created by Administrator on 2015/4/24.
 */
public class AnimUtil {
    public static Animation getLoopRotate(int milli) {
        Animation ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        LinearInterpolator lin = new LinearInterpolator();
        ra.setInterpolator(lin);
        ra.setDuration(milli);
        ra.setFillAfter(true);
        ra.setRepeatCount(-1);
        return ra;
    }

    public static Animation getCenterScaleShow(int milli){
        Animation anim = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(milli);
        return anim;
    }

    public static Animation getCenterScaleHide(int milli){
        Animation anim = new ScaleAnimation(1, 0, 1, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(milli);
        return anim;
    }
}
