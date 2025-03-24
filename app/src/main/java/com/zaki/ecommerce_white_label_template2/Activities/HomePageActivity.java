package com.zaki.ecommerce_white_label_template2.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.zaki.ecommerce_white_label_template2.Adapter.TabLayoutAdapter;
import com.zaki.ecommerce_white_label_template2.Fragment.AllProductFragment;
import com.zaki.ecommerce_white_label_template2.Fragment.CartItemFragment;
import com.zaki.ecommerce_white_label_template2.Fragment.CeramicsFragment;
import com.zaki.ecommerce_white_label_template2.Fragment.ChairsFragment;
import com.zaki.ecommerce_white_label_template2.Fragment.CrockeryFragment;
import com.zaki.ecommerce_white_label_template2.Fragment.LampsFragment;
import com.zaki.ecommerce_white_label_template2.Fragment.PlantPotsFragment;
import com.zaki.ecommerce_white_label_template2.Fragment.ProfileFragment;
import com.zaki.ecommerce_white_label_template2.Fragment.SearchFragment;
import com.zaki.ecommerce_white_label_template2.Fragment.SofasFragment;
import com.zaki.ecommerce_white_label_template2.Fragment.TablesFragment;
import com.zaki.ecommerce_white_label_template2.Fragment.WishListFragment;
import com.zaki.ecommerce_white_label_template2.Model.ProductsRecyclerModel;
import com.zaki.ecommerce_white_label_template2.R;

import java.util.ArrayList;

public class HomePageActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager;
    BottomNavigationView bottomNavigationView;
    RelativeLayout topBar,tabLayoutRL;
    FrameLayout frameLayout;
    static Fragment currentFragment;
    Boolean loadOtherFragment = false;
    CardView filterCardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE){
            EdgeToEdge.enable(this);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        topBar = findViewById(R.id.topBar);
        tabLayoutRL = findViewById(R.id.tabLayoutRL);
        frameLayout = findViewById(R.id.frameLayout);

        filterCardView = findViewById(R.id.filterCardView);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home){
                    filterCardView.setVisibility(View.VISIBLE);
                    topBar.setVisibility(View.VISIBLE);
                    tabLayoutRL.setVisibility(View.VISIBLE);
                    frameLayout.setVisibility(View.GONE);
                }else if (item.getItemId() == R.id.search){
                    filterCardView.setVisibility(View.GONE);
                    topBar.setVisibility(View.VISIBLE);
                    tabLayoutRL.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.VISIBLE);
                    loadFragment(new SearchFragment());
                }else if (item.getItemId() == R.id.wishlist){
                    filterCardView.setVisibility(View.GONE);
                    topBar.setVisibility(View.VISIBLE);
                    tabLayoutRL.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.VISIBLE);
                    loadFragment(new WishListFragment());
                }else if (item.getItemId() == R.id.cart){
                    filterCardView.setVisibility(View.GONE);
                    topBar.setVisibility(View.VISIBLE);
                    tabLayoutRL.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.VISIBLE);
                    loadFragment(new CartItemFragment());
                }else if (item.getItemId() == R.id.profile){
                    filterCardView.setVisibility(View.GONE);
                    tabLayoutRL.setVisibility(View.GONE);
                    topBar.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.VISIBLE);
                    loadFragment(new ProfileFragment());
//                       startActivity(new Intent(HomePageActivity.this,LoginActivity.class));
                    }
                return true;
            }
        });

        if (getIntent() != null){
            loadOtherFragment = getIntent().getBooleanExtra("LoadCartFrag",false);
        }

        if (loadOtherFragment){
            loadFragment(new CartItemFragment());
            bottomNavigationView.setSelectedItemId(R.id.cart);
        }

        // Filter Setup

        filterCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilterDialog();
            }
        });

        //Tab Layout SetUP
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        TabLayoutAdapter adapter = new TabLayoutAdapter(HomePageActivity.this);
        viewPager.setAdapter(adapter);

        // Link TabLayout with ViewPager
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0: {
                        tab.setText("All Product");
//                        bookTypeTabSelected = "books";
                        break;
                    }
                    case 1: {
                        tab.setText("Chairs");
//                        bookTypeTabSelected = "ebooks";
                        break;
                    }
                    case 2: {
                        tab.setText("Tables");
//                        bookTypeTabSelected = "ebooks";
                        break;
                    }
                    case 3: {
                        tab.setText("Sofas");
//                        bookTypeTabSelected = "ebooks";
                        break;
                    }
                    case 4: {
                        tab.setText("Lamps");
//                        bookTypeTabSelected = "ebooks";
                        break;
                    }
                    case 5: {
                        tab.setText("Crockery");
//                        bookTypeTabSelected = "ebooks";
                        break;
                    }
                    case 6: {
                        tab.setText("Ceramics");
//                        bookTypeTabSelected = "ebooks";
                        break;
                    }
                    case 7: {
                        tab.setText("Plant pots");
//                        bookTypeTabSelected = "ebooks";
                        break;
                    }
                }
                // Log when a tab is clicked
                tab.view.setOnClickListener(v -> {
                    Log.d("TabClicked", "Tab " + position + " clicked");
                });
            }
        }).attach();
    }
    public void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
        currentFragment = fragment;
    }
    public static Fragment getCurrentFragment(){
        return currentFragment;
    }
    public static void setCurrentFragment(Fragment fragment){
        currentFragment = fragment;
    }
    Dialog filterDialog;
    CardView cancelFilterBtn,applyFilterBtn;
    RadioGroup radioGroup1,radioGroup2;
    int priceFilter = 0,sortByFilter = 0;
    ArrayList<ProductsRecyclerModel> productsRecyclerModelArrayList = new ArrayList<>();
    @SuppressLint("ResourceAsColor")
    private void openFilterDialog() {
        filterDialog = new Dialog(this);
        filterDialog.setContentView(R.layout.filter_product_layout);

        radioGroup1 = filterDialog.findViewById(R.id.radio1);
        radioGroup2 = filterDialog.findViewById(R.id.radio2);
        cancelFilterBtn = filterDialog.findViewById(R.id.cancelBtn);
        applyFilterBtn = filterDialog.findViewById(R.id.applyFilterBtn);

        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.lowToHighRB){
                    priceFilter = 0;
                }else if (checkedId == R.id.highToLowRB){
                    priceFilter = 1;
                }
            }
        });
        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.oldRB){
                    sortByFilter = 0;
                }else if (checkedId == R.id.newRB){
                    sortByFilter = 1;
                }else if (checkedId == R.id.aToZRB){
                    sortByFilter = 0;
                }else if (checkedId == R.id.zToARB){
                    sortByFilter = 1;
                }
            }
        });

        cancelFilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDialog.dismiss();
            }
        });

        applyFilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = viewPager.getCurrentItem();
                TabLayoutAdapter adapter = (TabLayoutAdapter) viewPager.getAdapter();
                Fragment currentFragment = adapter.getRegisteredFragment(currentPosition);
                if (currentFragment instanceof AllProductFragment){
                    ((AllProductFragment) currentFragment).sortProductsByPrice(priceFilter);
                    ((AllProductFragment) currentFragment).sortProductsByDate(sortByFilter);
                }else  if (currentFragment instanceof CeramicsFragment){
                    ((CeramicsFragment) currentFragment).sortProductsByPrice(priceFilter);
                    ((CeramicsFragment) currentFragment).sortProductsByDate(sortByFilter);
                }else  if (currentFragment instanceof ChairsFragment){
                    ((ChairsFragment) currentFragment).sortProductsByPrice(priceFilter);
                    ((ChairsFragment) currentFragment).sortProductsByDate(sortByFilter);
                }else  if (currentFragment instanceof CrockeryFragment){
                    ((CrockeryFragment) currentFragment).sortProductsByPrice(priceFilter);
                    ((CrockeryFragment) currentFragment).sortProductsByDate(sortByFilter);
                }else  if (currentFragment instanceof LampsFragment){
                    ((LampsFragment) currentFragment).sortProductsByPrice(priceFilter);
                    ((LampsFragment) currentFragment).sortProductsByDate(sortByFilter);
                }else  if (currentFragment instanceof PlantPotsFragment){
                    ((PlantPotsFragment) currentFragment).sortProductsByPrice(priceFilter);
                    ((PlantPotsFragment) currentFragment).sortProductsByDate(sortByFilter);
                }else  if (currentFragment instanceof SofasFragment){
                    ((SofasFragment) currentFragment).sortProductsByPrice(priceFilter);
                    ((SofasFragment) currentFragment).sortProductsByDate(sortByFilter);
                }else  if (currentFragment instanceof TablesFragment){
                    ((TablesFragment) currentFragment).sortProductsByPrice(priceFilter);
                    ((TablesFragment) currentFragment).sortProductsByDate(sortByFilter);
                }
                filterDialog.dismiss();
            }
        });

        filterDialog.show();
        filterDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        filterDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        filterDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationDisplayBottomTop;
        filterDialog.getWindow().setGravity(Gravity.TOP);
        filterDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            filterDialog.getWindow().setStatusBarColor(R.color.white);
        }
    }
}