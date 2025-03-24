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
import androidx.recyclerview.widget.RecyclerView;

import com.zaki.ecommerce_white_label_template2.Activities.OrderSingleViewActivity;
import com.zaki.ecommerce_white_label_template2.Model.MyOrderModel;
import com.zaki.ecommerce_white_label_template2.R;

import java.util.ArrayList;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.ViewHolder> {
    ArrayList<MyOrderModel> productDetailsList;
    Context context;
    public MyOrdersAdapter(ArrayList<MyOrderModel> productDetailsList, Context context) {
        this.productDetailsList = productDetailsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_orders_item_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.orderStatusTxt.setText(productDetailsList.get(position).getOrderStatus());
        holder.orderDateTxt.setText(productDetailsList.get(position).getOrderDate());
        holder.orderIdTxt.setText("Order id: " +productDetailsList.get(position).getOrderId());
        holder.orderProductTitle.setText(productDetailsList.get(position).getOrderProductTitle());
        holder.orderPrice.setText("₹ " +productDetailsList.get(position).getOrderPrice());
//        holder.imageView.setImageResource(productDetailsList.get(position).getProductImage());

    }

    @Override
    public int getItemCount() {
        return productDetailsList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderStatusTxt, orderDateTxt, orderIdTxt, orderProductTitle, orderPrice;
        ImageView productImg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderStatusTxt = itemView.findViewById(R.id.orderStatusTxt);
            orderDateTxt = itemView.findViewById(R.id.orderDateTxt);
            orderIdTxt = itemView.findViewById(R.id.orderIdTxt);
            orderProductTitle = itemView.findViewById(R.id.orderProductTitle);
            orderPrice = itemView.findViewById(R.id.orderPrice);
            productImg = itemView.findViewById(R.id.productImg);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, OrderSingleViewActivity.class);
                    intent.putExtra("orderStatus", productDetailsList.get(getAdapterPosition()).getOrderStatus());
                    intent.putExtra("orderDate", productDetailsList.get(getAdapterPosition()).getOrderDate());
                    intent.putExtra("orderId", productDetailsList.get(getAdapterPosition()).getOrderId());
                    intent.putExtra("orderProductTitle", productDetailsList.get(getAdapterPosition()).getOrderProductTitle());
                    intent.putExtra("orderPrice", productDetailsList.get(getAdapterPosition()).getOrderPrice());
                    context.startActivity(intent);
                }
            });
        }
    }
}
