package com.lorenzorogers.atmosphere;

public class SearchResultItem {
    private final String title;
    private final String subtitle;

    public SearchResultItem(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }
}
