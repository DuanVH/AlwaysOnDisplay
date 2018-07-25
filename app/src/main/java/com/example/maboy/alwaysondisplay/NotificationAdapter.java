package com.example.maboy.alwaysondisplay;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by maboy on 21/09/2017.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.RecyclerViewHolder>{

    private List<ItemNotification> itemNotificationList;
    Context context;

    public NotificationAdapter(List<ItemNotification> itemNotificationList, Context context) {
        this.itemNotificationList = itemNotificationList;
        this.context = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.ivIcon.setImageBitmap(itemNotificationList.get(position).getBitmap());
    }

    @Override
    public int getItemCount() {
        return itemNotificationList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivIcon;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            ivIcon = (ImageView) itemView.findViewById(R.id.iv_icon_notification);
        }
    }
}
