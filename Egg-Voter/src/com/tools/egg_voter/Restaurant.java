package com.tools.egg_voter;

public class Restaurant {
	private String name;
	//For voter to return to the server
	private boolean selected;
	//For server to maintain count
	private int count;
	public Restaurant(String name){
		this.name = name;
		selected = false;
		count = 0;
	}

	public String getName(){
		return name;
	}

	public void setSelected(boolean tf){
		selected = tf;
	}
	public boolean getSelected(){
		return selected;
	}
	public void incrementCount(){
		count++;
	}
	public int getCount(){
		return count;
	}
}
