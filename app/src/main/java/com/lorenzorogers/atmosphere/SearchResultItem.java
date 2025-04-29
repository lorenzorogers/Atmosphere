package com.lorenzorogers.atmosphere;

public class SearchResultItem {
    private String title;
    private String subtitle;

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
