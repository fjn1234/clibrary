package utils;

import android.text.TextUtils;
import android.util.SparseArray;

import com.hugh.clibrary.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import obj.CApplication;

public class ValidateUtil {

    public static boolean idValideIDCard(String idCard) {
        return TextUtils.isEmpty(validateIDCard(idCard));
    }

    public static String validateIDCard(String IDStr) {
        String errorInfo = "";// 记录错误信息
        String[] ValCodeArr = {"1", "0", "x", "9", "8", "7", "6", "5", "4",
                "3", "2"};
        String[] Wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
                "9", "10", "5", "8", "4", "2"};
        String Ai = "";
        // ================ 号码的长度 15位或18位 ================
        if (TextUtils.isEmpty(IDStr) || IDStr.length() != 15 && IDStr.length() != 18) {
            errorInfo = CApplication.getGolbalContext().getString(R.string.validate_idcard_error1);
            return errorInfo;
        }
        IDStr = IDStr.toLowerCase();
        // =======================(end)========================

        // ================ 数字 除最后以为都为数字 ================
        if (IDStr.length() == 18) {
            Ai = IDStr.substring(0, 17);
        } else if (IDStr.length() == 15) {
            Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
        }
        if (isNumeric(Ai) == false) {
            errorInfo = CApplication.getGolbalContext().getString(R.string.validate_idcard_error2);
            return errorInfo;
        }
        // =======================(end)========================

        // ================ 出生年月是否有效 ================
        String strYear = Ai.substring(6, 10);// 年份
        String strMonth = Ai.substring(10, 12);// 月份
        String strDay = Ai.substring(12, 14);// 月份
        if (isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
            errorInfo = CApplication.getGolbalContext().getString(R.string.validate_idcard_error3);
            return errorInfo;
        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
                    || (gc.getTime().getTime() - s.parse(
                    strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
                errorInfo = CApplication.getGolbalContext().getString(R.string.validate_idcard_error4);
                return errorInfo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            errorInfo = CApplication.getGolbalContext().getString(R.string.validate_idcard_error5);
            return errorInfo;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            errorInfo = CApplication.getGolbalContext().getString(R.string.validate_idcard_error6);
            return errorInfo;
        }
        // =====================(end)=====================

        // ================ 地区码时候有效 ================
        SparseArray h = GetAreaCode();
        if (h.get(Integer.parseInt(Ai.substring(0, 2))) == null) {
            errorInfo = CApplication.getGolbalContext().getString(R.string.validate_idcard_error7);
            return errorInfo;
        }
        // ==============================================

        // ================ 判断最后一位的值 ================
        int TotalmulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            TotalmulAiWi = TotalmulAiWi
                    + Integer.parseInt(String.valueOf(Ai.charAt(i)))
                    * Integer.parseInt(Wi[i]);
        }
        int modValue = TotalmulAiWi % 11;
        String strVerifyCode = ValCodeArr[modValue];
        Ai = Ai + strVerifyCode;

        if (IDStr.length() == 18) {
            if (Ai.equals(IDStr) == false) {
                errorInfo = CApplication.getGolbalContext().getString(R.string.validate_idcard_error8);
                return errorInfo;
            }
        } else {
            return "";
        }
        // =====================(end)=====================
        return "";
    }

    /**
     * 功能：设置地区编码
     */
    private static SparseArray GetAreaCode() {
        SparseArray<String> array = new SparseArray<>();
        array.append(11, ""); //北京
        array.append(12, ""); //天津
        array.append(13, ""); //河北
        array.append(14, ""); //山西
        array.append(15, "");//内蒙古
        array.append(21, ""); //辽宁
        array.append(22, ""); //吉林
        array.append(23, "");//黑龙江
        array.append(31, ""); //上海
        array.append(32, ""); //江苏
        array.append(33, ""); //浙江
        array.append(34, ""); //安徽
        array.append(35, ""); //福建
        array.append(36, ""); //江西
        array.append(37, ""); //山东
        array.append(41, ""); //河南
        array.append(42, ""); //湖北
        array.append(43, ""); //湖南
        array.append(44, ""); //广东
        array.append(45, ""); //广西
        array.append(46, ""); //海南
        array.append(50, ""); //重庆
        array.append(51, ""); //四川
        array.append(52, ""); //贵州
        array.append(53, ""); //云南
        array.append(54, ""); //西藏
        array.append(61, ""); //陕西
        array.append(62, ""); //甘肃
        array.append(63, ""); //青海
        array.append(64, ""); //宁夏
        array.append(65, ""); //新疆
        array.append(71, ""); //台湾
        array.append(81, ""); //香港
        array.append(82, ""); //澳门
        array.append(91, ""); //国外
        return array;
    }

    /**
     * 功能：判断字符串是否为数字
     */
    private static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 功能：判断字符串是否为日期格式
     */
    public static boolean isDate(String strDate) {
        Pattern pattern = Pattern
                .compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
        Matcher m = pattern.matcher(strDate);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }


}
