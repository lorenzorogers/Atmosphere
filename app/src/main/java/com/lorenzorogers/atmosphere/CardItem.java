package com.lorenzorogers.atmosphere;

public class CardItem {
    private int imageResId;
    private String title;
    private String subtitle;
    private int resIcon;
    private boolean isMovable;

    public CardItem(int imageResId, String title, String subtitle, int resIcon, boolean isMovable) {
        this.imageResId = imageResId;
        this.title = title;
        this.subtitle= subtitle;
        this.resIcon = resIcon;
        this.isMovable = isMovable;
    }

    public int getIconResId() {
        return imageResId;
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
        return !isMovable;
    }

    public void setMovable(boolean movable) {
        isMovable = movable;
    }
}
