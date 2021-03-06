package net;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

import interfaces.INetConnection;
import utils.LogUtil;


public abstract class NetConnection extends AsyncTask<Object, Void, Result> {
    private boolean sslMode;
    private boolean doEncode;
    private String charset;
    private String url;
    private NetParams.HttpMethod method;
    private INetConnection.iSetHeader headerInterface;
    private INetConnection.iConnectListener connectListener;
    private int timeOut;
    private List<String> kvs;

    public NetConnection(boolean sslMode, boolean doEncode, String charset, String url, NetParams.HttpMethod method, INetConnection.iSetHeader headerInterface,
                         INetConnection.iConnectListener connectListener, int timeOut, List<String> kvs) {
        init(sslMode, doEncode, charset, url, method, headerInterface, connectListener, timeOut, kvs);
    }

    public NetConnection(boolean sslMode, boolean doEncode, String charset,
                         String url, NetParams.HttpMethod method, INetConnection.iSetHeader headerInterface,
                         INetConnection.iConnectListener connectListener, int timeOut, String... kvs) {
        List<String> params = new ArrayList<>();
        if (kvs != null)
            Collections.addAll(params, kvs);
        init(sslMode, doEncode, charset, url, method, headerInterface, connectListener, timeOut, params);
    }

    private void init(boolean sslMode, boolean doEncode, String charset,
                      String url, NetParams.HttpMethod method, INetConnection.iSetHeader headerInterface,
                      INetConnection.iConnectListener connectListener, int timeOut, List<String> kvs) {
        this.sslMode = sslMode;
        this.doEncode = doEncode;
        this.charset = charset;
        this.url = url;
        this.method = method;
        this.headerInterface = headerInterface;
        this.connectListener = connectListener;
        this.timeOut = timeOut;
        this.kvs = kvs;
    }

    @Override
    protected Result doInBackground(Object... params) {
        try {
            setDefaultParams(kvs);
            String resultStr = sslMode ?
                    connectSSL(doEncode, charset, url, method, headerInterface, timeOut, kvs)
                    : connect(doEncode, charset, url, method, headerInterface, timeOut, kvs);
            LogUtil.loge(NetConnection.class, resultStr);
            return getResult(resultStr);
        } catch (Exception e) {
            LogUtil.printStackTrace(NetConnection.class, e);
            return new Result();
        }
    }

    protected void onPostExecute(Result result) {
        onResult(result);
        if (connectListener == null) return;
        if (result.responseStatus.equals(NetParams.OPERATE_SUCCESS)) {
            connectListener.onSuccess(result);
        } else {
            connectListener.onFail(result);
        }
        connectListener.onFinish();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (sslMode && !isSSLInit()) throw new RuntimeException("SSL has not init");
        if (connectListener != null) connectListener.onStart();
    }

    protected abstract Result getResult(String response);

    protected abstract void setDefaultParams(List<String> params);

    protected abstract void onResult(Result result);

    private static boolean isSSLInit = false;

    public static boolean isSSLInit() {
        return isSSLInit;
    }

    public static void initSSL(SSLContext context) {
        if (context == null) return;
        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }
        });
        isSSLInit = true;
    }

    public static SSLContext generateSSLContext(InputStream certFile) {
        try {
            if (certFile == null) return null;
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Certificate ca = cf.generateCertificate(certFile);
            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);
            return context;
        } catch (Exception e) {
            LogUtil.printStackTrace(NetConnection.class, e);
            if (certFile != null)
                try {
                    certFile.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            return null;
        }
    }

    public static String connect(final boolean doEncode, final String charset,
                                 final String url, final NetParams.HttpMethod method, final INetConnection.iSetHeader headerInterface,
                                 final int timeOut, final List<String> kvs)
            throws Exception {
        HttpURLConnection uc = null;
        try {
            StringBuilder paramsBuffer = new StringBuilder();
            String k, v;
            for (int i = 0, size = kvs.size() - 1; i < size; i += 2) {
                k = kvs.get(i);
                v = kvs.get(i + 1);
                if (v == null) continue;
                if (paramsBuffer.length() > 0)
                    paramsBuffer.append("&");
                if (doEncode)
                    paramsBuffer.append(k + "=" + URLEncoder.encode(v, charset));
                else
                    paramsBuffer.append(k + "=" + v);
            }
//            paramsBuffer.append("&t=" + DateUtil.getCurrentDate().getTime());
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
            LogUtil.loge(NetConnection.class, url + "?" + paramsBuffer.toString());
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(uc.getInputStream(), charset));
            String line = null;
            StringBuilder result = new StringBuilder();
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } finally {
            if (uc != null) {
                uc.disconnect();
            }
        }
    }


    public static String connectSSL(final boolean doEncode, final String charset,
                                    final String url, final NetParams.HttpMethod method, final INetConnection.iSetHeader headerInterface,
                                    final int timeOut, final List<String> kvs)
            throws Exception {
        HttpsURLConnection uc = null;
        try {
            StringBuilder paramsBuffer = new StringBuilder();
            String k, v;
            for (int i = 0, size = kvs.size() - 1; i < size; i += 2) {
                k = kvs.get(i);
                v = kvs.get(i + 1);
                if (v == null) continue;
                if (paramsBuffer.length() > 0)
                    paramsBuffer.append("&");

                if (doEncode)
                    paramsBuffer.append(k + "=" + URLEncoder.encode(v, charset));
                else
                    paramsBuffer.append(k + "=" + v);
            }
//            paramsBuffer.append("&t=" + DateUtil.getCurrentDate().getTime());
            URL newUrl;
            switch (method) {
                case Post:
                    newUrl = new URL(url);
                    uc = (HttpsURLConnection) newUrl.openConnection();
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
                    uc = (HttpsURLConnection) newUrl.openConnection();
                    if (timeOut > 0) {
                        uc.setConnectTimeout(timeOut);
                        uc.setReadTimeout(timeOut);
                    }
                    if (headerInterface != null)
                        headerInterface.setHeader(uc);
                    break;
            }
            LogUtil.loge(NetConnection.class, url + "?" + paramsBuffer.toString());
            BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream(), charset));
            String line = null;
            StringBuilder result = new StringBuilder();
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } finally {
            if (uc != null) {
                uc.disconnect();
            }
        }
    }

}
