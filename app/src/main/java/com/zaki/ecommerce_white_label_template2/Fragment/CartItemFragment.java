package com.zaki.ecommerce_white_label_template2.Fragment;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zaki.ecommerce_white_label_template2.Activities.CheckOutOrderActivity;
import com.zaki.ecommerce_white_label_template2.Adapter.CartItemAdapter;
import com.zaki.ecommerce_white_label_template2.Adapter.CouponSelectingAdapter;
import com.zaki.ecommerce_white_label_template2.Model.CartItemModel;
import com.zaki.ecommerce_white_label_template2.Model.CouponSelectingModel;
import com.zaki.ecommerce_white_label_template2.R;
import com.zaki.ecommerce_white_label_template2.Utils.Constant;
import com.zaki.ecommerce_white_label_template2.Utils.MySingletonFragment;
import com.zaki.ecommerce_white_label_template2.Utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartItemFragment extends Fragment {
    ImageView backBtn;
    RecyclerView cartRecyclerView;
    CartItemAdapter cartItemAdapter;
    ArrayList<CartItemModel> cartItemModelArrayList;
    ArrayList<CouponSelectingModel> couponSelectingModelArrayList;
    TextView subTotalDisplayTxt, discountTxt, discountDisplayTxt, deliveryFeeDisplayTxt, totalAmountDisplayTxt,promoErrorTxt;
    Button applyPromoBtn,checkOutBtn;
    EditText promoCodeET;
    RelativeLayout noDataLayout,couponsViewRL;
    NestedScrollView mainLayout;
    SessionManager sessionManager;
    int totalProductQuantity = 0;
    EditText applyCouponEditTxt;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        sessionManager = new SessionManager(getContext());

        couponsViewRL = view.findViewById(R.id.couponsViewRL);
        noDataLayout = view.findViewById(R.id.noDataLayout);
        mainLayout = view.findViewById(R.id.mainLayout);

        // Cart item Setup
        cartRecyclerView = view.findViewById(R.id.cartRecyclerView);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        couponSelectingModelArrayList = new ArrayList<>();
        cartItemModelArrayList = new ArrayList<>();
        cartItemModelArrayList = sessionManager.getCart();
//        cartItemModelArrayList.add(new CartItemModel(,"Product 1","300","Large","Black",null,"2"));
//        cartItemModelArrayList.add(new CartItemModel("Product 2","250","Large","Black",null,"2"));
//        cartItemModelArrayList.add(new CartItemModel("Product 3","150","Large","Black",null,"2"));
//        cartItemModelArrayList.add(new CartItemModel("Product 4","200","Large","Black",null,"2"));
//        cartItemModelArrayList.add(new CartItemModel("Product 5","1500","Large","Blue",null,"2"));

        cartItemAdapter = new CartItemAdapter(cartItemModelArrayList, CartItemFragment.this);
        cartRecyclerView.setAdapter(cartItemAdapter);

        checkCartItemArraySize();

        //Order Summary Setup
        subTotalDisplayTxt = view.findViewById(R.id.subTotalDisplayTxt);
        discountTxt = view.findViewById(R.id.discountTxt);
        discountDisplayTxt = view.findViewById(R.id.discountDisplayTxt);
        deliveryFeeDisplayTxt = view.findViewById(R.id.sizeTxt);
        totalAmountDisplayTxt = view.findViewById(R.id.totalAmountDisplayTxt);
        promoErrorTxt = view.findViewById(R.id.invalidPromoTxt);
        applyCouponEditTxt = view.findViewById(R.id.promoCodeET);
        applyPromoBtn = view.findViewById(R.id.applyCodeBtn);
        checkOutBtn = view.findViewById(R.id.goToCheckOutTxt);
        promoCodeET = view.findViewById(R.id.promoCodeET);

        setOrderSummaryDetails();
        setUpCouponDialog();

        checkOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CheckOutOrderActivity.class));
            }
        });

        applyPromoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new Dialog(getContext());
                progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                progressDialog.setContentView(R.layout.progress_bar_dialog);
                progressDialog.setCancelable(false);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                progressDialog.getWindow().setGravity(Gravity.CENTER); // Center the dialog
                progressDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT); // Adjust the size
                progressDialog.show();
                checkAndApplyPromoCode(applyCouponEditTxt.getText().toString().trim());

            }
        });

        getCoupons();

        couponsViewRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCouponDialog();
            }
        });

        return view;
    }

    private void checkAndApplyPromoCode(String couponCode) {
        boolean couponFound = false;
        for (int i = 0; i < couponSelectingModelArrayList.size(); i++){
            if (couponCode.equals(couponSelectingModelArrayList.get(i).getCouponCode())){
                couponFound = true;
                String couponType,couponDiscountType;
                int couponDiscountValue,newFinalAmount,newTotalAmount,newDiscountAmount;
                couponType = couponSelectingModelArrayList.get(i).getCouponType();
                couponDiscountType = couponSelectingModelArrayList.get(i).getDiscountType();
                if (couponType.equalsIgnoreCase("ORDER")){
                    couponDiscountValue = Integer.parseInt(couponSelectingModelArrayList.get(i).getDiscountValue());
                    if (couponDiscountType.equalsIgnoreCase("PERCENTAGE")){
                        newTotalAmount = finalTotalAmount;
                        newDiscountAmount = finalTotalAmount * couponDiscountValue / 100;
                        newFinalAmount = finalTotalAmount - newDiscountAmount;

                        discountTxt.setText("Discount(-" + couponDiscountValue + "%)");
                        subTotalDisplayTxt.setText("₹" + String.valueOf(newTotalAmount) + ".00");
                        totalAmountDisplayTxt.setText("₹" + String.valueOf(newFinalAmount) + ".00");
                        discountDisplayTxt.setText("-₹" + String.valueOf(newDiscountAmount) + ".00");
                    }else if (couponDiscountType.equalsIgnoreCase("FIXED_AMOUNT")){
                        newTotalAmount = finalTotalAmount;
                        newDiscountAmount = couponDiscountValue ;
                        newFinalAmount = finalTotalAmount - newDiscountAmount;
                        discountTxt.setText("Discount(-₹ " + couponDiscountValue);
                        subTotalDisplayTxt.setText("₹" + String.valueOf(newTotalAmount) + ".00");
                        totalAmountDisplayTxt.setText("₹" + String.valueOf(newFinalAmount) + ".00");
                        discountDisplayTxt.setText("-₹" + String.valueOf(newDiscountAmount) + ".00");
                    }
                } else if (couponType.equalsIgnoreCase("SHIPPING")) {
                    newFinalAmount = finalTotalAmount - shippingCharge;
                    shippingCharge = 0;
                    totalAmountDisplayTxt.setText("₹" + String.valueOf(newFinalAmount) + ".00");
                    deliveryFeeDisplayTxt.setText("₹ 0.00");
                }
                progressDialog.dismiss();
                break;
            }
        }
        if (!couponFound){
            progressDialog.dismiss();
            promoErrorTxt.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), "Please enter a valid promo code", Toast.LENGTH_SHORT).show();
        }
    }


    int totalAmount = 0, finalTotalAmount = 0, shippingCharge = 99, discount = 0;
    public void setOrderSummaryDetails() {
        if (!cartItemModelArrayList.isEmpty()) {

            for (int i = 0; i < cartItemModelArrayList.size(); i++) {
                totalProductQuantity+= Integer.parseInt(cartItemModelArrayList.get(i).getProductQuantity());
                totalAmount += Integer.parseInt(cartItemModelArrayList.get(i).getProductPrice()) * Integer.parseInt(cartItemModelArrayList.get(i).getProductQuantity());
            }
            if (totalAmount > 500) {
                shippingCharge = 0;
                finalTotalAmount = totalAmount;
            } else {
                finalTotalAmount = totalAmount + shippingCharge;
            }
            subTotalDisplayTxt.setText("₹" + String.valueOf(totalAmount) + ".00");
            totalAmountDisplayTxt.setText("₹" + String.valueOf(finalTotalAmount) + ".00");
            discountDisplayTxt.setText("-₹" + String.valueOf(discount) + ".00");
            deliveryFeeDisplayTxt.setText("+₹" + String.valueOf(shippingCharge) + ".00");

        }
    }
    public int getShippingCharge(){
        return shippingCharge;
    }
    public void checkCartItemArraySize() {
        if (cartItemModelArrayList.isEmpty()){
            mainLayout.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.VISIBLE);
        }else {
            mainLayout.setVisibility(View.VISIBLE);
            noDataLayout.setVisibility(View.GONE);
        }
    }
    private void getCoupons() {
        String examCategoryURL = Constant.BASE_URL + "discount?pageNumber=1&pageSize=10&storeId=67d2b3da82e71e00672df277";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, examCategoryURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0;i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String couponId = jsonObject.getString("_id");
                                String couponCode = jsonObject.getString("code");
                                String couponType = jsonObject.getString("type");

                                JSONObject dateObj = jsonObject.getJSONObject("activeDates");
                                String startDate = dateObj.getString("startDate");
                                String endDate = dateObj.getString("endDate");

                                String discountType = null,discountValue = null;
                                if (jsonObject.has("discountValue")) {
                                    JSONObject discountObj = jsonObject.getJSONObject("discountValue");
                                    discountType = discountObj.getString("discountType");
                                    discountValue = discountObj.getString("value");
                                }

                                JSONObject discountObj = jsonObject.getJSONObject("minimumPurchase");
                                String purchaseType = discountObj.getString("purchaseType");
                                String purchaseValue = discountObj.getString("value");

                                String store = jsonObject.getString("store");
                                couponSelectingModelArrayList.add(new CouponSelectingModel(couponId,couponCode,couponType,startDate,endDate,discountType,discountValue,purchaseType,purchaseValue,store));
                            }
                        } catch (JSONException e) {
                            Log.e("Exam Catch error", "Error parsing JSON: " + e.getMessage());
                        }
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
                return headers;
            }
        };
        MySingletonFragment.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
    Dialog couponDialog,progressDialog;
    ImageView crossBtn;
    RecyclerView couponRecycler;
    CouponSelectingAdapter couponSelectingAdapter;
    private void setUpCouponDialog() {
        couponDialog = new Dialog(getContext());
        couponDialog.setContentView(R.layout.coupon_selecting_dialog_layout);

        crossBtn = couponDialog.findViewById(R.id.crossBtn);
        couponRecycler = couponDialog.findViewById(R.id.couponRecycler);
        couponRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

    }
    @SuppressLint("ResourceAsColor")
    private void openCouponDialog() {

        couponSelectingAdapter = new CouponSelectingAdapter(couponSelectingModelArrayList,CartItemFragment.this,finalTotalAmount,totalProductQuantity);
        couponRecycler.setAdapter(couponSelectingAdapter);

        crossBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                couponDialog.dismiss();
            }
        });

        couponSelectingAdapter.notifyDataSetChanged();

        couponDialog.show();
        couponDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        couponDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        couponDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationDisplayBottomTop;
        couponDialog.getWindow().setGravity(Gravity.TOP);
        couponDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            couponDialog.getWindow().setStatusBarColor(R.color.white);
        }
    }
    public void closeCouponDialog(int position){
        progressDialog = new Dialog(getContext());
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.progress_bar_dialog);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.getWindow().setGravity(Gravity.CENTER); // Center the dialog
        progressDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT); // Adjust the size
        progressDialog.show();

        String couponType,couponDiscountType;
        int couponDiscountValue,newFinalAmount,newTotalAmount,newDiscountAmount;
        couponType = couponSelectingModelArrayList.get(position).getCouponType();
        couponDiscountType = couponSelectingModelArrayList.get(position).getDiscountType();
        if (couponType.equalsIgnoreCase("ORDER")){
            couponDiscountValue = Integer.parseInt(couponSelectingModelArrayList.get(position).getDiscountValue());
            if (couponDiscountType.equalsIgnoreCase("PERCENTAGE")){
                newTotalAmount = finalTotalAmount;
                newDiscountAmount = finalTotalAmount * couponDiscountValue / 100;
                newFinalAmount = finalTotalAmount - newDiscountAmount;

                discountTxt.setText("Discount(-" + couponDiscountValue + "%)");
                subTotalDisplayTxt.setText("₹" + String.valueOf(newTotalAmount) + ".00");
                totalAmountDisplayTxt.setText("₹" + String.valueOf(newFinalAmount) + ".00");
                discountDisplayTxt.setText("-₹" + String.valueOf(newDiscountAmount) + ".00");
            }else if (couponDiscountType.equalsIgnoreCase("FIXED_AMOUNT")){
                newTotalAmount = finalTotalAmount;
                newDiscountAmount = couponDiscountValue ;
                newFinalAmount = finalTotalAmount - newDiscountAmount;
                discountTxt.setText("Discount(-₹ " + couponDiscountValue);
                subTotalDisplayTxt.setText("₹" + String.valueOf(newTotalAmount) + ".00");
                totalAmountDisplayTxt.setText("₹" + String.valueOf(newFinalAmount) + ".00");
                discountDisplayTxt.setText("-₹" + String.valueOf(newDiscountAmount) + ".00");
            }
        } else if (couponType.equalsIgnoreCase("SHIPPING")) {
            newFinalAmount = finalTotalAmount - shippingCharge;
            shippingCharge = 0;
            totalAmountDisplayTxt.setText("₹" + String.valueOf(newFinalAmount) + ".00");
            deliveryFeeDisplayTxt.setText("₹ 0.00");
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                couponDialog.dismiss();
                progressDialog.dismiss();
            }
        },500);
    }
    public ArrayList<CartItemModel> getCartItemModelArrayList() {
        return cartItemModelArrayList;
    }
}