package com.lorenzorogers.atmosphere;

public class CardItem {
    private String title;
    private String subtitle;
    private int iconResId;
    private boolean isMoveable;

    public CardItem(String title, String subtitle, int iconResId, boolean isMoveable) {
        this.title = title;
        this.subtitle = subtitle;
        this.iconResId = iconResId;
        this.isMoveable = isMoveable;
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

    public boolean isMoveable() {
        return isMoveable;
    }

    public void setMoveable(boolean moveable) {
        this.isMoveable = moveable;
    }
}
