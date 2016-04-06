package net;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsyncFileUpload extends AsyncTask<Void, Void, String> {

    ResultCallback resultCallback;
    String url, filePath;
    Result resultType = Result.None;

    public enum Result {
        FileError, NetError, ServerError, Success, None
    }

    public AsyncFileUpload() {
    }

    public AsyncFileUpload(String url, String filePath, ResultCallback resultCallback) {
        this.url = url;
        this.filePath = filePath;
        this.resultCallback = resultCallback;
    }

    @Override
    protected String doInBackground(Void... arg0) {
        try {
            return upload();
        } catch (IOException e) {
            resultType = Result.NetError;
            return "";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (resultType == Result.NetError) {
            if (resultCallback != null)
                resultCallback.onNetError();
        } else if (resultType == Result.Success) {
            if (resultCallback != null)
                resultCallback.onSuccess(result, filePath);
        } else if (resultType == Result.ServerError)
            if (resultCallback != null)
                resultCallback.onServerError(result);
        this.cancel(true);
    }

    private String upload() throws IOException {
        try {
            File file = new File(filePath);
            if (!file.exists() || !file.isFile()) {
                resultType = Result.FileError;
                return "";
            }
            URL urlObj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
            con.setRequestMethod(NetParams.POST);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setRequestProperty("Connection", NetParams.KEEPALIVE);
            con.setRequestProperty("Charset", NetParams.CHARSET);
            String BOUNDARY = "----------" + System.currentTimeMillis();
            con.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + BOUNDARY);
            OutputStream out = con.getOutputStream();
            FileInputStream fInStream = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(
                    fInStream);
            byte[] b = new byte[(int) file.length()];
            bufferedInputStream.read(b, 0, b.length);
            out.write(b, 0, b.length);
            out.flush();
            InputStream is = con.getInputStream();
            InputStreamReader bReader = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(bReader);
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                builder.append(line);
//                if (resultCallback != null)
//                    resultCallback.onProgress(line.length() * 1.0f / b.length);
            }
            bufferedInputStream.close();
            out.close();
            br.close();
            is.close();
            resultType = Result.Success;
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return NetParams.NET_ERROR;
        }
    }

    public interface ResultCallback {
        void onNetError();

        void onServerError(String error);

        void onSuccess(String result, String filePath);

        void onProgress(float percent);
    }
}
