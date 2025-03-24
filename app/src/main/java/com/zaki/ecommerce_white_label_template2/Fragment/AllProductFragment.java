package com.zaki.ecommerce_white_label_template2.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class AllProductFragment extends Fragment {

    RecyclerView allProductRecyclerView;
    ProductRecyclerForFragmentAdapter productRecyclerForFragmentAdapter;
    ArrayList<ProductsRecyclerModel> productsRecyclerModelArrayList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_all_products, container, false);

        allProductRecyclerView = view.findViewById(R.id.allProductRecyclerView);
        allProductRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));

//        setUpSpinners();

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

        productRecyclerForFragmentAdapter = new ProductRecyclerForFragmentAdapter(productsRecyclerModelArrayList, AllProductFragment.this);
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
//        if (productsRecyclerModelArrayList == null || productsRecyclerModelArrayList.isEmpty()) {
//            Log.e("Sort Error", "Product list is null or empty.");
//            return; // Exit if list is null or empty
//        }
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
