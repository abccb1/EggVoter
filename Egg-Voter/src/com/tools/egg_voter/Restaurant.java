package com.tools.egg_voter;

public class Restaurant {
	private String name;
	private String discription;
	//For voter to return to the server
	private boolean selected;
	//For server to maintain count
	private int count;
	public Restaurant(String name,String discription){
		this.name = name;
		this.discription = discription;
		selected = false;
		count = 0;
	}

	public String getName(){
		return name;
	}
	public void setDiscription(String discription){
		this.discription = discription;
	}
	public String getDiscription(){
		return discription;
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
