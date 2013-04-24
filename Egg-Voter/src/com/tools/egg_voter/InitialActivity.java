package com.tools.egg_voter;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.app.Activity;
import android.content.Intent;


public class InitialActivity extends Activity implements OnTouchListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_initial);
		View v= (View) findViewById(R.id.initial_view);
		v.setOnTouchListener(this);
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		final int action = arg1.getAction();
		if(action == MotionEvent.ACTION_DOWN) {
		Intent Login = new Intent(this, LoginActivity.class);
		startActivity(Login);
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		finish();
		}
		return true;
	}


}
