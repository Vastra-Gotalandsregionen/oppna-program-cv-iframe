package se.vgregion.portal.admin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import se.vgregion.portal.cs.domain.SiteKey;
import se.vgregion.portal.cs.service.CredentialService;

import java.util.*;

/**
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
@Controller
@RequestMapping("EDIT")
public class EditController {
    private static final Logger LOG = LoggerFactory.getLogger(EditController.class);

    @Autowired
    private CredentialService credentialService;

    @RenderMapping
    public String showEdit(Model model) {
        List<SiteKey> allSiteKeys = new ArrayList(credentialService.getAllSiteKeys());
        Collections.sort(allSiteKeys, new Comparator<SiteKey>() {
            @Override
            public int compare(SiteKey one, SiteKey other) {
                return one.getSiteKey().compareTo(other.getSiteKey());
            }
        });

        for (SiteKey siteKey : allSiteKeys) {
            siteKey.setDescription(ellipsis(siteKey.getDescription(), 25));
        }

        model.addAttribute("siteKeys", allSiteKeys);

        return "edit";
    }

    @RenderMapping(params = "action=editSiteKey")
    public String editSiteKey(@RequestParam(required = false, value = "siteKeyId") Long siteKeyId, Model model) {
        SiteKey currentSiteKey = null;
        if (siteKeyId == null) {
            currentSiteKey = new SiteKey();
        } else {
            currentSiteKey = credentialService.getSiteKey(siteKeyId);
            currentSiteKey.setDescription(convertBr(currentSiteKey.getDescription()));
        }
        model.addAttribute("currentSiteKey", currentSiteKey);

        return "editSiteKey";
    }

    @ActionMapping("saveSiteKey")
    public void saveSiteKey(@ModelAttribute("currentSiteKey") SiteKey siteKey,
            BindingResult result, Model model) {

        siteKey.setSiteKey(convertSpace(siteKey.getSiteKey()));
        siteKey.setDescription(convertNewline(siteKey.getDescription()));

        credentialService.save(siteKey);
    }


    @ActionMapping("deleteSiteKey")
    public void deleteSiteKey(@RequestParam("siteKeyId") Long siteKeyId) {
        credentialService.remove(siteKeyId);
    }

    private String convertNewline(String text) {
        return text.replace("\r", "").replace("\n", "<br/>");
    }

    private String convertBr(String text) {
        return text.replace("<br/>", "\n");
    }

    private String convertSpace(String text) {
        return text.replace(" ", "-");
    }

    private String ellipsis(String text, int len) {
        if (text.length() > len) {
            return text.substring(0, len - 2) + "...";
        } else {
            return text;
        }
    }
}
