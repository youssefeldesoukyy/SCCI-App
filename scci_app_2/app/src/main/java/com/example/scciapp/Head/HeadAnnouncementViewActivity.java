package com.example.scciapp.Head;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scciapp.HttpClient;
import com.example.scciapp.R;
import com.example.scciapp.Session;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HeadAnnouncementViewActivity extends AppCompatActivity {
	String id;
	TextInputLayout inputLayout;
	TextView body2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_head_announcement_view);
		inputLayout = (TextInputLayout) findViewById(R.id.inputLayout);
		content = findViewById(R.id.body);
		content.setFocusable(false);
		content.setFocusableInTouchMode(false);
		inputLayout.setHintEnabled(false);
		body2 = (TextView) findViewById(R.id.body2);
		editBtn = findViewById(R.id.submitEdit);
		MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("Announcement");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		String date = getIntent().getStringExtra("announcementDate");
		String AName = getIntent().getStringExtra("authorName");
		String body = getIntent().getStringExtra("announcementMessage");
		id = getIntent().getStringExtra("announcementID");
		
		TextView dateTextView = findViewById(R.id.date);
//		EditText bodyTextView = findViewById(R.id.body);
		TextView authorNameTextView = findViewById(R.id.aname);
		
		dateTextView.setText(date);
		content.setText(body);
		body2.setText(body);
		authorNameTextView.setText(AName);
		editBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				updateAnnouncement();
			}
		});
		
		TextView textView = findViewById(R.id.body);
	}
	
	    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ().inflate ( R.menu.post_toolbar_menu, menu );
        return super.onCreateOptionsMenu ( menu );

    }
	private TextInputEditText content;
	private FloatingActionButton editBtn;
	public void editAnnouncement() {
		content.setFocusable(true);
		content.setFocusableInTouchMode(true);
		body2.setVisibility(View.GONE);
		inputLayout.setVisibility(View.VISIBLE);
		inputLayout.setHintEnabled(true);
		editBtn.setVisibility(View.VISIBLE);
	}
	
	public void deleteAnnouncement() {
		Genson genson = new GensonBuilder().create();
		Request request = new Request.Builder().addHeader("Cookie", Session.cookieString).delete().url(HttpClient.baseUrl + "/announcements/"+id).build();
		HttpClient.getClient().newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
				Log.d("no delete", e.getMessage());
			}
			
			@Override
			public void onResponse(@NonNull okhttp3.Call call, @NonNull Response response) throws IOException {
				Log.d("deleted", response.code()+"\n"+response.body().string() );
				if(response.code() == 200){
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							HeadAnnouncementViewActivity.this.finish();
						}});
				}else{
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							HeadAnnouncementViewActivity.this.finish();
						}});
				}
			}
		});
	}
	
	public void updateAnnouncement() {
		Genson genson = new GensonBuilder().create();
		Map<String, Object> requestMap = new HashMap<String, Object>();
		requestMap.put("announcementMessage", content.getText().toString());
		Log.d("message", content.getText().toString());
		String json = genson.serialize(requestMap);
		RequestBody requestBody = RequestBody.create(json, MediaType.get("application/json"));
		Request request = new Request.Builder().addHeader("Cookie", Session.cookieString).put(requestBody).url(HttpClient.baseUrl + "/announcements/"+id).build();
		HttpClient.getClient().newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
				Log.d("no update", e.getMessage());
			}
			
			@Override
			public void onResponse(@NonNull okhttp3.Call call, @NonNull Response response) throws IOException {
				Log.d("updated", response.code()+"\n"+response.body().string() );
				if(response.code() == 200){
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							content.setFocusable(false);
							content.setFocusableInTouchMode(false);
							editBtn.setVisibility(View.GONE);
							inputLayout.setHintEnabled(false);
							body2.setText(content.getText().toString());
							body2.setVisibility(View.VISIBLE);
							inputLayout.setVisibility(View.GONE);
						}});
				}else{
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							content.setFocusable(false);
							content.setFocusableInTouchMode(false);
							editBtn.setVisibility(View.GONE);
							inputLayout.setHintEnabled(false);
							body2.setVisibility(View.VISIBLE);
							inputLayout.setVisibility(View.GONE);
						}});
				}
			}
		});
	}
	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		
		int id = item.getItemId();
		if(id == android.R.id.home){
			this.finish();
			return true;
		} else if(id == R.id.editBtn){
			editAnnouncement();
		} else if(id == R.id.deleteBtn){
			new MaterialAlertDialogBuilder(HeadAnnouncementViewActivity.this)
				.setTitle("Delete Announcement")
				.setMessage("Are you sure you want to delete this announcement?")
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						deleteAnnouncement();
					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				})
				.show();
		}
		return super.onOptionsItemSelected(item);
	}
}