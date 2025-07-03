package com.example.scciapp.Member;

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

import com.example.scciapp.HR.MemberAnnouncementAdapter;
import com.example.scciapp.HttpClient;
import com.example.scciapp.R;
import com.example.scciapp.Session;
import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MemberAnnouncementsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MemberAnnouncementsFragment extends Fragment {
	
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	
	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	
	public MemberAnnouncementsFragment() {
		// Required empty public constructor
	}
	
	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment MemberAnnouncementsFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static MemberAnnouncementsFragment newInstance(String param1, String param2) {
		MemberAnnouncementsFragment fragment = new MemberAnnouncementsFragment();
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
	private RecyclerView recyclerView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_member_announcements, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		recyclerView = view.findViewById(R.id.announcementsRecView);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		
		Genson genson = new GensonBuilder().create();
		Request request = new Request.Builder().get().addHeader("Cookie", Session.cookieString).url(HttpClient.baseUrl + "/announcements/General").build();
		HttpClient.getClient().newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(@NonNull Call call, @NonNull IOException e) {
				e.printStackTrace();
			}
			
			@Override
			public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
				Log.d("Status Code: ", response.code() + "");
				
				ResponseBody body = response.body();
				String bodyString = body.string();
				Log.d("Fds", bodyString);
				
				
				if (response.code() == 200){
					ArrayList<Map<String, Object>> decodedResponse =genson.deserialize(bodyString, ArrayList.class);
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							recyclerView.setAdapter(new MemberAnnouncementAdapter(getContext(), decodedResponse));
						}
					});
					
				}
				
			}
		});
	}
}