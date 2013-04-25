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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;


public class VoteActivity extends Activity {
	public ArrayList<Restaurant> restaurantList;
	public MyCustomAdapter dataAdapter;
	public ProgressDialog pd;
	public String roomname;
	public String owner;
	//socket
			Socket socket;
			PrintWriter out;
			Scanner in;
			
	public Handler finishedHandler = new Handler() {
        @Override public void handleMessage(Message msg) {
	        pd.dismiss();
	        //start new activity
	        finish();
			overridePendingTransition(R.anim.left_in,R.anim.right_out);
	    }
	};
	public Handler resultHandler = new Handler() {
        @Override public void handleMessage(Message msg) {
	        //start new activity        	
			Intent Result = new Intent(VoteActivity.this, ResultActivity.class);
			Result.putExtra("roomname", roomname);
			startActivity(Result);
			overridePendingTransition(R.anim.right_left,R.anim.left_right);
			finish();
	    }
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		roomname = getIntent().getStringExtra("roomname");
		owner = getIntent().getStringExtra("owner");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vote);
		displayListView();
		findViewById(R.id.end_vote).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(owner.equals(RoomActivity.username)){
            		
            		AlertDialog.Builder alert = new AlertDialog.Builder(VoteActivity.this);
    	        	alert.setMessage("End Your Vote?");

    	        	pd = new ProgressDialog(VoteActivity.this);
    	        	alert.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
    		        	public void onClick(DialogInterface dialog, int whichButton) {
    		        	 dataAdapter.updateGlobal();
    			         pd.setTitle("Submiting");
    			         pd.setMessage("Preparing result...");
    			         pd.show();
    			         final String request = "CloseVote|root|ttgwzmt5fz|"+roomname;
    			         Thread contactserver = new Thread(){ 
    				        	@Override 
    				        	public void run(){
    				        		//Add Operation here
    				        		//Send back the result String to server here.
    				        		try {
    									socket = new Socket("moore10.cs.purdue.edu", 4040);
    									out = new PrintWriter(socket.getOutputStream(),true);
    									out.println(request);
    									
    								} catch (UnknownHostException e) {
    									// TODO Auto-generated catch block
    									e.printStackTrace();
    								} catch (IOException e) {
    									// TODO Auto-generated catch block
    									e.printStackTrace();
    								}
    				        		resultHandler.sendEmptyMessage(0);
    				        	}
    				     };
    				     contactserver.start();
    		             }
    		        });

    	        	alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
    	        	  public void onClick(DialogInterface dialog, int whichButton) {
    	        	  }
    	        	});

    	        	alert.show();
            	}
            }
        });
		findViewById(R.id.vote_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	AlertDialog.Builder alert = new AlertDialog.Builder(VoteActivity.this);
	        	alert.setMessage("Submit Your Vote?");

	        	pd = new ProgressDialog(VoteActivity.this);
	        	alert.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
		        	public void onClick(DialogInterface dialog, int whichButton) {
		        	 dataAdapter.updateGlobal();
			         pd.setTitle("Submiting");
			         pd.setMessage("Thanks for your vote...");
			         pd.show();
			         final String result = "voteRes|root|ttgwzmt5fz|"  + roomname +"|" + formResult();
			         Thread contactserver = new Thread(){ 
				        	@Override 
				        	public void run(){
				        		//Add Operation here
				        		//Send back the result String to server here.
				        		try {
									socket = new Socket("moore10.cs.purdue.edu", 4040);
									out = new PrintWriter(socket.getOutputStream(),true);
									out.println(result);
									in = new Scanner(socket.getInputStream());
									
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

	        	alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	        	  public void onClick(DialogInterface dialog, int whichButton) {
	        	  }
	        	});

	        	alert.show();
             }
            });
	}
	private String formResult(){
		String result = "";
		for(Restaurant rest:restaurantList) {
			if(rest.getSelected()) {
			  result += (rest.getName() + "|");
			//debug
			System.out.println("formResult " + result);
			}
		}
		
		return result;
	}
	private void formList(String in){
		String[] input = in.split("\\|");
		for(int i = 2;i < input.length;i++)
		  restaurantList.add(new Restaurant(input[i]));
	}
	private void displayListView() {
		 
		  //Array list of restaurant, change this part to get from server
		  String in = getIntent().getStringExtra("resList");
		  restaurantList = new ArrayList<Restaurant>();
		  formList(in);
		 
		  //create an ArrayAdaptar from the String Array
		  dataAdapter = new MyCustomAdapter(this,R.layout.restaurant_info
		    , restaurantList);
		  ListView listView = (ListView) findViewById(R.id.vote_list);
		  // Assign adapter to ListView
		  listView.setAdapter(dataAdapter);
        }
	private class MyCustomAdapter extends ArrayAdapter<Restaurant> {
		 
		  private ArrayList<Restaurant> restaurantList;
		 
		  public MyCustomAdapter(Context context, int textViewResourceId, ArrayList<Restaurant> restaurantList) {
		   super(context, textViewResourceId, restaurantList);
		   this.restaurantList = new ArrayList<Restaurant>();
		   this.restaurantList.addAll(restaurantList);
		  }
		 
		  private class ViewHolder {
			   CheckBox name;
			  }
		  public void updateGlobal(){
			  VoteActivity.this.restaurantList.clear();
			  VoteActivity.this.restaurantList.addAll(this.restaurantList);
		  }
		  @Override
		  public View getView(int position, View convertView, ViewGroup parent) {
		 
		   ViewHolder holder = null;
		 
		   if (convertView == null) {
		   LayoutInflater vi = (LayoutInflater)getSystemService(
		     Context.LAYOUT_INFLATER_SERVICE);
		   convertView = vi.inflate(R.layout.restaurant_info, null);
		 
		   holder = new ViewHolder();
		   holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
		   convertView.setTag(holder);
		 
		    holder.name.setOnClickListener( new View.OnClickListener() {  
		     public void onClick(View v) {  
		      CheckBox cb = (CheckBox) v ;  
		      Restaurant rest = (Restaurant) cb.getTag();  
		      rest.setSelected(cb.isChecked());
		     }  	
		    });  
		   } 
		   else {
		    holder = (ViewHolder) convertView.getTag();
		   }
		 
		   Restaurant restaurant = restaurantList.get(position);
		   holder.name.setText(restaurant.getName());
		   holder.name.setChecked(false);
		   holder.name.setTag(restaurant);
		 
		   return convertView;
		 
		  }
		 }

}
