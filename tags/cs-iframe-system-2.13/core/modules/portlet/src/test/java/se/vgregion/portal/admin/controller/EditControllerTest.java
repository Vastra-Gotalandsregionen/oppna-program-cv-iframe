package se.vgregion.portal.admin.controller;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import se.vgregion.portal.cs.domain.SiteKey;
import se.vgregion.portal.cs.service.CredentialService;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 27/9-11
 * Time: 15:38
 */
public class EditControllerTest {

    EditController editController;

    @Mock
    CredentialService credentialService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        editController = new EditController();

        ReflectionTestUtils.setField(editController, "credentialService", credentialService);
    }

    @Test
    public void testShowEdit() throws Exception {
        Model model = new ExtendedModelMap();

        SiteKey sk1 = new SiteKey("key1", "Key1", "12345678901234567890123456789012345678901234567890", false, false,
                false);
        SiteKey sk2 = new SiteKey("key2", "Key2", "", false, false, true);
        SiteKey sk3 = new SiteKey("key3", "Key3", "", false, false, true);
        SiteKey sk4 = new SiteKey("key4", "iKey4", "", false, false, true);
        when(credentialService.getAllSiteKeys()).thenReturn(Lists.newArrayList(sk3, sk2, sk1, sk4));

        String result = editController.showEdit(model);

        assertEquals("edit", result);
        List<SiteKey> siteKeys = (List<SiteKey>) model.asMap().get("siteKeys");
        assertEquals(4, siteKeys.size());
        for (SiteKey siteKey: siteKeys) {
            assertTrue(siteKey.getDescription().length() <= 45);
        }
        assertEquals("key1", siteKeys.get(0).getSiteKey());
        assertEquals("key4", siteKeys.get(3).getSiteKey());
    }

    @Test
    public void testEditSiteKey_Add() throws Exception {
        Model model = new ExtendedModelMap();

        String result = editController.editSiteKey(null, model);

        assertEquals("editSiteKey", result);

        SiteKey siteKey = (SiteKey) model.asMap().get("currentSiteKey");

        assertNull(siteKey.getId());
    }

    @Test
    public void testEditSiteKey_Edit() throws Exception {
        Model model = new ExtendedModelMap();
        SiteKey sk1 = new SiteKey("key1", "Key1", "1<br/>2", false, false, true);
        sk1.setId(0L);

        when(credentialService.getSiteKey(0L)).thenReturn(sk1);

        String result = editController.editSiteKey(0L, model);

        assertEquals("editSiteKey", result);

        SiteKey siteKey = (SiteKey) model.asMap().get("currentSiteKey");

        assertEquals(sk1, siteKey);
        assertEquals("1\n2", siteKey.getDescription());
    }

    @Test
    public void testSaveSiteKey_Ok() throws Exception {
        Model model = mock(Model.class);
        BindingResult bindingResult = mock(BindingResult.class);

        SiteKey sk1 = new SiteKey("key1", "Key1", "1\n2", false, false, true);
        sk1.setId(0L);

        editController.saveSiteKey(sk1, bindingResult, model);

        verify(credentialService).save(sk1);
        verify(model).addAttribute("saveAction", "key1");
        assertEquals("1<br/>2", sk1.getDescription());
    }

    @Test
    public void testSaveSiteKey_Fail() throws Exception {
        Model model = mock(Model.class);
        BindingResult bindingResult = mock(BindingResult.class);

        SiteKey sk1 = new SiteKey("key1", "Key1", "1<br/>2", false, false, true);
        sk1.setId(0L);

        doThrow(new RuntimeException()).when(credentialService).save(sk1);

        editController.saveSiteKey(sk1, bindingResult, model);

        verify(credentialService).save(sk1);
        verify(model).addAttribute("saveActionFailed", "key1");
    }

    @Test
    public void testDeleteSiteKey_Ok() throws Exception {
        Model model = mock(Model.class);

        SiteKey sk1 = new SiteKey("key1", "Key1", "1<br/>2", false, false, true);
        sk1.setId(0L);

        when(credentialService.getSiteKey(0L)).thenReturn(sk1);

        editController.deleteSiteKey(0L, model);

        verify(credentialService).removeSiteKey(0L);
        verify(model).addAttribute("removeAction", "key1");
    }

    @Test
    public void testDeleteSiteKey_Fail1() throws Exception {
        Model model = mock(Model.class);

        SiteKey sk1 = new SiteKey("key1", "Key1", "1<br/>2", false, false, true);
        sk1.setId(0L);

        when(credentialService.getSiteKey(0L)).thenReturn(sk1);

        doThrow(new RuntimeException()).when(credentialService).removeSiteKey(0L);

        editController.deleteSiteKey(0L, model);

        verify(credentialService).removeSiteKey(0L);
        verify(model).addAttribute("removeActionFailed", "key1");
    }

    @Test
    public void testDeleteSiteKey_Fail2() throws Exception {
        Model model = mock(Model.class);

        SiteKey sk1 = new SiteKey("key1", "Key1", "1<br/>2", false, false, true);
        sk1.setId(0L);

        when(credentialService.getSiteKey(0L)).thenReturn(null);

        doThrow(new RuntimeException()).when(credentialService).removeSiteKey(0L);

        editController.deleteSiteKey(0L, model);

        verify(credentialService).removeSiteKey(0L);
        verify(model).addAttribute("removeActionFailed", true);
    }
}
