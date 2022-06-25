package com.example.recycle.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recycle.Model.ProductsItem;
import com.example.recycle.R;
import com.example.recycle.Network.RestClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductsAdapter extends PagedListAdapter<ProductsItem, ProductsAdapter.ProductViewHolder> {
    private PagedList<ProductsItem> mProductList;
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
    public ProductsAdapter(Context context) {
        super(DIFF_CALLBACK);
        //mProductList = exampleList;
        mContext = context;
    }

    private static DiffUtil.ItemCallback<ProductsItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<ProductsItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull ProductsItem oldItem, @NonNull ProductsItem newItem) {
            return oldItem.getProductID() == newItem.getProductID();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ProductsItem oldItem, @NonNull ProductsItem newItem) {
            return oldItem.equals(newItem);
//            return  (oldItem.getProductName().equals(newItem.getProductName()) ||
//                    oldItem.getImage().equals(newItem.getImage()) ||
//                    oldItem.getDescription().equals(newItem.getDescription()) ||
//                    oldItem.getUserName().equals(newItem.getUserName()) ||
//                    oldItem.getUserID() == newItem.getUserID() ||
//                    oldItem.getPrice() == newItem.getPrice() ||
//                    oldItem.getDate().equals(newItem.getDate()) ||
//                    oldItem.getYears() == newItem.getYears() ||
//                    oldItem.getStatus() == newItem.getStatus());
        }
    };

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        ProductViewHolder pvh = new ProductViewHolder(v, mListener);
        return pvh;
    }
    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        //ProductsItem currentItem = mProductList.get(position);
        ProductsItem currentItem = getItem(position);
//        holder.mImageView.setImageResource(currentItem.getImage());
        holder.mTextView1.setText(currentItem.getProductName());
        holder.mTextView2.setText("₹" + Integer.toString(currentItem.getPrice()));
        holder.mTextView1.setSelected(true);
        url = url + currentItem.getImage();
        Picasso.get().load(url).fit().centerInside().placeholder(R.drawable.ic_round_image_24).into(holder.mImageView);
        url = RestClient.BASE_URL + "product_image/";
    }
//    @Override
//    public int getItemCount() {
//        if(mProductList!=null)
//            return mProductList.size();
//        return 0;
//    }

//    public void addProduct(List<ProductsItem> products){
//        for(ProductsItem item : products){
//            mProductList.add(item);
//        }
//        notifyDataSetChanged();
//    }
}
