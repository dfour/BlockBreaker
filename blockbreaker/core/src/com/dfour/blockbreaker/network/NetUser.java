package com.dfour.blockbreaker.network;

public class NetUser {
	public int connectionID;
	public String username; // holds username
	public int xpos;		// holds pad x pos
	public int xposLast;	// holds last xpos for interpolation/prediction
	public int ping;
	public boolean isReady = false;
	
	public NetUser(int conid){
		this.connectionID = conid;
	}
	
	public NetUser(int conid, String name){
		this.connectionID = conid;
		this.username = name;
	}
}
