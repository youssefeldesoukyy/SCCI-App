package com.example.scciapp.Participant;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scciapp.R;
import com.google.android.material.card.MaterialCardView;

public class ParticipantTaskPostsViewHolder extends RecyclerView.ViewHolder {

    TextView authorName, shared_date, deadLine,  postBody;
    public MaterialCardView post;

    public ParticipantTaskPostsViewHolder(@NonNull View itemView) {
        super(itemView);
        authorName = itemView.findViewById(R.id.author_name);
        shared_date = itemView.findViewById(R.id.date);
        deadLine = itemView.findViewById(R.id.deadline);
        postBody = itemView.findViewById(R.id.body);
        post = itemView.findViewById(R.id.post_card);
    }
}
