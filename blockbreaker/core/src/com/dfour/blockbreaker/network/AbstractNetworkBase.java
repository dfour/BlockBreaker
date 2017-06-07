package com.dfour.blockbreaker.network;

import java.util.HashMap;

import com.dfour.blockbreaker.network.NetworkCommon.LobbyMessage;
import com.esotericsoftware.kryonet.Connection;

public abstract class AbstractNetworkBase {
	public HashMap<Integer, NetworkedUser> characters = new HashMap<Integer, NetworkedUser>();
	public boolean newMessage = false;
	public LobbyMessage lastMessage;
	
	public abstract void updateCharacterPosition(int id, int xpos);
	
	public abstract void addCharacter(CharacterConnection c);
	
	public abstract void sendMessage(String msg);
	
	public abstract void initiatePingCheck();
	
	public abstract void update();
	
	public abstract void readyUp(boolean rdy);
	
	// server only connection implementation
	static class CharacterConnection extends Connection {
		public NetworkedUser character;
	}
}
