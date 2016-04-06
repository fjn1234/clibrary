package uicontrols.datepicker.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import uicontrols.datepicker.interfaces.IPick;
import uicontrols.datepicker.interfaces.OnDateSelected;

public class DatePicker extends LinearLayout implements IPick {
    private MonthView monthView;
    private TitleView titleView;
    private WeekView weekView;
    private RelativeLayout dividerView;

    public DatePicker(Context context) {
        this(context, null);
    }

    public DatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.WHITE);
        setOrientation(VERTICAL);
        LayoutParams llParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        LayoutParams divParams = new LayoutParams(LayoutParams.MATCH_PARENT, 2);
        titleView = new TitleView(context);
        addView(titleView, llParams);
        dividerView =new RelativeLayout(context);
        addView(dividerView,divParams);
        weekView = new WeekView(context);
        addView(weekView, llParams);
        monthView = new MonthView(context);
        monthView.setOnPageChangeListener(titleView);
        monthView.setOnSizeChangedListener(titleView);
        addView(monthView, llParams);
    }

    @Override
    public void setOnDateSelected(OnDateSelected onDateSelected) {
        titleView.setOnDateSelected(onDateSelected, monthView);
    }

    @Override
    public void setColor(int color) {
        monthView.setColorBooking(color);
    }

    public MonthView getMonthView() {
        return monthView;
    }

    public TitleView getTitleView() {
        return titleView;
    }

    public WeekView getWeekView() {
        return weekView;
    }

    public void setDividerColor(int color){
        dividerView.setBackgroundColor(color);
    }

    @Override
    public void isLunarDisplay(boolean display) {
        monthView.setLunarShow(display);
    }
}
