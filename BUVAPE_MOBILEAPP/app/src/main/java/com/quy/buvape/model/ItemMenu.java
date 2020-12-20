package com.quy.buvape.model;

public class ItemMenu {
    private String id;
    private String nameItem;
    private int imageItem;

    public ItemMenu(String id, String nameItem, int imageItem) {
        this.id = id;
        this.nameItem = nameItem;
        this.imageItem = imageItem;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameItem() {
        return nameItem;
    }

    public void setNameItem(String nameItem) {
        this.nameItem = nameItem;
    }

    public int getImageItem() {
        return imageItem;
    }

    public void setImageItem(int imageItem) {
        this.imageItem = imageItem;
    }
}
