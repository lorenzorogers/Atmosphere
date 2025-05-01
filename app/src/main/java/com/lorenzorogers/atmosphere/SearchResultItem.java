package com.lorenzorogers.atmosphere;

public class SearchResultItem {
    private final String title;
    private final String subtitle;

    private final Double latitude;
    private final Double longitude;

    public SearchResultItem(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
        this.latitude = null;
        this.longitude = null;
    }

    public SearchResultItem(String title, String subtitle, double latitude, double longitude) {
        this.title = title;
        this.subtitle = subtitle;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public boolean hasCoordinates() {
        return latitude != null && longitude != null;
    }
}
