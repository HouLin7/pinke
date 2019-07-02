package com.gome.work.common.imageloader;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

/**
 * Created by liubomin on 2016/8/12.
 */
public class OKHttpsUtil {
    public OKHttpsUtil() {
    }

    public static void setCertificates(OkHttpClient okHttpClient, InputStream[] certificates, InputStream bksFile, String password) {
        try {
            TrustManager[] e = prepareTrustManager(certificates);
            KeyManager[] keyManagers = prepareKeyManager(bksFile, password);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagers, new TrustManager[]{new OKHttpsUtil.MyTrustManager(chooseTrustManager(e))}, new SecureRandom());
            okHttpClient.newBuilder()
                    .sslSocketFactory(sslContext.getSocketFactory())
                    .hostnameVerifier(new HostnameVerifier() {
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    });
//            okHttpClient.setSslSocketFactory(sslContext.getSocketFactory());
        } catch (NoSuchAlgorithmException var7) {
            var7.printStackTrace();
        } catch (KeyManagementException var8) {
            var8.printStackTrace();
        } catch (KeyStoreException var9) {
            var9.printStackTrace();
        }

    }

    private static TrustManager[] prepareTrustManager(InputStream... certificates) {
        if(certificates != null && certificates.length > 0) {
            try {
                CertificateFactory e = CertificateFactory.getInstance("X.509");
                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                keyStore.load(null);
                int index = 0;
                InputStream[] trustManagerFactory = certificates;
                int trustManagers = certificates.length;

                for(int i$ = 0; i$ < trustManagers; ++i$) {
                    InputStream certificate = trustManagerFactory[i$];
                    String certificateAlias = Integer.toString(index++);
                    keyStore.setCertificateEntry(certificateAlias, e.generateCertificate(certificate));

                    try {
                        if(certificate != null) {
                            certificate.close();
                        }
                    } catch (IOException var10) {
                    }
                }

                trustManagerFactory = null;
                TrustManagerFactory var15 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                var15.init(keyStore);
                TrustManager[] var16 = var15.getTrustManagers();
                return var16;
            } catch (NoSuchAlgorithmException var11) {
                var11.printStackTrace();
            } catch (CertificateException var12) {
                var12.printStackTrace();
            } catch (KeyStoreException var13) {
                var13.printStackTrace();
            } catch (Exception var14) {
                var14.printStackTrace();
            }

            return null;
        } else {
            return null;
        }
    }

    private static KeyManager[] prepareKeyManager(InputStream bksFile, String password) {
        try {
            if(bksFile != null && password != null) {
                KeyStore e = KeyStore.getInstance("BKS");
                e.load(bksFile, password.toCharArray());
                KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                keyManagerFactory.init(e, password.toCharArray());
                return keyManagerFactory.getKeyManagers();
            }

            return null;
        } catch (KeyStoreException var4) {
            var4.printStackTrace();
        } catch (NoSuchAlgorithmException var5) {
            var5.printStackTrace();
        } catch (UnrecoverableKeyException var6) {
            var6.printStackTrace();
        } catch (CertificateException var7) {
            var7.printStackTrace();
        } catch (IOException var8) {
            var8.printStackTrace();
        } catch (Exception var9) {
            var9.printStackTrace();
        }

        return null;
    }

    private static X509TrustManager chooseTrustManager(TrustManager[] trustManagers) {
        TrustManager[] arr$ = trustManagers;
        int len$ = trustManagers.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            TrustManager trustManager = arr$[i$];
            if(trustManager instanceof X509TrustManager) {
                return (X509TrustManager)trustManager;
            }
        }

        return null;
    }

    private static class MyTrustManager implements X509TrustManager {
        private X509TrustManager defaultTrustManager;
        private X509TrustManager localTrustManager;

        public MyTrustManager(X509TrustManager localTrustManager) throws NoSuchAlgorithmException, KeyStoreException {
            TrustManagerFactory var4 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            var4.init((KeyStore)null);
            this.defaultTrustManager = OKHttpsUtil.chooseTrustManager(var4.getTrustManagers());
            this.localTrustManager = localTrustManager;
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            try {
                this.defaultTrustManager.checkServerTrusted(chain, authType);
            } catch (CertificateException var4) {
                this.localTrustManager.checkServerTrusted(chain, authType);
            }

        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
}
