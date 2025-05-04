package com.lorenzorogers.atmosphere;

public class CardItem {
    private final String title;
    private final String subtitle;
    private final int resIcon;
    private boolean isMovable;

    public CardItem(String title, String subtitle, int resIcon, boolean isMovable) {
        this.title = title;
        this.subtitle = subtitle;
        this.resIcon = resIcon;
        this.isMovable = isMovable;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public int getResIcon() {
        return resIcon;
    }

    public boolean isMovable() {
        return isMovable;
    }
}