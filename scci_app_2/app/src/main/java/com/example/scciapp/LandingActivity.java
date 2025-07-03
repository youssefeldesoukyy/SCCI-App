package com.example.scciapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.scciapp.AC.ACHomeActivity;
import com.example.scciapp.Admin.AdminHomeActivity;
import com.example.scciapp.HR.HRHomeActivity;
import com.example.scciapp.Head.HeadHomeActivity;
import com.example.scciapp.Member.MemberHomeActivity;
import com.example.scciapp.Models.User;
import com.example.scciapp.Participant.ParticipantHomeActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class LandingActivity extends AppCompatActivity {
//	public void getVersionInfo() throws Exception {
//		Genson genson = new GensonBuilder().create();
//		Map<String, Integer> requestMap = new HashMap<String, Integer>() {{
//			put("versionNumber", 3);
//		}};
//		String json = genson.serialize(requestMap);
//		RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
//		Request request = new Request.Builder()
//			.url(HttpClient.baseUrl + "/version/check")
//			.post(body)
//			.build();
//
//		try (Response response = HttpClient.getClient().newCall(request).execute()) {
//			if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//			if (response.code() == 200){
//				Log.d("Version", "Up to date");
//			} else if(response.code() == 201) {
//				String rb = response.body().string();
//				Map<String, Object> decodedResponse = genson.deserialize(rb, Map.class);
//				AlertDialog alertDialog = new MaterialAlertDialogBuilder(LandingActivity.this)
//					.setTitle("There is an update available")
//					.setMessage((String) decodedResponse.get("versionLink"))
//					.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							dialog.dismiss();
//						}
//					}).create();
//				alertDialog.show();
//
//			}
//		}
//	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_landing_screen);
		
//		try {
////			getVersionInfo();
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
		
		SharedPreferences sharedPreferences = getSharedPreferences("sharedCookies", MODE_PRIVATE);
		Session.cookieString = sharedPreferences.getString("cookie", "");
		
		Genson genson = new GensonBuilder().create();
		String url = HttpClient.baseUrl + "/auth/currentUser";
		Request request = new Request.Builder().get().addHeader("Cookie", Session.cookieString).url(url).build();
		HttpClient.getClient().newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(@NonNull Call call, @NonNull IOException e) {
				e.printStackTrace();
				
			}
			
			@Override
			public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
				ResponseBody responseBody = response.body();
				
				if (response.isSuccessful() && responseBody != null) {
					Map<String, Object> decodedResponse = genson.deserialize(responseBody.string(), Map.class);
					Log.d("decodedResponse", decodedResponse.toString());
					User user = new User((Map<String, Object>) decodedResponse.get("data"));
					Session.currentUser = user;
//					Log.d("user", user.toString());
					Log.d("Status Code", response.code() + "");
					Intent intent;
					if (Session.currentUser.getUserType().equals("Admin")){
						intent = new Intent(LandingActivity.this, AdminHomeActivity.class);
					} else if (Session.currentUser.getUserType().equals("HR")){
						intent = new Intent(LandingActivity.this, HRHomeActivity.class);
					} else if(Session.currentUser.getUserType().equals("Member")){
						intent = new Intent(LandingActivity.this, MemberHomeActivity.class);
					}else if(Session.currentUser.getUserType().equals("Head")){
						intent = new Intent(LandingActivity.this, HeadHomeActivity.class);
					} else if(Session.currentUser.getUserType().equals("AC")){
						intent = new Intent(LandingActivity.this, ACHomeActivity.class);
					}else if(Session.currentUser.getUserType().equals("Participant")){
						intent = new Intent(LandingActivity.this, ParticipantHomeActivity.class);
					}else {
						intent = new Intent(LandingActivity.this, HomeActivity.class);
					}
					
					startActivity(intent);
					LandingActivity.this.finish();
				} else {
					Intent intent = new Intent(LandingActivity.this, AuthActivity.class);
					startActivity(intent);
					LandingActivity.this.finish();
				}
			}
		});
	}
}