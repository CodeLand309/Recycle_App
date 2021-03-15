package com.example.recycle.MainUI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.recycle.R;

import java.util.ArrayList;

public class SellAdapter extends RecyclerView.Adapter<SellAdapter.SellViewHolder> {
    private ArrayList<SellItem> mSellList;

    public static class SellViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public SellViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextView1 = itemView.findViewById(R.id.textView);
            mTextView2 = itemView.findViewById(R.id.textView2);
        }
    }
    public SellAdapter  (ArrayList<SellItem> sellList) {
        mSellList = sellList;
//        exampleListFull = new ArrayList<>(exampleList);
    }
    @Override
    public SellViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        SellViewHolder eivh = new SellViewHolder(v);
        return eivh;
    }
    @Override
    public void onBindViewHolder(SellViewHolder holder, int position) {
        SellItem sellCurrentItem = mSellList.get(position);
        holder.mImageView.setImageResource(sellCurrentItem.getImageResource());
        holder.mTextView1.setText(sellCurrentItem.getText1());
        holder.mTextView2.setText(sellCurrentItem.getText2());
    }
    @Override
    public int getItemCount() {
        return mSellList.size();
    }
}
