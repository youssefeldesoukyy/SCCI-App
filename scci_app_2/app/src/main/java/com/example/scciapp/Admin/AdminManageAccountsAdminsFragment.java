package com.example.scciapp.Admin;

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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.scciapp.HttpClient;
import com.example.scciapp.R;
import com.example.scciapp.Models.User;
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
 * Use the {@link AdminManageAccountsAdminsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminManageAccountsAdminsFragment extends Fragment {
	
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	
	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	
	public AdminManageAccountsAdminsFragment() {
		// Required empty public constructor
	}
	
	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment AdminManageAccountsAdminsFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static AdminManageAccountsAdminsFragment newInstance(String param1, String param2) {
		AdminManageAccountsAdminsFragment fragment = new AdminManageAccountsAdminsFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}
	ArrayList<User> users;
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
		return inflater.inflate(R.layout.fragment_admin_manage_accounts_admins, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		Genson genson = new GensonBuilder().create();
		String url = HttpClient.baseUrl + "/admin/accounts/Admin";
		Request request = new Request.Builder().get().addHeader("Cookie", Session.cookieString).url(url).build();
		ProgressBar progressCircular = (ProgressBar) view.findViewById(R.id.loading);
		TextView errTxt = (TextView) view.findViewById(R.id.errtxt);
		
		
		progressCircular.setVisibility(View.VISIBLE);
		
		
		HttpClient.getClient().newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(@NonNull Call call, @NonNull IOException e) {
				
				Log.d("ERROR", e.toString());
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						errTxt.setVisibility(View.VISIBLE);
						progressCircular.setVisibility(View.GONE);
					}
				});
			}
			
			@Override
			public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
				ResponseBody responseBody = response.body();
				
				if (response.isSuccessful() && responseBody != null && response.code() == 200) {
					Map<String, Object> decodedResponse = genson.deserialize(responseBody.string(), Map.class);
					ArrayList<Map<String, Object>> dataList = (ArrayList) decodedResponse.get("data");
					users = User.getUsersList(dataList);
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							progressCircular.setVisibility(View.GONE);
							RecyclerView accountsRecView = (RecyclerView) view.findViewById(R.id.accountsrecview);
							AdminManageAccountsRecViewAdapter adapter = new AdminManageAccountsRecViewAdapter(view.getContext());
							adapter.setUsers(users);
							accountsRecView.setAdapter(adapter);
							accountsRecView.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false));
							
						}
					});
					Log.d("User1", users.get(0).toString());
					
				}
				
			}
		});
	}
}