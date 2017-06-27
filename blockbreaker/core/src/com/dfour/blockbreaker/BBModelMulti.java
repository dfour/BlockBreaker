package com.dfour.blockbreaker;

import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.dfour.blockbreaker.controller.AppController;
import com.dfour.blockbreaker.entity.Ball;
import com.dfour.blockbreaker.entity.Bomb;
import com.dfour.blockbreaker.entity.Brick;
import com.dfour.blockbreaker.entity.EntityFactory;
import com.dfour.blockbreaker.entity.MultiPad;
import com.dfour.blockbreaker.entity.PowerUp;
import com.dfour.blockbreaker.loaders.BBAssetManager;
import com.dfour.blockbreaker.loaders.LevelLoader;
import com.dfour.blockbreaker.network.AbstractNetworkBase;
import com.dfour.blockbreaker.network.BBClient;
import com.dfour.blockbreaker.network.BBHost;
import com.dfour.blockbreaker.network.PlayerActions;
import com.dfour.blockbreaker.network.NetworkCommon.*;


public class BBModelMulti extends BBModel{
	
	//TODO split model from player
	//TODO split model into abstract, SoloModel, MultiHostModel, MultiClientModel
	//TODO move shop purchases from shop to model so clients can't hack extra items OR make multiplayer shopscreen
	
	//TODO update lives, score, cash for clients
	//TODO implement magneism for clients
	//TODO make shop work for clients and update host
	//TODO update contact listener to delagate contacts to multipads

	public static final int LOADING = 0;
	public static final int WAITING_CLIENT = 1;
	public static final int ALL_LOADED = 2;
	public static final int RUNNING = 3;
	public static final int DISCONNECTED = 4;
	public static final int PAUSED = 5;
	
	private int gameState = LOADING;
	private boolean isHost = false;
	private float fullUpdateTimer = 0.5f; // do full world update only twice per second
	private int worldUpdate = 3;
	
	private AbstractNetworkBase netbase;
	private BBHost host;
	private BBClient client;
	
	private HashMap<Integer,Player> playerMap = new HashMap<Integer,Player>();
	public Array<ItemDied> deadItems = new Array<ItemDied>();
	
	
	public BBModelMulti(AppController cont, BBAssetManager ass,AbstractNetworkBase base ) {
		super(cont, ass);
		netbase = base;
		netbase.multi = this;
		if(base instanceof BBHost){
			isHost = true;
			host = (BBHost) base;
		}else{
			client = (BBClient) base;
		}
	}

	@Override
	public void init() {
		super.init();
		if(isHost){
			WorldUpdate wu = new WorldUpdate(entFactory.getAllItems(true));
			host.sendLevel(wu);
		}
	}

	@Override
	protected void makeLevel() {
		if(isHost){
			super.makeLevel();
			gameState = RUNNING;
		}else{
			// load static items from level file
			ll.loadLevel(level,LevelLoader.MULTI_CLIENT);
			entFactory.makeWalls(cH,cW);
			entFactory.makeBin(cW);
			//now load dynamic stuff from TCP
			entFactory.updateWorldItems(client.lastUpdate);
			gameState = RUNNING;
		}
	}
	
	

	@Override
	protected void createPad() {
		lp.pad = entFactory.makeMultiPad(5, 5);
	}

	@Override
	public void doLogic(float delta) {
		netbase.update();		
		
		if(!isHost && client.newUpdate){
			entFactory.updateWorldItems(client.lastUpdate);
			client.newUpdate = false;
		}else{
			if(host != null){
				boolean fullUpdate = false;
				fullUpdateTimer -= delta;
				if(fullUpdateTimer <= 0){
					fullUpdateTimer = 0.5f;
					fullUpdate = true;
				}
				if(worldUpdate <= 0){
					WorldUpdate wu = new WorldUpdate(entFactory.getAllItems(fullUpdate));
					host.sendLevel(wu);
					// limit client update to 20 times per second. can try lower values as clients simulate physics too
					worldUpdate = 3;
				}
				worldUpdate--;
			}
		}
		
		controlRespond();
		
		controlPad();
		
		controlClientsPad();
		
		padSync();

		if(gameState == RUNNING){
			debugFeatures();
			updateLazer(delta);
			updateBombs();
			world.step(delta / 4, 3, 3);
			updateBalls();
			updateMagnet(lp);
			updateMagnetMulti();
			updatePowerUps();
			updateBricks();
			updateExplosions();
			updateObstacles();
			updatePortals();
			updateDrunk(delta);
			updateSlow(delta);
			updateSticky(delta);
			updateLevelState(delta);
		}
		
		
		killItems();

		controller.ffive = false;
	}
	
	@Override
	protected void controlRespond() {
		
		super.controlRespond();
		
		if(!isHost){
			if((controller.isMouse1Down() || controller.getPull()) && !lp.magPull){
				lp.magPull = true;
				client.sendAction(PlayerActions.PLAYER_ACTION_MAG_PULLS);
			}
			
			if((controller.isMouse2Down() || controller.getPush()) && !lp.magPush){
				lp.magPush = true;
				client.sendAction(PlayerActions.PLAYER_ACTION_MAG_PULLE);
			}
			
			if((!controller.isMouse1Down() && !controller.getPull()) && lp.magPull){
				lp.magPull = false;
				client.sendAction(PlayerActions.PLAYER_ACTION_MAG_PUSHS);
			}
			
			if((!controller.isMouse2Down() && !controller.getPush()) && lp.magPush){
				lp.magPush = false;
				client.sendAction(PlayerActions.PLAYER_ACTION_MAG_PUSHE);
			}
		}
	}

	private void controlClientsPad() {
		if(!isHost){
			
			//this.pad.body.getPosition().x += (Math.random() * 5) - 2.5;
			
			// send our pos to host if changed
			int curPadPos = (int)lp.pad.body.getPosition().x;
			//if(lastPadPos != curPadPos){
				client.sendMyPosition(curPadPos);
			//}
		}else{
			// send host + client pos to clients
			for(Player mp:playerMap.values()){
				PlayerUpdate pu = new PlayerUpdate();
				pu.playerId = mp.connectionId;
				pu.playerXPos = mp.position;
				host.sendPlayerPosition(pu);
			}
		}
	}
	
	public void padSync(){
		for(Player mp:playerMap.values()){
			((MultiPad)mp.pad).updatePos();
		}
	}
	
	public void updatePadPos(PlayerUpdate pu){
		Player p;
		if(playerMap.containsKey(pu.playerId)){
			p = playerMap.get(pu.playerId);
		}else{
			BBUtils.log("Making multipad for"+pu.playerId);
			MultiPad mpad = entFactory.makeMultiPad(pu.playerXPos, 5);
			mpad.clientId = pu.playerId;
			p = new Player();
			p.pad= mpad;
			playerMap.put(pu.playerId, p);
		}
		// set pad pos
		if(!world.isLocked()){
			p.pad.setPosition(pu.playerXPos, 5);
		}else{
			p.pad.newPos = pu.playerXPos;
		}
	}

	@Override
	protected void dispensePowerup(Brick brick) {
		if(isHost){
			// only host can dispense power ups
			super.dispensePowerup(brick);
		}
	}

	public void addDeadItems(ItemDied di) {
		deadItems.add(di);
	}
	
	public void killItems(){
		for(ItemDied di:deadItems){
			if(di.type == EntityFactory.BALL){
				for(Ball ball:entFactory.balls){
					if(ball.id == di.id){
						ball.isDead = true;
					}
				}
			}else if(di.type == EntityFactory.BRICKS){
				for(Brick b:entFactory.bricks){
					if(b.id == di.id){
						b.isDead = true;
					}
				}
			}else if(di.type == EntityFactory.BOMBS){
				for(Bomb b:entFactory.bombs){
					if(b.id == di.id){
						b.isDead = true;
					}
				}
			}else if(di.type == EntityFactory.POWERUPS){
				for(PowerUp b:entFactory.pups){
					if(b.id == di.id){
						b.isDead = true;
					}
				}
			}
		}
	}
	
	
	// update dead items
	@Override
	protected void deadBrick(Brick brick) {
		if(isHost){
			host.updateDead(EntityFactory.BRICKS, brick.id);
		}
	}
	
	@Override
	protected void deadPowerUp(PowerUp pup){
		if(isHost){
			host.updateDead(EntityFactory.POWERUPS, pup.id);
		}
	}

	@Override
	protected void deadBall(Ball ball) {
		if(isHost){
			host.updateDead(EntityFactory.BALL, ball.id);
		}
	}

	@Override
	protected void deadBomb(Bomb bomb) {
		if(isHost){
			host.updateDead(EntityFactory.BOMBS, bomb.id);
		}
	}
	
	

	@Override
	public void sendPause(boolean isPaused) {
		if(isHost){
			gameState = isPaused?PAUSED:RUNNING;
			host.sendGameState(gameState);
		}
	}

	public void playerAction(PlayerAction pa) {
		if(pa.playerAction == PlayerActions.PLAYER_ACTION_MAG_PULLS){
			playerMap.get(pa.playerId).magPull = true;
		}else if(pa.playerAction == PlayerActions.PLAYER_ACTION_MAG_PULLE){
			playerMap.get(pa.playerId).magPull = false;
		}else if(pa.playerAction == PlayerActions.PLAYER_ACTION_MAG_PUSHE){
			playerMap.get(pa.playerId).magPush = true;
		}else if(pa.playerAction == PlayerActions.PLAYER_ACTION_MAG_PUSHE){
			playerMap.get(pa.playerId).magPush = false;
		}
	}
	
	private void updateMagnetMulti(){
		for(Player pl:playerMap.values()){
			updateMagnet(pl);
		}
	}

	// override applyMagnitism to apply magnetism from all players
	@Override
	protected void applyMagnetism(Body body) {
		for(Player pl:playerMap.values()){
			
			if ((pl.magPull || pl.magPush) && pl.magnetPower > 0) {
				float velx = pl.pad.body.getPosition().x - body.getPosition().x;
				float vely = pl.pad.body.getPosition().y - pl.pad.body.getPosition().y;
				float length = (float) Math.sqrt(velx * velx + vely * vely);
				if (length != 0) {
					velx = velx / length;
					vely = vely / length;
				}
				if (pl.magnetPower > 0) {
					if (pl.magPull) {
						body.applyForceToCenter(new Vector2(velx * pl.magnetStrength, vely
								* pl.magnetStrength), true);
					} else {
						body.applyForceToCenter(new Vector2(velx * -pl.magnetStrength,
								vely * -pl.magnetStrength), true);
					}
				}
			}
		}
	}
}
