package com.example.recycle.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recycle.Model.DisposeCentre;
import com.example.recycle.R;

import java.util.ArrayList;
import java.util.List;

public class DisposeAdapter extends PagedListAdapter<DisposeCentre, DisposeAdapter.DisposeViewHolder> {
//    private PagedList<DisposeCentre> mDisposeList;
    private Context mContext;
    public OnCentreClickListener mListener;

    public interface OnCentreClickListener{
        void onCentreCLick(int position);
    }

//    public void setDisposeCentres(PagedList<DisposeCentre> centres){
//        mDisposeList = centres;
//    }
    public void setOnItemClickListener(OnCentreClickListener listener){
        mListener = listener;
    }

    public static class DisposeViewHolder extends RecyclerView.ViewHolder {
     //   public ImageView mImageView;
        public TextView mTextView1;
      //  public TextView mTextView2;
        public DisposeViewHolder(View itemView, final OnCentreClickListener listener) {
            super(itemView);
         //   mImageView = itemView.findViewById(R.id.imageView);
            mTextView1 = itemView.findViewById(R.id.center_name);
         //   mTextView2 = itemView.findViewById(R.id.textView2);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onCentreCLick(position);
                        }
                    }
                }
            });
        }
    }
    public DisposeAdapter(Context context) {
        super(DIFF_CALLBACK);
        mContext = context;
    }

    public DiffUtil.ItemCallback<DisposeCentre> getDiffCallback(){
        return DIFF_CALLBACK;
    }

    private static DiffUtil.ItemCallback<DisposeCentre> DIFF_CALLBACK = new DiffUtil.ItemCallback<DisposeCentre>() {
        @Override
        public boolean areItemsTheSame(@NonNull DisposeCentre oldItem, @NonNull DisposeCentre newItem) {
            return oldItem.getCentreID() == newItem.getCentreID();
        }

        @Override
        public boolean areContentsTheSame(@NonNull DisposeCentre oldItem, @NonNull DisposeCentre newItem) {
            return oldItem.equals(newItem);
//            return (oldItem.getCentreName().equals(newItem.getCentreName()) ||
//                    oldItem.getCentreAddress().equals(newItem.getCentreAddress()) ||
//                    oldItem.getCentrePhone().equals(newItem.getCentrePhone())) ||
//                    oldItem.getCentreImage().equals(newItem.getCentreImage());
        }
    };

    @Override
    public DisposeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dispose_center, parent, false);
        DisposeViewHolder dvh = new DisposeAdapter.DisposeViewHolder(v, mListener);
        return dvh;

    }

    @Override
    public void onBindViewHolder( DisposeAdapter.DisposeViewHolder holder, int position) {
         //DisposeCentre currentCentre = mDisposeList.get(position);
        DisposeCentre currentCentre = getItem(position);
         //   holder.mImageView.setImageResource(currentCenter.getImageResource());
            holder.mTextView1.setText(currentCentre.getCentreName());
         //   holder.mTextView2.setText(currentCenter.getText2());
    }

//    @Override
//    public int getItemCount() {
//        if(mDisposeList!=null)
//            return mDisposeList.size();
//        return 0;
//    }

//    public void addCentre(List<DisposeCentre> centres){
//        for(DisposeCentre centre : centres){
//            mDisposeList.add(centre);
//        }
//        notifyDataSetChanged();
//    }

}
