package utils;

import com.hugh.clibrary.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import obj.CApplication;

public class DateUtil {

    private final static String DEFUALT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 两个时间之间相差距离多少天
     *
     * @return 相差天数
     */
    public static long getDistanceDays(String str1, String str2) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date one;
        Date two;
        long days = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            days = getDistanceDays(one, two);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    public static long getDistanceDays(Date date1, Date date2) {
        return (date1.getTime() - date2.getTime()) / (1000 * 60 * 60 * 24);
    }

    /**
     * 两个时间之间相差距离多少小时
     *
     * @return 相差天数
     */
    public static long getDistanceHours(String str1, String str2) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date one;
        Date two;
        long days = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            days = getDistanceHours(one, two);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    public static long getDistanceHours(Date date1, Date date2) {
        return getDistanceHours(date1.getTime(), date2.getTime());
    }

    public static long getDistanceHours(long time1, long time2) {
        return (time1 - time2) / (1000 * 60 * 60);
    }

    /**
     * 两个时间之间相差距离多少分钟
     *
     * @return 相差天数
     */
    public static long getDistanceMinutes(String str1, String str2) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date one;
        Date two;
        long days = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            days = getDistanceDays(one, two);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    public static long getDistanceMinutes(Date date1, Date date2) {
        return (date1.getTime() - date2.getTime()) / (1000 * 60);
    }

    /**
     * 两个时间之间相差距离多少秒
     *
     * @return 相差天数
     */
    public static long getDistanceSecond(String str1, String str2) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date one;
        Date two;
        long days = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            days = getDistanceDays(one, two);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    public static long getDistanceSecond(Date date1, Date date2) {
        return (date1.getTime() - date2.getTime()) / 1000;
    }

    /**
     * 获取所给日期与现在的相差天数
     *
     * @return 相差天数
     */
    public static long getDistanceDaysToNow(Date date) {
        return getDistanceDays(new Date(), date);
    }

    public static long getAgeFromBirthday(Date birthday) {
        if (birthday == null || birthday.after(new Date())) return 0;
        return getDistanceDaysToNow(birthday) / 365;
    }

    /**
     * 两个时间相差距离多少天多少小时多少分多少秒 (经过计算，减去前面的单位)
     *
     * @return long[] 返回值为：{天, 时, 分, 秒}
     */
    public static long[] getDistanceTimes(long time1, long time2) {
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        long diff;
        if (time1 < time2) {
            diff = time2 - time1;
        } else {
            diff = time1 - time2;
        }
        day = diff / (24 * 60 * 60 * 1000);
        long passHour = day * 24;
        hour = diff / (60 * 60 * 1000) - passHour;
        long passMin = (passHour + hour) * 60;
        min = diff / (60 * 1000) - passMin;
        long passSec = (passMin + min) * 60;
        sec = diff / 1000 - passSec;
        long[] times = {day, hour, min, sec};
        return times;
    }

    public static long[] getDistanceTimes(String str1, String str2) {
        DateFormat df = new SimpleDateFormat(DEFUALT_DATE_FORMAT);
        Date one;
        Date two;
        long[] times;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            times = getDistanceTimes(time1, time2);
        } catch (ParseException e) {
            e.printStackTrace();
            times = new long[]{0, 0, 0, 0};
        }
        return times;
    }

    public static long[] getDistanceTimes(Date date1, Date date2) {
        return getDistanceTimes(date1.getTime(), date2.getTime());
    }

    public static String DateToString(String format, Date date) {
        SimpleDateFormat formater = new SimpleDateFormat(format);
        return formater.format(date);
    }

    public static String dateToString(Date date) {
        SimpleDateFormat formater = new SimpleDateFormat(DEFUALT_DATE_FORMAT);
        return formater.format(date);
    }

    //  格式化日期字符串
    public static String formatDate(String format, String date) {
        return formatDate(format,DateUtil.StringToDate(date));
    }

    //  格式化日期字符串
    public static String formatDate(String format, Date date) {
        try {
            DateFormat newFormat = new SimpleDateFormat(format);
            return newFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //  格式化日期字符串
    public static String formatDate(String format, long date) {
        return formatDate(format, DateUtil.convertTimeToDate(date));
    }


    // 按指定格式把String转换为Date
    public static Date StringToDate(String format, String date) {
        try {
            DateFormat newFormat = new SimpleDateFormat(format);
            return newFormat.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 按指定格式把String转换为Date
    public static Date StringToDate(String date) {
        try {
            if (date.length() == 19) {
                SimpleDateFormat format = new SimpleDateFormat(DEFUALT_DATE_FORMAT);
                return format.parse(date);
            } else {
                return stringTryToDate(date);
            }
        } catch (ParseException e) {
            return stringTryToDate(date);
        }
    }

    public static Date stringTryToDate(String date) {
        try {
            String format;
            date = date.trim();
            int length = date.length();
            switch (length) {
                case 14:
                case 12:
                case 10:
                case 8:
                    long v = StringUtil.stringToLong(date, 0);
                    if (v > 0) {
                        StringBuilder builder = new StringBuilder(date);
                        switch (length) {
                            case 14:
                                builder.insert(12, ":");
                            case 12:
                                builder.insert(10, ":");
                            case 10:
                                builder.insert(8, " ");
                                builder.insert(6, "-");
                                builder.insert(4, "-");
                                date = builder.toString();
                                length = date.length();
                                System.out.println(date);
                                break;
                        }
                    }
                    break;
            }
            if (length > 19)
                date = date.substring(0, 19);
            format = DEFUALT_DATE_FORMAT;
            if (length < 11) {
                date += " 00:00:00";
            } else if (length < 14) {
                date += ":00:00";
            } else if (length < 17) {
                date += ":00";
            }
            if (date.indexOf("-") > 0) {

            } else if (date.indexOf(".") > 0) {
                date.replace(".", "-");
            } else if (date.indexOf("/") > 0) {
                date.replace("/", "-");
            } else {
                if (date.indexOf("年") > 0)
                    date.replace("年", "-");
                if (date.indexOf("月") > 0)
                    date.replace("月", "-");
                if (date.indexOf("日") > 0)
                    date.replace("日", "-");
            }
            SimpleDateFormat formater = new SimpleDateFormat(format);
            return formater.parse(date);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Date getCurrentDate(String format) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat.parse(getCurrentDateString(format));
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date getCurrentDate() {
        return getCurrentDate(DEFUALT_DATE_FORMAT);
    }

    public static String getCurrentDateString() {
        return getCurrentDateString(DEFUALT_DATE_FORMAT);
    }

    public static String getCurrentDateString(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(new Date());
    }

    public static String getBeforeDate(int days) {
        Calendar rightNow = Calendar.getInstance();
        SimpleDateFormat sim = new SimpleDateFormat(DEFUALT_DATE_FORMAT);
        //如果是后退几天，就写 -天数 例如：
        rightNow.add(Calendar.DAY_OF_MONTH, -days);
        //进行时间转换
        return sim.format(rightNow.getTime());
    }

    public static String getAfterDate(int days) {
        Calendar rightNow = Calendar.getInstance();
        SimpleDateFormat sim = new SimpleDateFormat(DEFUALT_DATE_FORMAT);
        //得到当前时间，+3天
        rightNow.add(Calendar.DAY_OF_MONTH, days);
        return sim.format(rightNow.getTime());
    }

    public static String getCurrentWeekFirstDay() {
        Calendar rightNow = Calendar.getInstance();
        SimpleDateFormat sim = new SimpleDateFormat(DEFUALT_DATE_FORMAT);
        rightNow.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return sim.format(rightNow.getTime());
    }

    public static String getCurrentMonthFirstDay() {
        Calendar rightNow = Calendar.getInstance();
        SimpleDateFormat sim = new SimpleDateFormat(DEFUALT_DATE_FORMAT);
        rightNow.set(Calendar.DAY_OF_MONTH, 1);
        return sim.format(rightNow.getTime());
    }

    // 毫秒转日期
    public static String convertToDate(long l) {
        if (l <= 9999999999l)
            l = l * 1000;
        SimpleDateFormat sim = new SimpleDateFormat(DEFUALT_DATE_FORMAT);
        return sim.format(new Date(l));
    }

    // 毫秒转日期
    public static Date convertTimeToDate(long l) {
        if (l <= 9999999999l)
            l = l * 1000;
        return new Date(l);
    }

    // 毫秒转日期
    public static String convertToDate(long l, String format) {
        if (l <= 9999999999l)
            l = l * 1000;
        SimpleDateFormat sim = new SimpleDateFormat(format);
        return sim.format(new Date(l));
    }

    public static long getTimeShort(Date date) {
        return date.getTime() / 1000;
    }

    public static List<Date> getDatesAfterDate(Date date, int after) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        after += 1;
        List<Date> dateArr = new ArrayList<>(after);
        dateArr.add(date);
        for (int i = 1; i <= after; i++) {
            c.add(Calendar.DATE, 1);
            dateArr.add(c.getTime());
        }
        return dateArr;
    }

    public static boolean notInTimeSpace(Date date,Date dateStart,Date dateEnd){
        Calendar begin = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        begin.setTime(dateStart);
        end.setTime(dateEnd);
        now.setTime(date);
        return begin.before(end) ? now.before(begin) ? true : now.after(end) ? true : false : (now.before(begin) && now.after(end)) ? true : false;
    }
}
