package com.dfour.blockbreaker.network;

import java.io.IOException;
import java.util.HashMap;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.dfour.blockbreaker.BBUtils;
import com.dfour.blockbreaker.BlockBreaker;
import com.dfour.blockbreaker.network.NetworkCommon.*;

/**
 * Class to contain all hosting related functions
 * @author darkd
 *
 */
public class BBHost extends AbstractNetworkBase{
	Server server;
	private NetUser me;
	public int connectionCount = 0;
	public int readyCount = 0;
	public HashMap<Integer, Integer> playerPositions = new HashMap<Integer, Integer>();
	
	public BBHost(String uname){
		me = new NetUser(0, uname);
		this.characters.put(0, me);
		
		server = new Server() {
			protected Connection newConnection () {
				// By providing our own connection implementation, we can store per
				// connection state without a connection ID to state look up.
				CharacterConnection cn = new CharacterConnection();
				connectionCount+=1;
				if(connectionCount >3){
					NetError error = new NetError("Server Full");
					error.disconnect = true;
					cn.sendTCP(error);
				}
				return cn;
			}
		};
		
		NetworkCommon.register(server);
		
		Listener listener = new Listener() {
			public void received (Connection c, Object object) {
				CharacterConnection connection = (CharacterConnection)c;
				//NetworkedUser character = connection.character;
				
				System.out.println(object);
								
				if(object instanceof Login){
					if(logUserIn((Login)object, connection)){
						// logged in
						Accept msg = new Accept();
						msg.version = BlockBreaker.VERSION;
						msg.name = me.username; 
						msg.connectionId = connection.getID();
						c.sendTCP(msg);
						addCharacter(connection);
						Log.info("SERVER: client "+connection.getID()+" Connected");
					}else{
						c.sendTCP(new NetError("Version Mismatch"));
					}
				}else if(object instanceof LobbyMessage){
					BBUtils.log("LobbyMessage");
					sendAllMessage((LobbyMessage)object);
				}else if(object instanceof UserReady){
					BBUtils.log("UserReady");
					readyUpRelay((UserReady)object);
				}else if(object instanceof PlayerUpdate){
					BBUtils.log("PlayerUpdate");
					updatePlayerPos((PlayerUpdate) object);
				}else if(object instanceof PlayerAction){
					BBUtils.log("PlayerAction");
					performPlayerAction((PlayerAction)object);
				}
			}
			
			public void disconnected (Connection c) {
				
				removeCharacter(c);
				
				// disconneted user
				BBUtils.log("Disconnect");
				connectionCount-=1;
				
			}
		};
		
		if(BlockBreaker.debug_multilag){
			Listener.LagListener ll = new Listener.LagListener (100,150,listener);
			server.addListener(ll);
		}else{
			server.addListener(listener);
		}
		
		
		try {
			server.bind(NetworkCommon.tcpPort,NetworkCommon.udpPort);
			server.start(); 
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Disconnect error");
		}
	}
	
	protected void performPlayerAction(PlayerAction pa) {
		multi.playerAction(pa);
		
	}

	public boolean logUserIn(Login lg,CharacterConnection c){
		if(lg.version.equalsIgnoreCase(BlockBreaker.VERSION)){
			c.character = new NetUser(c.getID(),lg.name);
			return true;
		}
		return false;
	}
	
	public void updatePlayerPos(PlayerUpdate pu) {
		multi.updatePadPos(pu);
	}
	
	public void sendPlayerPosition(PlayerUpdate pu){
		server.sendToAllTCP(pu);
	}

	@Override
	public void receiveCharacterPosition(int id, int xp) {
		this.characters.get(id).xpos = xp;
	}
	
	public void removeCharacter(Connection c){
		// send all current users the new user details
		RemoveUser ru = new RemoveUser();
		ru.id = c.getID();
		this.characters.remove(ru.id);
		server.sendToAllTCP(ru);
	}

	public void addCharacter(CharacterConnection c) {
		
		// send all currentUsers to new connection
		for(NetUser netUser:this.characters.values()){
			if(netUser.connectionID != 0){
				// user not host
				AdditionalUser adduser = new AdditionalUser();
				adduser.id = netUser.connectionID;
				adduser.name = netUser.username;
				adduser.isReady = netUser.isReady;
				c.sendTCP(adduser);
			}
			
		}
		
		this.characters.put(c.character.connectionID, c.character);
		
		// send all current users the new user details
		AdditionalUser adduser = new AdditionalUser();
		adduser.id = c.character.connectionID;
		adduser.name = c.character.username;
		adduser.isReady = c.character.isReady;
		server.sendToAllTCP(adduser);
	}
	
	public void sendAllMessage(LobbyMessage lm){
		server.sendToAllTCP(lm);
		newMessage = true;
		lastMessage = lm;
	}

	@Override
	public void sendMessage(String msg) {
		LobbyMessage lm = new LobbyMessage();
		lm.message = msg;
		lm.name = me.username;
		server.sendToAllTCP(lm);
		newMessage = true;
		lastMessage = lm;
	}
	
	public void initiatePingCheck(){
		for(Connection c :server.getConnections()){
			c.updateReturnTripTime();
			
			if(c.getReturnTripTime() > 0){
				characters.get(c.getID()).ping = c.getReturnTripTime();
			}else{
				if(characters.get(c.getID()) != null){
					characters.get(c.getID()).ping = 999;
				}
			}
		}
	}
	
	public void updateDead(int type, long id){
		ItemDied di = new ItemDied();
		di.type = type;
		di.id = id;
		server.sendToAllTCP(di);
	}
	
	public void readyUp(boolean rdy){
		UserReady readyMessage = new UserReady();
		readyMessage.id = 0;
		readyMessage.status = rdy;
		readyUpRelay(readyMessage);
	}
	
	public void readyUpRelay(UserReady msg){
		if(msg.status){
			readyCount+=1;
		}else{
			readyCount-=1;
		}
		characters.get(msg.id).isReady = msg.status;
		server.sendToAllTCP(msg);
		
		if(readyCount > connectionCount){
			startLevelReady = true;
		}
		System.out.println(readyCount+"R:C"+connectionCount);
	}
	
	public void sendLevel(WorldUpdate wu){
		server.sendToAllTCP(wu);
	}
	
	public void update(){
		//try {
		//	server.update(30);
		//} catch (IOException e) {
		//	e.printStackTrace();
		//}
	}
	
	public void sendGameState(int state){
		GameState gs = new GameState();
		gs.state = state;
		server.sendToAllTCP(gs);
	}
	
}
