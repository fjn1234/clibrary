package net;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import interfaces.NetConnectionInterface;
import interfaces.NetConnectionInterface.iConnectListener;
import utils.DateUtil;


public class NetConnection {
    public NetConnection(final boolean doEncode, final String charset,
                         final String url, final HttpMethod method, final NetConnectionInterface.iSetHeader headerInterface,
                         final iConnectListener conectListener, final String... kvs) {
        this(doEncode, charset, url, method, headerInterface, conectListener, NetConfig.TIME_OUT, kvs);
    }

    public NetConnection(final boolean doEncode, final String charset,
                         final String url, final HttpMethod method, final NetConnectionInterface.iSetHeader headerInterface,
                         final iConnectListener conectListener, final int timeOut, final String... kvs) {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                HttpURLConnection uc = null;
                try {
                    if (conectListener instanceof NetConnectionInterface.iConnectListener2)
                        ((NetConnectionInterface.iConnectListener2) conectListener).onStart();
                    StringBuilder paramsBuffer = new StringBuilder();
                    for (int i = 0; i < kvs.length - 1; i += 2) {
                        if (kvs[i + 1] == null) {
                            continue;
                        }
                        if (paramsBuffer.length() > 0)
                            paramsBuffer.append("&");
                        if (doEncode)
                            paramsBuffer.append(kvs[i]
                                    + "="
                                    + URLEncoder.encode(kvs[i + 1],
                                    charset));
                        else
                            paramsBuffer.append(kvs[i] + "=" + kvs[i + 1]);
                    }
                    paramsBuffer.append("&t=" + DateUtil.getCurrentDate().getTime());
                    URL newUrl;
                    switch (method) {
                        case Post:
                            newUrl = new URL(url);
                            uc = (HttpURLConnection) newUrl.openConnection();
                            if (timeOut > 0) {
                                uc.setConnectTimeout(timeOut);
                                uc.setReadTimeout(timeOut);
                            }
                            if (headerInterface != null)
                                headerInterface.setHeader(uc);
                            uc.setDoOutput(true);
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(uc.getOutputStream(), charset));
                            bw.write(paramsBuffer.toString());
                            bw.flush();
                            break;
                        case Get:
                            newUrl = new URL(url + "?" + paramsBuffer.toString());
                            uc = (HttpURLConnection) newUrl.openConnection();
                            if (timeOut > 0) {
                                uc.setConnectTimeout(timeOut);
                                uc.setReadTimeout(timeOut);
                            }
                            if (headerInterface != null)
                                headerInterface.setHeader(uc);
                            break;
                    }
                    Log.e("url:", uc.getURL().toString());
                    Log.e("parama:", paramsBuffer.toString());
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(uc.getInputStream(), charset));
                    String line = null;
                    StringBuilder result = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        result.append(line);
                    }
                    return result.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (uc != null) {
                        uc.disconnect();
                    }
                }
                return NetParams.NET_ERROR;
            }

            protected void onPostExecute(String result) {
//                super.onPostExecute(result);
                if (conectListener == null) return;
                if (result != null) {
                    conectListener.onSuccess(result);
                } else {
                    conectListener.onFail(result);
                }
                if (conectListener instanceof NetConnectionInterface.iConnectListener2)
                    ((NetConnectionInterface.iConnectListener2) conectListener).onFinish();
            }

        }.execute();
    }



}
