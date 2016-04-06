package uicontrols.datepicker.views;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import uicontrols.datepicker.entities.Language;

/**
 * 日期选择器的标题视图
 *
 *  2015-05-21
 */
public class WeekView extends LinearLayout {
    private String[] weekTitles;
    private TextView tvYear;
    private int colorFont=0xffc9ae50;

    public WeekView(Context context) {
        super(context);
        setColor(Color.TRANSPARENT);
        setOrientation(HORIZONTAL);
        weekTitles = Language.getLanguage(context).weekTitles(0);
        LayoutParams llParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
        for (int i = 0; i < 7; i++) {
            tvYear = new TextView(context);
            tvYear.setGravity(Gravity.CENTER);
            tvYear.setTextColor(Color.WHITE);
            tvYear.setText(weekTitles[i]);
            tvYear.setPadding(0, 20, 0, 20);
            if (i==0||i==6)
                tvYear.setTextColor(colorFont);
            else
                tvYear.setTextColor(Color.BLACK);
            addView(tvYear, llParams);
        }
    }

    public void setColor(int color) {
        setBackgroundColor(color);
    }


}
