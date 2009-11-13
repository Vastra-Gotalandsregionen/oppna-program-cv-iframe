/**
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
package se.vgregion.portal.iframe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.ModelMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.vgregion.portal.iframe.model.PortletConfig;
import javax.portlet.PortletPreferences;
import javax.portlet.ValidatorException;
import javax.portlet.ActionRequest;

/**
 * Portlet mode=EDIT controller.
 */
@Controller
@RequestMapping("EDIT")
public class CVEditController {
    private static Logger log = LoggerFactory.getLogger(CVEditController.class);

    /**
     * RenderMapping for edit page.
     *
     * @param model - model
     * @param prefs - portletPreferences
     * @return view
     */
    @RenderMapping
    public String editPreferences(final ModelMap model, final PortletPreferences prefs) {
        PortletConfig portletConfig = PortletConfig.getInstance(prefs);
        log.debug("editPreferences: {}", portletConfig);

        model.addAttribute("portletConfig", portletConfig);

        return "edit";
    }

    /**
     *
     * @param actionRequest - action request
     * @param prefs - portlet preferences
     * @param portletConfig - request parameter
     */
    @ActionMapping
    public void savePreferences(ActionRequest actionRequest,
                                PortletPreferences prefs,
                                @ModelAttribute("credential") PortletConfig portletConfig) {

        log.debug("savePreferences 1: {}", portletConfig);

        String src = portletConfig.getSrc();
        if (!src.startsWith("/") &&
                !src.startsWith("http://") &&
                !src.startsWith("https://") &&
                !src.startsWith("mhtml://")) {

            if (actionRequest.isSecure()) {
                src =  "https://" + src;
            } else {
                src = "http://" + src;
            }
            portletConfig.setSrc(src);
        }

        log.debug("savePreferences 2: {}", portletConfig);

        try {
            portletConfig.store(prefs);
        } catch (ValidatorException e) {
            e.printStackTrace();
        }

        log.debug("src: {}", prefs.getValue("src", ""));
    }
}
