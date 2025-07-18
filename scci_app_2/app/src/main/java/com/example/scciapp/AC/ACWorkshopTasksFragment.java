package com.example.scciapp.AC;

import android.content.Context;
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
 * Use the {@link ACWorkshopTasksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ACWorkshopTasksFragment extends Fragment {
	
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	
	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	
	public ACWorkshopTasksFragment() {
		// Required empty public constructor
	}
	
	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment ACWorkshopTasksFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static ACWorkshopTasksFragment newInstance(String param1, String param2) {
		ACWorkshopTasksFragment fragment = new ACWorkshopTasksFragment();
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
		return inflater.inflate(R.layout.fragment_a_c_workshop_tasks, container, false);
	}
	private RecyclerView postsRecyclerView;
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		postsRecyclerView = view.findViewById(R.id.postsRecyclerView);
		
		
		FloatingActionButton addBtn = view.findViewById(R.id.AddBtn);
		addBtn.setOnClickListener(view1 -> {
			Intent intent = new Intent(getContext(), ACNewTaskActivity.class);
			startActivity(intent);
		});
		
		
		Genson genson = new GensonBuilder().create();
		Request request = new Request.Builder().get().addHeader("Cookie", Session.cookieString).url(HttpClient.baseUrl + "/tasks/" + Session.currentUser.getUserWorkshop()).build();
		HttpClient.getClient().newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(@NonNull Call call, @NonNull IOException e) {
			
			}
			
			@Override
			public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
				Log.d("Status Code", response.code()+"");
				if (response.code() == 200 && response.body() != null) {
					ArrayList<Map<String, Object>> decodedResponse = genson.deserialize(response.body().string(), ArrayList.class);
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
							postsRecyclerView.setLayoutManager(layoutManager);
							
							Log.d("ds", decodedResponse.toString());
							TaskPostsAdapter adapter = new TaskPostsAdapter(getContext(), decodedResponse);
							postsRecyclerView.setAdapter(adapter);
						}
					});
				}
			}
		});
	}
	
}