package obj;

import android.content.Context;
import android.os.Build;
import android.text.TextPaint;
import android.text.style.CharacterStyle;

public class TextSpan extends CharacterStyle {

    private int backgroudColor = 0x00000000, textColor = 0xff000000;
    private float fontSize = 18;
    private boolean fakeBold = false;
    private boolean setBg = false, setColor = false, setSize = false;
    private Context context;

    public TextSpan(Context context) {
        this.context = context;
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        if (setColor)
            tp.setColor(textColor);
        if (setBg)
            tp.bgColor = backgroudColor;
        if (setSize)
            tp.setTextSize(fontSize);
        tp.setFakeBoldText(fakeBold);
    }

    public int getBackgroudColor() {
        return backgroudColor;
    }

    public void setBackgroudColor(int backgroudColor) {
        setBg = true;
        this.backgroudColor = backgroudColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        setColor = true;
        this.textColor = textColor;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        setSize = true;
        this.fontSize = fontSize;
    }

    public boolean isFakeBold() {
        return fakeBold;
    }

    public void setFakeBold(boolean fakeBold) {
        this.fakeBold = fakeBold;
    }

    public void setTextColorResId(int resId) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            setTextColor(context.getResources().getColor(resId));
        else
            setTextColor(context.getResources().getColor(resId, null));
    }
}
