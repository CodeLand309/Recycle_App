package com.example.recycle.MainUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recycle.MainUI.Notifications.Token;
import com.example.recycle.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

public class ChatFragment extends Fragment {

    private ArrayList<ChatUsers> mUserList;
    private String phone, name, image, Phone_Number;
    private int id;

    private RecyclerView mRecyclerView;
    private UsersAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private DatabaseReference mDatabase;
    private FirebaseUser firebaseUser;

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerView);

        setHasOptionsMenu(true);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mUserList = new ArrayList<>();
        mAdapter = new UsersAdapter(mUserList,getContext(), false);
        mRecyclerView.setAdapter(mAdapter);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        readUsers();

        updateToken(FirebaseInstanceId.getInstance().getToken());
        return view;
    }
    private void updateToken(String token){
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Phone_Number = firebaseUser.getPhoneNumber();
        Phone_Number = Phone_Number.substring(3);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(Phone_Number).setValue(token1);
    }

    private void readUsers() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Phone_Number = firebaseUser.getPhoneNumber();
        Phone_Number = Phone_Number.substring(3);
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mUserList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ChatUsers user = dataSnapshot.getValue(ChatUsers.class);
                    assert user != null;
                    if (!(user.getPhone().equals(Phone_Number)))
                        mUserList.add(user);
                }
                mAdapter = new UsersAdapter(mUserList,getContext(), true);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                mAdapter.setOnItemClickListener(new UsersAdapter.OnItemClickListener() {
                    @Override
                    public void onItemCLick(int position) {
                        name = mUserList.get(position).getName();
                        phone = mUserList.get(position).getPhone();
                        image = mUserList.get(position).getImage();
                        id = mUserList.get(position).getId();
                        Intent i = new Intent(getContext(), ChatActivity.class);
                        i.putExtra("Phone", phone);
                        startActivity(i);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Could Not Load Data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
