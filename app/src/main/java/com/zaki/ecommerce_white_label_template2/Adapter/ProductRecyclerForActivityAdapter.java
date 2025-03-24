package com.zaki.ecommerce_white_label_template2.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.zaki.ecommerce_white_label_template2.Model.WishListModel;
import com.zaki.ecommerce_white_label_template2.R;
import com.zaki.ecommerce_white_label_template2.Utils.SessionManager;

import java.util.ArrayList;

public class ProductRecyclerForActivityAdapter extends RecyclerView.Adapter<ProductRecyclerForActivityAdapter.ViewHolder> {
    ArrayList<ProductsRecyclerModel> productArrayList;
    Context context;
    SessionManager sessionManager;
    public ProductRecyclerForActivityAdapter(ArrayList<ProductsRecyclerModel> productArrayList, Context context) {
        this.productArrayList = productArrayList;
        this.context = context;
        sessionManager = new SessionManager(context);
    }

    @NonNull
    @Override
    public ProductRecyclerForActivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_product_recycler_item_layout,parent,false);
        return new ProductRecyclerForActivityAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductRecyclerForActivityAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.productName.setText(productArrayList.get(position).getProductName());
        holder.productPrice.setText("₹ " + productArrayList.get(position).getProductPrice());
        holder.productImg.setImageResource(productArrayList.get(position).getProductImg());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SingleProductDetailsActivity.class);
                intent.putExtra("productId",productArrayList.get(position).getProductId());
                intent.putExtra("productName",productArrayList.get(position).getProductName());
                intent.putExtra("productPrice",productArrayList.get(position).getProductPrice());
                intent.putExtra("productImg",productArrayList.get(position).getProductImg());
                context.startActivity(intent);
            }
        });
        ArrayList<WishListModel> wishListModels = sessionManager.getWishList();
        for (int i = 0; i < wishListModels.size(); i++) {
            if (wishListModels.get(i).getProductId().equals(productArrayList.get(position).getProductId())) {
                holder.wishListImg.setImageResource(R.drawable.ic_heart_red);
                productArrayList.get(position).setWishlistToggle(1);
                break;
            } else {
                holder.wishListImg.setImageResource(R.drawable.ic_heart_grey);
                productArrayList.get(position).setWishlistToggle(0);
            }
        }

        holder.wishListImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int w = productArrayList.get(position).getWishlistToggle();
                if (w == 0){
                    WishListModel wishListModel1 = new WishListModel(productArrayList.get(position).getProductId(),productArrayList.get(position).getProductName(),productArrayList.get(position).getProductPrice(),"3.5",String.valueOf(productArrayList.get(position).getProductImg()),productArrayList.get(position).getWishlistToggle());
                    sessionManager.saveWishList(wishListModel1);
                    holder.wishListImg.setImageResource(R.drawable.ic_heart_red);
                    productArrayList.get(position).setWishlistToggle(1);
                }else if (w == 1){
                    WishListModel wishListModel1 = new WishListModel(productArrayList.get(position).getProductId(),productArrayList.get(position).getProductName(),productArrayList.get(position).getProductPrice(),"3.5",String.valueOf(productArrayList.get(position).getProductImg()),productArrayList.get(position).getWishlistToggle());
                    sessionManager.removeWishListItem(wishListModel1);
                    holder.wishListImg.setImageResource(R.drawable.ic_heart_grey);
                    productArrayList.get(position).setWishlistToggle(0);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName,productPrice;
        ImageView productImg,wishListImg;;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.productNameTxt);
            productPrice = itemView.findViewById(R.id.productPriceTxt);
            productImg = itemView.findViewById(R.id.productImg);
            wishListImg = itemView.findViewById(R.id.heartImg);
        }
    }
}