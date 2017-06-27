package com.dfour.blockbreaker;

import box2dLight.DirectionalLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.dfour.blockbreaker.controller.AppController;
import com.dfour.blockbreaker.entity.Ball;
import com.dfour.blockbreaker.entity.Bomb;
import com.dfour.blockbreaker.entity.Brick;
import com.dfour.blockbreaker.entity.EntityFactory;
import com.dfour.blockbreaker.entity.ExplosionParticle;
import com.dfour.blockbreaker.entity.LightBall;
import com.dfour.blockbreaker.entity.LocalEffectEntity;
import com.dfour.blockbreaker.entity.Pad;
import com.dfour.blockbreaker.entity.Portal;
import com.dfour.blockbreaker.entity.PowerBrick;
import com.dfour.blockbreaker.entity.PowerUp;
import com.dfour.blockbreaker.entity.Spinner;
import com.dfour.blockbreaker.loaders.BBAssetManager;
import com.dfour.blockbreaker.loaders.LevelLoader;

/**
 * Game Model, controls all game aspects
 * 
 * @author darkd 
 */
public class BBModel {

	// Box2d and World conversion constants
	public static final float BOX_TO_WORLD = 10;
	public static final float WORLD_TO_BOX = 0.1f;
	
	// Box2d
	public World world = new World(new Vector2(0, -10), false);
	public EntityFactory entFactory;
	public RayHandler rayHandler;
	private LightFactory lf;
	
	private AppPreferences preferences;
	public BBAssetManager assMan;
	protected LevelLoader ll;
	public AppController controller;
	public OrthographicCamera cam;
	public Player lp;
	
	private TextureAtlas atlas;
	
	private RayCastCallback callbackLeft;
	private RayCastCallback callbackRight;
	
	protected int cH = 60;
	protected int cW = 80;
	public int sw = 800;
	public int sh = 600;
	
	// Game objects, timers and counters
	
	public int score = 0;
	public int level = 0;
	private static final int MAX_LEVELS = 28; //26;
	private float levelTimer;

	private float padOffset = 0;
	private float padSpeed = 12f;
	public boolean needToAddBall = false;
	
	public boolean gameOver = false;
	public boolean gameOverWin = false;
	public boolean showShop = false;
	public boolean nextBallIsMag = false;
	private boolean changingLevel = false;
	private boolean isAddingBomb = false;
	public boolean isGuideLazerOn = false;
	
	public Array<Vector2> bombsToExplode = new Array<Vector2>();
	public Array<Vector2> sparks = new Array<Vector2>();
	public Array<Vector2> explosions = new Array<Vector2>();
	public Array<Vector2> pupExplosions = new Array<Vector2>();
	public Array<Vector2> blackHolePE = new Array<Vector2>();
	public Array<Vector2> cashPEToShow = new Array<Vector2>();
	public Array<Vector2> magBallPEToShow = new Array<Vector2>(); 
	public Array<Vector2> bombFText= new Array<Vector2>(); 
	public Array<Vector2> ballFText= new Array<Vector2>(); 
	public Array<Vector2> guideFText= new Array<Vector2>(); 
	public Array<Vector2> laserFtext= new Array<Vector2>(); 
	public Array<Vector2> magPowerFText= new Array<Vector2>(); 
	public Array<Vector2> magStrFText= new Array<Vector2>();
	public Array<Vector2> scoreFText = new Array<Vector2>();
	public Array<Vector2> slowFText = new Array<Vector2>();
	public Array<Vector2> drunkFText = new Array<Vector2>();
	public Array<Vector2> stickyFText = new Array<Vector2>();
	public Array<Vector2> bombShader = new Array<Vector2>();
	
	//public Pad pad;
	public Brick brickLazoredLeft;
	public Brick brickLazoredRight;
	public Vector2 lazerEndLeft;
	public Vector2 lazerEndRight;
	public DirectionalLight bgLight = null;
	
	// game sounds
	public static final int EXPLOSION_SOUND = 0;
	public static final int BOING_SOUND = 1;
	public static final int PING_SOUND = 2;
	
	private Sound explosion;
	private Sound boing;
	private Sound ping;
	
	

	public BBModel(AppController cont, BBAssetManager ass) {
		lp = new Player();
		lp.isLocal = true;
		
		assMan = ass;
		
		explosion = assMan.manager.get("sounds/explosion.wav", Sound.class);
		boing = assMan.manager.get("sounds/boing.wav", Sound.class);
		ping = assMan.manager.get("sounds/ping.wav", Sound.class);
		atlas = assMan.manager.get("images/images.pack", TextureAtlas.class);
		
		controller = cont;
		preferences = new AppPreferences();
		world.setContactListener(new BBContactListener(this));
		ll = new LevelLoader("",this);

		
		RayHandler.useDiffuseLight(true);
		rayHandler = new RayHandler(world);
		rayHandler.setCulling(true);
		rayHandler.setAmbientLight(0.7f);
		
		lf = new LightFactory(rayHandler);
		lf.filter = (short) -1;
		lf.rays = Math.round(LightFactory.RAYS_PER_LIGHT_HIGH * preferences.getLightingQuality()) + 6;
		lf.size = LightFactory.LIGHT_SIZE_MID;
		lf.soft = LightFactory.LIGHT_SOFTNESS_HARD;
		// update lf to use new raycount quality
		lf.updatePools();
		
		lf.addDirectionalLight(-90, true, new Color(1f, 1f, 1f, .5f));
		
		entFactory = new EntityFactory(world,atlas,lf);
		
		createPad();
		
		callbackLeft = new RayCastCallback() {
			@Override
			public float reportRayFixture(Fixture fixture, Vector2 point,Vector2 normal, float fraction) {
				if (fixture.getBody().getUserData() instanceof Brick) {
					Brick brick = (Brick) fixture.getBody().getUserData();
					brickLazoredLeft = brick;
				} else if(fixture.isSensor()){
					//return 1f;
					return -1;
				}else {
				
					brickLazoredLeft = null;
				}
				lazerEndLeft = point.cpy();
				return fraction;
			}
		};

		callbackRight = new RayCastCallback() {
			@Override
			public float reportRayFixture(Fixture fixture, Vector2 point,Vector2 normal, float fraction) {
				if (fixture.getBody().getUserData() instanceof Brick) {
					Brick brick = (Brick) fixture.getBody().getUserData();
					brickLazoredRight = brick;
				} else if(fixture.isSensor()){
					//return 1f;
					return -1;
				} else {
					brickLazoredRight = null;
				}
				lazerEndRight = point.cpy();
				return fraction;
			}
		};
	}
	
	// method to allow multi to override pad creation
	protected void createPad(){
		lp.pad = entFactory.makePad(5, 5);
	}

	public void init() {
		empty();
		
		// update lf to use new raycount quality
		lf.rays = Math.round(LightFactory.RAYS_PER_LIGHT_HIGH * preferences.getLightingQuality()) + 6;
		lf.updatePools();
		
		lf.addStaticPointLight(2, 1, new Color(1f, 0f, 0f, 1f));
		lf.addStaticPointLight(79, 1, new Color(0f, 1f, 1f, 1f));
		lf.addStaticPointLight(2, 59, new Color(0f, 0f, 1f, 1f));
		lf.addStaticPointLight(79, 59, new Color(0f, 1f, 0f, 1f));
		
		if(this.level == 0){
			//reset base stats
			lp.baseMagnetPower = 1000;
			lp.baseMagnetStrength = 100;
			lp.livesLeft = 3;
			lp.bombsLeft = 0;
			lp.baseGuideLazerTimer = 10f;
			lp.baseLazerTimer = 5f;
			lp.cash = 50000000;
			lp.magnetRechargeRate = 1;
			score = 0;
			lp.eternalMagBall = false;
		}
		
		gameOver = false;
		gameOverWin = false;
		changingLevel = false;
		entFactory.ballCount = 0;
		lp.isFiringLazer = false;
		lp.lazerTimer = 5f;
		
		makeLevel();
		
		// reset to base
		lp.magnetPower = lp.baseMagnetPower;
		lp.magnetStrength = lp.baseMagnetStrength;
		lp.lazerTimer = lp.baseLazerTimer;
		lp.guideLazerTimer = lp.baseGuideLazerTimer;
		
	}
	
	protected void makeLevel(){
		entFactory.makeBall(lp.eternalMagBall);
		if(BlockBreaker.isCustomMapMode){
			if(level < BlockBreaker.customMaps.size){
				ll.loadLevelFile(BlockBreaker.customMaps.get(level).path(), false, true);
			}else{
				gameOver = true;
			}
		}else{
			ll.loadLevel(level);
		}
		entFactory.makeWalls(cH,cW);
		entFactory.makeBin(cW);
	}
	
	public void doLogic(float delta) {
		
		controlRespond();
		
		controlPad();
		debugFeatures();
		updateLazer(delta);
		updateBombs();
		world.step(delta / 4, 3, 3);
		updateBalls();
		updateMagnet(lp);
		updatePowerUps();
		updateBricks();
		updateExplosions();
		updateObstacles();
		updatePortals();
		updateDrunk(delta);
		updateSlow(delta);
		updateSticky(delta);
		updateLevelState(delta);

		controller.ffive = false;
	}

	// responds to controls from user
	protected void controlRespond() {
		lp.magPull = (controller.isMouse1Down() || controller.getPull())?true:false;
		lp.magPush = (controller.isMouse2Down() || controller.getPush())?true:false;
	}

	// controls game over and shop screen changes
	protected void updateLevelState(float delta) {
		if (entFactory.bricks.size < 1 && !changingLevel) {
			levelTimer = 1f;
			changingLevel = true;
		}
		if(changingLevel){
			if(levelTimer <= 0){
				level += 1;
				changingLevel = false;
				levelTimer = 1f;
				if (level > MAX_LEVELS || BlockBreaker.isCustomMapMode) {
					gameOver = true;
					gameOverWin = true;
				} else {
					if(!gameOver){
						this.showShop = true;
					}
				}
			}else{
				levelTimer-= delta;
			}
		}
		
	}

	// controls sticky pad 
	protected void updateSticky(float delta) {
		if(lp.pad.isStickyPad){
			lp.stickyTimer -= delta;
			if(lp.stickyTimer <= 0){
				lp.pad.isStickyPad = false;
				lp.stickyTimer = 30f;
			}
		}
	}

	//controls slow pad
	protected void updateSlow(float delta) {
		if(lp.isSlow){
			lp.slowTimer -= delta;
			if(lp.slowTimer <= 0){
				lp.isSlow = false;
				lp.slowTimer = 5f;
				padSpeed = 12f;
			}
		}
	}

	// controls drunk pad
	protected void updateDrunk(float delta) {
		if(lp.isDrunk){
			lp.drunkTimer -= delta;
			if(lp.drunkTimer <= 0){
				lp.isDrunk = false;
				lp.drunkTimer = 5f;
			}
		}
	}

	
	/**
	 * controls local pad using mouse location
	 */
	protected void controlPad() {
		// move pad
		if(lp.isDrunk){
			padOffset -= (controller.getMousePosition().x - 400);
		}else{
			padOffset += (controller.getMousePosition().x - 400);
		}
		// set mouse pos
		Gdx.input.setCursorPosition(400,350);
		controller.getMousePosition().x = 400; 
		
		// move pad for keyboard
		if(controller.getLeft()){
			if(lp.isDrunk){
				padOffset+=padSpeed;
			}else{
				padOffset-=padSpeed;
			}
		}else if(controller.getRight()){
			if(lp.isDrunk){
				padOffset-=padSpeed;
			}else{
				padOffset+=padSpeed;
			}
		}
		//limit pad
		padOffset = MathUtils.clamp(padOffset, 0, 800);
		if(BlockBreaker.debug){
			//System.out.println("Pad Offset is :"+padOffset);
		}
		
		//pad from screen to box2d
		float screenx = MathUtils.clamp(padOffset * WORLD_TO_BOX, 6.5f, 73.5f);
		if(lp.isSlow){
			lp.pad.setPosition(MathUtils.lerp(lp.pad.body.getPosition().x, screenx, 0.05f), 5);
		}else{
			lp.pad.setPosition(MathUtils.lerp(lp.pad.body.getPosition().x, screenx, 0.1f), 5);
		}
		
	}

	// updates Portals
	protected void updatePortals() {
		for(Portal portal:entFactory.portals){
			portal.update();
		}
		
	}

	// update obstacles
	protected void updateObstacles() {
		for (Spinner spinner : entFactory.spinners){
			spinner.update();
		}
		
		for (LocalEffectEntity lee : entFactory.localEffectEntities){
			lee.update();
		}
		
	}

	// updates spinners
	protected void updateExplosions() {
		for (ExplosionParticle party : entFactory.explosionParticles) {
			if (Math.abs(party.body.getLinearVelocity().x) < 15f
					&& Math.abs(party.body.getLinearVelocity().y) < 15f) {
				world.destroyBody(party.body);
				entFactory.explosionParticles.removeValue(party, true);
			}
		}
	}

	//updates bricks
	protected void updateBricks() {
		for (Brick brick : entFactory.bricks) {
			brick.update();
			
			if (controller.ffive) {
				brick.isDead = true;
			}
			
			if (brick.isStatic == true && brick.wasHit) {
				brick.body.setType(BodyType.DynamicBody);
				brick.isStatic = false;
				dispensePowerup(brick);
			}

			if (brick.isStatic == false) {
				this.applyMagnetism(brick.body);
			}

			if (brick.isDead) {
				deadBrick(brick);
				if (brick.wasEatenByPad) {
					score += 2;
					lp.cash+=1;
				}
				this.explosions.add(brick.body.getPosition());
				playSound(EXPLOSION_SOUND);
				world.destroyBody(brick.body);
				entFactory.bricks.removeValue(brick, true);
			}
		}		
	}
	

	protected void dispensePowerup(Brick brick){
		int rup = (int) (Math.random() * 100);
		if (rup > 50 || brick instanceof PowerBrick) {
			entFactory.createNewPowerUp(brick.body.getPosition());
		}
	}

	// update power ups
	protected void updatePowerUps() {
		for (PowerUp pup : entFactory.pups) {
			pup.update();
			if (pup.isDead) {
				this.deadPowerUp(pup);
				pupExplosions.add(pup.body.getPosition());
				world.destroyBody(pup.body);
				entFactory.pups.removeValue(pup, true);
			} else {
				this.applyMagnetism(pup.body);
			}
		}
	}

	
	/**
	 * update magnet power counter
	 * @param p1 Player to update
	 */
	protected void updateMagnet(Player pl) {
		if (pl.magPull || pl.magPush) {
			pl.magnetPower = pl.magnetPower - (pl.magnetStrength / 35);
			if (pl.magnetPower < 0) {
				pl.magnetPower = 0;
			}
		} else {
			pl.magnetPower+= pl.magnetRechargeRate;
			if (pl.magnetPower > pl.baseMagnetPower) {
				pl.magnetPower = pl.baseMagnetPower;
			}
		}

		if (pl.magPull && pl.magnetPower > 0) {
			pl.pad.setImagePull();
		} else if (pl.magPush && pl.magnetPower > 0) {
			pl.pad.setImagePush();
		} else {
			pl.pad.setImageNormal();
		}		
	}

	//update balls
	protected void updateBalls() {
		
		// add balls
		if (this.needToAddBall) {
			if (nextBallIsMag || lp.eternalMagBall) {
				entFactory.makeBall(true);
				nextBallIsMag = false;
			}else{
				entFactory.makeBall(false);
			}
			this.needToAddBall = false;
		}
		
		
		for (Ball ball : entFactory.balls) {
			if (ball.isAttached) {
				ball.body.setTransform(lp.pad.body.getPosition().x+ball.xOffset, lp.pad.body.getPosition().y+ball.yOffset, 0); 
				ball.body.setLinearVelocity(0, 0);
				if (controller.isMouse1Down() || controller.isReleaseDown) {
					ball.isAttached = false; // release ball
					float x = (float) ((Math.random() * 10) - 5);
					// System.out.println("X ball i s"+x);
					ball.body.applyForceToCenter(x, 10, true); 
					controller.isReleaseDown = false;
				}
			}
			ball.update();
			if(ball.isMagBall){
				this.applyMagnetism(ball.body);
			}
			if (ball.isDead) { // dead ball, check count
				if (entFactory.ballCount > 1) { // have spare ball so destroy
					ball.light.setActive(false);
					world.destroyBody(ball.body);
					entFactory.balls.removeValue(ball, true);
					entFactory.ballCount--;
				} else { // last ball, attach to pad
					if(lp.livesLeft > 0){
						ball.isAttached = true;
						if(!lp.eternalMagBall){
							ball.setNormalBall(new Animation(0.1f,atlas.findRegions("ballanim")));
						}
						ball.isDead = false;
						
						lp.livesLeft-=1;
					}else{
						gameOver = true;
					}
					if (!gameOver) {
						score -= 10; // remove 10 for lost ball
					}
				}
				if(ball.isDead){
					deadBall(ball);
				}
			}
		}		
	}

	//controls lazer and raycasting
	protected void updateLazer(float delta) {
		//lazer a brick
		if (brickLazoredLeft != null) {
			brickLazoredLeft.brickHealth -= 1;
		}
		if (brickLazoredRight != null) {
			brickLazoredRight.brickHealth -= 1;
		}
		
		// GUIDE LAZER
		if (isGuideLazerOn) {
			if (lp.guideLazerTimer > 0) {
				lp.guideLazerTimer -= delta;
			} else {
				isGuideLazerOn = false;
				lp.guideLazerTimer = 10f;
			}
		}
		//						  _
		// IMA FIRING MA LAZER 0o//________________________________/////
		//						||======================================<<<<<
		//						 \\''''''''''''''''''''''''''''''''\\\\\
		if (lp.isFiringLazer) {
			if (lp.lazerTimer > 0) {
				lp.lazerTimer -= delta;
				lp.pad.lazLightLeft.setActive(true);
				lp.pad.lazLightRight.setActive(true);
			} else {
				lp.isFiringLazer = false;
				lp.lazerTimer = lp.baseLazerTimer;
			}
			
			world.rayCast(
					callbackLeft,
					new Vector2(lp.pad.body.getPosition().x - 5, lp.pad.body
							.getPosition().y),
					new Vector2(lp.pad.body.getPosition().x - 5, lp.pad.body
							.getPosition().y + 55));
			world.rayCast(
					callbackRight,
					new Vector2(lp.pad.body.getPosition().x + 5, lp.pad.body
							.getPosition().y),
					new Vector2(lp.pad.body.getPosition().x + 5, lp.pad.body
							.getPosition().y + 55));
		}else {
			lp.pad.lazLightLeft.setActive(false);
			lp.pad.lazLightRight.setActive(false);
		}
	}

	//updates bombs
	protected void updateBombs() {
		
		// TODO implement limit to bomb count per second to stop all bombs being released at once
		// B key to init bomb
		if (controller.useBomb) {
			controller.useBomb = false;
			if(lp.bombsLeft > 0){
				lp.bombsLeft -= 1;
				dispenseBomb();
			}
		}
		
		// adding bombs
		if (isAddingBomb) {
			entFactory.addBomb();
			isAddingBomb = false;
		}
		
		// updatign bombs
		for (Bomb bomb : entFactory.bombs) {
			bomb.update();
			if (bomb.isDead) {
				this.deadBomb(bomb);
				bombsToExplode.add(bomb.body.getPosition());
				playSound(EXPLOSION_SOUND);
				world.destroyBody(bomb.body);
				entFactory.bombs.removeValue(bomb, true);
			}
		}
		
		//exploding bombs
		if (bombsToExplode.size > 0) {
			for (Vector2 vector : bombsToExplode) {
				bombShader.add(vector);
				entFactory.addExplosionParticles(vector);
				explosions.add(vector);
				bombsToExplode.removeValue(vector, true);
			}
		}
	}

	// debug stuff
	protected void debugFeatures() {
		// debug lazer
		if (BlockBreaker.debug) {
			lp.isFiringLazer = controller.isMouse3Down() ? true : false;
		}

		// debug add brick
		if (controller.ffour) {
			// System.out.println("making a brick");
			entFactory.makeBrick(10, 10);
			controller.ffour = false;
		}
		
		if(controller.fseven){
			controller.fseven = false;
			entFactory.createNewPowerUp(new Vector2(10,10));
		}
		
		if (controller.fsix) {
			controller.fsix = false;
			dispenseBomb();
		}
		
		// toggle box2d debug renderer
		if (controller.feight) {
			controller.feight = false;
			BlockBreaker.debug_b2d_render = !BlockBreaker.debug_b2d_render;
		}
		
		// toggle texture rendering
		if (controller.fnine) {
			controller.fnine = false;
			BlockBreaker.debug_texture_render = !BlockBreaker.debug_texture_render;
		}
		
		// toggle contact logging
		if (controller.ften) {
			controller.ften = false;
			BlockBreaker.debug_contact_log = !BlockBreaker.debug_contact_log;
		}
	}

	/**
	 * Play a game sound File
	 * 
	 * @param sound
	 *            BBModel.sound
	 */
	public void playSound(int sound) {
		if (preferences.isSoundEffectsEnabled()) {
			float vol = preferences.getSoundVolume();
			switch (sound) {
			case EXPLOSION_SOUND:
				explosion.play(vol);
				break;
			case BOING_SOUND:
				boing.play(vol);
				break;
			case PING_SOUND:
				ping.play(vol);
				break;
			}
		}
	}

	/**
	 * @param body
	 * @param vector
	 */
	protected void applyMagnetism(Body body) {
		if ((controller.isMouse1Down() || controller.isMouse2Down()|| controller.getPush() || controller.getPull())
				&& lp.magnetPower > 0) {
			float velx = lp.pad.body.getPosition().x - body.getPosition().x;
			float vely = lp.pad.body.getPosition().y - body.getPosition().y;
			float length = (float) Math.sqrt(velx * velx + vely * vely);
			if (length != 0) {
				velx = velx / length;
				vely = vely / length;
			}
			if (lp.magnetPower > 0) {
				if (controller.isMouse1Down() || controller.getPull()) {
					body.applyForceToCenter(new Vector2(velx * lp.magnetStrength, vely
							* lp.magnetStrength), true);
				} else {
					body.applyForceToCenter(new Vector2(velx * -lp.magnetStrength,
							vely * -lp.magnetStrength), true);
				}
			}
		}
	}

	public void getMagPowerUp() {
		lp.magnetPower += 1000;
	}

	public void getExtraBall(boolean mag) {
		if (mag) {
			this.nextBallIsMag = true;
		}
		needToAddBall = true;
	}

	public void getMagStrengthPowerUP() {
		if (lp.magnetStrength < 1000) {
			lp.magnetStrength += 50;
		}
	}

	public void getLazerPowerUp() {
		lp.isFiringLazer = true;
		lp.lazerTimer = lp.baseLazerTimer;
	}

	public void empty() {
		
		for(LightBall light:entFactory.lightBalls){
			lf.plp.free(light.light);
		}

		lf.clearStaticLights();
		
		for(Ball ball: entFactory.balls){
			lf.plp.free(ball.light);
		}	

		entFactory.clear();
		
		pupExplosions.clear();
		bombsToExplode.clear();
		sparks.clear();
		explosions.clear();
		
		
		Array<Body> bodies = new Array<Body>();
		world.getBodies(bodies);
		for (Body bod : bodies) {
			if(!(bod.getUserData() instanceof Pad)){
				world.destroyBody(bod);
			}
		}
	}

	public void addBombPowerUp() {
		lp.bombsLeft += 1;
	}
	
	public void dispenseBomb(){
		isAddingBomb = true;
	}

	public void addGuidLazer() {
		this.isGuideLazerOn = true;
	}

	/**
	 * @param bomb
	 */
	public void createBlast(Bomb bomb) {
		bomb.isDead = true;
	}

	public boolean removeCash(int cost) {
		if(lp.cash - cost >= 0){
			lp.cash -= cost;
			return true;
		}else{
			return false;
		}
	}

	public void addCash(int cashAmount) {
		lp.cash+=cashAmount;	
	}

	public void isDrunk() {
		lp.isDrunk = true;
	}

	public void isSlow() {
		lp.isSlow = true;
		padSpeed= 7f;
	}

	public void ballHitPad(Ball ball) {
		if(lp.pad.isStickyPad){
			ball.isAttached = true;
			ball.xOffset = ball.body.getPosition().x - lp.pad.body.getPosition().x;
			ball.yOffset = ball.body.getPosition().y - lp.pad.body.getPosition().y;
		}
	}

	public void isSticky() {
		lp.pad.isStickyPad = true;
	}
	
	public static final int EXTRA_BALL = 0;
	public static final int EXTRA_LAZER_TIME = 1;
	public static final int EXTRA_G_LAZER_TIME = 2;
	public static final int EXTRA_MAG_POWER = 3;
	public static final int EXTRA_MAG_STR = 4;
	public static final int EXTRA_MAG_CHARGE = 5;
	public static final int EXTRA_C_MAG_BALL = 6;
	
	// shop actions
	public int calcCost(int type){
		float mul =1;
		float mod = 1;
		int base = 10;
		
		switch(type){
		case EXTRA_BALL:mul = lp.livesLeft;
			mod = 2.5f;
			break;
		case EXTRA_LAZER_TIME:
			mul = lp.baseLazerTimer;
			mod = 5;
			break;
		case EXTRA_G_LAZER_TIME:
			mul = lp.baseGuideLazerTimer;
			mod = 2;
			break;
		case EXTRA_MAG_POWER:
			mul = lp.baseMagnetPower/100;
			mod = 2;
			base = 15;
			break;
		case EXTRA_MAG_STR:
			mul = lp.baseMagnetStrength/10;
			mod = 2;
			base = 15;
			break;
		case EXTRA_MAG_CHARGE:
			mul = lp.magnetRechargeRate*2500;
			mod = 1;
			base = 2500;
			break;
		case EXTRA_C_MAG_BALL:
			mul = 1;
			mod = 5000;
		}
		
		return (int) Math.pow(mul,mod) + base;
	}
	
	public boolean purchaseItem(int type){
		if(this.removeCash(calcCost(type))){
			switch (type){
				case EXTRA_BALL: 		lp.livesLeft+=1; 				break;
				case EXTRA_LAZER_TIME: 	lp.baseLazerTimer+=0.5f; 		break;
				case EXTRA_G_LAZER_TIME:lp.baseGuideLazerTimer+=0.5f;	break;
				case EXTRA_MAG_POWER: 	lp.baseMagnetPower+=100; 		break;
				case EXTRA_MAG_STR: 	lp.baseMagnetStrength+=50; 	break;
				case EXTRA_MAG_CHARGE: 	lp.magnetRechargeRate+=1;		break;
				case EXTRA_C_MAG_BALL: 	lp.eternalMagBall = true;		break;
			}
			return true;
		}
		return false;
	}

	
	
	
	
	// Multiplayer required methods
	protected void deadBrick(Brick brick) {}
	protected void deadPowerUp(PowerUp pup){}
	protected void deadBall(Ball ball){}
	protected void deadBomb(Bomb bomb){}
	public void sendPause(boolean isPaused) {}
	
	
	
}
