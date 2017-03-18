package com.dfour.blockbreaker;

import box2dLight.DirectionalLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.dfour.blockbreaker.controller.AppController;
import com.dfour.blockbreaker.entity.Ball;
import com.dfour.blockbreaker.entity.BlackHole;
import com.dfour.blockbreaker.entity.Bomb;
import com.dfour.blockbreaker.entity.Brick;
import com.dfour.blockbreaker.entity.EntityFactory;
import com.dfour.blockbreaker.entity.ExplosionParticle;
import com.dfour.blockbreaker.entity.LightBall;
import com.dfour.blockbreaker.entity.Pad;
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
	private LevelLoader ll;
	private AppController controller;
	public OrthographicCamera cam;
	
	private TextureAtlas atlas;
	
	private RayCastCallback callbackLeft;
	private RayCastCallback callbackRight;
	
	private int cH = 60;
	private int cW = 80;
	public int sw = 800;
	public int sh = 600;
	
	// Game objects, timers and counters
	public int magnetRechargeRate = 1;
	public int baseMagnetPower = 1000; // power at start of level
	public int magnetPower = 1000;
	public int baseMagnetStrength = 100; // power at start
	public int magnetStrength = 100;
	public int cash = 500;
	public int score = 0;
	public int level = 0;
	public int livesLeft = 3;	// initial lives
	public int bombsLeft = 0;
	public boolean eternalMagBall = false;
	private static final int MAX_LEVELS = 28; //26;
	
	private float levelTimer;
	public float baseGuideLazerTimer = 10f;
	public float guideLazerTimer = 10f;
	public float baseLazerTimer = 5f;
	public float lazerTimer = 5f;
	private float padOffset = 0;
	
	public boolean needToAddBall = false;
	public boolean isFiringLazer = false;
	public boolean gameOver = false;
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
	
	private Vector3 lastMousePos;
	public Pad pad;
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
		
		lf.addDirectionalLight(-90, true, new Color(1f, 1f, 1f, .2f));
		
		entFactory = new EntityFactory(world,atlas,lf);
		
		pad = entFactory.makePad(5, 5);
		
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

	public void init() {
		empty();
		
		// update lf to use new raycount quality
		lf.rays = Math.round(LightFactory.RAYS_PER_LIGHT_HIGH * preferences.getLightingQuality()) + 6;
		lf.updatePools();
		
		lf.addStaticPointLight(2, 1, new Color(1f, 0f, 0f, 1f));
		lf.addStaticPointLight(79, 1, new Color(0f, 1f, 1f, 1f));
		lf.addStaticPointLight(2, 59, new Color(0f, 0f, 1f, 1f));
		lf.addStaticPointLight(79, 59, new Color(0f, 1f, 0f, 1f));
		
		// TODO verify this
		if(this.level == 0){
			//reset base stats
			baseMagnetPower = 1000;
			baseMagnetStrength = 100;
			livesLeft = 3;
			bombsLeft = 0;
			baseGuideLazerTimer = 10f;
			baseLazerTimer = 5f;
			cash = 500000;
			magnetRechargeRate = 1;
			score = 0;
			eternalMagBall = false;
		}
		
		gameOver = false;
		changingLevel = false;
		entFactory.ballCount = 0;
		isFiringLazer = false;
		lazerTimer = 5f;
		entFactory.makeBall(eternalMagBall);
		if(BlockBreaker.isCustomMapMode){
			if(level < BlockBreaker.customMaps.size){
				ll.loadLevelFile(BlockBreaker.customMaps.get(level).path(), false);
			}else{
				gameOver = true;
			}
		}else{
			ll.loadLevel(level);
		}
		entFactory.makeWalls(cH,cW);
		entFactory.makeBin(cW);
		
		// reset to base
		magnetPower = baseMagnetPower;
		magnetStrength = baseMagnetStrength;
		lazerTimer = baseLazerTimer;
		guideLazerTimer = baseGuideLazerTimer;
		
	}

	public void doLogic(float delta) {
		// TODO check pad offset stuff
		if(controller.getLeft()){
			Gdx.input.setCursorPosition(sw/2,300);
			controller.overrideMouseLocation(sw/2,300);
			padOffset-=12f;
			if(padOffset < -335){
				padOffset = -335;
			}
		}else if(controller.getRight()){
			Gdx.input.setCursorPosition(sw/2,300);
			controller.overrideMouseLocation(sw/2,300);
			padOffset+=12f;
			if(padOffset > 335){
				padOffset = 335;
			}
		}
		if(BlockBreaker.debug){
			//System.out.println("Pad Offset is :"+padOffset);
		}
		Vector3 mousePosition = cam.unproject(new Vector3(controller.getMousePosition(), 0));
		if(lastMousePos != null){
			if(!mousePosition.equals(lastMousePos)){
				padOffset = 0;
			}
		}
		float screenx = MathUtils.clamp((mousePosition.x + padOffset) * WORLD_TO_BOX, 6.5f, 73.5f);
		pad.setPosition(MathUtils.lerp(pad.body.getPosition().x, screenx, 0.1f), 5);
		
		lastMousePos = mousePosition.cpy();
		
		debugFeatures();
		updateLazer(delta);
		updateBombs();
		world.step(delta / 5, 3, 3);
		updateBalls();
		updateMagnet();
		updatePowerUps();
		updateBricks();
		updateExplosions();
		updateObstacles();
		
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
				} else {
					if(!gameOver){
						this.showShop = true;
					}
				}
			}else{
				levelTimer-= delta;
			}
		}
		
		controller.ffive = false;
	}

	private void updateObstacles() {
		for (Spinner spinner : entFactory.spinners){
			spinner.update();
		}
		
		for (BlackHole blackHole : entFactory.blackHoles){
			blackHole.update();
		}
		
	}

	private void updateExplosions() {
		for (ExplosionParticle party : entFactory.explosionParticles) {
			if (Math.abs(party.body.getLinearVelocity().x) < 15f
					&& Math.abs(party.body.getLinearVelocity().y) < 15f) {
				world.destroyBody(party.body);
				entFactory.explosionParticles.removeValue(party, true);
			}
		}
	}

	private void updateBricks() {
		for (Brick brick : entFactory.bricks) {
			brick.update();
			
			if (controller.ffive) {
				brick.isDead = true;
			}
			
			if (brick.isStatic == true && brick.wasHit) {
				brick.body.setType(BodyType.DynamicBody);
				brick.isStatic = false;
				int rup = (int) (Math.random() * 100);
				if (rup > 50 || brick instanceof PowerBrick) {
					entFactory.createNewPowerUp(brick.body.getPosition());
				}
			}

			if (brick.isStatic == false) {
				if ((controller.isMouse1Down() || controller.isMouse2Down())) {
					this.applyMagnetism(brick.body, pad.body.getPosition());
				}
			}

			if (brick.isDead) {
				if (brick.wasEatenByPad) {
					score += 2;
					cash+=1;
				}
				this.explosions.add(brick.body.getPosition());
				playSound(EXPLOSION_SOUND);
				world.destroyBody(brick.body);
				entFactory.bricks.removeValue(brick, true);
			}
		}		
	}

	private void updatePowerUps() {
		for (PowerUp pup : entFactory.pups) {
			pup.update();
			if (pup.isDead) {
				pupExplosions.add(pup.body.getPosition());
				world.destroyBody(pup.body);
				entFactory.pups.removeValue(pup, true);
			} else {
				if ((controller.isMouse1Down() || controller.isMouse2Down())
						&& magnetPower > 0) {
					this.applyMagnetism(pup.body, pad.body.getPosition());
				}
			}
		}
	}

	private void updateMagnet() {
		if (controller.isMouse1Down() || controller.isMouse2Down()|| controller.getPush() || controller.getPull()) {
			this.magnetPower = magnetPower - (this.magnetStrength / 35);
			if (magnetPower < 0) {
				magnetPower = 0;
			}
		} else {
			magnetPower+= magnetRechargeRate;
			if (magnetPower > this.baseMagnetPower) {
				magnetPower = this.baseMagnetPower;
			}
		}

		if ((controller.isMouse1Down() || controller.getPull()) && magnetPower > 0) {
			pad.setImagePull();
		} else if ((controller.isMouse2Down() || controller.getPush()) && magnetPower > 0) {
			pad.setImagePush();
		} else {
			pad.setImageNormal();
		}		
	}

	private void updateBalls() {
		
		// add balls
		if (this.needToAddBall) {
			if (nextBallIsMag || eternalMagBall) {
				entFactory.makeBall(true);
				nextBallIsMag = false;
			}else{
				entFactory.makeBall(false);
			}
			this.needToAddBall = false;
		}
		
		
		for (Ball ball : entFactory.balls) {
			if (ball.isAttached) {
				ball.body.setTransform(pad.body.getPosition().x, 7, 0); 
				ball.body.setLinearVelocity(0, 0);
				if (controller.isMouse1Down()) {
					ball.isAttached = false; // release ball
					float x = (float) ((Math.random() * 10) - 5);
					// System.out.println("X ball i s"+x);
					ball.body.applyForceToCenter(x, 10, true); 
				}
			}
			ball.update();
			if ((controller.isMouse1Down() || controller.isMouse2Down())
					&& magnetPower > 0 && ball.isMagBall) {
				this.applyMagnetism(ball.body, pad.body.getPosition());
			}
			if (ball.isDead) { // dead ball, check count
				if (entFactory.ballCount > 1) { // have spare ball so destroy
					ball.light.setActive(false);
					world.destroyBody(ball.body);
					entFactory.balls.removeValue(ball, true);
					entFactory.ballCount--;
				} else { // last ball, attach to pad
					if(this.livesLeft > 0){
						ball.isAttached = true;
						if(!eternalMagBall){
							ball.setNormalBall(atlas.findRegion("ball"));
						}
						ball.isDead = false;
						this.livesLeft-=1;
					}else{
						gameOver = true;
					}
					if (!gameOver) {
						score -= 10; // remove 10 for lost ball
					}
				}
			}
		}		
	}

	private void updateLazer(float delta) {
		//lazer a brick
		if (brickLazoredLeft != null) {
			brickLazoredLeft.brickHealth -= 1;
		}
		if (brickLazoredRight != null) {
			brickLazoredRight.brickHealth -= 1;
		}
		
		// GUIDE LAZER
		if (isGuideLazerOn) {
			if (guideLazerTimer > 0) {
				guideLazerTimer -= delta;
			} else {
				isGuideLazerOn = false;
				guideLazerTimer = 10f;
			}
		}
		//						  _
		// IMA FIRING MA LAZER 0o//________________________________/////
		//						||======================================<<<<<
		//						 \\''''''''''''''''''''''''''''''''\\\\\
		if (isFiringLazer) {
			if (lazerTimer > 0) {
				lazerTimer -= delta;
				pad.lazLightLeft.setActive(true);
				pad.lazLightRight.setActive(true);
			} else {
				isFiringLazer = false;
				lazerTimer = 5f;
			}
			
			world.rayCast(
					callbackLeft,
					new Vector2(pad.body.getPosition().x - 5, pad.body
							.getPosition().y),
					new Vector2(pad.body.getPosition().x - 5, pad.body
							.getPosition().y + 55));
			world.rayCast(
					callbackRight,
					new Vector2(pad.body.getPosition().x + 5, pad.body
							.getPosition().y),
					new Vector2(pad.body.getPosition().x + 5, pad.body
							.getPosition().y + 55));
		}else {
			pad.lazLightLeft.setActive(false);
			pad.lazLightRight.setActive(false);
		}
	}

	private void updateBombs() {
		
		// TODO implement limit to bomb count per second to stop all bombs being released at once
		// B key to init bomb
		if (controller.useBomb) {
			controller.useBomb = false;
			if(this.bombsLeft > 0){
				this.bombsLeft -= 1;
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
				bombsToExplode.add(bomb.body.getPosition());
				playSound(EXPLOSION_SOUND);
				world.destroyBody(bomb.body);
				entFactory.bombs.removeValue(bomb, true);
			}
		}
		
		//exploding bombs
		if (bombsToExplode.size > 0) {
			for (Vector2 vector : bombsToExplode) {
				entFactory.addExplosionParticles(vector);
				explosions.add(vector);
				bombsToExplode.removeValue(vector, true);
			}
		}
	}

	private void debugFeatures() {
		// debug lazer
		if (BlockBreaker.debug) {
			this.isFiringLazer = controller.isMouse3Down() ? true : false;
		}

		// debug add brick
		if (controller.ffour) {
			// System.out.println("making a brick");
			entFactory.makeBrick(10, 10);
			controller.ffour = false;
		}
		
		if (controller.fsix) {
			controller.fsix = false;
			dispenseBomb();
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

	private void applyMagnetism(Body body, Vector2 vector) {
		float velx = vector.x - body.getPosition().x;
		float vely = vector.y - body.getPosition().y;
		float length = (float) Math.sqrt(velx * velx + vely * vely);
		if (length != 0) {
			velx = velx / length;
			vely = vely / length;
		}
		if (magnetPower > 0) {
			if (controller.isMouse1Down()) {
				body.applyForceToCenter(new Vector2(velx * magnetStrength, vely
						* magnetStrength), true);
			} else {
				body.applyForceToCenter(new Vector2(velx * -magnetStrength,
						vely * -magnetStrength), true);
			}
		}
	}

	public void getMagPowerUp() {
		this.magnetPower += 1000;
	}

	public void getExtraBall(boolean mag) {
		if (mag) {
			this.nextBallIsMag = true;
		}
		needToAddBall = true;
	}

	public void getMagStrengthPowerUP() {
		if (this.magnetStrength < 1000) {
			this.magnetStrength += 50;
		}
	}

	public void getLazerPowerUp() {
		this.isFiringLazer = true;
		this.lazerTimer += 4f;
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
		this.bombsLeft += 1;
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
		if(this.cash - cost >= 0){
			this.cash -= cost;
			return true;
		}else{
			return false;
		}
	}

	public void addCash(int cashAmount) {
		this.cash+=cashAmount;	
	}
}
