package com.zaki.ecommerce_white_label_template2.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zaki.ecommerce_white_label_template2.Model.CartItemModel;
import com.zaki.ecommerce_white_label_template2.Model.WishListModel;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SessionManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private static  final String PREF_NAME = "SessionPrefs";
    private static final String WISHLIST_KEY = "wishlist";
    private static final String CART_KEY = "cart";

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();
    }
    public void saveWishList(WishListModel wishListModel){

        ArrayList<WishListModel> wishList = getWishList(); // Retrieve the existing list
        wishList.add(wishListModel); // Add new item

        String json = gson.toJson(wishList); // Convert to JSON
        editor.putString(WISHLIST_KEY, json);
        editor.apply();
    }
    public void saveCart(CartItemModel cartItemModel){

        ArrayList<CartItemModel> cartItemModels = getCart(); // Retrieve the existing list
        cartItemModels.add(cartItemModel); // Add new item

        String json = gson.toJson(cartItemModels); // Convert to JSON
        editor.putString(CART_KEY, json);
        editor.apply();
    }

    // Get cart
    public ArrayList<CartItemModel> getCart() {
        String json = sharedPreferences.getString(CART_KEY, null);
        Type type = new TypeToken<ArrayList<CartItemModel>>() {}.getType();

        if (json != null) {
            return gson.fromJson(json, type);
        } else {
            return new ArrayList<>(); // Return empty list if no data found
        }
    }
    // Get wishlist
    public ArrayList<WishListModel> getWishList() {
        String json = sharedPreferences.getString(WISHLIST_KEY, null);
        Type type = new TypeToken<ArrayList<WishListModel>>() {}.getType();

        if (json != null) {
            return gson.fromJson(json, type);
        } else {
            return new ArrayList<>(); // Return empty list if no data found
        }
    }
    // Remove a single item from cart
    public void removeCartItem(CartItemModel cartItemModel) {
        ArrayList<CartItemModel> cartList = getCart();

        // Remove item using an Iterator (compatible with all Android versions)
        for (int i = 0; i < cartList.size(); i++) {
            if (cartList.get(i).getProductId().equals(cartItemModel.getProductId())) {
                cartList.remove(i);
                break; // Exit loop after removing the first matching item
            }
        }

        // Save updated list back to SharedPreferences
        String json = gson.toJson(cartList);
        editor.putString(WISHLIST_KEY, json);
        editor.apply();
    }
    // Remove a single item from wishlist
    public void removeWishListItem(WishListModel wishListModel) {
        ArrayList<WishListModel> wishList = getWishList();

        // Remove item using an Iterator (compatible with all Android versions)
        for (int i = 0; i < wishList.size(); i++) {
            if (wishList.get(i).getProductId().equals(wishListModel.getProductId())) {
                wishList.remove(i);
                break; // Exit loop after removing the first matching item
            }
        }

        // Save updated list back to SharedPreferences
        String json = gson.toJson(wishList);
        editor.putString(WISHLIST_KEY, json);
        editor.apply();
    }
    // Clear wishlist
    public void clearWishList() {
        editor.remove(WISHLIST_KEY);
        editor.apply();
    }
}