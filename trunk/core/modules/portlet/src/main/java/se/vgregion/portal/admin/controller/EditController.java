package se.vgregion.portal.admin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import se.vgregion.portal.csiframe.service.CredentialService;

/**
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
@Controller
@RequestMapping("EDIT")
public class EditController {
    private static final Logger LOG = LoggerFactory.getLogger(EditController.class);

    @Autowired
    private CredentialService userSiteCredentialService;

    @RenderMapping
    public String showEdit() {
        return "edit";
    }
}
