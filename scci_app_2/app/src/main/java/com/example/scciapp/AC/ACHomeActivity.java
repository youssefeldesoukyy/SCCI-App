package com.example.scciapp.AC;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.scciapp.AuthActivity;
import com.example.scciapp.Head.HeadAnnouncementsFragment;
import com.example.scciapp.Head.HeadHomeActivity;
import com.example.scciapp.HttpClient;
import com.example.scciapp.Member.MemberAnnouncementsFragment;
import com.example.scciapp.ProfileFragment;
import com.example.scciapp.R;
import com.example.scciapp.Session;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ACHomeActivity extends AppCompatActivity {
	private Boolean isPaused = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_achome);
		MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("AC " + Session.currentUser.getUserWorkshop());
		BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomnavigationview);
		bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem item) {
				if (item.getItemId() == R.id.profile) {
					getSupportFragmentManager()
						.beginTransaction()
						.setReorderingAllowed(true)
						.replace(R.id.fragmentContainerView, ProfileFragment.class, null)
						.commit();
				} else if (item.getItemId() == R.id.announcements) {
					getSupportFragmentManager()
						.beginTransaction()
						.setReorderingAllowed(true)
						.replace(R.id.fragmentContainerView, MemberAnnouncementsFragment.class, null)
						.commit();
				} else if(item.getItemId() == R.id.workshop){
					getSupportFragmentManager()
						.beginTransaction()
						.setReorderingAllowed(true)
						.replace(R.id.fragmentContainerView, ACWorkshopFragment.class, null)
						.commit();
				}
				
				return true;
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater ().inflate ( R.menu.hr_toolbar_menu,menu );
		return super.onCreateOptionsMenu ( menu );
		
	}
	
	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		int itemID = item.getItemId();
		if (itemID == R.id.logoutBtn){
			String url = HttpClient.baseUrl + "/auth/logout";
			RequestBody body = RequestBody.create("", MediaType.parse("application/json; charset=utf-8"));
			Request request = new Request.Builder().addHeader("Cookie", Session.cookieString).post(body).url(url).build();
			HttpClient.getClient().newCall(request).enqueue(new Callback() {
				@Override
				public void onFailure(@NonNull Call call, @NonNull IOException e) {
				
				}
				
				@Override
				public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
					ResponseBody responseBody = response.body();
					if (response.isSuccessful() && response.code() == 200 && responseBody != null) {
						SharedPreferences sharedPreferences = getSharedPreferences("sharedCookies", MODE_PRIVATE);
						SharedPreferences.Editor editor = sharedPreferences.edit();
						Log.d("Logout", "Logged Out");
						editor.putString("cookie", "");
						editor.apply();
						Session.cookieString = "";
						Intent intent = new Intent(ACHomeActivity.this, AuthActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
						startActivity(intent);
					}
				}
			});
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		isPaused = true;
	}
	@Override
	protected void onResume() {
		super.onResume();
		if (isPaused){
			ACHomeActivity.this.recreate();
			isPaused = false;
		}
	}
}