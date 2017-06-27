package com.dfour.blockbreaker.network;

import java.io.IOException;

import com.dfour.blockbreaker.BBModelMulti;
import com.dfour.blockbreaker.BBUtils;
import com.dfour.blockbreaker.BlockBreaker;
import com.dfour.blockbreaker.network.NetworkCommon.RemoveUser;
import com.dfour.blockbreaker.network.NetworkCommon.*;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage.Ping;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;
import com.esotericsoftware.minlog.Log;

public class BBClient extends AbstractNetworkBase{
	Client client;
	private NetworkedUser me;
	public WorldUpdate lastUpdate;
	public boolean newUpdate = false;
	public int gameState = 0;
	
	public BBClient(String uname){
		me = new NetworkedUser(99999999,uname);
		client = new Client();
		client.start();

		NetworkCommon.register(client);
		
		Listener listener = new Listener() {
			

			public void connected (Connection connection) {
				BBUtils.log("Connected");
			}
			
			
			
			@Override
			public void disconnected(Connection connection) {
				super.disconnected(connection);
			}



			@Override
			public void idle(Connection connection) {
				super.idle(connection);
			}



			public void received (Connection connection, Object object) {
				if(object instanceof Accept){
					Accept msg = (Accept)object;
					
					System.out.println("Client:Accept msg ="+msg.toString());
					
					NetworkedUser host = new NetworkedUser(0, msg.name);
					me.connectionID = connection.getID();
					
					characters.put(0,host);
					characters.put(me.connectionID,me);
					Log.info("CLIENT: cnnected to server my con id "+connection.getID());
				}else if (object instanceof Ping) {
					Ping ping = (Ping)object;
					
					if (ping.isReply){ 
						me.ping = connection.getReturnTripTime();
					}
				}else if(object instanceof LobbyMessage){
					BBUtils.log("Lobby Message");
					receivedMessage((LobbyMessage)object);
				}else if(object instanceof UserReady){
					BBUtils.log("UserReady");
					userReady((UserReady)object);
				}else if(object instanceof AdditionalUser){
					BBUtils.log("AdditionalUser");
					addUser((AdditionalUser)object);
				}else if(object instanceof NetError){
					BBUtils.log("NetError");
					respondError((NetError)object);
				}else if(object instanceof WorldUpdate){
					//BBUtils.log("WorldUpdate");
					lastUpdate = (WorldUpdate) object;
					startLevelReady = true;
					newUpdate = true;
				}else if(object instanceof GameState){
					BBUtils.log("GameState");
					gameState = ((GameState) object).state;
				}else if(object instanceof ItemDied){
					BBUtils.log("ItemDied");
					killItem(((ItemDied)object));
				}else if(object instanceof RemoveUser){
					removeCharacter(((RemoveUser) object));
				}
			}
		};
		
		if(BlockBreaker.debug_multilag){
			client.addListener(new ThreadedListener(new Listener.LagListener(
					BlockBreaker.debug_min_lag, BlockBreaker.debug_max_lag,
					listener)));
		}else{
			client.addListener(new ThreadedListener(listener));
		}
	}

	protected void killItem(ItemDied item) {
		multi.addDeadItems(item);
	}

	protected void respondError(NetError error) {
		if(error.disconnect){
			client.stop();
		}
		LobbyMessage lm = new LobbyMessage();
		lm.message = error.error;
		lm.name = "Server";
		newMessage = true;
		lastMessage = lm;
	}
	
	public void removeCharacter(RemoveUser ru) {
		characters.remove(ru.id);
	}

	protected void addUser(AdditionalUser user) {
		NetworkedUser notme = new NetworkedUser(user.id,user.name);
		characters.put(notme.connectionID, notme).isReady = notme.isReady;
	}

	public void userReady(UserReady ur) {
		characters.get(ur.id).isReady = ur.status;
	}
	
	public void sendMyPosition(int xpos){
		PlayerUpdate pu = new PlayerUpdate();
		pu.playerXPos = xpos;
		pu.playerId = me.connectionID;
		client.sendTCP(pu);
	}

	@Override
	public void receiveCharacterPosition( int id, int xpos) {		
	}
	
	@Override
	public void sendMessage(String msg) {
		LobbyMessage lbmsg = new LobbyMessage();
		lbmsg.message = msg;
		lbmsg.name = me.username;
		client.sendTCP(lbmsg);
	}
	
	private void receivedMessage(LobbyMessage lm) {
		newMessage = true;
		lastMessage = lm;
	}
	
	public void initiatePingCheck(){
		client.updateReturnTripTime();
	}
	
	public void update(){
		/*/
		try {
			 client.update(30);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//*/
	}
	
	public void readyUp(boolean rdy){
		UserReady readyMessage = new UserReady();
		readyMessage.status = rdy;
		readyMessage.id = me.connectionID;
		client.sendTCP(readyMessage);
	}
	
	public void start(String host){
		try {
			client.connect(5000, host, NetworkCommon.tcpPort, NetworkCommon.udpPort);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		Login login = new Login();
		login.name = me.username;
		login.version = BlockBreaker.VERSION;
		client.sendTCP(login);
	}

	public void sendAction(int act) {
		PlayerAction pa = new PlayerAction();
		pa.playerId = me.connectionID;
		pa.playerAction = act;
		client.sendTCP(pa);
		
	}

}
