package com.zaki.ecommerce_white_label_template2.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zaki.ecommerce_white_label_template2.Adapter.SearchingAdapter;
import com.zaki.ecommerce_white_label_template2.Model.ProductsRecyclerModel;
import com.zaki.ecommerce_white_label_template2.R;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    private SearchView searchView;
    RecyclerView searchingItemRV;
    ArrayList<ProductsRecyclerModel> productsRecyclerModelArrayList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Initialize the SearchView
        searchView = view.findViewById(R.id.searchView);

        // Set the SearchView to be expanded
        searchView.setIconified(false);

        // Add a small delay to ensure the SearchView is initialized before requesting focus
        new Handler().postDelayed(() -> {
            searchView.requestFocus();
            openKeyboard(searchView);
        }, 100);  // Adjust the delay if needed

        searchingItemRV = view.findViewById(R.id.searchingItemRV);
        searchingItemRV.setLayoutManager(new GridLayoutManager(getContext(),3));

        productsRecyclerModelArrayList = new ArrayList<>();
        productsRecyclerModelArrayList.add(new ProductsRecyclerModel("1a","Chair","2000",R.drawable.chair_img,0));
        productsRecyclerModelArrayList.add(new ProductsRecyclerModel("2a","Table","4000",R.drawable.table_img,0));
        productsRecyclerModelArrayList.add(new ProductsRecyclerModel("3a","Sofa","3000",R.drawable.sofa_img,0));
        productsRecyclerModelArrayList.add(new ProductsRecyclerModel("4a","Lamp","1000",R.drawable.lamp_img,0));
        productsRecyclerModelArrayList.add(new ProductsRecyclerModel("5a","La Opala Crockery","2000",R.drawable.crockery_img,0));
        productsRecyclerModelArrayList.add(new ProductsRecyclerModel("6a","Sofa","3000",R.drawable.sofa_img,0));
        productsRecyclerModelArrayList.add(new ProductsRecyclerModel("7a","Lamp","1000",R.drawable.lamp_img,0));
        productsRecyclerModelArrayList.add(new ProductsRecyclerModel("8a","Azure Ceramic","5000",R.drawable.ceramic_img,0));
        productsRecyclerModelArrayList.add(new ProductsRecyclerModel("9a","Plant Vase","2000",R.drawable.plant_pot_img,0));
        productsRecyclerModelArrayList.add(new ProductsRecyclerModel("10a","Chair","2000",R.drawable.chair_img,0));
        productsRecyclerModelArrayList.add(new ProductsRecyclerModel("11a","Sofa","3000",R.drawable.sofa_img,0));
        productsRecyclerModelArrayList.add(new ProductsRecyclerModel("12a","Table","4000",R.drawable.table_img,0));
        productsRecyclerModelArrayList.add(new ProductsRecyclerModel("13a","Sofa","3000",R.drawable.sofa_img,0));
        productsRecyclerModelArrayList.add(new ProductsRecyclerModel("14a","Lamp","1000",R.drawable.lamp_img,1));
        productsRecyclerModelArrayList.add(new ProductsRecyclerModel("15a","Table","4000",R.drawable.table_img,0));
        productsRecyclerModelArrayList.add(new ProductsRecyclerModel("16a","Azure Ceramic","5000",R.drawable.ceramic_img,0));
        productsRecyclerModelArrayList.add(new ProductsRecyclerModel("17a","Plant Vase","2000",R.drawable.plant_pot_img,0));
        productsRecyclerModelArrayList.add(new ProductsRecyclerModel("18a","Chair","2000",R.drawable.chair_img,0));
        productsRecyclerModelArrayList.add(new ProductsRecyclerModel("19a","Table","4000",R.drawable.table_img,0));
        productsRecyclerModelArrayList.add(new ProductsRecyclerModel("20a","Sofa","3000",R.drawable.sofa_img,0));
        productsRecyclerModelArrayList.add(new ProductsRecyclerModel("21a","Lamp","1000",R.drawable.lamp_img,0));
        productsRecyclerModelArrayList.add(new ProductsRecyclerModel("22a","La Opala Crockery","2000",R.drawable.crockery_img,0));
        productsRecyclerModelArrayList.add(new ProductsRecyclerModel("23a","Azure Ceramic","5000",R.drawable.ceramic_img,0));
        productsRecyclerModelArrayList.add(new ProductsRecyclerModel("24a","Sofa","3000",R.drawable.sofa_img,0));
        productsRecyclerModelArrayList.add(new ProductsRecyclerModel("25a","Lamp","1000",R.drawable.lamp_img,0));
        productsRecyclerModelArrayList.add(new ProductsRecyclerModel("26a","Plant Vase","2000",R.drawable.plant_pot_img,0));
        productsRecyclerModelArrayList.add(new ProductsRecyclerModel("27a","La Opala Crockery","2000",R.drawable.crockery_img,0));
        productsRecyclerModelArrayList.add(new ProductsRecyclerModel("28a","Plant Vase","2000",R.drawable.plant_pot_img,0));
        productsRecyclerModelArrayList.add(new ProductsRecyclerModel("29a","Chair","2000",R.drawable.chair_img,0));
        productsRecyclerModelArrayList.add(new ProductsRecyclerModel("30a","Sofa","3000",R.drawable.sofa_img,0));
        productsRecyclerModelArrayList.add(new ProductsRecyclerModel("31a","Table","4000",R.drawable.table_img,0));
        productsRecyclerModelArrayList.add(new ProductsRecyclerModel("32a","Sofa","3000",R.drawable.sofa_img,0));
        productsRecyclerModelArrayList.add(new ProductsRecyclerModel("33a","Azure Ceramic","5000",R.drawable.ceramic_img,0));
        productsRecyclerModelArrayList.add(new ProductsRecyclerModel("34a","Lamp","1000",R.drawable.lamp_img,0));
        productsRecyclerModelArrayList.add(new ProductsRecyclerModel("35a","Plant Vase","2000",R.drawable.plant_pot_img,0));
        productsRecyclerModelArrayList.add(new ProductsRecyclerModel("36a","Lamp","1000",R.drawable.lamp_img,0));
        productsRecyclerModelArrayList.add(new ProductsRecyclerModel("37a","Table","4000",R.drawable.table_img,1));
        searchingItemRV.setAdapter(new SearchingAdapter(productsRecyclerModelArrayList,SearchFragment.this));

        return view;
    }

    // Method to open the keyboard
    private void openKeyboard(View view) {
        // Get the InputMethodManager
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }

        // Ensure the keyboard stays visible
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
}
