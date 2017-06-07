package com.dfour.blockbreaker.network;

import java.io.IOException;

import com.dfour.blockbreaker.BlockBreaker;
import com.dfour.blockbreaker.network.NetworkCommon.AdditionalUser;
import com.dfour.blockbreaker.network.NetworkCommon.NetError;
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
	
	public BBClient(String uname){
		me = new NetworkedUser(99999999,uname);
		client = new Client();
		client.start();

		NetworkCommon.register(client);
		
		client.addListener(new ThreadedListener(new Listener() {
			

			public void connected (Connection connection) {
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
					receivedMessage((LobbyMessage)object);
				}else if(object instanceof UserReady){
					userReady((UserReady)object);
				}else if(object instanceof AdditionalUser){
					addUser((AdditionalUser)object);
				}else if(object instanceof NetError){
					respondError((NetError)object);
				}
			}
		}));
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

	protected void addUser(AdditionalUser user) {
		NetworkedUser notme = new NetworkedUser(user.id,user.name);
		characters.put(notme.connectionID, notme).isReady = notme.isReady;
	}

	public void userReady(UserReady ur) {
		characters.get(ur.id).isReady = ur.status;
	}

	@Override
	public void updateCharacterPosition( int id, int xpos) {		
	}

	@Override
	public void addCharacter(CharacterConnection c) {		
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

}
