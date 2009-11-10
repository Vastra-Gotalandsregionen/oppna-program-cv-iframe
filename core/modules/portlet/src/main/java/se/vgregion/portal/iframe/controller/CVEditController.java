/**
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
package se.vgregion.portal.iframe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;

import org.springframework.web.bind.annotation.*;
import org.springframework.ui.ModelMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.vgregion.portal.iframe.model.Credential;
import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;
import javax.portlet.ActionRequest;
import java.io.IOException;

@Controller
@RequestMapping("EDIT")
public class CVEditController {
    private static Logger log = LoggerFactory.getLogger(CVEditController.class);

    @RenderMapping
    public String editPreferences(ModelMap model, PortletPreferences prefs) {
        Credential credential = Credential.getInstance(prefs);
        log.debug("editPreferences: {}", credential);

        model.addAttribute("credential", credential);

        return "edit";
    }

    @ActionMapping
    public void savePreferences(ActionRequest actionRequest,
                                PortletPreferences prefs,
                                @ModelAttribute("credential") Credential credential)
            throws ReadOnlyException, ValidatorException, IOException {

        log.debug("savePreferences 1: {}", credential);

        String src = credential.getSrc();
        if (!src.startsWith("/") &&
                !src.startsWith("http://") &&
                !src.startsWith("https://") &&
                !src.startsWith("mhtml://")) {

            credential.setSrc((actionRequest.isSecure() ? "https" : "http") + "://" + src);
        }

        log.debug("savePreferences 2: {}", credential);

        credential.store(prefs);

        log.debug("src: {}", prefs.getValue("src", ""));
    }
}
