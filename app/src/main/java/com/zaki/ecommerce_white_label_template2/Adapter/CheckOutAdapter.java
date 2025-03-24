package com.zaki.ecommerce_white_label_template2.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zaki.ecommerce_white_label_template2.Activities.CheckOutOrderActivity;
import com.zaki.ecommerce_white_label_template2.Activities.HomePageActivity;
import com.zaki.ecommerce_white_label_template2.Model.CheckOutModel;
import com.zaki.ecommerce_white_label_template2.R;

import java.util.ArrayList;

public class CheckOutAdapter extends RecyclerView.Adapter<CheckOutAdapter.ViewHolder> {
    ArrayList<CheckOutModel> productDetailsList;
    Context context;
    public CheckOutAdapter(ArrayList<CheckOutModel> productDetailsList, Context context) {
        this.productDetailsList = productDetailsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkout_items_layout,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.productTitleTxt.setText(productDetailsList.get(position).getProductTitle());
        holder.productPriceTxt.setText("₹ " +productDetailsList.get(position).getProductPrice());
//        holder.productImg.setImageResource(productDetailsList.get(position).getProductImage());

        holder.editLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomePageActivity.class);
                intent.putExtra("LoadCartFrag",true);
                context.startActivity(intent);
            }
        });
        holder.deleteLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productDetailsList.remove(position);
                notifyDataSetChanged();
                ((CheckOutOrderActivity) context).setOrderSummaryDetails();
                ((CheckOutOrderActivity) context).checkOutItemArraySize();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productDetailsList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productTitleTxt, productPriceTxt;
        ImageView productImg;
        LinearLayout editLL, deleteLL;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productTitleTxt = itemView.findViewById(R.id.productTitleTxt);
            productPriceTxt = itemView.findViewById(R.id.productPriceTxt);
            productImg = itemView.findViewById(R.id.productImg);
            editLL = itemView.findViewById(R.id.editLL);
            deleteLL = itemView.findViewById(R.id.deleteLL);
        }
    }
}
