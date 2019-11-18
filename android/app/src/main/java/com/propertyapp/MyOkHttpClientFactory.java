package com.propertyapp;

import android.content.res.AssetManager;
import android.util.Log;

import com.facebook.react.modules.network.OkHttpClientFactory;
import com.facebook.react.modules.network.OkHttpClientProvider;

import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.Collection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;

public class MyOkHttpClientFactory implements OkHttpClientFactory {

  private final String CLIENT_KET_PASSWORD = "password";
  AssetManager assetManager;

  public MyOkHttpClientFactory(AssetManager assetManager) {
    this.assetManager = assetManager;
  }

  @Override
  public OkHttpClient createNewNetworkModuleClient() {
    OkHttpClient.Builder builder = OkHttpClientProvider.createClientBuilder();
    setCertificates(builder);
    return builder.build();
  }

  public void setCertificates(OkHttpClient.Builder builder) {
    X509TrustManager trustManager;
    SSLSocketFactory sslSocketFactory;
    try {
      trustManager = trustManagerForCertificates(trustedCertificatesInputStream());
      SSLContext sslContext = SSLContext.getInstance("TLS");
      sslContext.init(null, new TrustManager[] { trustManager }, null);
      sslSocketFactory = sslContext.getSocketFactory();
    } catch (GeneralSecurityException e) {
      throw new RuntimeException(e);
    }
    builder.sslSocketFactory(sslSocketFactory, trustManager)
      .hostnameVerifier(new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
          //忽略域名检查
          return BuildConfig.VERIFY_HOSTNAME.equals("true");
        }
      });
  }

  private InputStream trustedCertificatesInputStream() {
    InputStream cert = null;
    try {
      // 后端服务器https证书
      InputStream mainCert = assetManager.open("cers/cert.cer");
      // 读取oss图片地址证书，否则图片无法显示
      InputStream ossCert = assetManager.open("cers/oss.cer");
      cert = new SequenceInputStream(mainCert, ossCert);
    } catch (IOException e) {
      Log.e("MyOkHttpClientFactory", "未找到签名文件");
      e.printStackTrace();
    }
    return cert;
  }

  private X509TrustManager trustManagerForCertificates(InputStream in)
    throws GeneralSecurityException {
    System.out.println("签名文件in" + in.toString());
    CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
    Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(in);
    if (certificates.isEmpty()) {
      throw new IllegalArgumentException("expected non-empty set of trusted certificates");
    }

    // Put the certificates a key store.
    char[] password = CLIENT_KET_PASSWORD.toCharArray(); // Any password will work.
    KeyStore keyStore = newEmptyKeyStore(password);
    int index = 0;
    for (Certificate certificate : certificates) {
      String certificateAlias = Integer.toString(index++);
      keyStore.setCertificateEntry(certificateAlias, certificate);
    }

    // Use it to build an X509 trust manager.
    KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
      KeyManagerFactory.getDefaultAlgorithm());
    keyManagerFactory.init(keyStore, password);
    TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
      TrustManagerFactory.getDefaultAlgorithm());
    trustManagerFactory.init(keyStore);
    TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
    if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
      throw new IllegalStateException("Unexpected default trust managers:"
        + Arrays.toString(trustManagers));
    }
    return (X509TrustManager) trustManagers[0];
  }

  private KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
    try {
      KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
      InputStream in = null; // By convention, 'null' creates an empty key store.
      keyStore.load(in, password);
      return keyStore;
    } catch (IOException e) {
      throw new AssertionError(e);
    }
  }
}
