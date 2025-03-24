package com.zaki.ecommerce_white_label_template2.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zaki.ecommerce_white_label_template2.Adapter.WishlistAdapter;
import com.zaki.ecommerce_white_label_template2.Model.WishListModel;
import com.zaki.ecommerce_white_label_template2.R;
import com.zaki.ecommerce_white_label_template2.Utils.SessionManager;

import java.util.ArrayList;

public class WishListFragment extends Fragment {
    RecyclerView wishListRecycler;
    ArrayList<WishListModel> newArrivalList;
    RelativeLayout noDataLayout,mainLayout;
    SessionManager sessionManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);

        sessionManager = new SessionManager(getContext());

        noDataLayout = view.findViewById(R.id.noDataLayout);
        mainLayout = view.findViewById(R.id.mainLayout);
        wishListRecycler = view.findViewById(R.id.wishListRecyclerView);
        wishListRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        newArrivalList = new ArrayList<>();
        newArrivalList = sessionManager.getWishList();
//        newArrivalList.add(new WishListModel("1","Product 1","200","2.5",0));
//        newArrivalList.add(new WishListModel("2","Product 2","300","5",0));
//        newArrivalList.add(new WishListModel("3","Product 3","250","3",0));
//        newArrivalList.add(new WishListModel("4","Product 4","3000","4.5",0));
//        newArrivalList.add(new WishListModel("5","Product 5","199","1.2",0));

        wishListRecycler.setAdapter(new WishlistAdapter(newArrivalList, WishListFragment.this));
        checkWishListItemArraySize();
        return  view;
    }
    public void checkWishListItemArraySize() {
        if (newArrivalList.isEmpty()){
            mainLayout.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.VISIBLE);
        }else {
            mainLayout.setVisibility(View.VISIBLE);
            noDataLayout.setVisibility(View.GONE);
        }
    }
}
