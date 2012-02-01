package se.vgregion.portal.admin.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import se.vgregion.portal.cs.domain.SiteKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 23/9-11
 * Time: 23:57
 */
public class SiteKeyHelperTest {
    SiteKeyHelper helper;

    @Before
    public void setUp() throws Exception {
        List<SiteKey> siteKeys = new ArrayList(Arrays.asList(
                new SiteKey("c", "C", "C - description", false, false, true),
                new SiteKey("a", "A", "A - description", false, false, true),
                new SiteKey("d", "D", "D - description", false, false, false),
                new SiteKey("e", "E", "E - description", false, false, true),
                new SiteKey("b", "B", "B - description", false, false, true)
        ));
        helper = new SiteKeyHelper(siteKeys);
    }

    @Test
    public void testGet() throws Exception {
        assertEquals(5, helper.get().size());
        assertEquals("c", helper.get().get(0).getSiteKey());
        assertEquals("b", helper.get().get(4).getSiteKey());
    }

    @Test
    public void testOrderBySiteKey() throws Exception {
        List<SiteKey> result = helper.orderBySiteKey().get();

        assertEquals(5, result.size());
        assertEquals("a", result.get(0).getSiteKey());
        assertEquals("e", result.get(4).getSiteKey());
    }

    @Test
    public void testFilterActive() throws Exception {
        List<SiteKey> result = helper.filterActive().get();

        assertEquals(4, result.size());
        assertEquals("c", result.get(0).getSiteKey());
        assertEquals("b", result.get(3).getSiteKey());
    }

    @Test
    public void testDescriptionElipsis() throws Exception {
        List<SiteKey> result = helper.descriptionElipsis(7).get();
        for (SiteKey siteKey : result) {
            assertEquals(7, siteKey.getDescription().length());
            assertEquals("...", siteKey.getDescription().substring(4));
        }
    }

    @Test
    public void testDescriptionElipsis2() throws Exception {
        List<SiteKey> result = helper.descriptionElipsis(15).get();
        for (SiteKey siteKey : result) {
            assertEquals(15, siteKey.getDescription().length());
            assertEquals("ion", siteKey.getDescription().substring(12));
        }
    }

    @Test
    public void testDescriptionElipsis3() throws Exception {
        List<SiteKey> result = helper.descriptionElipsis(14).get();
        for (SiteKey siteKey : result) {
            assertEquals(14, siteKey.getDescription().length());
            assertEquals("...", siteKey.getDescription().substring(11));
        }
    }

    @Test
    public void testDescriptionElipsis4() throws Exception {
        List<SiteKey> result = helper.descriptionElipsis(16).get();
        for (SiteKey siteKey : result) {
            assertEquals(15, siteKey.getDescription().length());
            assertEquals("ion", siteKey.getDescription().substring(12));
        }
    }
}
