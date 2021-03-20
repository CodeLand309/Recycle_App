package com.example.recycle.MainUI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.recycle.R;
import com.example.recycle.RetrofitFolder.RestClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SellAdapter extends RecyclerView.Adapter<SellAdapter.SellViewHolder> {
//    private ArrayList<SellItem> mSellList;

    private ArrayList<ProductsItem> mProductList;
    private Context mContext;
    public OnItemClickListener mListener;
    String url = RestClient.BASE_URL + "product_image/";

    public interface OnItemClickListener{
        void onItemCLick(int position);
        void onOptionsCLick(int position, Button options);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class SellViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView1;
        public Button mButton;
//        public TextView mTextView2;
        public SellViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextView1 = itemView.findViewById(R.id.textView);
            mButton = itemView.findViewById(R.id.options);
//            mTextView2 = itemView.findViewById(R.id.textView2);
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
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onOptionsCLick(position, mButton);
                        }
                    }
                }
            });
        }
    }
    public SellAdapter(ArrayList<ProductsItem> sellList, Context context) {
        mProductList = sellList;
        mContext = context;
//        exampleListFull = new ArrayList<>(exampleList);
    }
    @Override
    public SellViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sell_item, parent, false);
        SellViewHolder eivh = new SellViewHolder(v, mListener);
        return eivh;
    }
    @Override
    public void onBindViewHolder(SellViewHolder holder, int position) {
        ProductsItem currentItem = mProductList.get(position);
        holder.mTextView1.setText(currentItem.getProductName());
        url = url + currentItem.getImage();
        Picasso.get().load(url).fit().centerInside().into(holder.mImageView);
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
