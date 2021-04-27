package com.example.recycle.MainUI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recycle.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{
    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    private ArrayList<Chat> mChatList;
    private Context mContext;
    public OnItemClickListener mListener;
    //    private String url;

    FirebaseUser fuser;

    public interface OnItemClickListener{
        void onItemCLick(int position);
    }
        public Context getItemContext(){
            return mContext;
        }
        public void setOnItemClickListener(OnItemClickListener listener){
            mListener = listener;
        }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        //        public ImageView show_message;
        public TextView show_message;
        public TextView txt_seen;

        public MessageViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
    //            mImageView = itemView.findViewById(R.id.profile);
            show_message = itemView.findViewById(R.id.show_message);
            txt_seen = itemView.findViewById(R.id.txt_seen);
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

        public MessageAdapter(ArrayList<Chat> exampleList, Context context) {
            mChatList = exampleList;
            mContext = context;
        }

        @NonNull
        @Override
        public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if(viewType == MSG_TYPE_RIGHT) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right, parent, false);
                MessageViewHolder uvh = new MessageViewHolder(v, mListener);
                return uvh;
            }
            else{
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false);
                MessageViewHolder uvh = new MessageViewHolder(v, mListener);
                return uvh;
            }
        }

        @Override
        public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
            Chat chat = mChatList.get(position);
            holder.show_message.setText(chat.getMessage());

            if(position == mChatList.size()-1){
                if(chat.isSeen_Status()){
                    holder.txt_seen.setText("Seen");
                }else{
                    holder.txt_seen.setText("Delivered");
                }
            }else{
                holder.txt_seen.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            if(mChatList !=null)
                return mChatList.size();
            return 0;
        }

        @Override
        public int getItemViewType(int position) {
            fuser = FirebaseAuth.getInstance().getCurrentUser();
            String Phone_Number = (fuser.getPhoneNumber()).substring(3);
            if(mChatList.get(position).getSender().equals(Phone_Number)){
                return MSG_TYPE_RIGHT;
            }
            else {
                return MSG_TYPE_LEFT;
            }
        }
}
