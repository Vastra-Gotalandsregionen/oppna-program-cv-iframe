package se.vgregion.portal.iframe.el;

import java.util.regex.Pattern;

/**
 * Purpose of this class is to be able to use RegExp inside EL expressions in the JSP.
 * 
 * @author Claes Lundahl, vgrid=clalu4, Robert de Bésche, vgrid=robde1
 */
public final class RegExp {

    private RegExp() {

    }

    /**
     * The method evaluates whether a sequence of characters matches a given pattern.
     *
     * @param pattern the pattern String
     * @param str the {@link CharSequence}
     * @return <code>true</code> if it matches or <code>false</code> otherwise
     */
    public static boolean matches(String pattern, CharSequence str) {
        return Pattern.compile(pattern).matcher(str).matches();
    }

}
