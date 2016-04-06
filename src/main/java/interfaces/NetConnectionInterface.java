package interfaces;

import java.net.URLConnection;

/**
 * Created by Hugh on 2015/8/25.
 */
public class NetConnectionInterface {
    public interface iSetHeader {
        void setHeader(URLConnection urlConnection);
    }

    public interface iConnectListener {
        void onSuccess(String result);
        void onFail(String result);
    }
    public interface iConnectListener2 extends iConnectListener {
        void onStart();
        void onFinish();
        void onSuccess(String result);
        void onFail(String result);
    }

    public interface iUpdateConnectListener extends iConnectListener {
        void onNoChange(String result);
    }

}
