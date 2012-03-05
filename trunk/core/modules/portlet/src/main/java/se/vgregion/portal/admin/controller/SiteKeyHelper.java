package se.vgregion.portal.admin.controller;

import se.vgregion.portal.cs.domain.SiteKey;

import java.io.Serializable;
import java.util.*;

public class SiteKeyHelper {
    private final List<SiteKey> siteKeys;

    /**
     * Constructor.
     *
     * @param value List of {@link SiteKey}s
     */
    public SiteKeyHelper(List<SiteKey> value) {
        this.siteKeys = value;
    }

    /**
     * Constructor.
     *
     * @param value Collection of {@link SiteKey}s
     */
    public SiteKeyHelper(Collection<SiteKey> value) {
        this(new ArrayList<SiteKey>(value));
    }

    /**
     * Get the {@link SiteKey}s.
     *
     * @return the {@link SiteKey}s
     */
    public List<SiteKey> get() {
        return siteKeys;
    }

    /**
     * Creates a new instance of {@link SiteKeyHelper} where the site key order is determined by the site key attribute.
     *
     * @return a new instance of {@link SiteKeyHelper}
     */
    public SiteKeyHelper orderBySiteKey() {
        Collections.sort(siteKeys, new SiteKeyComparator());
        return new SiteKeyHelper(siteKeys);
    }

    private static class SiteKeyComparator implements Comparator<SiteKey>, Serializable {
        private static final long serialVersionUID = -4400606224805136030L;

        @Override
        public int compare(SiteKey one, SiteKey other) {
            return one.getSiteKey().toLowerCase().compareTo(other.getSiteKey().toLowerCase());
        }
    }

    /**
     * Creates a new instance of {@link SiteKeyHelper} with only active {@link SiteKey}s.
     *
     * @return a new instance of {@link SiteKeyHelper}
     */
    public SiteKeyHelper filterActive() {
        for (Iterator<SiteKey> it = siteKeys.iterator(); it.hasNext();) {
            SiteKey siteKey = it.next();
            if (!siteKey.getActive()) {
                it.remove();
            }
        }
        return new SiteKeyHelper(siteKeys);
    }

    /**
     * Creates a new instance of {@link SiteKeyHelper} where the {@link SiteKey}s' descriptions are cut so that they do
     * not exceed the length of <code>maxLen</code>.
     *
     * @param maxLen the maximum length of the {@link SiteKey}s' descriptions.
     * @return a new instance of {@link SiteKeyHelper}
     */
    public SiteKeyHelper descriptionElipsis(int maxLen) {
        for (SiteKey siteKey : siteKeys) {
            siteKey.setDescription(ellipsis(siteKey.getDescription(), maxLen));
        }
        return new SiteKeyHelper(siteKeys);
    }

    private String ellipsis(String text, int len) {
        final int three = 3;
        if (text.length() > Math.max(three, len)) {
            return text.substring(0, len - three) + "...";
        } else {
            return text;
        }
    }
}
