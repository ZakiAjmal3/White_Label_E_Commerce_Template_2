package com.zaki.ecommerce_white_label_template2.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.zaki.ecommerce_white_label_template2.Adapter.ProductRecyclerForActivityAdapter;
import com.zaki.ecommerce_white_label_template2.Adapter.ProductRecyclerForFragmentAdapter;
import com.zaki.ecommerce_white_label_template2.Model.CartItemModel;
import com.zaki.ecommerce_white_label_template2.Model.ProductsRecyclerModel;
import com.zaki.ecommerce_white_label_template2.R;
import com.zaki.ecommerce_white_label_template2.Utils.SessionManager;

import java.util.ArrayList;
import java.util.Objects;

public class SingleProductDetailsActivity extends AppCompatActivity {
    ImageView backBtn,productImg, quantityPlusIV, quantityMinusIV;
    TextView productTitleTxt, productPriceTxt,quantityTxt;
    int quantityInt = 1;
    RecyclerView newProductRecyclerView,popularProductRecyclerView;
    ProductRecyclerForActivityAdapter productRecyclerForActivityAdapter;
    ArrayList<ProductsRecyclerModel> productsRecyclerModelArrayList1,productsRecyclerModelArrayList2;
    MaterialCardView viewAllCard1,viewAllCard2;
    Button addToCartBtn;
    SessionManager sessionManager;
    String productIdStr,productNameStr,productPriceStr,productImgStr;
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
        sessionManager = new SessionManager(this);
        backBtn = findViewById(R.id.imgBack);
        productImg = findViewById(R.id.productImg);
        productTitleTxt = findViewById(R.id.productTitleTxt);
        productPriceTxt = findViewById(R.id.productPriceTxt);

        viewAllCard1 = findViewById(R.id.viewAllCard1);
        viewAllCard2 = findViewById(R.id.viewAllCard2);

        addToCartBtn = findViewById(R.id.addToCartBtn);

        productIdStr = getIntent().getStringExtra("productId");
        productNameStr = getIntent().getStringExtra("productName");
        productPriceStr = getIntent().getStringExtra("productPrice");
        productImgStr = String.valueOf(getIntent().getIntExtra("productImg",0));

        productTitleTxt.setText(productNameStr);
        productPriceTxt.setText("₹ " + productPriceStr);
        productImg.setImageResource(Integer.parseInt(productImgStr));

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
        newProductRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        popularProductRecyclerView.setLayoutManager(new GridLayoutManager(this,2));

        productsRecyclerModelArrayList1 = new ArrayList<>();
        productsRecyclerModelArrayList1.add(new ProductsRecyclerModel("sf1","Sofa","3000",R.drawable.sofa_img,0));
        productsRecyclerModelArrayList1.add(new ProductsRecyclerModel("lam1","Lamp","1000",R.drawable.lamp_img,0));
        productsRecyclerModelArrayList1.add(new ProductsRecyclerModel("ce1","Azure Ceramic","5000",R.drawable.ceramic_img,0));
        productsRecyclerModelArrayList1.add(new ProductsRecyclerModel("pl1","Plant Vase","2000",R.drawable.plant_pot_img,0));
        productsRecyclerModelArrayList1.add(new ProductsRecyclerModel("ch1","Chair","2000",R.drawable.chair_img,0));
        productsRecyclerModelArrayList1.add(new ProductsRecyclerModel("sf1","Sofa","3000",R.drawable.sofa_img,0));
        productsRecyclerModelArrayList1.add(new ProductsRecyclerModel("tb1","Table","4000",R.drawable.table_img,0));
        productsRecyclerModelArrayList1.add(new ProductsRecyclerModel("sf2","Sofa","3000",R.drawable.sofa_img,0));
        productRecyclerForActivityAdapter = new ProductRecyclerForActivityAdapter(productsRecyclerModelArrayList1,SingleProductDetailsActivity.this);
        newProductRecyclerView.setAdapter(productRecyclerForActivityAdapter);

        productsRecyclerModelArrayList2 = new ArrayList<>();
        productsRecyclerModelArrayList2.add(new ProductsRecyclerModel("lam2","Lamp","1000",R.drawable.lamp_img,0));
        productsRecyclerModelArrayList2.add(new ProductsRecyclerModel("pl2","Plant Vase","2000",R.drawable.plant_pot_img,0));
        productsRecyclerModelArrayList2.add(new ProductsRecyclerModel("cr1","La Opala Crockery","2000",R.drawable.crockery_img,0));
        productsRecyclerModelArrayList2.add(new ProductsRecyclerModel("pl3","Plant Vase","2000",R.drawable.plant_pot_img,0));
        productsRecyclerModelArrayList2.add(new ProductsRecyclerModel("ch2","Chair","2000",R.drawable.chair_img,0));
        productsRecyclerModelArrayList2.add(new ProductsRecyclerModel("sf3","Sofa","3000",R.drawable.sofa_img,0));
        productsRecyclerModelArrayList2.add(new ProductsRecyclerModel("tb2","Table","4000",R.drawable.table_img,0));
        productsRecyclerModelArrayList2.add(new ProductsRecyclerModel("sf4","Sofa","3000",R.drawable.sofa_img,0));
        productRecyclerForActivityAdapter = new ProductRecyclerForActivityAdapter(productsRecyclerModelArrayList2,SingleProductDetailsActivity.this);
        popularProductRecyclerView.setAdapter(productRecyclerForActivityAdapter);

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.saveCart(new CartItemModel(productIdStr,productNameStr,productPriceStr,null,null,productImgStr,String.valueOf(quantityInt)));
                Toast.makeText(SingleProductDetailsActivity.this, "Item Added to Cart", Toast.LENGTH_SHORT).show();
            }
        });
    }
}