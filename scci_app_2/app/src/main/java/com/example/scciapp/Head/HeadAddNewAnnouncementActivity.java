package com.example.scciapp.Head;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.scciapp.AuthActivity;
import com.example.scciapp.HttpClient;
import com.example.scciapp.R;
import com.example.scciapp.Session;
import com.google.android.material.appbar.MaterialToolbar;
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
import okhttp3.ResponseBody;

public class HeadAddNewAnnouncementActivity extends AppCompatActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_head_add_new_announcement);
		MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("Add new Announcement");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		TextInputEditText editTextBody = findViewById(R.id.edittextpost);
		
		Button submitButton = findViewById(R.id.submit);
		
		submitButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String bodyText = editTextBody.getText().toString().trim();
				
				if (!bodyText.isEmpty()) {
					
					Genson genson = new GensonBuilder().create();
					Map<String,Object> requestMap = new HashMap<String, Object>();
					requestMap.put("announcementMessage", editTextBody.getText().toString());
					String json = genson.serialize(requestMap);
					Log.d("fdsds", json.toString());
					RequestBody requestBody = RequestBody.create(json, MediaType.get("application/json"));
					Request request = new Request.Builder().addHeader("Cookie", Session.cookieString).post(requestBody).url(HttpClient.baseUrl + "/announcements/General").build();
					HttpClient.getClient().newCall(request).enqueue(new Callback() {
						@Override
						public void onFailure(@NonNull Call call, @NonNull IOException e) {
							e.printStackTrace();
						}
						
						@Override
						public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
							Log.d("Status Code: ", response.code() + "");
							
							ResponseBody body = response.body();
							Log.d("Fds", body.string());
							finish();
						}
						
					});
					finish();
				}
			}
		});
	}
	
	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		int itemID = item.getItemId();
		if(itemID == android.R.id.home){
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}