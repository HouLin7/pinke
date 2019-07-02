package com.gome.work.core.net;

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

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by liuletao on 2017/1/6.
 */
public class SSLFactoryUtil {

    public static SSLSocketFactory getSSLSocketFactory(InputStream bksFile, String password)
            throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException, CertificateException, IOException, UnrecoverableKeyException {
        SSLContext sslContext = SSLContext.getInstance("TLS");
//        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        KeyStore clientKeyStore = KeyStore.getInstance("bks");
        clientKeyStore.load(bksFile, password.toCharArray());
        tmf.init(clientKeyStore);
        sslContext.init(null, tmf.getTrustManagers(), new SecureRandom());
        return sslContext.getSocketFactory();
    }


    public static SSLSocketFactory getSSLSocketFactory(InputStream certificate) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance("PKCS12", "BC");
            keyStore.load(null, null);
            keyStore.setCertificateEntry("trust", certificateFactory.generateCertificate(certificate));
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init
                    (
                            null,
                            trustManagerFactory.getTrustManagers(),
                            new SecureRandom()
                    );

            if (certificate != null)
                certificate.close();
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
