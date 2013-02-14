package se.vgregion.portal.csiframe.svc;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpMailInfService implements MailInfService {

    private String serviceUrl;

    /**
     * There are several mail servers that the user might be registred on. This method finds the symbolic name of
     * the server.
     *
     * @param userId
     *            vgrid id that is used as id to lookup what mail server the user have.
     * @return name of the server.
     */
    @Override
    public String findServerName(String userId) {
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

            String bodyStartTag = "<body text=\"#000000\">";
            String bodyEndTag = "</body>";
            int bodyStartPos = sb.indexOf(bodyStartTag), bodyEndPos = sb.indexOf(bodyEndTag);
            String result = sb.substring(bodyStartPos + bodyStartTag.length(), bodyEndPos);
            result = result.split(Pattern.quote(";"))[0].trim();
            result = result.split(Pattern.quote("."))[0].trim();

            return result.trim();
        } catch (IOException e) {
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
