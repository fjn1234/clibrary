package utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import interfaces.NetConnectionInterface.iConnectListener;

/**
 * Created by Hugh on 2015/12/31.
 */
public class NetUtil {


    public static void download(final String url, final String path, final iConnectListener connectListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream is = null;
                try {
                    URL myFileUrl = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) myFileUrl
                            .openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    is = conn.getInputStream();
                    boolean b = FileUtil.inputstreamToFile(is, path);
                    if (b == false) {
                        connectListener.onFail("file create fail");
                    }
                    connectListener.onSuccess("");
                } catch (Exception ex) {
                    connectListener.onFail(ex.toString());
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
