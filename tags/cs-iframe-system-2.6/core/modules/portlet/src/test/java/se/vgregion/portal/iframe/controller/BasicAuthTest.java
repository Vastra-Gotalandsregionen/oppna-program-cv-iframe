package se.vgregion.portal.iframe.controller;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * Created: 2012-01-23 18:39
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class BasicAuthTest {

    @Test
    public void parseUrl() throws Exception {
        String url = "http://apa.bepa.se/cepa?depa=epa";

        String[] splitUrl = url.split("://");
        assertEquals(2, splitUrl.length);
        assertEquals("http", splitUrl[0]);
        assertEquals("apa.bepa.se/cepa?depa=epa", splitUrl[1]);
    }

    @Test
    public void parseSllUrl() throws Exception {
        String url = "https://apa.bepa.se/cepa?depa=epa";

        String[] splitUrl = url.split("://");
        assertEquals(2, splitUrl.length);
        assertEquals("https", splitUrl[0]);
        assertEquals("apa.bepa.se/cepa?depa=epa", splitUrl[1]);
    }

    @Test
    public void parseRelativeUrl() throws Exception {
        String url = "/cepa?depa=epa";

        String[] splitUrl = url.split("://");
        assertEquals(1, splitUrl.length);
        assertEquals("/cepa?depa=epa", splitUrl[0]);
    }
}
