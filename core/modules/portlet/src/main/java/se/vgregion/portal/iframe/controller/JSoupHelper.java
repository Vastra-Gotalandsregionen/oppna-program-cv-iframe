package se.vgregion.portal.iframe.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.net.ssl.*;
import java.net.URL;

/**
 * Helper class for JSoup.
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class JSoupHelper {

    /**
     * Invokes the given url and returns the response as a {@link Document}.
     * @param target the target url
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
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            oldSslSocketFactory = HttpsURLConnection.getDefaultSSLSocketFactory();
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            throw new RuntimeException(e); // Will probably not happen
        }
        return oldSslSocketFactory;
    }

    private void resetSSLSocketFactory(SSLSocketFactory sslSocketFactory) {
        HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory);
    }
}
