package com.example.scciapp.Participant;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.scciapp.AC.ACWorkshopAddAnnouncement;
import com.example.scciapp.AC.AnnouncementDetails;
import com.example.scciapp.AC.WorkshopAnnouncementsAdapter;
import com.example.scciapp.HttpClient;
import com.example.scciapp.R;
import com.example.scciapp.Session;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ParticipantWorkshopAnnouncementsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ParticipantWorkshopAnnouncementsFragment extends Fragment {
	
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	
	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	
	public ParticipantWorkshopAnnouncementsFragment() {
		// Required empty public constructor
	}
	
	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment ParticipantWorkshopAnnouncementsFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static ParticipantWorkshopAnnouncementsFragment newInstance(String param1, String param2) {
		ParticipantWorkshopAnnouncementsFragment fragment = new ParticipantWorkshopAnnouncementsFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_participant_workshop_announcements, container, false);
	}
	ParticipantWorkshopAnnouncementsAdapter adapter;
	RecyclerView recyclerView;
	ArrayList<Map<String, Object>> announcementModels = new ArrayList<>();
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		getAnnouncements();
		
		adapter = new ParticipantWorkshopAnnouncementsAdapter(getContext(), this::onAnnouncementClick);
		
		recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
		
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false));
	}
	
	public void onAnnouncementClick(int position) {
		Intent intent = new Intent(getActivity(), ParticipantAnnouncementDetails.class);
		Map<String, Object> i = announcementModels.get(position);
		intent.putExtra("announcementID", String.valueOf( i.get("announcementID")));
		intent.putExtra("announcementDate", (String) i.get("announcementDate"));
		intent.putExtra("announcementMessage", (String) i.get("announcementMessage"));
		intent.putExtra("authorName", (String) i.get("authorName"));
		startActivity(intent);
	}
	
	public void getAnnouncements() {
		Genson genson = new GensonBuilder().create();
		Request request = new Request.Builder().get().addHeader("Cookie", Session.cookieString).url(HttpClient.baseUrl + "/announcements/"+ Session.currentUser.getUserWorkshop()).build();
		HttpClient.getClient().newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(@NonNull Call call, @NonNull IOException e) {
				Log.d("error", e.getMessage());
			}
			
			@Override
			public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
				Log.d("Success", response.code() + "");
				if (response.code() == 200) {
					try {
						String responseBody = response.body().string();
						ArrayList<Map<String, Object>> announcements = genson.deserialize(responseBody, ArrayList.class);
						
						
						announcementModels.addAll(announcements);
						Log.d("wewewe", announcementModels.toString());
						getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								recyclerView.setAdapter(adapter);
								recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
								adapter.setAnnouncements(announcementModels);
							}
						});
						
					} catch (Exception e) {
						Log.d("Req error", e.getMessage());
					}
				}
			}
		});
		
	}
}