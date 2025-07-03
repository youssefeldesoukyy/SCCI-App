package com.example.scciapp.HR;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.scciapp.R;
import com.google.android.material.appbar.MaterialToolbar;

public class AnnouncementViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.announcement_view_layout);
        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Announcement");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String date = getIntent().getStringExtra("announcementDate");
        String AName = getIntent().getStringExtra("authorName");
        String body = getIntent().getStringExtra("announcementMessage");
        String id = getIntent().getStringExtra("announcementID");

        TextView dateTextView = findViewById(R.id.date);
        TextView bodyTextView = findViewById(R.id.body);
        TextView authorNameTextView = findViewById(R.id.aname);

        dateTextView.setText(date);
        bodyTextView.setText(body);
        authorNameTextView.setText(AName);
        
        TextView textView = findViewById(R.id.body);
    }
    
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater ().inflate ( R.menu.post_toolbar_menu, menu );
//        return super.onCreateOptionsMenu ( menu );
//
//    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        
        int id = item.getItemId();
        if(id == android.R.id.home){
            this.finish();
            return true;
        } else if(id == R.id.editBtn){
            Toast.makeText(this, "edit", Toast.LENGTH_SHORT).show();
        } else if(id == R.id.deleteBtn){
            Toast.makeText(this, "delete", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
