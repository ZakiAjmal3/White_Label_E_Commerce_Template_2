package com.zaki.ecommerce_white_label_template2.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.zaki.ecommerce_white_label_template2.Adapter.ProductRecyclerForFragmentAdapter;
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
import com.zaki.ecommerce_white_label_template2.Model.AllCollectionsModel;
import com.zaki.ecommerce_white_label_template2.Model.ProductDetailsModel;
import com.zaki.ecommerce_white_label_template2.Model.ProductImagesModel;
import com.zaki.ecommerce_white_label_template2.Model.ProductsRecyclerModel;
import com.zaki.ecommerce_white_label_template2.R;
import com.zaki.ecommerce_white_label_template2.Utils.Constant;
import com.zaki.ecommerce_white_label_template2.Utils.MySingleton;
import com.zaki.ecommerce_white_label_template2.Utils.MySingletonFragment;
import com.zaki.ecommerce_white_label_template2.Utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomePageActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager;
    BottomNavigationView bottomNavigationView;
    RelativeLayout topBar,tabLayoutRL;
    FrameLayout frameLayout;
    static Fragment currentFragment;
    Boolean loadOtherFragment = false;
    CardView filterCardView;
    ArrayList<AllCollectionsModel> collectionIdsArrayList = new ArrayList<>();
    SessionManager sessionManager;
    String storeId,authToken;
    TabLayoutAdapter adapter;
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

        sessionManager = new SessionManager(this);
        storeId = sessionManager.getStoreId();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        topBar = findViewById(R.id.topBar);
        tabLayoutRL = findViewById(R.id.tabLayoutRL);
        frameLayout = findViewById(R.id.frameLayout);

        filterCardView = findViewById(R.id.filterCardView);

        // Handle back press using OnBackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (currentFragment instanceof AllProductFragment) {
                    // If on HomeFragment, use the default behavior
                    setEnabled(false); // Disable this callback
                } else {
                    // If on another fragment, navigate back to HomeFragment
                    loadFragment(new AllProductFragment());
                    bottomNavigationView.setSelectedItemId(R.id.home);
                }
            }
        });

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
                }else if (item.getItemId() == R.id.profile) {
                    if (sessionManager.isLoggedIn()) {
                        filterCardView.setVisibility(View.GONE);
                        tabLayoutRL.setVisibility(View.GONE);
                        topBar.setVisibility(View.GONE);
                        frameLayout.setVisibility(View.VISIBLE);
                        loadFragment(new ProfileFragment());
                    }else {
                        startActivity(new Intent(HomePageActivity.this,LoginActivity.class));
                    }
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

        adapter = new TabLayoutAdapter(HomePageActivity.this);
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

        getAllCollections();

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                Fragment fragment = adapter.getRegisteredFragment(position);
                if (fragment instanceof AllProductFragment) {
                    ((AllProductFragment) fragment).onTabVisible();
                }
            }
        });
    }

    private void getAllCollections() {
        String newArrivalURL = Constant.BASE_URL + "collection/" + sessionManager.getStoreId() + "?pageNumber=1&pageSize=10";
        Log.e("ProductsURL", newArrivalURL);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, newArrivalURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray dataArray = response.optJSONArray("data");
                            if (dataArray != null) {

                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject collectionObj = dataArray.optJSONObject(i);
                                    if (collectionObj != null) {
                                        String collectionId = collectionObj.optString("_id", null);
                                        String collectionName = collectionObj.optString("name", null);
                                        collectionIdsArrayList.add(new AllCollectionsModel(collectionId,collectionName));
                                    }
                                }
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
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
                                Toast.makeText(HomePageActivity.this, message, Toast.LENGTH_LONG).show();
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
                }
                else  if (currentFragment instanceof CeramicsFragment){
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
    private final int MAX_RETRY = 5;
    private final long RETRY_DELAY = 200; // in milliseconds
    public void setWishlistCount() {
        setWishlistCountWithRetry(0);
    }
    private void setWishlistCountWithRetry(int attempt) {
        if (attempt >= MAX_RETRY) return;

        int count = sessionManager.getWishListCount();
        BadgeDrawable badge = bottomNavigationView.getOrCreateBadge(R.id.wishlist);

        if (badge == null && attempt < MAX_RETRY) {
            // Retry after delay
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                setWishlistCountWithRetry(attempt + 1);
            }, RETRY_DELAY);
            return;
        }

        if (count == 0) {
            badge.setVisible(false);
        } else {
            badge.setVisible(true);
            badge.setNumber(count);
            badge.setBackgroundColor(ContextCompat.getColor(HomePageActivity.this, R.color.red));
            badge.setBadgeTextColor(ContextCompat.getColor(HomePageActivity.this, R.color.white));
        }
    }
    public void setCartCount() {
        setCartCountWithRetry(0);
    }

    private void setCartCountWithRetry(int attempt) {
        if (attempt >= MAX_RETRY) return;

        int count = sessionManager.getCartCount();
        BadgeDrawable badge = bottomNavigationView.getOrCreateBadge(R.id.cart);

        if (badge == null && attempt < MAX_RETRY) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                setCartCountWithRetry(attempt + 1);
            }, RETRY_DELAY);
            return;
        }

        if (count == 0) {
            badge.setVisible(false);
        } else {
            badge.setVisible(true);
            badge.setNumber(count);
            badge.setBackgroundColor(ContextCompat.getColor(HomePageActivity.this, R.color.red));
            badge.setBadgeTextColor(ContextCompat.getColor(HomePageActivity.this, R.color.white));
        }
    }

    public ArrayList<AllCollectionsModel> getCollectionIdsArrayList() {
        return collectionIdsArrayList;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Fragment fragment2 = getCurrentFragment();
//        Log.e("tabFrag",fragment2.toString());
        if (fragment2 instanceof AllProductFragment) {
            ((AllProductFragment) fragment2).onTabVisible();
        }
        setWishlistCount();
        setCartCount();
        Fragment fragment = getCurrentFragment();
        if (fragment instanceof WishListFragment) {
            bottomNavigationView.setSelectedItemId(R.id.wishlist);
        }else if (fragment instanceof CartItemFragment){
            bottomNavigationView.setSelectedItemId(R.id.cart);
        }else if (fragment instanceof ProfileFragment){
            bottomNavigationView.setSelectedItemId(R.id.profile);
        }else if (fragment instanceof AllProductFragment){
            bottomNavigationView.setSelectedItemId(R.id.home);
        }else if (fragment instanceof SearchFragment){
            bottomNavigationView.setSelectedItemId(R.id.search);
        }
    }
}