package com.example.scciapp.AC;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.scciapp.HttpClient;
import com.example.scciapp.R;
import com.example.scciapp.Session;
import com.google.android.material.appbar.MaterialToolbar;
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

public class ACWorkshopAddAnnouncement extends AppCompatActivity {
	Button submit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_acworkshop_add_announcement);
		submit = (Button) findViewById(R.id.submit);
		MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("New Announcement");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText acText = (EditText) findViewById(R.id.edittextpost);
				Genson genson = new GensonBuilder().create();
				Map<String, Object> requestMap = new HashMap<String, Object>();
				requestMap.put("announcementMessage", acText.getText().toString());
				String json = genson.serialize(requestMap);
				RequestBody requestBody = RequestBody.create(json, MediaType.get("application/json"));
				Request request = new Request.Builder().addHeader("Cookie", Session.cookieString).post(requestBody).url(HttpClient.baseUrl + "/announcements/" + Session.currentUser.getUserWorkshop()).build();
				HttpClient.getClient().newCall(request).enqueue(new Callback() {
					@Override
					public void onFailure(@NonNull Call call, @NonNull IOException e) {
						Log.d("error", e.getMessage());
					}
					
					@Override
					public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
						Log.d("Success", response.code()+"" );
						if(response.code() == 200){
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(ACWorkshopAddAnnouncement.this, "Announcement Added!", Toast.LENGTH_SHORT).show();
									ACWorkshopAddAnnouncement.this.finish();
								}});
							
						}else{
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(ACWorkshopAddAnnouncement.this, "Can't Add Announcement!", Toast.LENGTH_SHORT).show();
								}});
						}
					}
				});
			}
		});
	}
	
	
	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				this.finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}