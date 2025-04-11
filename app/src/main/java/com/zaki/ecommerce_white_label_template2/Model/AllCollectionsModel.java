package com.zaki.ecommerce_white_label_template2.Model;

public class AllCollectionsModel {
    String collectionId,collectionName;

    public AllCollectionsModel(String collectionId, String collectionName) {
        this.collectionId = collectionId;
        this.collectionName = collectionName;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }
}
