package com.dfour.blockbreaker.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class NetworkCommon {

	static public final int tcpPort = 54555;
	static public final int udpPort = 54777;

	// This registers objects that are going to be sent over the network.
	static public void register (EndPoint endPoint) {
		Kryo kryo = endPoint.getKryo();
		kryo.register(Login.class);
		kryo.register(Accept.class);
		kryo.register(AdditionalUser.class);
		kryo.register(LobbyMessage.class);
		kryo.register(UserReady.class);
		kryo.register(NetError.class);
		kryo.register(PlayerUpdate.class);
		
	}
	
	static public class Login{
		public String version;  // game version (client to host)
		public String name;		// client username
	}
	
	static public class Accept{
		public String version;  // game version (host to client)
		public String name;		// host username
		public int connectionId;// conid from host to client
	}
	
	static public class AdditionalUser{
		public int id;
		public String name;
		public boolean isReady = false;
	}
	
	static public class LobbyMessage{
		public String name;
		public String message;
	}
	
	static public class UserReady{
		public int id;
		public boolean status; // true = rdy 
	}
	
	static public class UpdatePause{
		public boolean isPaused;
	}
	
	static public class NetError{
		public boolean disconnect = false;
		public String error = "An Unknown Error Occured";
		
		public NetError(String error){
			this.error = error;
		}
	}
	
	static public class PlayerUpdate{
		public int playerId;
		public int playerXPos;
	}
}







