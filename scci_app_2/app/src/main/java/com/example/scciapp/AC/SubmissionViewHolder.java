package com.example.scciapp.AC;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scciapp.R;

public class SubmissionViewHolder extends RecyclerView.ViewHolder{
    TextView participantName, shared_date,  link;
    public ConstraintLayout sub;

    public SubmissionViewHolder(@NonNull View itemView) {
        super(itemView);
        participantName = itemView.findViewById(R.id.participant_name);
        shared_date = itemView.findViewById(R.id.shared_date);
        link = itemView.findViewById(R.id.sublink);
        sub = itemView.findViewById(R.id.sub);
    }
}
