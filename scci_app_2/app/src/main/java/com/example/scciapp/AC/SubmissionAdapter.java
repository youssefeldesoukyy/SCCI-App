package com.example.scciapp.AC;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scciapp.R;

import java.util.ArrayList;
import java.util.Map;

public class SubmissionAdapter extends RecyclerView.Adapter<SubmissionViewHolder> {
    Context submitContext;
    ArrayList<Map<String ,Object>> submit;

    public SubmissionAdapter(Context submitContext ,ArrayList<Map<String ,Object>> submit) {
        this.submitContext = submitContext;
        this.submit = submit;
    }

    public void updateData(ArrayList<Map<String, Object>> newSubmission) {
        this.submit = newSubmission;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SubmissionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SubmissionViewHolder(LayoutInflater.from(submitContext)
                .inflate(R.layout.submissions_post_layout, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SubmissionViewHolder holder, int position) {
        Map<String, Object> i = submit.get(position);
        holder.participantName.setText((String) i.get("authorName"));
        holder.shared_date.setText((String) i.get("submissionDate"));
        holder.link.setText((String) i.get("submissionMessage"));
    }

    @Override
    public int getItemCount() {
        Log.d("SubmissionAdapter", "getItemCount: " + submit.size());
        return submit.size();}
}
