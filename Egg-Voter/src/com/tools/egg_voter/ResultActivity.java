package com.tools.egg_voter;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import com.tools.egg_voter.util.SystemUiHider;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;


public class ResultActivity extends Activity {
	public ArrayAdapter<String> dataAdapter;
	public ArrayList<String> resultList;
	private static final boolean AUTO_HIDE = true;
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
	private static final boolean TOGGLE_ON_CLICK = true;
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;
	private SystemUiHider mSystemUiHider;
	//socket
			Socket socket;
			PrintWriter out;
			Scanner in;
	public Handler initialHandler = new Handler() {
		        @Override public void handleMessage(Message msg) {
					//create an ArrayAdaptar from the String Array
					  dataAdapter = new ArrayAdapter<String>(ResultActivity.this,android.R.layout.simple_list_item_1, resultList);
					  ListView listView = (ListView) findViewById(R.id.output_list);
					  // Assign adapter to ListView
					  listView.setAdapter(dataAdapter);
			    }
			};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_result);

		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		final View contentView = findViewById(R.id.full_screen);
		displayListView();
		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider
				.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
					// Cached values.
					int mControlsHeight;
					int mShortAnimTime;

					@Override
					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
					public void onVisibilityChange(boolean visible) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
							// If the ViewPropertyAnimator API is available
							// (Honeycomb MR2 and later), use it to animate the
							// in-layout UI controls at the bottom of the
							// screen.
							if (mControlsHeight == 0) {
								mControlsHeight = controlsView.getHeight();
							}
							if (mShortAnimTime == 0) {
								mShortAnimTime = getResources().getInteger(
										android.R.integer.config_shortAnimTime);
							}
							controlsView
									.animate()
									.translationY(visible ? 0 : mControlsHeight)
									.setDuration(mShortAnimTime);
						} else {
							// If the ViewPropertyAnimator APIs aren't
							// available, simply show or hide the in-layout UI
							// controls.
							controlsView.setVisibility(visible ? View.VISIBLE
									: View.GONE);
						}

						if (visible && AUTO_HIDE) {
							// Schedule a hide().
							delayedHide(AUTO_HIDE_DELAY_MILLIS);
						}
					}
				});

		// Set up the user interaction to manually show or hide the system UI.
		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (TOGGLE_ON_CLICK) {
					mSystemUiHider.toggle();
				} else {
					mSystemUiHider.show();
				}
			}
		});


		findViewById(R.id.dummy_button).setOnTouchListener(
				mDelayHideTouchListener);
		findViewById(R.id.dummy_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	finish();
        		overridePendingTransition(R.anim.left_in,R.anim.right_out);
             }
            });
		
	    }

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);
	}
	private void displayListView() {
		 
		  final String request = "GET-ALL-RES|root|ttgwzmt5fz|" + getIntent().getStringExtra("roomname") + "|" + RoomActivity.username;
		  Thread contactserver = new Thread(){ 
	        	@Override 
	        	public void run(){
	        		//Add Operation here
	        		//Send back the result String to server here.
	        		try {
						socket = new Socket("moore10.cs.purdue.edu", 4040);
						if (socket.isConnected()) {
							System.out.println("host connected");
						}
						out = new PrintWriter(socket.getOutputStream(),true);
						out.println(request);
						in = new Scanner(socket.getInputStream());
						final String resList = (String) in.nextLine();
						resultList = new ArrayList<String>();
						formList(resList);						  
						  
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        		initialHandler.sendEmptyMessage(0);
	        	}
	     };
	     contactserver.start();
      }
	public void formList(String in){
		String[] input = in.split("\\|");
		for(int i = 2;i < input.length-1;i+=2)
		  resultList.add(input[i]+": "+input[i+1]+" Times");
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
		}
	};

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}
}
