package se.vgregion.portal.admin.controller;

import org.junit.Before;
import org.junit.Test;
import se.vgregion.portal.cs.domain.SiteKey;
import se.vgregion.portal.cs.domain.UserSiteCredential;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 24/9-11
 * Time: 00:34
 */
public class UserSiteCredentialHelperTest {
    UserSiteCredentialHelper helper;

    @Before
    public void setUp() throws Exception {
        UserSiteCredential b = new UserSiteCredential("b", "b");
        b.setValid(true);
        List<UserSiteCredential> credentials = new ArrayList(Arrays.asList(
                new UserSiteCredential("c", "c"),
                b,
                new UserSiteCredential("a", "a"),
                new UserSiteCredential("e", "e"),
                new UserSiteCredential("d", "d")
        ));
        helper = new UserSiteCredentialHelper(credentials);
    }

    @Test
    public void constructor() {
        Collection<UserSiteCredential> credentials = Arrays.asList(
                new UserSiteCredential("a", "a"),
                new UserSiteCredential("e", "e")
        );

        UserSiteCredentialHelper helper = new UserSiteCredentialHelper(credentials);
        assertEquals(2, helper.get().size());
    }

    @Test
    public void testGet() throws Exception {
        assertEquals(5, helper.get().size());
        assertEquals("c", helper.get().get(0).getSiteKey());
        assertEquals("d", helper.get().get(4).getSiteKey());
    }

    @Test
    public void testOrderBySiteKey() throws Exception {
        List<UserSiteCredential> result = helper.orderBySiteKey().get();

        assertEquals(5, result.size());
        assertEquals("a", result.get(0).getSiteKey());
        assertEquals("e", result.get(4).getSiteKey());
    }

    @Test
    public void testFilterValid() throws Exception {
        List<UserSiteCredential> result = helper.filterValid().get();

        assertEquals(1, result.size());
        assertEquals("b", result.get(0).getSiteKey());
    }
}
