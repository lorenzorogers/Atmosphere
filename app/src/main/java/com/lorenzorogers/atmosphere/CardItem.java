package com.lorenzorogers.atmosphere;

public class CardItem {
    private String title;
    private String subtitle;
    private int iconResId;
    private boolean isMovable; // Add a boolean flag to control whether the card is movable

    public CardItem(String title, String subtitle, int iconResId, boolean isMovable) {
        this.title = title;
        this.subtitle = subtitle;
        this.iconResId = iconResId;
        this.isMovable = isMovable; // Initialize the movable status
    }

    // Getter and setter for the isMovable flag
    public boolean isMovable() {
        return isMovable;
    }

    public void setMovable(boolean movable) {
        isMovable = movable;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public int getIconResId() {
        return iconResId;
    }
}
