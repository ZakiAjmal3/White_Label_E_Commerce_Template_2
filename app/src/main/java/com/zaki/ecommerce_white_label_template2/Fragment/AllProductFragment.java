package com.zaki.ecommerce_white_label_template2.Fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zaki.ecommerce_white_label_template2.Adapter.ProductRecyclerForFragmentAdapter;
import com.zaki.ecommerce_white_label_template2.Model.ProductDetailsModel;
import com.zaki.ecommerce_white_label_template2.Model.ProductImagesModel;
import com.zaki.ecommerce_white_label_template2.Model.ProductsRecyclerModel;
import com.zaki.ecommerce_white_label_template2.R;
import com.zaki.ecommerce_white_label_template2.Utils.Constant;
import com.zaki.ecommerce_white_label_template2.Utils.MySingletonFragment;
import com.zaki.ecommerce_white_label_template2.Utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class AllProductFragment extends Fragment {

    RecyclerView allProductRecyclerView;
    ProductRecyclerForFragmentAdapter productRecyclerForFragmentAdapter;
    ArrayList<ProductDetailsModel> productsRecyclerModelArrayList;
    NestedScrollView mainNestedLayout;
    int itemPerPage = 10, totalPages = 1,currentPage = 1;
    ProgressBar nextItemLoadingProgressBar;
    SessionManager sessionManager;
    String authToken;
    Dialog progressBarDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_all_products, container, false);

        sessionManager = new SessionManager(getContext());
        authToken = sessionManager.getUserData().get("authToken");

        allProductRecyclerView = view.findViewById(R.id.allProductRecyclerView);
        allProductRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));

        allProductRecyclerView.setVisibility(View.GONE);

        mainNestedLayout = view.findViewById(R.id.mainNestedLayout);
        nextItemLoadingProgressBar = view.findViewById(R.id.nextItemLoadingProgressBar);
//        setUpSpinners();
        progressBarDialog = new Dialog(getContext());
        progressBarDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressBarDialog.setContentView(R.layout.progress_bar_dialog);
        progressBarDialog.setCancelable(false);
        progressBarDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressBarDialog.getWindow().setGravity(Gravity.CENTER); // Center the dialog
        progressBarDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT); // Adjust the size
        progressBarDialog.show();
        productsRecyclerModelArrayList = new ArrayList<>();

        getAllProducts();

        mainNestedLayout.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // Check if we are near the bottom, but leave a small threshold to avoid issues with small screens
                Log.e("ScrollDebug", "scrollY: " + scrollY +
                        " measuredHeight: " + v.getChildAt(0).getMeasuredHeight() +
                        " scrollHeight: " + v.getMeasuredHeight());
                int scrollThreshold = 50; // threshold to trigger load more data
                int diff = (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) - scrollY;
                Log.e("ScrollDebug", "diff: " + diff);
                // Check if we have scrolled to the bottom or near bottom
                if (diff <= scrollThreshold && currentPage <= totalPages) {
                    // Only increment the page and load more data if there's more data to load
                    currentPage++;
                    nextItemLoadingProgressBar.setVisibility(View.VISIBLE);
                    getAllProducts();
                    Log.e("Scroll","Scroll Happened");
                }else {
                    nextItemLoadingProgressBar.setVisibility(View.GONE);
                }
            }
        });

        return view;
    }
//    private void setUpSpinners() {
//        String[] languages = getResources().getStringArray(R.array.price_sorting_item);
//        dropdownStatusArrayAdapter = new ArrayAdapter<>(getContext(), R.layout.drop_down_item_text_layout, languages);
//        autoCompPriceTV.setAdapter(dropdownStatusArrayAdapter);
//        autoCompPriceTV.setDropDownBackgroundDrawable(new ColorDrawable(Color.WHITE));
//
//        String[] languages2 = getResources().getStringArray(R.array.old_new_sorting_item);
//        dropdownFilterArrayAdapter = new ArrayAdapter<>(getContext(), R.layout.drop_down_item_text_layout, languages2);
//        autoCompSortByTV.setAdapter(dropdownFilterArrayAdapter);
//        autoCompSortByTV.setDropDownBackgroundDrawable(new ColorDrawable(Color.WHITE));
//
//        autoCompPriceTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                sortProductsByPrice(position);
//            }
//        });
//        autoCompSortByTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                sortProductsByDate(position);
//            }
//        });
//
//    }
    public void sortProductsByPrice(int position) {
        if (position == 0) { // Assuming 0 is for "Low to High"
            Collections.sort(productsRecyclerModelArrayList, new Comparator<ProductDetailsModel>() {
                @Override
                public int compare(ProductDetailsModel o1, ProductDetailsModel o2) {
                    return Integer.compare(Integer.parseInt(o2.getProductPrice()), Integer.parseInt(o1.getProductPrice()));
                }
            });
        } else if (position == 1) { // Assuming 1 is for "High to Low"
            Collections.sort(productsRecyclerModelArrayList, new Comparator<ProductDetailsModel>() {
                @Override
                public int compare(ProductDetailsModel o1, ProductDetailsModel o2) {
                    return Integer.compare(Integer.parseInt(o1.getProductPrice()), Integer.parseInt(o2.getProductPrice()));
                }
            });
        }

        productRecyclerForFragmentAdapter.notifyDataSetChanged();
    }

    // Sort by date (for example, newest to oldest)
    public void sortProductsByDate(int position) {
        if (productsRecyclerModelArrayList == null || productsRecyclerModelArrayList.isEmpty()) {
            Log.e("Sort Error", "Product list is null or empty.");
            return; // Exit if list is null or empty
        }
        if (position == 0) { // Assuming 0 is for "Newest First"
            Collections.reverse(productsRecyclerModelArrayList); // Reverse order (assuming default order is oldest to newest)
        } else if (position == 1) { // Assuming 1 is for "Oldest First"
            Collections.sort(productsRecyclerModelArrayList, new Comparator<ProductDetailsModel>() {
                @Override
                public int compare(ProductDetailsModel o1, ProductDetailsModel o2) {
                    return o1.getProductTitle().compareTo(o2.getProductTitle()); // Example sorting by name instead of actual date
                }
            });
        }

        productRecyclerForFragmentAdapter.notifyDataSetChanged();
    }
    private void getAllProducts() {
        String newArrivalURL = Constant.BASE_URL + "product/" + sessionManager.getStoreId() + "?pageNumber=" + currentPage + "&pageSize=" + itemPerPage;
        Log.e("ProductsURL", newArrivalURL);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, newArrivalURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray dataArray = response.optJSONArray("data");
                            if (dataArray != null) {
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject productObj = dataArray.optJSONObject(i);
                                    if (productObj != null) {
                                        String productId = productObj.optString("_id", null);
                                        String title = productObj.optString("title", null);

                                        JSONObject slugObj = productObj.optJSONObject("meta");
                                        String slug = (slugObj != null) ? slugObj.optString("slug", null) : null;

                                        String MRP = productObj.optString("MRP", null);
                                        String price = productObj.optString("price", null);

                                        JSONObject discountObj = productObj.optJSONObject("discount");
                                        String discountAmount = (discountObj != null) ? discountObj.optString("amount", null) : null;
                                        String discountPercentage = (discountObj != null) ? discountObj.optString("percentage", null) : null;

                                        String stock = productObj.optString("stock", null);
                                        String description = productObj.optString("description", null);

                                        JSONArray tagsArray = productObj.optJSONArray("tags");
                                        String tags = (tagsArray != null) ? parseTags(tagsArray) : null;

                                        String SKU = productObj.optString("SKU", null);

                                        ArrayList<ProductImagesModel> imagesList = new ArrayList<>();
                                        JSONArray imageArray = productObj.optJSONArray("images");
                                        if (imageArray != null) {
                                            for (int j = 0; j < imageArray.length(); j++) {
                                                String imageUrl = imageArray.optString(j, null);
                                                if (imageUrl != null) {
                                                    Log.e("JSONIMG", imageUrl);
                                                    imagesList.add(new ProductImagesModel(imageUrl));
                                                }
                                            }
                                        }

                                        String store = productObj.optString("store", null);
                                        String category = productObj.optString("category", null);
                                        String inputTag = productObj.optString("inputTag", null);

                                        productsRecyclerModelArrayList.add(new ProductDetailsModel(productId, title, slug, MRP, price,
                                                discountAmount, discountPercentage, stock, description, tags, SKU, store,
                                                category, inputTag, "4", 0, imagesList));
                                    }
                                }

                                if (!productsRecyclerModelArrayList.isEmpty()) {
                                    if (changingWishListIcon()) {
                                        productRecyclerForFragmentAdapter = new ProductRecyclerForFragmentAdapter(productsRecyclerModelArrayList, AllProductFragment.this);
                                        allProductRecyclerView.setAdapter(productRecyclerForFragmentAdapter);
                                        allProductRecyclerView.setVisibility(View.VISIBLE);
                                        progressBarDialog.dismiss();
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            Log.e("JSONError", "Parsing error: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Error: " + error.toString();
                        if (error.networkResponse != null) {
                            try {
                                String jsonError = new String(error.networkResponse.data);
                                JSONObject jsonObject = new JSONObject(jsonError);
                                String message = jsonObject.optString("message", "Unknown error");
                                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
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

        MySingletonFragment.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private String parseTags(JSONArray tagsArray) throws JSONException {
        StringBuilder tags = new StringBuilder();
        for (int j = 0; j < tagsArray.length(); j++) {
            tags.append(tagsArray.getString(j)).append(", ");
        }
        if (tags.length() > 0) {
            tags.setLength(tags.length() - 2); // Remove trailing comma and space
        }
        return tags.toString();
    }
    private boolean changingWishListIcon() {
        ArrayList<ProductDetailsModel> wishlistItem = sessionManager.getWishList();
        Log.e("wishlist",wishlistItem.toString());
        for (int i = 0; i < productsRecyclerModelArrayList.size(); i++) {
            for (int j = 0; j < wishlistItem.size(); j++) {
                if (productsRecyclerModelArrayList.get(i).getProductId().equals(wishlistItem.get(j).getProductId())) {
                    productsRecyclerModelArrayList.get(i).setWishListImgToggle(1);
                    Log.e("changing","true");
                }
            }
        }
        return true;
    }

    public void onTabVisible() {
        Log.d("TabVisible", "AllProductFragment is now visible");
        if (changingWishListIcon()) {
            if (productRecyclerForFragmentAdapter != null) {
                productRecyclerForFragmentAdapter.notifyDataSetChanged();
                Log.e("changing","true");
            }else {
                Log.e("changing","false");
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("resume", "true");
    }
}
