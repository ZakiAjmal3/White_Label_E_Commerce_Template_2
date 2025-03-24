package com.zaki.ecommerce_white_label_template2.Model;

public class WishListModel {
    String productId,productName,productPrice,productRating,productImg;
    int wishListImgToggle;

    public WishListModel(String productId,String productName, String productPrice, String productRating, String productImg, int wishListImgToggle) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productRating = productRating;
        this.productImg = productImg;
        this.wishListImgToggle = wishListImgToggle;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getWishListImgToggle() {
        return wishListImgToggle;
    }

    public void setWishListImgToggle(int wishListImgToggle) {
        this.wishListImgToggle = wishListImgToggle;
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

    public String getProductRating() {
        return productRating;
    }

    public void setProductRating(String productRating) {
        this.productRating = productRating;
    }
}
