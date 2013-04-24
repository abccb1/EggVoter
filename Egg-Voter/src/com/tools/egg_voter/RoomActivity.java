package com.tools.egg_voter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class RoomActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_room);
        findViewById(R.id.host_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent Host = new Intent(RoomActivity.this, HostActivity.class);
				startActivityForResult(Host, 0);
				overridePendingTransition(R.anim.right_left,R.anim.left_right);
            }
        });
		TextView uname=new TextView(this); 
	    uname=(TextView)findViewById(R.id.user_id); 
	    uname.setText("Welcome, "+getIntent().getStringExtra("username")+".");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.room, menu);
		return true;
	}

}
