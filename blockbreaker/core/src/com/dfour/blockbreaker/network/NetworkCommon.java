package com.dfour.blockbreaker.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class NetworkCommon {

	static public final int tcpPort = 54555;
	static public final int udpPort = 54777;
	
	static public final int STATE_ALL_READY = 1;

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
		kryo.register(WorldUpdate.class);
		kryo.register(ItemBase.class);
		kryo.register(ItemBase[].class);
		kryo.register(GameState.class);
		kryo.register(ItemDied.class);
		kryo.register(PlayerAction.class);
		
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
	
	static public class PlayerAction{
		public int playerId;
		public int playerAction;
	}
	
	static public class GameState{
		public int state;
	}
	
	static public class ItemBase{
		public long id;
		public int item;
		public float x,y,vx,vy,r;
		public int extra = 0;
	}
	
	static public class ItemDied{
		public int type;
		public long id;
	}
	
	/**
	 * @author darkd
	 * For updating world on client with server
	 */
	static public class WorldUpdate{
		public ItemBase[] spinners;
		public ItemBase[] balls;
		public ItemBase[] bombs;
		public ItemBase[] bricks;
		public ItemBase[] pups;
		
		public WorldUpdate(){}
		
		public WorldUpdate(ItemBase[][] all){
			balls = all[0];
			bombs = all[1];
			bricks = all[2];
			pups = all[3];
			spinners = all[4];
		}
	}
}







