package com.example.recycle.SubActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.recycle.R;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>{
    private ArrayList<HistoryItem> mHistoryList;
    private Context mContext;

    public OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemCLick(int position);
    }
    public Context getItemContext(){
        return mContext;
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
//        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public HistoryViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
//            mImageView = itemView.findViewById(R.id.imageView);
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
    public HistoryAdapter(ArrayList<HistoryItem> historyList,  Context context) {
        mHistoryList = historyList;
        mContext = context;
    }
    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        HistoryViewHolder hvh = new HistoryViewHolder(v, mListener);
        return hvh;
    }
    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        HistoryItem currentItem = mHistoryList.get(position);
//        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mTextView1.setText(currentItem.getProductName());
        holder.mTextView2.setText(currentItem.getDate());
    }
    @Override
    public int getItemCount() {
        if(mHistoryList!=null)
            return mHistoryList.size();
        return 0;
    }
}
