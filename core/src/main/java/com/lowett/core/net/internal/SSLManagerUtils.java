package com.lowett.core.net.internal;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Collection;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

/**
 * Created by Hyu on 2016/7/12.
 * Email: fvaryu@qq.com
 */
public class SSLManagerUtils {


    public static TrustManagerFactory getTrustManagerFactory(InputStream cerInput) {

        CertificateFactory factory;
        Collection<? extends Certificate> cers;
        KeyStore keyStore;
        TrustManagerFactory f;

        try {
            factory = CertificateFactory.getInstance("X.509");
            cers = factory.generateCertificates(cerInput);
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (Certificate c : cers) {
                keyStore.setCertificateEntry(String.valueOf(index++), c);
            }

            f = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            f.init(keyStore);
            return f;
        } catch (KeyStoreException e) {
            return null;
        } catch (CertificateException e) {
            return null;
        } catch (NoSuchAlgorithmException e) {
            return null;
        } catch (IOException e) {
            return null;
        }finally {
            if (cerInput != null) {
                try {
                    cerInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static KeyManagerFactory getKeyManagerFactory(InputStream keyInput, String password) {
        try {
            KeyStore client = KeyStore.getInstance(KeyStore.getDefaultType());
            client.load(keyInput, password.toCharArray());
            KeyManagerFactory k = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            k.init(client, password.toCharArray());
            return k;
        } catch (NoSuchAlgorithmException e) {
            return null;
        } catch (KeyStoreException e) {
            return null;
        } catch (CertificateException e) {
            return null;
        } catch (IOException e) {
            return null;
        } catch (UnrecoverableKeyException e) {
            return null;
        }finally {
            if (keyInput != null) {
                try {
                    keyInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void sslSocketFactory(OkHttpClient.Builder builder, InputStream serverInput, InputStream clientInput, String password) {

        try {
            KeyManagerFactory factory = SSLManagerUtils.getKeyManagerFactory(clientInput, password);
            if (factory == null) {
                return;
            }
            TrustManagerFactory trustManagerFactory = SSLManagerUtils.getTrustManagerFactory(serverInput);
            if (trustManagerFactory == null) {
                return;
            }
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            X509TrustManager manager = null;
            for (TrustManager m: trustManagers) {
                if (m instanceof X509TrustManager) {
                    manager = (X509TrustManager) m;
                    break;
                }
            }

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(factory.getKeyManagers(), trustManagerFactory.getTrustManagers(),
                    new SecureRandom());
            builder.sslSocketFactory(sslContext.getSocketFactory(), manager);
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
