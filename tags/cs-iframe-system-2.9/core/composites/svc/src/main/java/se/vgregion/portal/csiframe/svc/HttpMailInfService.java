package se.vgregion.portal.csiframe.svc;

import java.io.InputStream;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpMailInfService implements MailInfService {

    private String serviceUrl = "http://aida.vgregion.se/activate.nsf/msrv?openagent&";

    @Override
    public String findServerName(String userId) {
        return fetchMailServerName(userId);
    }

    /**
     * There are several mail servers that the user might be registred on. This method finds the symbolic name of
     * the server.
     * 
     * @param userId
     *            vgrid id that is used as id to lookup what mail server the user have.
     * @return name of the server.
     */
    String fetchMailServerName(String userId) {

        return fetchMailServerName(serviceUrl, userId);
    }

    /**
     * There are several mail servers that the user might be registred on. This method finds the symbolic name of
     * the server.
     * 
     * @param serviceUrl
     *            the url to the service that looks up the server name.
     * @param userId
     *            vgrid id that is used as id to lookup what mail server the user have.
     * @return name of the server.
     */
    String fetchMailServerName(String serviceUrl, String userId) {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        String url = serviceUrl + userId;
        HttpGet httpGet = new HttpGet(url);

        try {
            HttpResponse response = httpclient.execute(httpGet);

            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();

            int c = is.read();
            StringBuilder sb = new StringBuilder();
            while (c != -1) {
                sb.append((char) c);
                c = is.read();
            }

            String bodyStartTag = "<body text=\"#000000\">", bodyEndTag = "</body>";
            int bodyStartPos = sb.indexOf(bodyStartTag), bodyEndPos = sb.indexOf(bodyEndTag);
            String result = sb.substring(bodyStartPos + bodyStartTag.length(), bodyEndPos);
            result = result.split(Pattern.quote(";"))[0].trim();
            result = result.split(Pattern.quote("."))[0].trim();

            return result.trim();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

}
