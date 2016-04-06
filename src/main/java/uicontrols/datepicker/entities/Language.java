package uicontrols.datepicker.entities;

import android.content.Context;

import utils.SystemUtil;


public abstract class Language {
    private static Language sLanguage = null;

    public static Language getLanguage(Context context) {
        if (null == sLanguage) {
            String locale = SystemUtil.getCurrentLocale(context);
            if (locale.equals("CN")) {
                sLanguage = new CN();
            } else {
                sLanguage = new EN();
            }
        }
        return sLanguage;
    }

    public abstract String[] monthTitles();
    public abstract String[] weekTitles(int type);
    public abstract String ensureTitle();
}
