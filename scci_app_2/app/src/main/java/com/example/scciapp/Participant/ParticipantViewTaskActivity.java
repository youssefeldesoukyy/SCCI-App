package com.example.scciapp.Participant;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.scciapp.AC.ACViewTaskActivity;
import com.example.scciapp.AC.SubmissionAdapter;
import com.example.scciapp.HttpClient;
import com.example.scciapp.R;
import com.example.scciapp.Session;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ParticipantViewTaskActivity extends AppCompatActivity {
	String taskID, authorName, taskMessage, taskDate, taskDeadline;
	
	TextView body2;
	private TextView dateTxt;
	TextView deadlineTextView;
	public RecyclerView submissionsRecyclerView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_participant_view_task);
		MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("Task");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		submissionsRecyclerView = findViewById(R.id.submissionsRecyclerView);
		
		body2 = (TextView) findViewById(R.id.body2);
		
		taskID = getIntent().getStringExtra("taskID");
		authorName = getIntent().getStringExtra("authorName");
		taskMessage = getIntent().getStringExtra("taskMessage");
		taskDate = getIntent().getStringExtra("taskDate");
		taskDeadline = getIntent().getStringExtra("taskDeadline");
		
		TextView dateTextView = findViewById(R.id.date);
//		EditText bodyTextView = findViewById(R.id.body);
		TextView authorNameTextView = findViewById(R.id.aname);
		deadlineTextView = (TextView) findViewById(R.id.deadline);
		
		dateTextView.setText(taskDate);
		body2.setText(taskMessage);
		authorNameTextView.setText(authorName);
		deadlineTextView.setText("Deadline: " + taskDeadline);
		submitTaskBtn = (Button) findViewById(R.id.submitTaskBtn);
	
		submitTaskBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				View view1 = LayoutInflater.from(ParticipantViewTaskActivity.this).inflate(R.layout.submit_task_layout, null);
				TextInputEditText submissionLink = (TextInputEditText) view1.findViewById(R.id.submissionLink);
				AlertDialog alertDialog = new MaterialAlertDialogBuilder(ParticipantViewTaskActivity.this)
					.setTitle("Enter your Google Drive Link")
					.setView(view1)
					.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					}).setPositiveButton("Submit", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Genson genson = new GensonBuilder().create();
							Map<String,Object> requestMap = new HashMap<String, Object>();
							requestMap.put("submissionMessage", submissionLink.getText().toString().trim());
							String  json = genson.serialize(requestMap);
							Log.d("json" , json);
							RequestBody requestBody = RequestBody.create(json, MediaType.get("application/json"));
							Request request = new Request.Builder().addHeader("Cookie", Session.cookieString).post(requestBody).url(HttpClient.baseUrl + "/tasks/"+taskID+"/submissions").build();
							HttpClient.getClient().newCall(request).enqueue(new Callback() {
								@Override
								public void onFailure(@NonNull Call call, @NonNull IOException e) {
									e.printStackTrace();
								}
								
								@Override
								public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
									Log.d("Response Code " , response.code()+"");
									ResponseBody body = response.body();
									Log.d("erw" , body.string());
									dialog.dismiss();
									ParticipantViewTaskActivity.this.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											submitTaskBtn.setVisibility(View.GONE);
										}
									});
								}
							});
						}
					})
					.create();
				alertDialog.show();
			}
		});
		
		
		Genson genson = new GensonBuilder().create();
		Request request = new Request.Builder().get().addHeader("Cookie", Session.cookieString).url(HttpClient.baseUrl + "/tasks/" + taskID + "/submissions/user").build();
		HttpClient.getClient().newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(@NonNull Call call, @NonNull IOException e) {
				e.printStackTrace();
			}
			
			@Override
			public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
				String rb = response.body().string();
				Log.d("submission user" , rb);
				Log.d("status code", response.code() + "");
				if (response.code() == 200 && response.body() != null) {
					Map<String, Object> decodedResponse = genson.deserialize(rb, Map.class);
					ArrayList<Map<String, Object>> arrayList = new ArrayList<>();
					arrayList.add((Map<String, Object>) decodedResponse.get("data"));
					ParticipantViewTaskActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							ParticipantViewTaskActivity.this.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									LinearLayoutManager layoutManager = new LinearLayoutManager(ParticipantViewTaskActivity.this);
									submissionsRecyclerView.setLayoutManager(layoutManager);
									
									SubmissionAdapter adapter = new SubmissionAdapter(ParticipantViewTaskActivity.this, arrayList);
									submissionsRecyclerView.setAdapter(adapter);
								}
							});
							
							Log.d("Submission", decodedResponse.toString());
							Log.d("Status Code", response.code() + "");
						}
					});
				} else {
					ParticipantViewTaskActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							submissionsRecyclerView.setVisibility(View.GONE);
							submitTaskBtn.setVisibility(View.VISIBLE);
						}
					});
					
				}
			}
		});
		
	}
	Button submitTaskBtn;
	
	
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