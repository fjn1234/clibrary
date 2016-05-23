package net;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import interfaces.INetConnection;
import utils.LogUtil;

public abstract class AsyncFileUpload extends AsyncTask<Void, Void, Result> {

    INetConnection.iFileUploadListener fileUploadListener;
    String url;
    Map<String, String> params;
    Map<String, File> files;

    private AsyncFileUpload() {
    }

    public AsyncFileUpload(String url, Map<String, String> params, Map<String, File> files, INetConnection.iFileUploadListener fileUploadListener) {
        this.url = url;
        this.params = params;
        this.files = files;
        this.fileUploadListener = fileUploadListener;
        if (fileUploadListener != null)
            fileUploadListener.onStart();
        execute();
    }

    @Override
    protected Result doInBackground(Void... arg0) {
        return upload();
    }

    private Result upload() {
        try {
            String BOUNDARY = java.util.UUID.randomUUID().toString();
            String PREFIX = "--", LINEND = "\r\n";
            String MULTIPART_FROM_DATA = "multipart/form-data";
            URL uri = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
            conn.setReadTimeout(10 * 1000); // 缓存的最长时间
            conn.setConnectTimeout(120000);
            conn.setDoInput(true);// 允许输入
            conn.setDoOutput(true);// 允许输出
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST");
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Charset", NetParams.CHARSET);
            conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);

            // 首先组拼文本类型的参数
            if (params == null) params = new HashMap<>();
            setDefaultParams(params);
            StringBuilder sb = new StringBuilder();
            LogUtil.loge(AsyncFileUpload.class, "url:" + url);
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINEND);
                sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
                sb.append("Content-Type: text/plain; charset=" + NetParams.CHARSET + LINEND);
                sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
                sb.append(LINEND);
                sb.append(entry.getValue());
                sb.append(LINEND);
            }
            DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
            outStream.write(sb.toString().getBytes());
            // 发送文件数据
            if (files != null) {
                for (Map.Entry<String, File> file : files.entrySet()) {
                    StringBuilder sb1 = new StringBuilder();
                    sb1.append(PREFIX);
                    sb1.append(BOUNDARY);
                    sb1.append(LINEND);
                    sb1.append("Content-Disposition: form-data; name=\"" + file.getKey() + "\"; filename=\""
                            + file.getValue().getName() + "\"" + LINEND);
                    sb1.append("Content-Type: application/octet-stream; charset=" + NetParams.CHARSET + LINEND);
                    sb1.append("Content-Transfer-Encoding: binary" + LINEND);
                    sb1.append(LINEND);
                    outStream.write(sb1.toString().getBytes());
                    FileInputStream fis = new FileInputStream(file.getValue());
                    byte[] buffer = new byte[8192]; // 8k
                    int count = 0;
                    // 读取文件
                    while ((count = fis.read(buffer)) != -1) {
                        outStream.write(buffer, 0, count);
                    }
                    fis.close();
                    outStream.write(LINEND.getBytes());
                    LogUtil.loge(AsyncFileUpload.class, file.getKey() + "=" + file.getValue().getAbsolutePath());
                }
            }
            // 请求结束标志
            outStream.write((PREFIX + BOUNDARY + PREFIX + LINEND).getBytes());
            outStream.flush();
            // 得到响应码
            int res = conn.getResponseCode();
            InputStream in = conn.getInputStream();
            StringBuilder sb2 = new StringBuilder();
            if (res == 200) {
                int ch;
                while ((ch = in.read()) != -1) {
                    sb2.append((char) ch);
                }
            }
            outStream.close();
            conn.disconnect();
            String resultStr = sb2.toString();
            LogUtil.loge(NetConnection.class, resultStr);
            return getResult(resultStr);

        } catch (Exception e) {
            e.printStackTrace();
            Result result = new Result();
            result.responseStatus = NetParams.OPERATE_FAIL;
            result.resultData = e.getMessage();
            return result;
        }
    }

    @Override
    protected void onPostExecute(Result result) {
        onResult(result);
        if (fileUploadListener == null) return;
        if (result.responseStatus.equals(NetParams.OPERATE_SUCCESS)) {
            fileUploadListener.onSuccess(result);
        } else {
            fileUploadListener.onFail(result);
        }
        this.cancel(true);
    }

    protected abstract Result getResult(String response);

    protected abstract void setDefaultParams(Map<String, String> params);

    protected abstract void onResult(Result result);
}
