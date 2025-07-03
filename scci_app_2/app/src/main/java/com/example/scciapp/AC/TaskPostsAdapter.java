package com.example.scciapp.AC;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scciapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Map;

public class TaskPostsAdapter extends RecyclerView.Adapter<TaskPostsViewHolder> {
    
    FloatingActionButton addBtn;
    Context postsContext;
    ArrayList<Map<String ,Object>> taskPosts;
    public TaskPostsAdapter(FloatingActionButton Btn) {
        this.addBtn = Btn;
    }

    public TaskPostsAdapter(Context postsContext, ArrayList<Map<String ,Object>> taskPosts) {
        this.postsContext = postsContext;
        this.taskPosts = taskPosts;
        notifyDataSetChanged();

    }
    public void updateData(ArrayList<Map<String, Object>> newTaskPosts) {
        this.taskPosts = newTaskPosts;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public TaskPostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskPostsViewHolder(LayoutInflater.from(postsContext).inflate(R.layout.task_item_layout, parent, false));
    }

    @SuppressLint({"SetTextI18n", "RecyclerView"})
    @Override
    public void onBindViewHolder(@NonNull TaskPostsViewHolder holder, int position) {
        holder.authorName.setText((String)taskPosts.get(position).get("authorName"));
        holder.shared_date.setText((String)taskPosts.get(position).get("taskDate"));
        holder.deadLine.setText("Deadline: " + taskPosts.get(position).get("taskDeadline"));
        holder.postBody.setText((String)taskPosts.get(position).get("taskMessage"));
        holder.post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(postsContext, ACViewTaskActivity.class);
                Map<String, Object> i = taskPosts.get(position);
                intent.putExtra("taskID", i.get("taskID").toString());
                intent.putExtra("authorName", (String) i.get("authorName"));
                intent.putExtra("taskMessage", (String) i.get("taskMessage"));
                intent.putExtra("taskDate", (String) i.get("taskDate"));
                intent.putExtra("taskDeadline", (String) i.get("taskDeadline"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                postsContext.startActivity(intent);
            }

        });
    }

    @Override
    public int getItemCount() {
        return taskPosts.size();
    }
}

