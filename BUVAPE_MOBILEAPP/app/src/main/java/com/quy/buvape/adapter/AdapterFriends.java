package com.quy.buvape.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quy.buvape.R;
import com.quy.buvape.model.Friend;
import com.quy.buvape.view.ChatActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterFriends extends RecyclerView.Adapter<AdapterFriends.FriendHolder> {

    public ArrayList<Friend> arrFriends;
    private Context context;

    public AdapterFriends(ArrayList<Friend> arrFriends,Context context){
        this.arrFriends = arrFriends;
        this.context = context;
    }
    @NonNull
    @Override
    public FriendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FriendHolder(LayoutInflater.from(context).inflate(R.layout.custom_friends_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull FriendHolder holder, int position) {
        Friend friend = arrFriends.get(position);
        holder.txtName.setText(friend.getFirstname() + " " +  friend.getLastname());
        holder.txtPhone.setText(friend.getPhone());
        if (friend.isOnline()){
            holder.online.setImageResource(0);
        }else{
            holder.online.setImageResource(R.drawable.background_online_user);
        }

        holder.btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("friends_name",friend.getFirstname() + " " + friend.getLastname());
                intent.putExtra("friend_id",String.valueOf(friend.getId()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrFriends.size();
    }

    class FriendHolder extends RecyclerView.ViewHolder{

        private CircleImageView avatar;
        private ImageView online;
        private TextView txtName;
        private TextView txtPhone;
        private Button btnMessage;
        private Button btnDelete;

        public FriendHolder(@NonNull View view) {
            super(view);
            avatar = view.findViewById(R.id.img_contact_person);
            online = view.findViewById(R.id.img_status_online);
            txtName = view.findViewById(R.id.txt_name_friend);
            txtPhone = view.findViewById(R.id.txt_Phone_Number);
            btnMessage = view.findViewById(R.id.btn_message);
            btnDelete = view.findViewById(R.id.btn_delete);

        }
    }



}
