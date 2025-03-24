package com.zaki.ecommerce_white_label_template2.Model;

public class ProductsRecyclerModel {
    String productId,productName,productPrice;
    int productImg,wishlistToggle;

    public ProductsRecyclerModel(String productId,String productName, String productPrice, int productImg,int wishlistToggle) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImg = productImg;
        this.wishlistToggle = wishlistToggle;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getWishlistToggle() {
        return wishlistToggle;
    }

    public void setWishlistToggle(int wishlistToggle) {
        this.wishlistToggle = wishlistToggle;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductImg() {
        return productImg;
    }

    public void setProductImg(int productImg) {
        this.productImg = productImg;
    }
}
