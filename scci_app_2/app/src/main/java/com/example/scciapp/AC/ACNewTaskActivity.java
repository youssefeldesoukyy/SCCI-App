package com.example.scciapp.AC;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scciapp.HttpClient;
import com.example.scciapp.R;
import com.example.scciapp.Session;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ACNewTaskActivity extends AppCompatActivity {
	private TextView dateTxt;
	private String datePicked;
	private String dateView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_acnew_task);
		MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("New Task");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		EditText taskTxt = findViewById(R.id.newTxt);
		Button submitBtn = findViewById(R.id.SubmitBtn);
		dateTxt = (TextView) findViewById(R.id.datetxt);
		String Date = String.valueOf(dateTxt.getText());
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
						dateTxt.setText(dateView);
					}
				});
			}
		});
		
		submitBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(datePicked == null || taskTxt.getText().toString().trim().isEmpty()){
					Toast.makeText(ACNewTaskActivity.this, "Please fill all the required fields", Toast.LENGTH_SHORT).show();
					return;
				}
				Genson genson = new GensonBuilder().create();
				Map<String,Object> requestMap = new HashMap<String, Object>();
				requestMap.put("taskMessage",taskTxt.getText().toString().trim());
				requestMap.put("taskDeadline",datePicked);
				String  json = genson.serialize(requestMap);
				RequestBody requestBody = RequestBody.create(json, MediaType.get("application/json"));
				Request request = new Request.Builder().addHeader("Cookie", Session.cookieString).post(requestBody).url(HttpClient.baseUrl + "/tasks/"+ Session.currentUser.getUserWorkshop()).build();
				HttpClient.getClient().newCall(request).enqueue(new Callback() {
					@Override
					public void onFailure(@NonNull Call call, @NonNull IOException e) {
						e.printStackTrace();
					}
					
					@Override
					public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
						ResponseBody body = response.body();
						if(response.code() == 200){
							ACNewTaskActivity.this.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(ACNewTaskActivity.this, "Task added!", Toast.LENGTH_SHORT).show();
								}
							});
							finish();
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