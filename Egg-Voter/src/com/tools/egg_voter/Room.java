package com.tools.egg_voter;

public class Room {
	private String roomname;
	private String owner;
	private boolean status;
	public Room(String roomname,String owner,boolean status){
		this.roomname = roomname;
		this.owner = owner;
		this.status = status;
	}
	public String getName(){
		return roomname;
	}
	public String getOwner(){
		return owner;
	}
	public boolean getStatus(){
		return status;
	}
	public void setStatus(boolean status){
		this.status = status;
	}
}
