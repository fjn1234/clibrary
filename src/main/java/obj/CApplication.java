package obj;

import android.app.Application;
import android.content.Context;

import utils.SystemUtil;

public abstract class CApplication extends Application {

    public static Context applicationContext;
    private static DebugMode debugMode;
    private static String entityDB="clibrary.db";

    public enum DebugMode {
        Release, Test, Debug
    }

    public static void setDebugMode(DebugMode debugMode) {
        CApplication.debugMode = debugMode;
    }

    public static DebugMode getDebugMode() {
        return debugMode;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        try {
            initRootApplication();
            initScreenSize();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    protected abstract void initRootApplication();

    public static Context getContext() {
        return applicationContext;
    }

    public static boolean isAppInBackgroud() {
        return SystemUtil.isBackground(getContext());
    }

    public static String getEntityDB() {
        return entityDB;
    }

    public static void setEntityDB(String entityDB) {
        CApplication.entityDB = entityDB;
    }


    private void initScreenSize() {
        try {
            CustomAttrs.init(getContext());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
