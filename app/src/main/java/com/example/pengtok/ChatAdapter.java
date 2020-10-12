package com.example.pengtok;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    private List<ChatData> mDataset;
    private String myNickName;

    /*private static View.OnClickListener onClickListener;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder*/

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView TextView_nickname;
        public TextView TextView_msg;
        public TextView TextView_time;
        public ImageView imageView_profile;
        public LinearLayout linearLayout_destinamtion;
        public LinearLayout linearLayout_main;


        public MyViewHolder(View v) {
            super(v);
            TextView_nickname = v.findViewById(R.id.TextView_nickname);
            TextView_msg = v.findViewById(R.id.TextView_msg);
            TextView_time = v.findViewById(R.id.TextView_time);
            imageView_profile = v.findViewById(R.id.ImageView_profile);
            linearLayout_destinamtion = v.findViewById(R.id.LinearLayout_destination);
            linearLayout_main = v.findViewById(R.id.LinearLayout_main);
        }

        public void setItem(ChatData chat) {
            TextView_nickname.setText(chat.getNickname());
            TextView_msg.setText(chat.getMsg());

            long mNow = System.currentTimeMillis();
            Date mDate = new Date(mNow);
            SimpleDateFormat mFormat = new SimpleDateFormat("hh:mm aa");
            String formatDate = mFormat.format(mDate);

            TextView_time.setText(formatDate);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ChatAdapter(List<ChatData> myDataset, Context context, String myNickName) {//제일처음 가져오는곳
        mDataset = myDataset;
        this.myNickName = myNickName;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {

        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_chat, parent, false);//어떤 레이아웃을 쓸건지 지정


        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        ChatData chat = mDataset.get(position);

        holder.setItem(chat);

        Log.d("채팅확인", chat.toString());
        //내가 보낸 메세지
        if (chat.getNickname().equals(this.myNickName)) {
            /*holder.TextView_msg.setGravity(Gravity.RIGHT);*/
            holder.TextView_nickname.setVisibility(View.INVISIBLE);
            holder.linearLayout_destinamtion.setVisibility(View.INVISIBLE);
            holder.TextView_msg.setBackgroundResource(R.drawable.rightbubble);
            holder.TextView_msg.setTextSize(20);
            holder.linearLayout_main.setGravity(Gravity.RIGHT);
            //상대방이 보낸 메세지
        } else {
            holder.TextView_nickname.setVisibility(View.VISIBLE);
            holder.TextView_msg.setBackgroundResource(R.drawable.leftbubble);
            holder.TextView_msg.setTextSize(20);
            holder.linearLayout_main.setGravity(Gravity.LEFT);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        //삼항 연산자
        return mDataset == null ? 0 : mDataset.size();
    }

    public ChatData getChat(int position) {
        return mDataset != null ? mDataset.get(position) : null;
    }

    public void addChat(ChatData chat) {
        mDataset.add(chat);
        notifyDataSetChanged();//갱신용
/*
        notifyItemInserted(mDataset.size() - 1);//데이터의 사이즈를 넣어줌-갱신용
*/
        //0,1,2=3 3번지가 없기 때문에 -1

    }


}
