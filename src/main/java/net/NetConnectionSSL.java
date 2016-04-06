package net;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

import interfaces.NetConnectionInterface;
import utils.DateUtil;

public class NetConnectionSSL {

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
            e.printStackTrace();
            if (certFile != null)
                try {
                    certFile.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            return null;
        }
    }

    public NetConnectionSSL(final boolean doEncode, final String charset,
                            final String url, final HttpMethod method, final NetConnectionInterface.iSetHeader headerInterface,
                            final NetConnectionInterface.iConnectListener conectListener, final String... kvs) {
        this(doEncode, charset, url, method, headerInterface, conectListener, NetParams.TIME_OUT, kvs);
    }

    public NetConnectionSSL(final boolean doEncode, final String charset,
                            final String url, final HttpMethod method, final NetConnectionInterface.iSetHeader headerInterface,
                            final NetConnectionInterface.iConnectListener connectListener, final int timeOut, final String... kvs) {

        if (!isSSLInit())
            throw new RuntimeException("SSL has not init");
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                try {
                    if (connectListener instanceof NetConnectionInterface.iConnectListener2)
                        ((NetConnectionInterface.iConnectListener2) connectListener).onStart();
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
                    HttpsURLConnection uc = null;
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
                    Log.e("url:", uc.getURL().toString());
                    Log.e("parama:", paramsBuffer.toString());
                    BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream(), charset));
                    String line = null;
                    StringBuilder result = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        result.append(line);
                    }
                    return result.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return NetParams.NET_ERROR;
            }

            protected void onPostExecute(String result) {
//                super.onPostExecute(result);
                if (connectListener == null) return;
                if (result != null) {
                    connectListener.onSuccess(result);
                } else {
                    connectListener.onFail(result);
                }
                if (connectListener instanceof NetConnectionInterface.iConnectListener2)
                    ((NetConnectionInterface.iConnectListener2) connectListener).onFinish();
            }

        }.execute();
    }

}
