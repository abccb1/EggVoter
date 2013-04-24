package com.tools.egg_voter;

import java.util.ArrayList;


import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class VoteActivity extends Activity {
	public ArrayList<Restaurant> restaurantList;
	public MyCustomAdapter dataAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vote);
		displayListView();
		findViewById(R.id.vote_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	dataAdapter.updateGlobal();
            	//Send back the restaurantList to server here
            	
            	finish();
				overridePendingTransition(R.anim.left_in,R.anim.right_out);
             }
            });
	}
	private void displayListView() {
		 
		  //Array list of restaurant, change this part to get from server
		  restaurantList = new ArrayList<Restaurant>();
		   
		  restaurantList.add(new Restaurant("Ki Xiang","Korean Grill"));
		  restaurantList.add(new Restaurant("Tian Xinag Xinag","Chinese Food Buffet"));
		  restaurantList.add(new Restaurant("Mai Dang Xiang","American Food"));
		  restaurantList.add(new Restaurant("K F Xiang","Fry Chiken"));
		  restaurantList.add(new Restaurant("Taste of Xiang","Chinese Food Buffet"));
		  restaurantList.add(new Restaurant("Yue Nan Xiang","Yue Nan Noodle"));
		 
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
			   TextView discription;
			  }
		  public void updateGlobal(){
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
		   holder.discription = (TextView) convertView.findViewById(R.id.discription);
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
		   holder.discription.setText(" (" +  restaurant.getDiscription() + ")");
		   holder.name.setText(restaurant.getName());
		   holder.name.setChecked(false);
		   holder.name.setTag(restaurant);
		 
		   return convertView;
		 
		  }
		 }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.vote, menu);
		return true;
	}

}
