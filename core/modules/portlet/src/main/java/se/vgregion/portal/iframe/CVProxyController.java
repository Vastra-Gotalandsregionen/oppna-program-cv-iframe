package se.vgregion.portal.iframe;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This action do that and that, if it has something special it is.
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
@Controller
@RequestMapping("PROXY")
public class CVProxyController {
    Logger log = LoggerFactory.getLogger(CVProxyController.class);

    @RenderMapping
    public String proxy() {
        log.debug("proxy");

        return "proxy";
    }
}
