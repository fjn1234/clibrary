package utils;

import android.text.TextUtils;

public class StringUtil {
    public static byte[] charToByte(char c) {
        byte[] b = new byte[2];
        b[0] = (byte) ((c & 0xFF00) >> 8);
        b[1] = (byte) (c & 0xFF);
        return b;
    }

    public static boolean isAscii(char c) {
        byte[] b = charToByte(c);
        if (b[0] == 0) {
            return true;
        }
        return false;
    }

    public static int stringToInteger(String value) {
        return stringToInteger(value, 0);
    }

    public static int stringToInteger(String value, int defaultValue) {
        if (TextUtils.isEmpty(value)) return defaultValue;
        try {
            return Integer.valueOf(value);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static long stringToLong(String value) {
        return stringToLong(value, 0);
    }

    public static long stringToLong(String value, long defaultValue) {
        if (TextUtils.isEmpty(value)) return defaultValue;
        try {
            return Long.valueOf(value);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static float stringToFloat(String value) {
        if (TextUtils.isEmpty(value)) return 0;
        try {
            return Float.valueOf(value.trim());
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String intToIp(int i) {
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

}
