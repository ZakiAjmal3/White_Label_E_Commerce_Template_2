package com.zaki.ecommerce_white_label_template2.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.zaki.ecommerce_white_label_template2.Adapter.ProductRecyclerForActivityAdapter;
import com.zaki.ecommerce_white_label_template2.Adapter.ProductRecyclerForFragmentAdapter;
import com.zaki.ecommerce_white_label_template2.Model.CartItemModel;
import com.zaki.ecommerce_white_label_template2.Model.ProductDetailsModel;
import com.zaki.ecommerce_white_label_template2.Model.ProductImagesModel;
import com.zaki.ecommerce_white_label_template2.Model.ProductsRecyclerModel;
import com.zaki.ecommerce_white_label_template2.R;
import com.zaki.ecommerce_white_label_template2.Utils.Constant;
import com.zaki.ecommerce_white_label_template2.Utils.MySingleton;
import com.zaki.ecommerce_white_label_template2.Utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SingleProductDetailsActivity extends AppCompatActivity {
    ImageView backBtn,productImg, quantityPlusIV, quantityMinusIV;
    TextView productTitleTxt, productPriceTxt, productDiscountTxt,quantityTxt;
    WebView descriptionWebView;
    int quantityInt = 1;
    RecyclerView newProductRecyclerView,popularProductRecyclerView;
    ProductRecyclerForActivityAdapter productRecyclerForActivityAdapter;
    ArrayList<ProductDetailsModel> singleProductArrayList,productsRecyclerModelArrayList;
    NestedScrollView nestedScrollView;
    MaterialCardView viewAllCard1,viewAllCard2;
    Button addToCartBtn;
    SessionManager sessionManager;
    String authToken;
    Dialog progressBarDialog;
    String productIdStr,productNameStr,productRatingStr, productDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product_details);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE){
            EdgeToEdge.enable(this);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }
        sessionManager = new SessionManager(SingleProductDetailsActivity.this);
        authToken = sessionManager.getUserData().get("authToken");

        progressBarDialog = new Dialog(SingleProductDetailsActivity.this);
        progressBarDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressBarDialog.setContentView(R.layout.progress_bar_dialog);
        progressBarDialog.setCancelable(false);
        progressBarDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressBarDialog.getWindow().setGravity(Gravity.CENTER); // Center the dialog
        progressBarDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT); // Adjust the size
        progressBarDialog.show();

        backBtn = findViewById(R.id.imgBack);
        productImg = findViewById(R.id.productImg);
        productTitleTxt = findViewById(R.id.productTitleTxt);
        productPriceTxt = findViewById(R.id.productPriceTxt);
        productDiscountTxt = findViewById(R.id.productDiscountTxtTxt);
        nestedScrollView = findViewById(R.id.mainNestedLayout);
        descriptionWebView = findViewById(R.id.productionDescriptionWebView);

        viewAllCard1 = findViewById(R.id.viewAllCard1);
        viewAllCard2 = findViewById(R.id.viewAllCard2);

        addToCartBtn = findViewById(R.id.addToCartBtn);

        productIdStr = getIntent().getStringExtra("productId");

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        viewAllCard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SingleProductDetailsActivity.this, HomePageActivity.class));
            }
        });
        viewAllCard2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SingleProductDetailsActivity.this, HomePageActivity.class));
            }
        });

        quantityPlusIV = findViewById(R.id.quantityPlusTxt);
        quantityMinusIV = findViewById(R.id.quantityMinusTxt);
        quantityTxt = findViewById(R.id.quantityTxt);

        quantityTxt.setText(String.valueOf(quantityInt));

        quantityPlusIV.setOnClickListener(v -> {
            quantityInt++;
            quantityTxt.setText(String.valueOf(quantityInt));
        });
        quantityMinusIV.setOnClickListener(v -> {
            if (quantityInt > 1) {
                quantityInt--;
                quantityTxt.setText(String.valueOf(quantityInt));
            }
        });

        newProductRecyclerView = findViewById(R.id.newProductRecyclerView);
        popularProductRecyclerView = findViewById(R.id.popularProductsRecyclerView);
        newProductRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        popularProductRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        productsRecyclerModelArrayList = new ArrayList<>();
        singleProductArrayList = new ArrayList<>();

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   if (!sessionManager.isLoggedIn()){
                    sessionManager.saveCart(new CartItemModel(null,productIdStr,
                            singleProductArrayList.get(0).getProductTitle(),
                            String.valueOf(quantityInt),singleProductArrayList.get(0).getSlug(),
                            singleProductArrayList.get(0).getProductMRP(),
                            singleProductArrayList.get(0).getProductPrice(),
                            singleProductArrayList.get(0).getDiscountAmount(),
                            singleProductArrayList.get(0).getDiscountPercentage(),
                            singleProductArrayList.get(0).getStock(),
                            singleProductArrayList.get(0).getDescription(),
                            singleProductArrayList.get(0).getTags(),
                            singleProductArrayList.get(0).getProductSKU(),
                            singleProductArrayList.get(0).getStore(),
                            singleProductArrayList.get(0).getCategory(),
                            singleProductArrayList.get(0).getInputTag(),
                            "4.5f", 0,
                            singleProductArrayList.get(0).getProductImagesModelsArrList()));
                    Toast.makeText(SingleProductDetailsActivity.this, "Product Added to cart", Toast.LENGTH_SHORT).show();
                }else {
                    addToCart();
                }
            }
        });
        getProductById();
        getSuggestionProducts();
    }
    private void addToCart() {
        String newArrivalURL = Constant.BASE_URL + "cart";
        Log.e("addTCartURL", newArrivalURL);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("productId", productIdStr);
            jsonObject.put("quantity", quantityInt);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, newArrivalURL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String message = response.optString("message", null);
                        Toast.makeText(SingleProductDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                        sessionManager.getCartFromServer();
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
                                Toast.makeText(SingleProductDetailsActivity.this, message, Toast.LENGTH_LONG).show();
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
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
    private void getProductById() {
        String productURL = Constant.BASE_URL + "product/productById/" + productIdStr;
        Log.e("ProductsURL", productURL);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, productURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject dataObj = response.optJSONObject("data");
                            if (dataObj != null) {

                                String productId = dataObj.optString("_id", null);
                                String title = dataObj.optString("title", null);
//
                                JSONObject slugObj = dataObj.optJSONObject("meta");
                                String slug = (slugObj != null) ? slugObj.optString("slug", null) : null;

                                String MRP = dataObj.optString("MRP", null);
                                String price = dataObj.optString("price", null);

                                JSONObject discountObj = dataObj.optJSONObject("discount");
                                String discountAmount = (discountObj != null) ? discountObj.optString("amount", null) : null;
                                String discountPercentage = (discountObj != null) ? discountObj.optString("percentage", null) : null;

                                String stock = dataObj.optString("stock", null);
                                String description = dataObj.optString("description", null);

                                JSONArray tagsArray = dataObj.optJSONArray("tags");
                                String tags = (tagsArray != null) ? parseTags(tagsArray) : null;

                                String SKU = dataObj.optString("SKU", null);

                                // Handling Images
                                ArrayList<ProductImagesModel> imagesList = new ArrayList<>();
                                JSONArray imageArray = dataObj.optJSONArray("images");
                                if (imageArray != null) {
                                    for (int j = 0; j < imageArray.length(); j++) {
                                        String imageUrl = imageArray.optString(j, null);
                                        if (imageUrl != null) {
                                            Log.e("JSONIMG", imageUrl);
                                            imagesList.add(new ProductImagesModel(imageUrl));
                                        }
                                    }
                                }

                                String store = dataObj.optString("store", null);
                                String category = dataObj.optString("category", null);
                                String inputTag = dataObj.optString("inputTag", null);

                                singleProductArrayList.add(new ProductDetailsModel(
                                        productId, title, slug, MRP, price, discountAmount, discountPercentage,
                                        stock, description, tags, SKU, store, category, inputTag, "4", 0, imagesList
                                ));
                            }
                            setProductDetails();
//                            }
//
//                            if (!casualDressArrayList.isEmpty()) {
//                                casualDressRecyclerView.setAdapter(new CasualMensClothsForActivityAdapter(casualDressArrayList, MensCasualClothesActivity.this));
//                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("JSONParsingError", "Error parsing response: " + e.getMessage());
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
                                Toast.makeText(SingleProductDetailsActivity.this, message, Toast.LENGTH_LONG).show();
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
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
    SpannableStringBuilder spannableText;
    private void setProductDetails() {
        ProductDetailsModel product = singleProductArrayList.get(0);

        // Set product name
        productNameStr = product.getProductTitle();
        productRatingStr = product.getProductRating();

        productTitleTxt.setText(productNameStr);
        productTitleTxt.setEllipsize(TextUtils.TruncateAt.END);
        productTitleTxt.setMaxLines(2);

        // Set product description in WebView
        productDescription = product.getDescription();
        descriptionWebView.loadData(productDescription, "text/html", "UTF-8");

        // Load main product image
        Glide.with(this)
                .load(product.getProductImagesModelsArrList().get(0).getProductImage())
                .error(R.drawable.no_image)
                .into(productImg);

        // Set price and discount
        if (!product.getDiscountAmount().equals("0")) {
            String originalPrice = product.getProductMRP();
            String disPercent = product.getDiscountPercentage();
            String sellingPrice = product.getProductPrice();

            // Strikethrough for original price
            SpannableString spannableOriginalPrice = new SpannableString("₹" + originalPrice);
            spannableOriginalPrice.setSpan(new StrikethroughSpan(), 0, spannableOriginalPrice.length(), 0);

            // Combine selling price and original price
            spannableText = new SpannableStringBuilder();
            spannableText.append("₹").append(sellingPrice).append(" ");
            spannableText.append(spannableOriginalPrice);

            // Apply to price TextView
            productPriceTxt.setText(spannableText);

            // Set discount percent separately (you can show this in a separate TextView like: productDiscountTxt)
            String discountText = "-" + disPercent + "%";
            SpannableString discountSpan = new SpannableString(discountText);
            discountSpan.setSpan(new ForegroundColorSpan(Color.GREEN), 0, discountText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            // Example usage if you have a separate TextView for discount
            productDiscountTxt.setVisibility(View.VISIBLE);
            productDiscountTxt.setText(discountSpan);
        } else {
            productPriceTxt.setText("₹" + product.getProductPrice());

            // Hide discount text if no discount
            productDiscountTxt.setVisibility(View.GONE);
        }

        // Load all images
        ArrayList<String> images = new ArrayList<>();
        for (ProductImagesModel imgModel : product.getProductImagesModelsArrList()) {
            images.add(imgModel.getProductImage());
        }

        // If you have the adapter ready, set it
        // productAllImagesRecycler.setAdapter(new AllImagesRecyclerAdapter(images, SingleProductDetailsActivity.this));

        progressBarDialog.dismiss();
        nestedScrollView.setVisibility(View.VISIBLE);
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
    private void getSuggestionProducts() {
        String newArrivalURL = Constant.BASE_URL + "product/" + sessionManager.getStoreId() + "?pageNumber=1&pageSize=5";
        Log.e("ProductsURL",newArrivalURL);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, newArrivalURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray dataArray = response.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject productObj = dataArray.getJSONObject(i);
                                String productId = productObj.getString("_id");
                                String title = productObj.getString("title");

                                JSONObject slugObj = productObj.getJSONObject("meta");
                                String slug = slugObj.getString("slug");

                                String MRP = productObj.getString("MRP");
                                String price = productObj.getString("price");

                                JSONObject discountObj = productObj.getJSONObject("discount");
                                String discountAmount = discountObj.getString("amount");
                                String discountPercentage = discountObj.getString("percentage");

                                String stock = productObj.getString("stock");
                                String description = productObj.getString("description");

                                String tags = parseTags(productObj.getJSONArray("tags"));

                                String SKU = productObj.getString("SKU");

                                ArrayList<ProductImagesModel> imagesList = new ArrayList<>();
                                JSONArray imageArray = productObj.getJSONArray("images");
                                for (int j = 0; j < imageArray.length(); j++) {
                                    String imageUrl = imageArray.getString(j);
                                    Log.e("JSONIMG",imageUrl);
                                    imagesList.add(new ProductImagesModel(imageUrl));
                                }

                                String store = productObj.getString("store");
                                String category = productObj.getString("category");
                                String inputTag = productObj.getString("inputTag");

                                productsRecyclerModelArrayList.add(new ProductDetailsModel(productId,title,slug,MRP,price,
                                        discountAmount,discountPercentage,stock,description,tags,SKU,store,
                                        category,inputTag,"4",0,imagesList));
                            }
                            if (!productsRecyclerModelArrayList.isEmpty()){
                                newProductRecyclerView.setAdapter(new ProductRecyclerForActivityAdapter(productsRecyclerModelArrayList, SingleProductDetailsActivity.this));
                                popularProductRecyclerView.setAdapter(new ProductRecyclerForActivityAdapter(productsRecyclerModelArrayList, SingleProductDetailsActivity.this));
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        progressBarDialog.dismiss();
                        String errorMessage = "Error: " + error.toString();
                        if (error.networkResponse != null) {
                            try {
                                // Parse the error response
                                String jsonError = new String(error.networkResponse.data);
                                JSONObject jsonObject = new JSONObject(jsonError);
                                String message = jsonObject.optString("message", "Unknown error");
                                // Now you can use the message
                                Toast.makeText(SingleProductDetailsActivity.this, message, Toast.LENGTH_LONG).show();
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
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}