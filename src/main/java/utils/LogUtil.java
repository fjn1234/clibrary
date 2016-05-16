package utils;

import android.util.Log;

import java.io.RandomAccessFile;

import io.CFile;
import obj.CApplication;


public class LogUtil {
    private LogUtil() {
    }

    public static void write(Class<?> clazz, String msg) {
        System.out.println("----------------------------");
        System.out.println(clazz.getName() + ":");
        System.out.println(msg);
    }

    public static void write(String exInfo, String strFilePath) {
        //每次写入时，都换行写
        String strContent = exInfo + "\n";
        try {
            CFile file = new CFile(strFilePath);
            if (!file.exists()) {
                file.createNewFileAndDirectory();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {

        }
    }

    public static void loge(Class c, String... msg) {
        if (CApplication.getDebugMode() != CApplication.DebugMode.Release) {
            for (String s : msg) {
                Log.e(c.getClass().getName(), s);
            }
        }
    }

    public static void printStackTrace(Class c, Exception ex) {
        if (CApplication.getDebugMode() != CApplication.DebugMode.Release) {
            ToastUtil.makeShortToast(CApplication.getAppContext(),"crash");
            System.out.println(c.getClass().getName());
            ex.printStackTrace();
        }
    }
}
