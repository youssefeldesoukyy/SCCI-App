package com.example.scciapp.Participant;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scciapp.AC.AnnouncementInterface;
import com.example.scciapp.AC.WorkshopAnnouncementsAdapter;
import com.example.scciapp.R;

import java.util.ArrayList;
import java.util.Map;

public class ParticipantWorkshopAnnouncementsAdapter extends RecyclerView.Adapter<ParticipantWorkshopAnnouncementsAdapter.ViewHolder>{
	private Context context;
	private ArrayList<Map<String, Object>> announcementModels = new ArrayList<>();
	private final AnnouncementInterface announcementInterface;
	
	public void setAnnouncements(ArrayList<Map<String, Object>> announcementModelss) {
		Log.d("ya aboya", announcementModelss.toString());
		
		this.announcementModels = announcementModelss;
		notifyDataSetChanged();
	}
	
	@Override
	public int getItemCount() {
		return announcementModels.size();
	}
	
	public ParticipantWorkshopAnnouncementsAdapter(Context context, AnnouncementInterface announcementInterface) {
		this.context = context;
		this.announcementInterface = announcementInterface;
	}
	
	
	@Override
	public ParticipantWorkshopAnnouncementsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.announcement_item_layout, parent, false);
		return new ParticipantWorkshopAnnouncementsAdapter.ViewHolder(view);
	}
	
	// TODO:Fill
	public class ViewHolder extends RecyclerView.ViewHolder {
		TextView authorName;
		TextView date;
		TextView content;
		
		public ViewHolder(@NonNull View itemView) {
			super(itemView);
			authorName = (TextView) itemView.findViewById(R.id.author_name);
			date = (TextView) itemView.findViewById(R.id.date);
			content = (TextView) itemView.findViewById(R.id.body);
			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (announcementInterface != null) {
						int position = getAdapterPosition();
						if (position != RecyclerView.NO_POSITION) {
							announcementInterface.onAnnouncementClick(position);
						}
					}
				}
			});
		}
	}
	
	@Override
	public void onBindViewHolder(@NonNull ParticipantWorkshopAnnouncementsAdapter.ViewHolder holder, int position) {
		
		holder.authorName.setText((String) announcementModels.get(position).get("authorName"));
		holder.date.setText((String) announcementModels.get(position).get("announcementDate"));
		// Set the maximum number of lines based on your layout requirements
		holder.content.setMaxLines(6); // Adjust this value as needed
		
		// Set ellipsize to indicate that the text is truncated
		holder.content.setEllipsize(TextUtils.TruncateAt.END);
		
		// Set the truncated content to the content TextView
		holder.content.setText((String) announcementModels.get(position).get("announcementMessage"));
		
	}
}
