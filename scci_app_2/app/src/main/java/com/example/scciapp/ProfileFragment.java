package com.example.scciapp;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scciapp.Participant.ParticipantViewTaskActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
	
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	
	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	
	public ProfileFragment() {
		// Required empty public constructor
	}
	
	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment ProfileFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static ProfileFragment newInstance(String param1, String param2) {
		ProfileFragment fragment = new ProfileFragment();
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
		return inflater.inflate(R.layout.fragment_profile, container, false);
	}
	
	Button fullNameBtn, emailBtn, changePassword;
	TextView fullNameTextView, emailTextView;
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		fullNameBtn = (Button) view.findViewById(R.id.editFullNameBtn);
		emailBtn = (Button) view.findViewById(R.id.editMailBtn);
		changePassword = (Button) view.findViewById(R.id.changePasswordBtn);
		fullNameTextView = (TextView) view.findViewById(R.id.fullNameTextView);
		emailTextView = (TextView) view.findViewById(R.id.emailTextView);
		
		fullNameTextView.setText(Session.currentUser.getUserFullName());
		emailTextView.setText(Session.currentUser.getUserEmail());
		changePassword.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				View view1 = LayoutInflater.from(getContext()).inflate(R.layout.change_password_layout, null);
				TextInputEditText currentPasswordEditText = (TextInputEditText) view1.findViewById(R.id.currentPasswordEditText);
				TextInputEditText newPasswordEditText = (TextInputEditText) view1.findViewById(R.id.newPasswordEditText);
				AlertDialog alertDialog = new MaterialAlertDialogBuilder(getContext())
					.setTitle("Change your Password")
					.setView(view1)
					.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					}).setPositiveButton("Submit", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (currentPasswordEditText.getText().toString().trim().isEmpty() || newPasswordEditText.getText().toString().trim().isEmpty()){
								Toast.makeText(getContext(), "Fill all the required Fields", Toast.LENGTH_SHORT).show();
								return;
							}
							Genson genson = new GensonBuilder().create();
							String url = HttpClient.baseUrl + "/auth/edit/password";
							Map<String, String> requestMap = new HashMap<String, String>() {{
								put("currentPassword", currentPasswordEditText.getText().toString().trim());
								put("newPassword", newPasswordEditText.getText().toString().trim());
							}};
							String json = genson.serialize(requestMap);
							RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
							Request request = new Request.Builder().addHeader("Cookie", Session.cookieString).patch(body).url(url).build();
							HttpClient.getClient().newCall(request).enqueue(new Callback() {
								@Override
								public void onFailure(@NonNull Call call, @NonNull IOException e) {
									e.printStackTrace();
								}
								
								@Override
								public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
									if (response.code() == 200){
										getActivity().runOnUiThread(new Runnable() {
											@Override
											public void run() {
												Toast.makeText(getContext(), "Password Updated!", Toast.LENGTH_SHORT).show();
											}
										});
									} else {
										getActivity().runOnUiThread(new Runnable() {
											@Override
											public void run() {
												Toast.makeText(getContext(), "Current Password incorrect!", Toast.LENGTH_SHORT).show();
											}
										});
									}
									dialog.dismiss();
								}
							});
						}
					}).create();
				alertDialog.show();
			}
		});
		
		fullNameBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				View view1 = LayoutInflater.from(getContext()).inflate(R.layout.submit_task_layout, null);
				TextInputEditText fullNameEditText = (TextInputEditText) view1.findViewById(R.id.submissionLink);
				AlertDialog alertDialog = new MaterialAlertDialogBuilder(getContext())
					.setTitle("Edit your Full Name")
					.setView(view1)
					.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					}).setPositiveButton("Submit", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (fullNameEditText.getText().toString().trim().isEmpty()){
								Toast.makeText(getContext(), "Fill the Field", Toast.LENGTH_SHORT).show();
								return;
							}
							Genson genson = new GensonBuilder().create();
							String url = HttpClient.baseUrl + "/auth/edit/fullName";
							Map<String, String> requestMap = new HashMap<String, String>() {{
								put("userFullName", fullNameEditText.getText().toString().trim());
							}};
							String json = genson.serialize(requestMap);
							RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
							Request request = new Request.Builder().addHeader("Cookie", Session.cookieString).patch(body).url(url).build();
							HttpClient.getClient().newCall(request).enqueue(new Callback() {
								@Override
								public void onFailure(@NonNull Call call, @NonNull IOException e) {
									e.printStackTrace();
								}
								
								@Override
								public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
									if (response.code() == 200){
										getActivity().runOnUiThread(new Runnable() {
											@Override
											public void run() {
												Toast.makeText(getContext(), "Full Name Updated!", Toast.LENGTH_SHORT).show();
												Session.currentUser.setUserFullName(fullNameEditText.getText().toString().trim());
												fullNameTextView.setText(Session.currentUser.getUserFullName());
												emailTextView.setText(Session.currentUser.getUserEmail());
											}
										});
									} else {
										getActivity().runOnUiThread(new Runnable() {
											@Override
											public void run() {
												Toast.makeText(getContext(), "Something happened!", Toast.LENGTH_SHORT).show();
											}
										});
									}
									dialog.dismiss();
								}
							});
						}
					}).create();
				alertDialog.show();
			}
		});
		
		emailBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				View view1 = LayoutInflater.from(getContext()).inflate(R.layout.submit_task_layout, null);
				TextInputEditText emailEditText = (TextInputEditText) view1.findViewById(R.id.submissionLink);
				AlertDialog alertDialog = new MaterialAlertDialogBuilder(getContext())
					.setTitle("Edit your Email")
					.setView(view1)
					.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					}).setPositiveButton("Submit", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (emailEditText.getText().toString().trim().isEmpty()){
								Toast.makeText(getContext(), "Fill the Field", Toast.LENGTH_SHORT).show();
								return;
							}
							Genson genson = new GensonBuilder().create();
							String url = HttpClient.baseUrl + "/auth/edit/email";
							Map<String, String> requestMap = new HashMap<String, String>() {{
								put("userEmail", emailEditText.getText().toString().trim());
							}};
							String json = genson.serialize(requestMap);
							RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
							Request request = new Request.Builder().addHeader("Cookie", Session.cookieString).patch(body).url(url).build();
							HttpClient.getClient().newCall(request).enqueue(new Callback() {
								@Override
								public void onFailure(@NonNull Call call, @NonNull IOException e) {
									e.printStackTrace();
								}
								
								@Override
								public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
									if (response.code() == 200){
										getActivity().runOnUiThread(new Runnable() {
											@Override
											public void run() {
												Toast.makeText(getContext(), "Full Name Updated!", Toast.LENGTH_SHORT).show();
												Session.currentUser.setUserEmail(emailEditText.getText().toString().trim());
												fullNameTextView.setText(Session.currentUser.getUserFullName());
												emailTextView.setText(Session.currentUser.getUserEmail());
											}
										});
									} else {
										getActivity().runOnUiThread(new Runnable() {
											@Override
											public void run() {
												Toast.makeText(getContext(), "Email already in use!", Toast.LENGTH_SHORT).show();
											}
										});
									}
									dialog.dismiss();
								}
							});
						}
					}).create();
				alertDialog.show();
			}
		});

//		submitBtn.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Genson genson = new GensonBuilder().create();
//				String url = HttpClient.baseUrl + "/auth/edit";
//				Map<String, String> requestMap = new HashMap<String, String>(){{
//					put("userFullName", fullname.getText().toString().trim());
//					put("userEmail", email.getText().toString().trim());
//				}};
//				String json = genson.serialize(requestMap);
//				RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
//				Request request = new Request.Builder().addHeader("Cookie", Session.cookieString).put(body).url(url).build();
//				HttpClient.getClient().newCall(request).enqueue(new Callback() {
//					@Override
//					public void onFailure(@NonNull Call call, @NonNull IOException e) {
//					e.printStackTrace();
//					}
//
//					@Override
//					public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//						if(response.code() == 200){
//							getActivity().runOnUiThread(new Runnable() {
//								@Override
//								public void run() {
//									Toast.makeText(getContext(), "Profile Updated!", Toast.LENGTH_SHORT).show();
//									Session.currentUser.setUserEmail(email.getText().toString().trim());
//									Session.currentUser.setUserFullName(fullname.getText().toString().trim());
//								}
//							});
//						} else {
//							getActivity().runOnUiThread(new Runnable() {
//								@Override
//								public void run() {
//									Toast.makeText(getContext(), "This mail is used!", Toast.LENGTH_SHORT).show();
//								}
//							});
//						}
//					}
//				});
//
//			}
//		});
	}
}