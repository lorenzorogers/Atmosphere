package com.lorenzorogers.atmosphere;

public class CardItem {
    private String title;
    private String subtitle;
    private int iconResId;
    private boolean movable;

    public CardItem(String title, String subtitle, int iconResId, boolean movable) {
        this.title = title;
        this.subtitle = subtitle;
        this.iconResId = iconResId;
        this.movable = movable;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public int getIconResId() {
        return iconResId;
    }

    public boolean isMovable() {
        return movable;
    }

    public void setMovable(boolean movable) {
        this.movable = movable;
    }
}
