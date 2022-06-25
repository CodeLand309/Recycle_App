package com.example.recycle.SubActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recycle.Adapters.MessageAdapter;
import com.example.recycle.Model.Chat;
import com.example.recycle.Model.ChatUsers;
import com.example.recycle.Notifications.Client;
import com.example.recycle.Notifications.Data;
import com.example.recycle.Notifications.MyResponse;
import com.example.recycle.Notifications.Sender;
import com.example.recycle.Notifications.Token;
import com.example.recycle.R;
import com.example.recycle.Network.APIService;
import com.example.recycle.Network.RestClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    private String name, phone, user, Phone_Number, image, url = RestClient.BASE_URL + "user_image/";
    private int id, count;
    private ImageButton send;

    private CircleImageView profile;
    private TextView user_name;

    private EditText type_message;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser fUser;
    private DatabaseReference reference;

    private MessageAdapter messageAdapter;
    private ArrayList<Chat> mChat;

    private ValueEventListener seenListener;

    private RecyclerView recyclerView;

    private APIService apiService;
    boolean notify=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent i = getIntent();
        phone = i.getStringExtra("Phone");
//        ActionBar toolbar = getSupportActionBar();
//        toolbar.setTitle(name);
////        toolbar.setLogo(R.drawable.ic_round_person_24);
//        toolbar.setDisplayHomeAsUpEnabled(true);


        profile = findViewById(R.id.profile_image);
        user_name = findViewById(R.id.user_name);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(phone);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ChatUsers users = dataSnapshot.getValue(ChatUsers.class);
                try {
                    name = users.getName();
//                image=users.getImage();
                    id = users.getId();
                    url = url + phone + "/" + name;
                    user_name.setText(name);
                    if (url == null)
                        profile.setImageResource(R.drawable.ic_round_person_24);
                    else
                        Picasso.get().load(url).fit().centerInside().placeholder(R.drawable.ic_round_person_24).into(profile);
                }catch (Exception e){
                    Toast.makeText(ChatActivity.this, "Cannot Chat with this User", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);


        recyclerView = findViewById(R.id.chat_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        type_message = findViewById(R.id.text_send);
        send = findViewById(R.id.button_send);

        Phone_Number = (fUser.getPhoneNumber()).substring(3);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                notify=true;
                String msg = type_message.getText().toString();
                if(!msg.equals("")){
                    sendMessage(Phone_Number, phone, msg);
                    user = Phone_Number;
                }
                else{
                    Toast.makeText(ChatActivity.this, "Type Something", Toast.LENGTH_SHORT).show();
                }
                type_message.setText("");
            }
        });

        try {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(phone, 101);
        }catch(Exception e){
        }

        readMessage(Phone_Number, phone);
        seenMessage(phone);
    }
    private void sendMessage(String send_phone, String rec_phone, String msg) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Sender", send_phone);
        hashMap.put("Receiver", rec_phone);
        hashMap.put("Message", msg);
        hashMap.put("Seen_Status", false);

        reference.child("Chats").push().setValue(hashMap);

        final String message = msg;

        reference = FirebaseDatabase.getInstance().getReference("Users").child(send_phone);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ChatUsers user = dataSnapshot.getValue(ChatUsers.class);
                if(notify) {
                    SendNotification(rec_phone, user.getName(), message);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SendNotification(String rec_phone, String name, String message) {
        Phone_Number = (fUser.getPhoneNumber()).substring(3);
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(rec_phone);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(Phone_Number, name, R.mipmap.ic_launcher_round, name+": "+message, "New Message", rec_phone);

                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                            if(response.code() == 200){
                                if(response.body().success != 1){
                                    Toast.makeText(ChatActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void seenMessage(String rec_phone){
        Phone_Number = (fUser.getPhoneNumber()).substring(3);
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(Phone_Number) && chat.getSender().equals(rec_phone)){
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("Seen_Status", true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readMessage(String my_phone, String rec_phone){
        mChat = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mChat.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(my_phone) && chat.getSender().equals(rec_phone) ||
                            chat.getReceiver().equals(rec_phone) && chat.getSender().equals(my_phone)){
                        mChat.add(chat);
                    }
                    messageAdapter = new MessageAdapter(mChat, ChatActivity.this);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void currentUser(String user_phone){
        SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
        editor.putString("current_user", user_phone);
        editor.commit();
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
////        switch (item.getItemId()) {
////            // Respond to the action bar's Up/Home button
////            case android.R.id.home:
//////                NavUtils.navigateUpFromSameTask(this);
//////                return true;
////        }
//
//        if (item.getItemId() == android.R.id.home) // Press Back Icon
//        {
//            finish();
////            onBackPressed();
//        }
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    protected void onResume() {
        super.onResume();
        currentUser(phone);
    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListener);
        currentUser("none");
    }
}