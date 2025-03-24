package com.zaki.ecommerce_white_label_template2.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.zaki.ecommerce_white_label_template2.Activities.SingleProductDetailsActivity;
import com.zaki.ecommerce_white_label_template2.Model.ProductsRecyclerModel;
import com.zaki.ecommerce_white_label_template2.R;

import java.util.ArrayList;

public class SearchingAdapter extends RecyclerView.Adapter<SearchingAdapter.ViewHolder> {
    ArrayList<ProductsRecyclerModel> productArrayList;
    Fragment context;
    public SearchingAdapter(ArrayList<ProductsRecyclerModel> productArrayList, Fragment context) {
        this.productArrayList = productArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.searching_items_recycler_layout,parent,false);
        return new SearchingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchingAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
//        holder.productName.setText(productArrayList.get(position).getProductName());
//        holder.productPrice.setText("₹ " + productArrayList.get(position).getProductPrice());
        holder.productImg.setImageResource(productArrayList.get(position).getProductImg());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getContext(), SingleProductDetailsActivity.class);
                intent.putExtra("productName",productArrayList.get(position).getProductName());
                intent.putExtra("productPrice",productArrayList.get(position).getProductPrice());
                intent.putExtra("productImg",productArrayList.get(position).getProductImg());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
//        TextView productName,productPrice;
        ImageView productImg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

//            productName = itemView.findViewById(R.id.productNameTxt);
//            productPrice = itemView.findViewById(R.id.productPriceTxt);
            productImg = itemView.findViewById(R.id.productImg);

        }
    }
}