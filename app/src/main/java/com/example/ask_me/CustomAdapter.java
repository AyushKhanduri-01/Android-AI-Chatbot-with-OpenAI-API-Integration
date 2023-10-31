package com.example.ask_me;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {


    private List<message> messageList;

    public CustomAdapter(List<message> messageList) {
       this.messageList=messageList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftchat;
        LinearLayout rightchat;
        TextView leftmessage;
        TextView rightmessage;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            rightchat = view.findViewById(R.id.rightchat);
            leftchat=view.findViewById(R.id.leftchat);
            leftmessage=view.findViewById(R.id.leftmessage);
            rightmessage=view.findViewById(R.id.rightmessage);

        }

//        public TextView getTextView() {
//
//        }
    }




    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.chatlayout, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        message msz = messageList.get(position);
        if(msz.getSender().equals(message.me)){
            viewHolder.leftchat.setVisibility(View.GONE);
            viewHolder.rightchat.setVisibility(View.VISIBLE);
            viewHolder.rightmessage.setText(msz.getMessage());
        }
        else{
            viewHolder.rightchat.setVisibility(View.GONE);
            viewHolder.leftchat.setVisibility(View.VISIBLE);
            viewHolder.leftmessage.setText(msz.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}

