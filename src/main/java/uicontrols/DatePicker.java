package uicontrols;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.TimePicker;
import java.util.Calendar;
import java.util.Date;

import utils.DateUtil;

public class DatePicker {

    private SelectType mSelectType;
    private Context mContext;
    private boolean isPicked;
    private Date mInitialDate;

    public enum SelectType {Date, Time, DateAndTime}

    public DatePicker setSelectType(SelectType selectType) {
        this.mSelectType = selectType;
        return this;
    }

    private DatePicker() {
    }

    private DatePicker(Context context) {
        this.mContext = context;
    }

    public DatePicker setInitialDate(Date initialDate) {
        this.mInitialDate = initialDate;
        return this;
    }

    public static DatePicker build(Context context) {
        return  new DatePicker(context);
    }


    public void show(final SlideDateTimeListener slideDateTimeListener) {
        isPicked = false;
        Calendar calendar = Calendar.getInstance();
        if (mInitialDate != null) calendar.setTime(mInitialDate);
        final int yy = calendar.get(Calendar.YEAR);
        final int mm = calendar.get(Calendar.MONTH);
        final int dd = calendar.get(Calendar.DATE);
        final int hh = calendar.get(Calendar.HOUR);
        final int m = calendar.get(Calendar.MINUTE);
        if (mSelectType == SelectType.Time) {
            new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    if (slideDateTimeListener != null)
                        slideDateTimeListener.onDateTimeSet(DateUtil.StringToDate(String.format(yy + "-" + mm + "-" + dd + " %s:%s:00", hourOfDay, minute)));
                }
            }, hh, m, true).show();
            return;
        }
        new DatePickerDialog(mContext,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(android.widget.DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        final String dateTime = String.format("%d-%d-%d", year,
                                monthOfYear + 1, dayOfMonth);
                        if (mSelectType == SelectType.Date) {
                            slideDateTimeListener.onDateTimeSet(DateUtil.StringToDate(dateTime + " 00:00:00"));
                            return;
                        }
                        if (!isPicked) {
                            isPicked = true;
                            new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    slideDateTimeListener.onDateTimeSet(DateUtil.StringToDate(String.format(dateTime + " %s:%s:00", hourOfDay, minute)));
                                }
                            }, hh, m, true).show();
                        } else
                            isPicked = false;
                    }
                }, yy, mm, dd).show();
    }

    public interface SlideDateTimeListener {
        void onDateTimeSet(Date date);

        void onDateTimeCancel();
    }

}
