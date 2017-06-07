package com.dfour.blockbreaker.network;

public class NetworkedUser {
	public int connectionID;
	public String username; // holds username
	public int xpos;		// holds pad x pos
	public int xposLast;	// holds last xpos for interpolation/prediction
	public int ping;
	public boolean isReady = false;
	
	public NetworkedUser(int conid){
		this.connectionID = conid;
	}
	
	public NetworkedUser(int conid, String name){
		this.connectionID = conid;
		this.username = name;
	}
}
