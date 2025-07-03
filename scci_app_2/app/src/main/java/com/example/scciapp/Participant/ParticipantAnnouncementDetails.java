package com.example.scciapp.Participant;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.scciapp.HttpClient;
import com.example.scciapp.R;
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

public class ParticipantAnnouncementDetails extends AppCompatActivity {
	String id;
	TextView body2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_participant_announcement_details);
		body2 = (TextView) findViewById(R.id.body2);
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
		body2.setText(body);
		authorNameTextView.setText(AName);
		
		TextView textView = findViewById(R.id.body);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		
		int id = item.getItemId();
		if(id == android.R.id.home){
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}