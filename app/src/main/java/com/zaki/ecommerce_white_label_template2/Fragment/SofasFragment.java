package com.zaki.ecommerce_white_label_template2.Fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zaki.ecommerce_white_label_template2.Adapter.ProductRecyclerForFragmentAdapter;
import com.zaki.ecommerce_white_label_template2.Model.ProductsRecyclerModel;
import com.zaki.ecommerce_white_label_template2.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SofasFragment extends Fragment {
    RecyclerView allProductRecyclerView;
    ProductRecyclerForFragmentAdapter productRecyclerForFragmentAdapter;
    ArrayList<ProductsRecyclerModel> productsRecyclerModelArrayList;
//    ArrayAdapter<String> dropdownStatusArrayAdapter;
//    ArrayAdapter<String> dropdownFilterArrayAdapter;
//    AutoCompleteTextView autoCompPriceTV, autoCompSortByTV;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_sofas, container, false);

//        autoCompPriceTV = view.findViewById(R.id.autoCompPriceTV);
//        autoCompSortByTV = view.findViewById(R.id.autoCompSortByTV);
//        setUpSpinners();

        allProductRecyclerView = view.findViewById(R.id.allProductRecyclerView);
        allProductRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));

        productsRecyclerModelArrayList = new ArrayList<>();
        productsRecyclerModelArrayList.add(new ProductsRecyclerModel("1sf","Sofa 1","2000",R.drawable.sofa_img,1));
        productsRecyclerModelArrayList.add(new ProductsRecyclerModel("2sf","Sofa 2","5000",R.drawable.sofa_img,0));
        productsRecyclerModelArrayList.add(new ProductsRecyclerModel("3sf","Sofa 3","1000",R.drawable.sofa_img,0));
        productsRecyclerModelArrayList.add(new ProductsRecyclerModel("4sf","Sofa 4","500",R.drawable.sofa_img,0));
        productsRecyclerModelArrayList.add(new ProductsRecyclerModel("5sf","Sofa 5","1500",R.drawable.sofa_img,0));
        productsRecyclerModelArrayList.add(new ProductsRecyclerModel("6sf","Sofa 6","3500",R.drawable.sofa_img,0));

        productRecyclerForFragmentAdapter = new ProductRecyclerForFragmentAdapter(productsRecyclerModelArrayList, SofasFragment.this);
        allProductRecyclerView.setAdapter(productRecyclerForFragmentAdapter);

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
            Collections.sort(productsRecyclerModelArrayList, new Comparator<ProductsRecyclerModel>() {
                @Override
                public int compare(ProductsRecyclerModel o1, ProductsRecyclerModel o2) {
                    return Integer.compare(Integer.parseInt(o2.getProductPrice()), Integer.parseInt(o1.getProductPrice()));
                }
            });
        } else if (position == 1) { // Assuming 1 is for "High to Low"
            Collections.sort(productsRecyclerModelArrayList, new Comparator<ProductsRecyclerModel>() {
                @Override
                public int compare(ProductsRecyclerModel o1, ProductsRecyclerModel o2) {
                    return Integer.compare(Integer.parseInt(o1.getProductPrice()), Integer.parseInt(o2.getProductPrice()));
                }
            });
        }

        productRecyclerForFragmentAdapter.notifyDataSetChanged();
    }

    // Sort by date (for example, newest to oldest)
    public void sortProductsByDate(int position) {
        if (position == 0) { // Assuming 0 is for "Newest First"
            Collections.reverse(productsRecyclerModelArrayList); // Reverse order (assuming default order is oldest to newest)
        } else if (position == 1) { // Assuming 1 is for "Oldest First"
            Collections.sort(productsRecyclerModelArrayList, new Comparator<ProductsRecyclerModel>() {
                @Override
                public int compare(ProductsRecyclerModel o1, ProductsRecyclerModel o2) {
                    return o1.getProductName().compareTo(o2.getProductName()); // Example sorting by name instead of actual date
                }
            });
        }

        productRecyclerForFragmentAdapter.notifyDataSetChanged();
    }
}
