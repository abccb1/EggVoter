package com.tools.egg_voter;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;


public class HostActivity extends Activity {
	public ArrayList<Restaurant> restaurantList;
	public MyCustomAdapter dataAdapter;
	public String roomname;
	public ProgressDialog pd;
	public Handler finishedHandler = new Handler() {
        @Override public void handleMessage(Message msg) {
	        pd.dismiss();
	        //start new activity
	        finish();
			overridePendingTransition(R.anim.left_in,R.anim.right_out);
	    }
	}; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_host);
		displayListView();
		findViewById(R.id.add_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	EditText input = (EditText)findViewById(R.id.add_text);
            	restaurantList.add(new Restaurant(input.getText().toString()));
            	dataAdapter.addNew(new Restaurant(input.getText().toString()));
            	dataAdapter.notifyDataSetChanged();
             }
            });
		findViewById(R.id.submit_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	AlertDialog.Builder alert = new AlertDialog.Builder(HostActivity.this);
	        	alert.setTitle("Name Your Room");
	        	alert.setMessage("Enter a room name for others to find");
	        	// Set an EditText view to get user input 
	        	final EditText input = new EditText(HostActivity.this);
	        	alert.setView(input);
	        	pd = new ProgressDialog(HostActivity.this);
	        	alert.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
		        	public void onClick(DialogInterface dialog, int whichButton) {
		        	 dataAdapter.updateGlobal();
			         pd.setTitle("Submiting");
			         pd.setMessage("Wait while submiting...");
			         pd.show();
			         final String result = "host|root|ttgwzmt5fz|" + RoomActivity.username + "|" + input.getText().toString()+"|" + formResult();
			         Thread contactserver = new Thread(){ 
				        	@Override 
				        	public void run(){
				        		//Add Operation here
				        		//Send back the result String to server here.
				        		
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
		for(Restaurant rest:restaurantList){
			if(rest.getSelected())
			 result += (rest.getName() + "|");
		}
		return result;
	}
	private void formList(String in){
		String[] input = in.split("\\|");
		for(int i = 0;i < input.length;i++)
		  restaurantList.add(new Restaurant(input[i]));
	}
	private void displayListView() {
		 
		  //Array list of restaurant, change this part to get from server
		  String in = "Ki Xiang|Korean Restaurant";
		  restaurantList = new ArrayList<Restaurant>();
		  formList(in);
		 
		  //create an ArrayAdaptar from the String Array
		  dataAdapter = new MyCustomAdapter(this,R.layout.restaurant_info
		    , restaurantList);
		  ListView listView = (ListView) findViewById(R.id.candidate_list);
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
		  public void addNew(Restaurant rest){
			  this.restaurantList.add(rest);
		  }
		  public void updateGlobal(){
			  HostActivity.this.restaurantList.clear();
			  HostActivity.this.restaurantList.addAll(this.restaurantList);
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
		   holder.name.setChecked(restaurant.getSelected());
		   holder.name.setTag(restaurant);
		 
		   return convertView;
		 
		  }
		 }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.host, menu);
		return true;
	}

}
