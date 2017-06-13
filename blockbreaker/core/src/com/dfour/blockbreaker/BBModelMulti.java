package com.dfour.blockbreaker;

import java.util.HashMap;

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
import com.dfour.blockbreaker.network.MultiPlayer;
import com.dfour.blockbreaker.network.NetworkCommon.ItemDied;
import com.dfour.blockbreaker.network.NetworkCommon.PlayerAction;
import com.dfour.blockbreaker.network.NetworkCommon.PlayerUpdate;
import com.dfour.blockbreaker.network.NetworkCommon.WorldUpdate;

public class BBModelMulti extends BBModel{

	public static final int LOADING = 0;
	public static final int WAITING_CLIENT = 1;
	public static final int ALL_LOADED = 2;
	public static final int RUNNING = 3;
	public static final int DISCONNECTED = 4;
	
	public static final int PLAYER_ACTION_MAG_PULLS = 0; // start pull
	public static final int PLAYER_ACTION_MAG_PULLE = 1; // stop pull
	public static final int PLAYER_ACTION_MAG_PUSHS = 2; // start push
	public static final int PLAYER_ACTION_MAG_PUSHE = 3; // stop push
	public static final int PLAYER_ACTION_BOMB = 4; //release bomb
	
	
	
	private int gameState = LOADING;
	private AbstractNetworkBase netbase;
	private boolean isHost = false;
	private BBHost host;
	private BBClient client;
	
	private int playerCount = 0;
	private HashMap<Integer,MultiPlayer> playerMap = new HashMap<Integer,MultiPlayer>();
	
	public int totalPlayers = 0;
	public Array<ItemDied> deadItems = new Array<ItemDied>();
	public int lastPadPos = 0;
	
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
			WorldUpdate wu = new WorldUpdate(entFactory.getAllItems());
			host.sendLevel(wu);
		}
	}

	@Override
	protected void makeLevel() {
		if(isHost){
			super.makeLevel();
			gameState = WAITING_CLIENT;
		}else{
			// load static items from level file
			ll.loadLevel(level,LevelLoader.MULTI_CLIENT);
			entFactory.makeWalls(cH,cW);
			entFactory.makeBin(cW);
			//now load dynamic stuff from TCP
			entFactory.updateWorldItems(client.lastUpdate);
		}
	}

	@Override
	public void doLogic(float delta) {
		
		if(!isHost && client.newUpdate){
			entFactory.updateWorldItems(client.lastUpdate);
			client.newUpdate = false;
		}else{
			if(host != null){
				WorldUpdate wu = new WorldUpdate(entFactory.getAllItems());
				host.sendLevel(wu);
			}
		}
		
		controlPad();
		
		controlClientsPad();
		
		debugFeatures();
		updateLazer(delta);
		updateBombs();
		world.step(delta / 4, 3, 3);
		updateBalls();
		updateMagnet();
		updatePowerUps();
		updateBricks();
		updateExplosions();
		updateObstacles();
		updatePortals();
		updateDrunk(delta);
		updateSlow(delta);
		updateSticky(delta);
		updateLevelState(delta);
		
		killItems();

		controller.ffive = false;
	}

	private void controlClientsPad() {
		if(!isHost){
			// send our pos to host if changed
			int curPadPos = (int)this.pad.body.getPosition().x;
			if(lastPadPos != curPadPos){
				client.sendMyPosition(curPadPos);
			}
		}
	}
	
	public void updatePadPos(PlayerUpdate pu){
		MultiPlayer p;
		if(playerMap.containsKey(pu.playerId)){
			p = playerMap.get(pu.playerId);
		}else{
			MultiPad mpad = entFactory.makeMultiPad(pu.playerXPos, 5);
			mpad.clientId = pu.playerId;
			p = new MultiPlayer();
			p.pad= mpad;
			playerMap.put(pu.playerId, p);
		}
		// set pad pos
		p.pad.setPosition(pu.playerXPos, 5);	
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

	public void playerAction(PlayerAction pa) {
		if(pa.playerAction == PLAYER_ACTION_MAG_PULLS){
			playerMap.get(pa.playerId).magPull = true;
		}else if(pa.playerAction == PLAYER_ACTION_MAG_PULLE){
			playerMap.get(pa.playerId).magPull = false;
		}else if(pa.playerAction == PLAYER_ACTION_MAG_PUSHE){
			playerMap.get(pa.playerId).magPush = true;
		}else if(pa.playerAction == PLAYER_ACTION_MAG_PUSHE){
			playerMap.get(pa.playerId).magPush = false;
		}
	}
	
	private void performMagPull(){
		
	}
	
	
}
