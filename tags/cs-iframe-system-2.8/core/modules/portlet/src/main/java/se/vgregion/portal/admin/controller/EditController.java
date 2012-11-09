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
 * Controller class for the edit view.
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
@Controller
@RequestMapping("EDIT")
public class EditController {
    private static final Logger LOG = LoggerFactory.getLogger(EditController.class);

    @Autowired
    private CredentialService credentialService;

    /**
     * Shows the edit view.
     *
     * @param model Model
     * @return a view
     */
    @RenderMapping
    public String showEdit(Model model) {
        final int maxLen = 45;
        List<SiteKey> siteKeys = new SiteKeyHelper(credentialService.getAllSiteKeys()).
                orderBySiteKey().descriptionElipsis(maxLen).get();

        model.addAttribute("siteKeys", siteKeys);

        return "edit";
    }

    /**
     * Shows the edit site key view.
     *
     * @param siteKeyId siteKeyId
     * @param model model
     * @return the view name
     */
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

    /**
     * Action mapping which saves the site key.
     *
     * @param siteKey the site key
     * @param result BindingResult
     * @param model Model
     */
    @ActionMapping("saveSiteKey")
    public void saveSiteKey(@ModelAttribute("currentSiteKey") SiteKey siteKey,
            BindingResult result, Model model) {
        try {
            siteKey.setSiteKey(convertSpace(siteKey.getSiteKey()));
            siteKey.setDescription(convertNewline(siteKey.getDescription()));

            credentialService.save(siteKey);

            model.addAttribute("saveAction", siteKey.getSiteKey());
        } catch (RuntimeException ex) {
            model.addAttribute("saveActionFailed", siteKey.getSiteKey());
        }
    }

    /**
     * Action mapping which deletes a site key.
     *
     * @param siteKeyId the site key
     * @param model Model
     */
    @ActionMapping("deleteSiteKey")
    public void deleteSiteKey(@RequestParam("siteKeyId") Long siteKeyId, Model model) {
        SiteKey siteKey = null;
        try {
            siteKey = credentialService.getSiteKey(siteKeyId);

            credentialService.removeSiteKey(siteKeyId);

            model.addAttribute("removeAction", siteKey.getSiteKey());
        } catch (RuntimeException ex) {
            if (siteKey != null) {
                model.addAttribute("removeActionFailed", siteKey.getSiteKey());
            } else {
                model.addAttribute("removeActionFailed", true);
            }
        }
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

}
