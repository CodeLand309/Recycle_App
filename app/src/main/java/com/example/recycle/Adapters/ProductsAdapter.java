package com.example.recycle.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.recycle.Model.ProductsItem;
import com.example.recycle.R;
import com.example.recycle.RetrofitFolder.RestClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {
    private ArrayList<ProductsItem> mProductList;
    private Context mContext;
    public OnItemClickListener mListener;
    String url = RestClient.BASE_URL + "product_image/";

    public interface OnItemClickListener{
        void onItemCLick(int position);
    }
    public Context getItemContext(){
        return mContext;
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public ProductViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextView1 = itemView.findViewById(R.id.textView);
            mTextView2 = itemView.findViewById(R.id.textView2);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemCLick(position);
                        }
                    }
                }
            });
        }
    }
    public ProductsAdapter(ArrayList<ProductsItem> exampleList, Context context) {
        mProductList = exampleList;
        mContext = context;
    }
    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        ProductViewHolder pvh = new ProductViewHolder(v, mListener);
        return pvh;
    }
    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        ProductsItem currentItem = mProductList.get(position);
//        holder.mImageView.setImageResource(currentItem.getImage());
        holder.mTextView1.setText(currentItem.getProductName());
        holder.mTextView2.setText("₹" + Integer.toString(currentItem.getPrice()));
        url = url + currentItem.getImage();
        Picasso.get().load(url).fit().centerInside().placeholder(R.drawable.ic_round_image_24).into(holder.mImageView);
        url = RestClient.BASE_URL + "product_image/";
    }
    @Override
    public int getItemCount() {
        if(mProductList!=null)
            return mProductList.size();
        return 0;
    }

    public void addProduct(List<ProductsItem> products){
        for(ProductsItem item : products){
            mProductList.add(item);
        }
        notifyDataSetChanged();
    }
}
