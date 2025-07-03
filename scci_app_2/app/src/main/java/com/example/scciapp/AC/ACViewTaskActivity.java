package com.example.scciapp.AC;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class ACViewTaskActivity extends AppCompatActivity {
	String taskID, authorName, taskMessage, taskDate, taskDeadline;
	private TextInputEditText content;
	private FloatingActionButton editBtn;
	TextInputLayout inputLayout;
	TextView body2;
	private TextView dateTxt;
	private String datePicked;
	private String dateView;
	TextView deadlineTextView;
	public RecyclerView submissionsRecyclerView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_acview_task);
		datePickerCombine = (LinearLayout) findViewById(R.id.datePickerCombine);
		MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("Task");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		inputLayout = (TextInputLayout) findViewById(R.id.inputLayout);
		submissionsRecyclerView = findViewById(R.id.submissionsRecyclerView);
		content = findViewById(R.id.body);
		content.setFocusable(false);
		content.setFocusableInTouchMode(false);
		inputLayout.setHintEnabled(false);
		body2 = (TextView) findViewById(R.id.body2);
		editBtn = findViewById(R.id.submitEdit);
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
		content.setText(taskMessage);
		body2.setText(taskMessage);
		authorNameTextView.setText(authorName);
		deadlineTextView.setText("Deadline: " + taskDeadline);
		editBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				updateAnnouncement();
			}
		});
		
		TextView textView = findViewById(R.id.body);
		
		Button dateBtn = (Button) findViewById(R.id.datebtn);
		dateBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//                openDialog();
				CalendarConstraints constraintsBuilder =
					new CalendarConstraints.Builder()
						.setValidator(DateValidatorPointForward.now()).build();
				MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker().setCalendarConstraints(constraintsBuilder).setTitleText("Select Deadline").build();
				datePicker.show(getSupportFragmentManager(), "tag");
				
				
				datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
					@Override
					public void onPositiveButtonClick(Object selection) {
						Long selectedDate = (Long) selection;
						Instant selectedInstant = Instant.ofEpochMilli(selectedDate);
						
						Instant secondInstant = selectedInstant.plusSeconds(79140);
						datePicked = secondInstant.toString();
						dateView = (datePicked.split("T")[0]);
						dateBtn.setText(dateView);
					}
				});
			}
		});
		
		
		Genson genson = new GensonBuilder().create();
		Request request = new Request.Builder().get().addHeader("Cookie", Session.cookieString).addHeader("Cookie", Session.cookieString).url(HttpClient.baseUrl + "/tasks/" + taskID + "/submissions").build();
		HttpClient.getClient().newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(@NonNull Call call, @NonNull IOException e) {
				e.printStackTrace();
			}
			
			@Override
			public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
				if (response.code() == 200 && response.body() != null) {
					String rb = response.body().string();
					Log.d("dsd" ,rb);
					ArrayList<Map<String, Object>> decodedResponse = genson.deserialize(rb, ArrayList.class);
					ACViewTaskActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							LinearLayoutManager layoutManager = new LinearLayoutManager(ACViewTaskActivity.this);
							submissionsRecyclerView.setLayoutManager(layoutManager);
							
							SubmissionAdapter adapter = new SubmissionAdapter(ACViewTaskActivity.this, decodedResponse);
							submissionsRecyclerView.setAdapter(adapter);
							Log.d("Submission", decodedResponse.toString());
							Log.d("Status Code", response.code() + "");
						}
					});
				}
			}
		});
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater ().inflate ( R.menu.post_toolbar_menu, menu );
		return super.onCreateOptionsMenu ( menu );
		
	}
	
	public void updateAnnouncement() {
		Genson genson = new GensonBuilder().create();
		Map<String, Object> requestMap = new HashMap<String, Object>();
		requestMap.put("taskMessage", content.getText().toString());
		requestMap.put("taskDeadline", datePicked);
		Log.d("message", content.getText().toString());
		String json = genson.serialize(requestMap);
		RequestBody requestBody = RequestBody.create(json, MediaType.get("application/json"));
		Request request = new Request.Builder().put(requestBody).addHeader("Cookie", Session.cookieString).url(HttpClient.baseUrl + "/tasks/"+taskID).build();
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
							deadlineTextView.setText("Deadline: " + dateView);
							body2.setVisibility(View.VISIBLE);
							inputLayout.setVisibility(View.GONE);
							datePickerCombine.setVisibility(View.GONE);
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
							datePickerCombine.setVisibility(View.GONE);
						}});
				}
			}
		});
	}
	LinearLayout datePickerCombine;
	public void editAnnouncement() {
		content.setFocusable(true);
		content.setFocusableInTouchMode(true);
		body2.setVisibility(View.GONE);
		inputLayout.setVisibility(View.VISIBLE);
		inputLayout.setHintEnabled(true);
		editBtn.setVisibility(View.VISIBLE);
		datePickerCombine.setVisibility(View.VISIBLE);
		
	}
	
	public void deleteAnnouncement() {
		Genson genson = new GensonBuilder().create();
		Request request = new Request.Builder().delete().addHeader("Cookie", Session.cookieString).url(HttpClient.baseUrl + "/tasks/"+taskID).build();
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
							ACViewTaskActivity.this.finish();
						}});
				}else{
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							ACViewTaskActivity.this.finish();
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
			new MaterialAlertDialogBuilder(ACViewTaskActivity.this)
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