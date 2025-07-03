package com.example.scciapp.HR;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scciapp.R;

import java.util.ArrayList;
import java.util.Map;

public class MemberAnnouncementAdapter extends RecyclerView.Adapter<MemberAnnouncementAdapter.myviewholder> {
    Context context;
    ArrayList<Map<String, Object>> items;

    public MemberAnnouncementAdapter(Context context, ArrayList<Map<String,Object>> items) {
        this.context = context;
        this.items = items;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new myviewholder(LayoutInflater.from(context).inflate(R.layout.announcement_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        holder.AName.setText((String) items.get(position).get("authorName"));
        holder.Body.setText((String) items.get(position).get("announcementMessage"));
        holder.Date.setText((String) items.get(position).get("announcementDate"));
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AnnouncementViewActivity.class);
                intent.putExtra("announcementID", items.get(position).get("announcementID").toString());
                intent.putExtra("authorName", (String) items.get(position).get("authorName"));
                intent.putExtra("announcementMessage", (String) items.get(position).get("announcementMessage"));
                intent.putExtra("announcementDate", (String) items.get(position).get("announcementDate"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public class myviewholder extends RecyclerView.ViewHolder {
        TextView AName, Body, Date;
        CardView parent;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            AName = itemView.findViewById(R.id.author_name);
            Body = itemView.findViewById(R.id.body);
            Date = itemView.findViewById(R.id.date);
            parent = itemView.findViewById(R.id.post_card);
        }
    }
}
