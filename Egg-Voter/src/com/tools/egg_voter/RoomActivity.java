package com.tools.egg_voter;

import java.util.ArrayList;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class RoomActivity extends Activity {
	public static String username;
	public ArrayList<Room> roomList;
	public MyCustomAdapter dataAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_room);
		displayListView();
        findViewById(R.id.host_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent Host = new Intent(RoomActivity.this, HostActivity.class);
				startActivityForResult(Host, 0);
				overridePendingTransition(R.anim.right_left,R.anim.left_right);
            }
        });
		TextView uname=new TextView(this); 
	    uname=(TextView)findViewById(R.id.user_id); 
	    username = getIntent().getStringExtra("username");
	    uname.setText("Welcome, "+username+".");
	}
	private void displayListView() {
		 
		  //Array list of Room, change this part to get from server
		  roomList = new ArrayList<Room>();
		  roomList.add(new Room("What For Lunch?","monkey"));
		  
		  //create an ArrayAdaptar from the String Array
		  dataAdapter = new MyCustomAdapter(this,R.layout.room_info
		    , roomList);
		  ListView listView = (ListView) findViewById(R.id.room_list);
		  // Assign adapter to ListView
		  listView.setAdapter(dataAdapter);
      }
	private class MyCustomAdapter extends ArrayAdapter<Room> {
		 
		  private ArrayList<Room> roomList;
		 
		  public MyCustomAdapter(Context context, int textViewResourceId, ArrayList<Room> roomList) {
		   super(context, textViewResourceId, roomList);
		   this.roomList = new ArrayList<Room>();
		   this.roomList.addAll(roomList);
		  }
		 
		  private class ViewHolder {
			   ImageView egg;
			   TextView name;
			   TextView owner;
			  }
		  public void updateGlobal(){
			  RoomActivity.this.roomList.addAll(this.roomList);
		  }
		  @Override
		  public View getView(int position, View convertView, ViewGroup parent) {
		 
		   ViewHolder holder = null;
		 
		   if (convertView == null) {
		   LayoutInflater vi = (LayoutInflater)getSystemService(
		     Context.LAYOUT_INFLATER_SERVICE);
		   convertView = vi.inflate(R.layout.room_info, null);
		 
		   holder = new ViewHolder();
		   holder.egg = (ImageView) convertView.findViewById(R.id.room_image);
		   holder.name = (TextView) convertView.findViewById(R.id.room_name);
		   holder.owner = (TextView) convertView.findViewById(R.id.owner_name);
		   convertView.setTag(holder);
		 
		    holder.egg.setOnClickListener( new View.OnClickListener() {  
		     public void onClick(View v) {  
		      ImageView egg = (ImageView) v ;  
		      Room rom = (Room) egg.getTag();  
		      Intent Vote = new Intent(RoomActivity.this, VoteActivity.class);
		      startActivity(Vote);
		      overridePendingTransition(R.anim.right_left,R.anim.left_right);
		     }  	
		    });  
		   } 
		   else {
		    holder = (ViewHolder) convertView.getTag();
		   }
		 
		   Room Room = roomList.get(position);
		   holder.name.setText(Room.getName());
		   holder.owner.setText(Room.getOwner());
		   holder.egg.setTag(Room);
		 
		   return convertView;
		 
		  }
		 }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.room, menu);
		return true;
	}

}
