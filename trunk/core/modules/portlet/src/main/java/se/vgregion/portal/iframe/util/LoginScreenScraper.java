package se.vgregion.portal.iframe.util;

import se.vgregion.portal.iframe.model.PortletConfig;

/**
 * This action do that and that, if it has something special it is.
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public interface LoginScreenScraper {

    /**
     * Utility method for accessing form information from login screens.
     *
     * @param portletConfig - CS-iFrame configuration to know where to look.
     */
    void advancedScraping(PortletConfig portletConfig);
}
