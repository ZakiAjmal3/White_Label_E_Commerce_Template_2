package com.zaki.ecommerce_white_label_template2.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.zaki.ecommerce_white_label_template2.Activities.HomePageActivity;
import com.zaki.ecommerce_white_label_template2.Activities.SingleProductDetailsActivity;
import com.zaki.ecommerce_white_label_template2.Fragment.WishListFragment;
import com.zaki.ecommerce_white_label_template2.Model.WishListModel;
import com.zaki.ecommerce_white_label_template2.R;
import com.zaki.ecommerce_white_label_template2.Utils.CustomRatingBar;
import com.zaki.ecommerce_white_label_template2.Utils.SessionManager;

import java.util.ArrayList;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {
    ArrayList<WishListModel> productDetailsList;
    Fragment context;
    SessionManager sessionManager;
    public WishlistAdapter(ArrayList<WishListModel> productDetailsList, Fragment context) {
        this.productDetailsList = productDetailsList;
        this.context = context;
        sessionManager = new SessionManager(context.getContext());
    }

    @NonNull
    @Override
    public WishlistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.productName.setText(productDetailsList.get(position).getProductName());
        holder.productPrice.setText("₹ " + productDetailsList.get(position).getProductPrice());
        holder.productRating.setText(productDetailsList.get(position).getProductRating() + "/5");
        holder.productImg.setImageResource(Integer.parseInt(productDetailsList.get(position).getProductImg()));
        holder.ratingBar.setRating(3.5f);

        holder.buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getContext(), HomePageActivity.class);
                intent.putExtra("LoadCartFrag",true);
                context.startActivity(intent);
            }
        });

        holder.wishlistImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int state;
                state = productDetailsList.get(position).getWishListImgToggle();
                if (state == 0) {
                    holder.wishlistImg.setImageResource(R.drawable.ic_heart_red);
                    productDetailsList.get(position).setWishListImgToggle(1);
                }else {
                    holder.wishlistImg.setImageResource(R.drawable.ic_heart_grey);
                    productDetailsList.get(position).setWishListImgToggle(0);
                }
            }
        });

        if (context instanceof WishListFragment){
            Log.e("enter1","true");
            holder.wishlistImg.setImageResource(R.drawable.ic_delete);
            holder.wishlistImg.setPadding(15,15,15,15);
            holder.buyBtn.setVisibility(View.VISIBLE);

            holder.wishlistImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sessionManager.removeWishListItem(new WishListModel(productDetailsList.get(position).getProductId(),productDetailsList.get(position).getProductName(),productDetailsList.get(position).getProductPrice(),"3.5",productDetailsList.get(position).getProductImg(),0));
                    productDetailsList.remove(position);
                    notifyDataSetChanged();
                    ((WishListFragment) context).checkWishListItemArraySize();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return productDetailsList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName,productPrice,productRating;
        ImageView productImg,wishlistImg;
        CustomRatingBar ratingBar;
        Button buyBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.productNameTxt);
            productPrice = itemView.findViewById(R.id.productPriceTxt);
            productRating = itemView.findViewById(R.id.ratingTxt);
            productImg = itemView.findViewById(R.id.productImg);
            wishlistImg = itemView.findViewById(R.id.wishlistImg);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            buyBtn = itemView.findViewById(R.id.buy);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context.getContext(), SingleProductDetailsActivity.class);
                    intent.putExtra("productName",productDetailsList.get(getAdapterPosition()).getProductName());
                    intent.putExtra("productPrice",productDetailsList.get(getAdapterPosition()).getProductPrice());
                    intent.putExtra("productRating",productDetailsList.get(getAdapterPosition()).getProductRating());
                    context.startActivity(intent);

                }
            });
        }
    }
}
