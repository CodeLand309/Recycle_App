package com.example.recycle.MainUI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recycle.R;
import com.example.recycle.RetrofitFolder.RestClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {
    private ArrayList<ChatUsers> mUserList;
    private Context mContext;
    public OnItemClickListener mListener;
    private String theLastMessage, url= RestClient.BASE_URL + "user_image/";
    private boolean isChat;

    public interface OnItemClickListener{
        void onItemCLick(int position);
    }
    public Context getItemContext(){
        return mContext;
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView last_msg;

        public UserViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.profile);
            mTextView1 = itemView.findViewById(R.id.friend_name);
            last_msg = itemView.findViewById(R.id.last_message);
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

    public UsersAdapter(ArrayList<ChatUsers> exampleList, Context context, boolean isChat) {
        mUserList = exampleList;
        mContext = context;
        this.isChat = isChat;
//        exampleListFull = new ArrayList<>(exampleList);
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends, parent, false);
        UserViewHolder uvh = new UserViewHolder(v, mListener);
        return uvh;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        ChatUsers currentItem = mUserList.get(position);
//        holder.mImageView.setImageURI(currentItem.getProfile());
        holder.mTextView1.setText(currentItem.getName());
        if(currentItem.getImage().equals("None"))
            holder.mImageView.setImageResource(R.drawable.ic_round_person_24);
        else {
            url = url + currentItem.getPhone() + "/" + currentItem.getName();
            Picasso.get().load(url).fit().centerInside().placeholder(R.drawable.ic_round_person_24).into(holder.mImageView);
        }
        url = RestClient.BASE_URL + "user_image/";
//        if(isChat) {
        setTheLastMessage(currentItem.getPhone(), holder.last_msg);
//        }
//        else{
//            holder.last_msg.setVisibility(View.GONE);
//        }
    }

    @Override
    public int getItemCount() {
        if(mUserList !=null)
            return mUserList.size();
        return 0;
    }

    public void setTheLastMessage(final String rec_phone, final TextView last_msg){
        theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    String Phone_Number = (firebaseUser.getPhoneNumber()).substring(3);
                    if(chat.getReceiver().equals(Phone_Number) && chat.getSender().equals(rec_phone) ||
                            chat.getReceiver().equals(rec_phone) && chat.getSender().equals(Phone_Number)){
                        theLastMessage = chat.getMessage();
                    }
                }

                switch (theLastMessage){
                    case "default":
                        last_msg.setText("____No Messages____");
                        break;

                    default:
                        last_msg.setText(theLastMessage);
                        break;
                }

                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
