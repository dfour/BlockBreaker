package com.dfour.blockbreaker.network;

import java.util.HashMap;

import com.dfour.blockbreaker.BBModelMulti;
import com.dfour.blockbreaker.network.NetworkCommon.LobbyMessage;
import com.dfour.blockbreaker.network.NetworkCommon.RemoveUser;
import com.esotericsoftware.kryonet.Connection;

public abstract class AbstractNetworkBase {
	public HashMap<Integer, NetworkedUser> characters = new HashMap<Integer, NetworkedUser>();
	public boolean newMessage = false;
	public LobbyMessage lastMessage;
	public boolean startLevelReady = false;
	public BBModelMulti multi;
	
	public abstract void receiveCharacterPosition(int id, int xpos);
	
	public abstract void sendMessage(String msg);
	
	public abstract void initiatePingCheck();
	
	public abstract void update();
	
	public abstract void readyUp(boolean rdy);
	
	// server only connection implementation
	static class CharacterConnection extends Connection {
		public NetworkedUser character;
	}
}
