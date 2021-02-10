package com.example.recycle.MainUI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recycle.R;

import java.util.ArrayList;

public class DisposeAdapter extends RecyclerView.Adapter<DisposeAdapter.DisposeViewHolder> /*implements Filterable*/ {
    private ArrayList<DisposeCenter> mDisposeList;


    public static class DisposeViewHolder extends RecyclerView.ViewHolder {
     //   public ImageView mImageView;
        public TextView mTextView1;
      //  public TextView mTextView2;
        public DisposeViewHolder(View itemView) {
            super(itemView);
         //   mImageView = itemView.findViewById(R.id.imageView);
            mTextView1 = itemView.findViewById(R.id.textView);
         //   mTextView2 = itemView.findViewById(R.id.textView2);
        }
    }
    public DisposeAdapter(ArrayList<DisposeCenter> disposeList) {
        mDisposeList = disposeList;
//        exampleListFull = new ArrayList<>(exampleList);
    }


    @Override
    public DisposeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item, parent, false);
        DisposeViewHolder dvh = new DisposeAdapter.DisposeViewHolder(v);
        return dvh;

    }

    @Override
    public void onBindViewHolder( DisposeAdapter.DisposeViewHolder holder, int position) {
         DisposeCenter currentCenter = mDisposeList.get(position);
         //   holder.mImageView.setImageResource(currentCenter.getImageResource());
            holder.mTextView1.setText(currentCenter.getName());
         //   holder.mTextView2.setText(currentCenter.getText2());


    }

    @Override
    public int getItemCount() {
        return mDisposeList.size();
    }
}
