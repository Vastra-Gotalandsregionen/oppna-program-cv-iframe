package se.vgregion.portal.iframe.util;

import se.vgregion.portal.iframe.controller.CSViewController;
import se.vgregion.portal.iframe.model.PortletConfig;

import java.net.URI;
import java.net.URISyntaxException;
import java.io.IOException;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.BufferedHttpEntity;
import org.htmlparser.Parser;
import org.htmlparser.tags.FormTag;
import org.htmlparser.tags.InputTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.SimpleNodeIterator;
import org.htmlparser.util.ParserException;
import org.htmlparser.filters.TagNameFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginScreenScraper {
    private Logger log = LoggerFactory.getLogger(CSViewController.class);

    public void advancedScraping(PortletConfig portletConfig) {
        try {
            if (portletConfig == null
                    || portletConfig.getSrc() == null
                    || portletConfig.getSrc().length() < 1) {
                return;
            }
            log.debug("Target page: {}", portletConfig.getSrc());
            URI uri = new URI(portletConfig.getSrc());
            HttpGet get = new HttpGet(uri);
            HttpClient client = new DefaultHttpClient();

            HttpResponse httpResponse = client.execute(get);

            HttpEntity entity = httpResponse.getEntity();
            HttpEntity bufferedEntity = new BufferedHttpEntity(entity);
            String pageContent = EntityUtils.toString(bufferedEntity);
//            System.out.println(pageContent);

            Parser parser = Parser.createParser(pageContent, "UTF-8");
            TagNameFilter formTagFilter = new TagNameFilter("form");
            NodeList nodeList = parser.extractAllNodesThatMatch(formTagFilter);

            for (SimpleNodeIterator it = nodeList.elements(); it.hasMoreNodes();) {
                FormTag tag = (FormTag) it.nextNode();
                log.debug("Form id: {} name: {}", tag.getAttribute("id"), tag.getAttribute("name"));
                log.debug("Form name: {}", tag.getFormName());
                log.debug("Form location: {} resolved: {}",
                        tag.getFormLocation(), uri.resolve(tag.getFormLocation()));
                log.debug("Form method: {}", tag.getFormMethod());
                log.debug("Form size: {}", tag.getFormInputs().size());
                for (SimpleNodeIterator itInputs = tag.getFormInputs().elements();
                     itInputs.hasMoreNodes();) {
                    InputTag input = (InputTag) itInputs.nextNode();
                    log.debug("Input type: {} id: {} name: {} value: {}",
                            new Object[] {
                                    input.getAttribute("type"),
                                    input.getAttribute("id"),
                                    input.getAttribute("name"),
                                    input.getAttribute("value")
                            });
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}