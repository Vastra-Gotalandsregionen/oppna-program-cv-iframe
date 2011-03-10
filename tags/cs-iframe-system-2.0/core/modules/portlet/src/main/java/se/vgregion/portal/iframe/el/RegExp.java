package se.vgregion.portal.iframe.el;

import java.util.regex.Pattern;

/**
 * Purpose of this class is to be able to use RegExp inside EL expressions in the JSP.
 * 
 * @author Claes Lundahl, vgrid=clalu4, Robert de Bésche, vgrid=robde1
 */
public class RegExp {

    public static boolean matches(String pattern, CharSequence str) {
        return Pattern.compile(pattern).matcher(str).matches();
    }

}
