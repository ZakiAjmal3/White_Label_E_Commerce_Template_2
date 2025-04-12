package com.zaki.ecommerce_white_label_template2.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.zaki.ecommerce_white_label_template2.Activities.HomePageActivity;
import com.zaki.ecommerce_white_label_template2.Activities.SingleProductDetailsActivity;
import com.zaki.ecommerce_white_label_template2.Fragment.WishListFragment;
import com.zaki.ecommerce_white_label_template2.Model.ProductDetailsModel;
import com.zaki.ecommerce_white_label_template2.Model.ProductsRecyclerModel;
import com.zaki.ecommerce_white_label_template2.Model.WishListModel;
import com.zaki.ecommerce_white_label_template2.R;
import com.zaki.ecommerce_white_label_template2.Utils.Constant;
import com.zaki.ecommerce_white_label_template2.Utils.MySingletonFragment;
import com.zaki.ecommerce_white_label_template2.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductRecyclerForFragmentAdapter extends RecyclerView.Adapter<ProductRecyclerForFragmentAdapter.ViewHolder> {
    ArrayList<ProductDetailsModel> productArrayList;
    Fragment context;
    SpannableStringBuilder spannableText;
    SessionManager sessionManager;
    String authToken;
    public ProductRecyclerForFragmentAdapter(ArrayList<ProductDetailsModel> productArrayList, Fragment context) {
        this.productArrayList = productArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductRecyclerForFragmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_product_recycler_item_layout,parent,false);
        this.sessionManager = new SessionManager(context.getContext());
        authToken = sessionManager.getUserData().get("authToken");
        return new ProductRecyclerForFragmentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductRecyclerForFragmentAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.productName.setText(productArrayList.get(position).getProductTitle());

        if (!productArrayList.get(position).getDiscountAmount().equals("0")) {
            String originalPrice = productArrayList.get(position).getProductMRP();
            String disPercent = productArrayList.get(position).getDiscountPercentage();
            String sellingPrice = productArrayList.get(position).getProductPrice();

            // Create a SpannableString for the original price with strikethrough
            SpannableString spannableOriginalPrice = new SpannableString("₹" + originalPrice);
            spannableOriginalPrice.setSpan(new StrikethroughSpan(), 0, spannableOriginalPrice.length(), 0);

            // Combine selling price + original price
            spannableText = new SpannableStringBuilder();
            spannableText.append("₹").append(sellingPrice).append(" ");
            spannableText.append(spannableOriginalPrice);

            // Set combined text to productPrice
            holder.productPrice.setText(spannableText);

            // Set discount % separately to productDiscount with green color
            String discountText = "-" + disPercent + "%";
            SpannableString coloredDiscount = new SpannableString(discountText);
            coloredDiscount.setSpan(new ForegroundColorSpan(Color.GREEN), 0, discountText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.productDiscount.setText(" (" + coloredDiscount + ")");
            holder.productDiscount.setVisibility(View.VISIBLE);
        } else {
            // No discount, just show the selling price
            holder.productPrice.setText("₹" + productArrayList.get(position).getProductPrice());
            holder.productDiscount.setVisibility(View.GONE);
        }

        Glide.with(context).load(productArrayList.get(position).getProductImagesModelsArrList().get(0).getProductImage()).into(holder.productImg);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getContext(), SingleProductDetailsActivity.class);
                intent.putExtra("productId",productArrayList.get(position).getProductId());
                context.startActivity(intent);
            }
        });
//        ArrayList<WishListModel> wishListModels = sessionManager.getWishList();
//        for (int i = 0; i < wishListModels.size(); i++) {
//            if (wishListModels.get(i).getProductId().equals(productArrayList.get(position).getProductId())) {
//                holder.wishListImg.setImageResource(R.drawable.ic_heart_red);
//                productArrayList.get(position).setWishlistToggle(1);
//                break;
//            } else {
//                holder.wishListImg.setImageResource(R.drawable.ic_heart_grey);
//                productArrayList.get(position).setWishlistToggle(0);
//            }
//        }
        int wishlistState = productArrayList.get(position).getWishListImgToggle();
        if (wishlistState == 1){
            holder.wishListImg.setImageResource(R.drawable.ic_heart_red);
            Log.e("WishList","WishListTrue");
        }else {
            holder.wishListImg.setImageResource(R.drawable.ic_heart_grey);
            Log.e("WishList","WishListFalse");
        }
        holder.wishListImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.wishListImg.animate()
                        .scaleX(1.2f)
                        .scaleY(1.2f)
                        .setDuration(150)
                        .withEndAction(() -> {
                            holder.wishListImg.animate().scaleX(1f).scaleY(1f).start();
                        }).start();
                setWishlistCount();
                int state;
                state = productArrayList.get(position).getWishListImgToggle();
                if (sessionManager.isLoggedIn()) {
                    if (state == 0) {
                        addToWishList(position);
                        holder.wishListImg.setImageResource(R.drawable.ic_heart_red);
                        productArrayList.get(position).setWishListImgToggle(1);
                    } else {
                        removeFromWishList(position);
                        holder.wishListImg.setImageResource(R.drawable.ic_heart_grey);
                        productArrayList.get(position).setWishListImgToggle(0);
                    }
                }else {
                    if (state == 0) {
                        sessionManager.saveWishList(productArrayList.get(position));
                        holder.wishListImg.setImageResource(R.drawable.ic_heart_red);
                        productArrayList.get(position).setWishListImgToggle(1);
                        Toast.makeText(context.getContext(), "Item added to WishList", Toast.LENGTH_SHORT).show();
                    } else {
                        sessionManager.removeWishListItem(productArrayList.get(position).getProductId());
                        holder.wishListImg.setImageResource(R.drawable.ic_heart_grey);
                        productArrayList.get(position).setWishListImgToggle(0);
                        Toast.makeText(context.getContext(), "Item removed from WishList", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });

    }
    private void setWishlistCount() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                HomePageActivity activity = (HomePageActivity) context.getContext();
                activity.setWishlistCount();
            }
        }, 1500);  // Match the duration of the logo animation
    }

    private void removeFromWishList(int position) {
        String orderURL = Constant.BASE_URL + "wishlist/remove/" + productArrayList.get(position).getProductId();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, orderURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(context.getContext(), "Item removed from wishlist", Toast.LENGTH_SHORT).show();
                        sessionManager.removeWishListItem(productArrayList.get(position).getProductId());
                        if (context instanceof WishListFragment){
                            productArrayList.remove(position);
                            ((WishListFragment) context).checkWishListItemArraySize();
                        }
                        sessionManager.getWishlistFromServer();
                        notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Error: " + error.toString();
                        if (error.networkResponse != null) {
                            try {
                                // Parse the error response
                                String jsonError = new String(error.networkResponse.data);
                                JSONObject jsonObject = new JSONObject(jsonError);
                                String message = jsonObject.optString("message", "Unknown error");
                                // Now you can use the message
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        Log.e("ExamListError", errorMessage);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + authToken);
                return headers;
            }
        };
        MySingletonFragment.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    private void addToWishList(int position) {
        String orderURL = Constant.BASE_URL + "wishlist";
        String productId = productArrayList.get(position).getProductId();
        String userId = sessionManager.getUserData().get("userId");

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("productId", productId);
            jsonObject.put("userId", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, orderURL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(context.getContext(), "Item added to wishlist", Toast.LENGTH_SHORT).show();
                        sessionManager.getWishlistFromServer();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Error: " + error.toString();
                        if (error.networkResponse != null) {
                            try {
                                // Parse the error response
                                String jsonError = new String(error.networkResponse.data);
                                JSONObject jsonObject = new JSONObject(jsonError);
                                String message = jsonObject.optString("message", "Unknown error");
                                // Now you can use the message
                                Toast.makeText(context.getContext(), message, Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        Log.e("ExamListError", errorMessage);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + authToken);
                return headers;
            }
        };
        MySingletonFragment.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName,productPrice,productDiscount;
        ImageView productImg,wishListImg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.productNameTxt);
            productPrice = itemView.findViewById(R.id.productPriceTxt);
            productDiscount = itemView.findViewById(R.id.productDiscountTxt);
            productImg = itemView.findViewById(R.id.productImg);
            wishListImg = itemView.findViewById(R.id.heartImg);

        }
    }
}