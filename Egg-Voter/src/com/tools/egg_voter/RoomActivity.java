package com.tools.egg_voter;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class RoomActivity extends Activity {
	public static String username;
	public Intent next;
	public ArrayList<Room> roomList;
	public MyCustomAdapter dataAdapter;
	//socket
			Socket socket;
			PrintWriter out;
			Scanner in;
	public Handler finishedHandler = new Handler() {
        @Override public void handleMessage(Message msg) {
		      startActivity(next);
		      overridePendingTransition(R.anim.right_left,R.anim.left_right);   
	    }
	};
	public Handler initialHandler = new Handler() {
        @Override public void handleMessage(Message msg) {
			//create an ArrayAdaptar from the String Array
			  dataAdapter = new MyCustomAdapter(RoomActivity.this,R.layout.restaurant_info
			    , roomList);
			  ListView listView = (ListView) findViewById(R.id.room_list);
			  // Assign adapter to ListView
			  listView.setAdapter(dataAdapter);
	    }
	};
	public Handler refreshHandler = new Handler() {
        @Override public void handleMessage(Message msg) {
        	dataAdapter.updateView();
        	dataAdapter.notifyDataSetChanged();
	    }
	};
	public void refresh(){
		final String request = "getHosts|root|ttgwzmt5fz";
		  //Send this request to server
		  
		  Thread contactserver = new Thread(){ 
	        	@Override 
	        	public void run(){
	        		String resList = null;
	        		//Add Operation here
	        		//Send back the result String to server here.
	        		try {
						socket = new Socket("moore10.cs.purdue.edu", 4040);
						if (socket.isConnected()) {
							System.out.println("gethosts connected");
						}
						out = new PrintWriter(socket.getOutputStream(),true);
						//debug
						System.out.println(request);
						out.println(request);
						in = new Scanner(socket.getInputStream());
						if (in.hasNextLine()) {
							resList = (String) in.nextLine();
						} else {
							System.out.println("no rooms");
						}
						//debug
						System.out.println("room activity" + resList);
						formList(resList);						  
						  
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        		refreshHandler.sendEmptyMessage(0);
	        	}
	     };
	     contactserver.start();
	}
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
        findViewById(R.id.refresh).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	refresh();
            }
        });
		TextView uname=new TextView(this); 
	    uname=(TextView)findViewById(R.id.user_id); 
	    username = getIntent().getStringExtra("username");
	    uname.setText("Welcome, "+username+".");
	}
	private void formList(String in){
		roomList.clear();
		String[] input = in.split("\\|");
		boolean status = false;
		for(int i = 0;i < input.length-2;i+=3){
		  if(input[i+2].equals("open")){
			  status = true;
		  }
		  else{
			  status = false;
		  }
		  roomList.add(new Room(input[i],input[i+1],status));
		  }
	}
	private void displayListView() {
		  final String request = "getHosts|root|ttgwzmt5fz";
		  //Send this request to server
		  
		  Thread contactserver = new Thread(){ 
	        	@Override 
	        	public void run(){
	        		String resList = null;
	        		//Add Operation here
	        		//Send back the result String to server here.
	        		try {
						socket = new Socket("moore10.cs.purdue.edu", 4040);
						if (socket.isConnected()) {
							System.out.println("gethosts connected");
						}
						out = new PrintWriter(socket.getOutputStream(),true);
						//debug
						System.out.println(request);
						out.println(request);
						in = new Scanner(socket.getInputStream());
						if (in.hasNextLine()) {
							resList = (String) in.nextLine();
						} else {
							System.out.println("no rooms");
						}
						//debug
						System.out.println("room activity" + resList);
						
						roomList = new ArrayList<Room>();
						
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
		  public void updateView(){
			  this.roomList.clear();
			  this.roomList.addAll(RoomActivity.this.roomList);
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
		      if(rom.getStatus()){
		      next = new Intent(RoomActivity.this, VoteActivity.class);
		      next.putExtra("roomname", rom.getName());
		      next.putExtra("owner", rom.getOwner());
		      }
		      else{
		      next = new Intent(RoomActivity.this, ResultActivity.class);
		      next.putExtra("roomname", rom.getName());
		      next.putExtra("owner", rom.getOwner());
		      }
		      final String request = "GET-ALL-RES|root|ttgwzmt5fz|" +rom.getName()+"|" + username;
		      //Send request to server and get the result from server, use finish handler when finish.
		      Thread contactserver = new Thread(){ 
		        	@Override 
		        	public void run(){
		        		//Add Operation here
		        		//Send back the result String to server here.
		        		
		        		try {
							socket = new Socket("moore10.cs.purdue.edu", 4040);
							out = new PrintWriter(socket.getOutputStream(),true);
							out.println(request);
							in = new Scanner(socket.getInputStream());
							//debug
							System.out.println(request);
							out.println(request);
							
							final String resList = (String) in.nextLine();
							//debug
							System.out.println("Room" + resList);
							next.putExtra("resList", resList);
						} catch (UnknownHostException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		  		        
		        		finishedHandler.sendEmptyMessage(0);
		        	}
		     };
		     contactserver.start();
		      
		      

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


}
