package se.vgregion.portal.iframe.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.net.ssl.*;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Helper class for JSoup.
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class JSoupHelper {

    /**
     * Invokes the given url and returns the response as a {@link Document}.
     *
     * @param target  the target url
     * @param timeout timeout in millis
     * @return the response as a {@link Document}
     * @throws Exception Exception
     */
    public Document invoke(URL target, long timeout) throws Exception {
        SSLSocketFactory oldSSslSocketFactory = null;
        try {
            oldSSslSocketFactory = trustAllSSLSocketFactory();
            final int timeoutMillis = 3000;
            return Jsoup.parse(target, timeoutMillis);
        } finally {
            if (oldSSslSocketFactory != null) {
                resetSSLSocketFactory(oldSSslSocketFactory);
            }
        }
    }

    private SSLSocketFactory trustAllSSLSocketFactory() {
        SSLSocketFactory oldSslSocketFactory = null;
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new TrustAllX509TrustManager()};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            oldSslSocketFactory = HttpsURLConnection.getDefaultSSLSocketFactory();
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }
        return oldSslSocketFactory;
    }

    private void resetSSLSocketFactory(SSLSocketFactory sslSocketFactory) {
        HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory);
    }

    private static class TrustAllX509TrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
}
