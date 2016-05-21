package interfaces;

import net.Result;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLConnection;

/**
 * Created by Hugh on 2015/8/25.
 */
public class INetConnection {
    public interface iSetHeader {
        void setHeader(URLConnection urlConnection);
    }

    public interface iConnectListener {
        void onStart();
        void onFinish();
        void onSuccess(Result result);
        void onFail(Result result);
    }

    public interface iFileUploadListener extends iConnectListener {
        void onProgress(float percent);
    }

//    public interface iUpdateConnectListenerString extends iConnectListener {
//        void onNoChange(String resultData);
//    }

}
