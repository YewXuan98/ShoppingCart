package com.example.shoppingcart.Model.common;

public class ShopItem {
    String category;
    String product;
    int quantity;
    String unit;

    public ShopItem() {
    }

    public ShopItem(String category, String product, int quantity, String unit) {
        this.category = category;
        this.product = product;
        this.quantity = quantity;
        this.unit = unit;
    }

    public String getCategory() {
        return category;
    }

    public String getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
