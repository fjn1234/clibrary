package utils;

import android.content.Context;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;


public class WebViewUtil {

    public static void initWV(WebView wv,Context context)
    {
        WebSettings setting = wv.getSettings();
        setting.setJavaScriptEnabled(true);
        wv.getSettings().setDomStorageEnabled(true);
        // Set cache size to 8 mb by default. should be more than enough
        wv.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = context.getApplicationContext().getCacheDir()
                .getAbsolutePath();
        wv.getSettings().setAppCachePath(appCachePath);
        wv.getSettings().setAllowFileAccess(true);
        wv.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
                result.confirm();
                return true;
            }
        });
        wv.setDrawingCacheEnabled(true);
    }




}
