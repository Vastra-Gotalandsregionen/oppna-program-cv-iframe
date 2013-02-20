package se.vgregion.portal.admin.controller;

import se.vgregion.portal.cs.domain.SiteKey;

import java.io.Serializable;
import java.util.*;

public class SiteKeyHelper {
    private final List<SiteKey> siteKeys;

    public SiteKeyHelper(List<SiteKey> value) {
        this.siteKeys = value;
    }

    public SiteKeyHelper(Collection<SiteKey> value) {
        this(new ArrayList<SiteKey>(value));
    }

    public List<SiteKey> get() {
        return siteKeys;
    }

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

    public SiteKeyHelper filterActive() {
        for (Iterator<SiteKey> it = siteKeys.iterator(); it.hasNext();) {
            SiteKey siteKey = it.next();
            if (!siteKey.getActive()) it.remove();
        }
        return new SiteKeyHelper(siteKeys);
    }

    public SiteKeyHelper descriptionElipsis(int maxLen) {
        for (SiteKey siteKey : siteKeys) {
            siteKey.setDescription(ellipsis(siteKey.getDescription(), maxLen));
        }
        return new SiteKeyHelper(siteKeys);
    }

    private String ellipsis(String text, int len) {
        if (text.length() > Math.max(3,len)) {
            return text.substring(0, len - 3) + "...";
        } else {
            return text;
        }
    }
}