package com.example.pengtok;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.pengtok.model.ChatModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ChatData> chatList;
   
    private EditText EditText_chat;
    private Button Button_send;
    private DatabaseReference myRef;
    private String destinationUid;

    private String uid;
    private String chatRoomUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid(); //채팅을 요구하는 아이디 즉 단말기에 로그인된 uid
        Button_send = findViewById(R.id.Button_send);
        EditText_chat = findViewById(R.id.EditText_chat);
        destinationUid = getIntent().getStringExtra("destinationUid"); //채팅을 당하는 아이디
        checkChatRoom();


        Button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatModel chatModel = new ChatModel();
                chatModel.users.put(uid,true);


                if(chatRoomUid == null){
                    FirebaseDatabase.getInstance().getReference().child("chatrooms").push().setValue(chatModel);
                }else{
                    String msg = EditText_chat.getText().toString(); //msg
                    if (msg != null) {
                        ChatData chat = new ChatData();
                        chat.setMsg(msg);

                        long mNow = System.currentTimeMillis();
                        Date mDate = new Date(mNow);
                        SimpleDateFormat mFormat = new SimpleDateFormat("hh:mm aa");
                        String formatDate = mFormat.format(mDate);

                        chat.setTime(formatDate);
                        myRef.push().setValue(chat);
/*
                        FirebaseDatabase.getInstance().getReference().child("chatrooms").child("chatroomUid").child("comments").push().setValue(chat);
*/

                        /*Log.d("챗챗확인",get);*/
                        EditText_chat.setText("");//자판 초기화
                    }

                }

            }
        });

        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        chatList = new ArrayList<>();
        mAdapter = new ChatAdapter(chatList, ChatActivity.this, destinationUid);
        mRecyclerView.setAdapter(mAdapter);
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        Log.d("REF", String.valueOf(myRef));

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.e("CHATCHAT", snapshot.getValue().toString());
                ChatData chat = (ChatData)snapshot.getValue(ChatData.class);
                mRecyclerView.scrollToPosition(chatList.size());
                ((ChatAdapter) mAdapter).addChat(chat);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
       /* passPushTokenToServer();*/
    }

    /*void passPushTokenToServer(){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String token = FirebaseInstanceId.getInstance().getToken();
        Map<String,Object> map = new HashMap<>();
        map.put("pushToken",token);

        FirebaseDatabase.getInstance().getReference().child("nick").child(uid).updateChildren(map);
    }*/

    //채팅방 중복체크
    public void checkChatRoom(){
        FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+uid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot item : snapshot.getChildren()){
                    ChatModel chatModel = item.getValue(ChatModel.class);
                    if(chatModel.users.containsKey(destinationUid)){
                        chatRoomUid = item.getKey(); //방 아이디를 가지고 온다
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
