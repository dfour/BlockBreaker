package com.dfour.blockbreaker.entity;

import box2dLight.PointLight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.dfour.blockbreaker.BodyFactory;
import com.dfour.blockbreaker.LightFactory;

public class EntityFactory {
	
	private BodyFactory bodyFactory;
	private TextureAtlas atlas;
	private Pad pad;
	private LightFactory lf;
	private World world;
	public Array<Ball> balls = new Array<Ball>();
	public Array<Bomb> bombs = new Array<Bomb>();
	public Array<Brick> bricks = new Array<Brick>();
	public Array<LightBall> lightBalls = new Array<LightBall>();
	public Array<ExplosionParticle> explosionParticles = new Array<ExplosionParticle>();
	public Array<PowerUp> pups = new Array<PowerUp>();
	public Array<Obstacle> obstacles = new Array<Obstacle>();
	public Array<Body> walls = new Array<Body>();
	public Array<Spinner> spinners = new Array<Spinner>();
	public Array<LocalEffectEntity> localEffectEntities = new Array<LocalEffectEntity>();
	public Array<Portal> portals = new Array<Portal>();
	
	private Portal[] preLinkedPortals = new Portal[10];
	private int currentPortlaCount = 0;
	
	
	public int ballCount;
	
	// object sizes
	// pad 		10 x 1.1
	// ball 	1
	// light 	1.2
	// brick 	1.9 x  0.9 (non textured)
	// obstacle 2.4 x 2
	// spinner 	0.5 x 3
	// bhole	2
	// portal	2
	// szone	40
	// bomb		1
	// powerup	2
	
	
	public EntityFactory(World world, TextureAtlas atlas, LightFactory lf){
		this.world = world;
		bodyFactory = BodyFactory.getInstance(world);
		this.atlas = atlas;
		this.lf = lf;
	}
	
	
	public Pad makePad(float x, float y){
		
		
		// old pad
		//Body padBody = bodyFactory.makeBoxPolyBody(x, y, // location
		//		10, 0.5f, // size
		//		BodyFactory.RUBBER, BodyType.KinematicBody);
		
		//new pad
		
		Vector2[] verts = new Vector2[8];
		verts[0] = new Vector2(-5,-0.5f); 
		verts[1] = new Vector2(-5,0.2f);
		verts[2] = new Vector2(-4,0.4f);
		verts[3] = new Vector2(-2,0.6f);
		verts[4] = new Vector2(2,0.6f);
		verts[5] = new Vector2(4,0.4f);
		verts[6] = new Vector2(5,0.2f);
		verts[7] = new Vector2(5,-0.5f);
		
		Body padBody = bodyFactory.makePolygonShapeBody(verts, x, y,  BodyFactory.RUBBER, BodyType.KinematicBody);
		
		
		
		
		bodyFactory.addCircleFixture(padBody, -5, 0, 0.5f, BodyFactory.RUBBER);
		bodyFactory.addCircleFixture(padBody, 5, 0, 0.5f, BodyFactory.RUBBER);
		pad = new Pad(padBody, atlas.createSprite("paddel"),
				atlas.createSprite("paddel-magnet-pull"),
				atlas.createSprite("paddel-magnet-push"), new Animation(0.05f,
						atlas.findRegions("hover")));
		padBody.setUserData(pad);
		
		pad.lazLightLeft = lf.addChainLight(new float[] { -5.5f, 0, -5, 0, -4.5f, 0 });
		pad.lazLightRight = lf.addChainLight(new float[] { 4.5f, 0, 5, 0, 5.5f, 0 });
		pad.lazLightLeft.attachToBody(pad.body);
		pad.lazLightRight.attachToBody(pad.body);
		pad.lazLightLeft.setActive(false);
		pad.lazLightRight.setActive(false);
		
		return pad;
	}
	
	public void makeWalls(float cH, float cW) {
		Body bod;
		// left
		bod = bodyFactory.makeBoxPolyBody(0.5f, (cH / 2), 1, cH - 2,
				BodyFactory.RUBBER, BodyType.StaticBody);
		walls.add(bod);
		bodyFactory.setAllFixtureMask(bod, (short) -1);
		// bottom
		bod = bodyFactory.makeBoxPolyBody((cW / 2), -0.5f, cW, 1,
				BodyFactory.RUBBER, BodyType.StaticBody);
		walls.add(bod);
		bodyFactory.setAllFixtureMask(bod, (short) -1);
		// top
		bod = bodyFactory.makeBoxPolyBody((cW / 2), cH - 0.5f, cW, 1,
				BodyFactory.RUBBER, BodyType.StaticBody);
		walls.add(bod);
		bodyFactory.setAllFixtureMask(bod, (short) -1);
		// right
		bod = bodyFactory.makeBoxPolyBody(cW - 0.5f, (cH / 2), 1, cH - 2,
				BodyFactory.RUBBER, BodyType.StaticBody);
		walls.add(bod);
		bodyFactory.setAllFixtureMask(bod, (short) -1);
	}
	
	public Bin makeBin(float cW){
		Body bod = bodyFactory.makeBoxPolyBody((cW / 2), -1f, cW, 3,
				BodyFactory.RUBBER, BodyType.StaticBody);
		Bin bin = new Bin(bod);
		bod.setUserData(bin);
		bodyFactory.makeAllFixturesSensors(bod);
		return bin;
	}
	
	public Ball makeBall(boolean isMag){
		ballCount+=1;
		Body ballBody = bodyFactory.makeCirclePolyBody(32, 15, 0.5f,
				BodyFactory.RUBBER, BodyType.DynamicBody);
		Ball ball = new Ball(ballBody, new Animation(0.1f,atlas.findRegions("ballanim")));
		ballBody.setUserData(ball);
		ballBody.setGravityScale(0);
		ballBody.setLinearDamping(0);
		ballBody.setLinearVelocity(5, -20);
		PointLight light = lf.addPointLight(ballBody, new Color(.5f, .5f, 1, 0.5f));
		ball.light = light;
		ball.light.setDistance(LightFactory.LIGHT_SIZE_LOW);
		if(isMag) {
			ball.setMagBall(new Animation(0.1f,atlas.findRegions("magballanim")));
		}
		balls.add(ball);
		return ball;
	}
	
	public LightBall makeLight(float x, float y){
		x+=1f;
		Body lightBody = bodyFactory.makeCirclePolyBody(x, y, // location
				0.6f, // size
				BodyFactory.RUBBER, BodyType.StaticBody);
		LightBall lBall = new LightBall(lightBody,
				atlas.findRegion("lightBulb"));
		lBall.light = lf.addPointLight(x, y);
		lBall.light.setDistance(LightFactory.LIGHT_SIZE_LOW);
		lightBalls.add(lBall);
		return lBall;
	}
	
	private LightBall makeLight(float x, float y, Color col){
		x+=1f;
		Body lightBody = bodyFactory.makeCirclePolyBody(x, y, // location
				0.6f, // size
				BodyFactory.RUBBER, BodyType.StaticBody);
		LightBall lBall = new LightBall(lightBody,
				atlas.findRegion("lightBulb"),col);
		lBall.light = lf.addPointLight(x, y, col);
		lBall.light.setDistance(LightFactory.LIGHT_SIZE_LOW);
		lightBalls.add(lBall);
		return lBall;
	}
	
	public Brick makeBrick(float x, float y) {
		Body brickBody = bodyFactory.makeBoxPolyBody(x + 1f, y, 1.9f, 0.9f,
				BodyFactory.WOOD, BodyType.StaticBody);
		Brick brick = new Brick(brickBody);
		bricks.add(brick);
		brickBody.setUserData(brick);
		return brick;
	}
	
	public PowerBrick makePowerBrick(float x, float y) {
		Body brickBody = bodyFactory.makeBoxPolyBody(x + 1f, y, 1.9f, 0.9f,
				BodyFactory.WOOD, BodyType.StaticBody);
		PowerBrick pbrick = new PowerBrick(brickBody);
		bricks.add(pbrick);
		brickBody.setUserData(pbrick);
		return pbrick;
	}
	
	// makes a tilted square
	public Obstacle makeStaticObstacle(float x, float y, boolean flip){
		Vector2[] verts = new Vector2[4];
		if(!flip){
			verts[0] = new Vector2(-1.2f,-1f); 
			verts[1] = new Vector2(1.2f,1f);
			verts[2] = new Vector2(0.8f,1f);
			verts[3] = new Vector2(-0.8f,-1f);
		}else{
			verts[0] = new Vector2(-1.2f,1); 
			verts[1] = new Vector2(1.2f,-1);
			verts[2] = new Vector2(0.8f,-1);
			verts[3] = new Vector2(-0.8f,1);
		}
		Body obBody = bodyFactory.makePolygonShapeBody(verts, x+1, y,  BodyFactory.ICE, BodyType.StaticBody);
		Obstacle obstacle = new Obstacle(obBody, atlas.findRegion("obstacle"), flip);
		obstacles.add(obstacle);
		return obstacle;
	}
	
	public Spinner addSpinner(float x, float y, boolean clockwise){
		// plus sign in verctor coords
		Vector2[] verts = new Vector2[4];
		verts[0]  = new Vector2(-0.25f,-1.5f); 
		verts[1]  = new Vector2(-0.25f, 1.5f); 
		verts[2]  = new Vector2(0.25f, 1.5f); 
		verts[3]  = new Vector2(0.25f, -1.5f); 
		
		Body spBody = bodyFactory.makePolygonShapeBody(verts, x, y, BodyFactory.WOOD, BodyType.KinematicBody);
		//TODO add second spinner fixture to body here
		Spinner spinny = new Spinner(spBody, atlas.findRegion("spinner"), clockwise);
		spinners.add(spinny);
		return spinny;
		
	}
	
	public BlackHole addBlackHole(int x, int y) {
		Body bhbod = bodyFactory.makeCirclePolyBody(x+1, y, 1, BodyFactory.WOOD, BodyType.StaticBody);
		bodyFactory.makeSensorFixture(bhbod, 10);
		BlackHole bh = new BlackHole(bhbod, atlas.findRegion("blackhole"));
		bhbod.setUserData(bh);
		bodyFactory.setAllFixtureMask(bhbod,(short) -1); // add filter to filter box2d light conacts
		localEffectEntities.add(bh);
		return bh;
	}
	
	public Portal makePortal(int x, int y){
		Body bod = bodyFactory.makeSensorBody(x, y, 1, BodyType.StaticBody);
		Portal p = new Portal(bod, atlas.findRegion("effectrange"));
		bod.setUserData(p);
		preLinkedPortals[currentPortlaCount] = p;
		currentPortlaCount++;
		return p;
	}
	
	
	/**
	 * Link Portals and add portals to array for model use
	 * @param p1 Portal to link an exit to
	 * @param p2 Portal that is the exit
	 */
	public void pairPortals(int p1, int p2){
		preLinkedPortals[p1].setExit(preLinkedPortals[p2]);
		portals.add(preLinkedPortals[p1]);
	}
	
	public SpeedZone makeSpeedZone(int x, int y) {
		makeLight(x, y, new Color(Color.BLUE));
		Body bhbod = bodyFactory.makeCirclePolyBody(x+1, y, 20, BodyFactory.WOOD, BodyType.KinematicBody);
		bodyFactory.makeAllFixturesSensors(bhbod);
		SpeedZone sz = new SpeedZone(bhbod, atlas.findRegion("effectrange"),2f);
		bhbod.setUserData(sz);
		bodyFactory.setAllFixtureMask(bhbod,(short) -1); // add filter to filter box2d light conacts
		localEffectEntities.add(sz);
		return sz;		
	}
	
	public SpeedZone makeSlowZone(int x, int y) {
		makeLight(x, y, new Color(Color.RED));
		Body bhbod = bodyFactory.makeCirclePolyBody(x+1, y, 20, BodyFactory.WOOD, BodyType.KinematicBody);
		bodyFactory.makeAllFixturesSensors(bhbod);
		SpeedZone sz = new SpeedZone(bhbod, atlas.findRegion("effectrange"),0.5f);
		bhbod.setUserData(sz);
		bodyFactory.setAllFixtureMask(bhbod,(short) -1); // add filter to filter box2d light conacts
		localEffectEntities.add(sz);
		return sz;		
	}
	
	public Bomb addBomb() {
		Body bombBody = bodyFactory.makeCirclePolyBody(
				pad.body.getPosition().x, pad.body.getPosition().y + 5, // location
				0.5f, // size
				BodyFactory.WOOD, BodyType.DynamicBody);
		Bomb bomb = new Bomb(bombBody, atlas.findRegion("bomb"));
		bombs.add(bomb);
		return bomb;
	}
	
	public void addExplosionParticles(Vector2 vector){
		int numRays = ExplosionParticle.NUMRAYS;
		// one to free static blocks
		for (int i = 0; i < numRays; i++) {
			float angle = ((i / (float) numRays) * 360)
					* MathUtils.degreesToRadians;
			Vector2 rayDir = new Vector2((float) Math.sin(angle),
					(float) Math.cos(angle));
			explosionParticles.add(new ExplosionParticle(world, vector, rayDir, 150));
		}
		// one to apply force after freeing
		for (int i = 0; i < numRays; i++) {
			float angle = ((i / (float) numRays) * 360)
					* MathUtils.degreesToRadians;
			Vector2 rayDir = new Vector2((float) Math.sin(angle),
					(float) Math.cos(angle));
			explosionParticles.add(new ExplosionParticle(world, vector, rayDir, 149));
		}
	}
	
	public PowerUp createNewPowerUp(Vector2 position) {
		Body pupBody = bodyFactory.makeCirclePolyBody(position.x, position.y,
				1f, BodyFactory.RUBBER, BodyType.DynamicBody);
		TextureRegion tex;
		int type = (int) (Math.random() * 15);
		if (type == PowerUp.MAG_POWER) {
			tex = atlas.findRegion("mag_power_pup");
		} else if (type == PowerUp.MAG_STRENGTH) {
			tex = atlas.findRegion("mag_strength_pup");
		} else if (type == PowerUp.BALL) {
			tex = atlas.findRegion("extra_ball_pup");
		} else if (type == PowerUp.MAG_BALL) {
			tex = atlas.findRegion("extra_mag_ball_pup");
		} else if (type == PowerUp.LAZER) {
			tex = atlas.findRegion("lazer_pup");
		} else if (type == PowerUp.SCORE) {
			tex = atlas.findRegion("score_pup");
		} else if (type == PowerUp.GUIDE_LAZER) {
			tex = atlas.findRegion("ball_lazer_pup");
		} else if (type == PowerUp.BOMB) {
			tex = atlas.findRegion("bomb_pup");
		} else if (type == PowerUp.CASH5) { //TODO add images
			tex = atlas.findRegion("cash5_pup");
		} else if (type == PowerUp.CASH10) {
			tex = atlas.findRegion("cash10_pup");
		} else if (type == PowerUp.CASH25) {
			tex = atlas.findRegion("cash25_pup");
		} else if (type == PowerUp.CASH100) {
			tex = atlas.findRegion("cash100_pup");
		}else if (type == PowerUp.SLOW) {
			tex = atlas.findRegion("slow_pup");
		}else if (type == PowerUp.DRUNK) {
			tex = atlas.findRegion("drunk_pup");
		} else if (type == PowerUp.STICKY) {
			tex = atlas.findRegion("sticky_pup");
		} else {
			tex = null;
		}

		PowerUp pup = new PowerUp(pupBody, tex, type);
		pup.body.setUserData(pup);
		pups.add(pup);
		
		return pup;
	}
	

	public void clear() {
		bricks.clear();
		balls.clear();
		pups.clear();
		walls.clear();
		lightBalls.clear();
		bombs.clear();
		obstacles.clear();
		explosionParticles.clear();
		spinners.clear();
		localEffectEntities.clear();
	}
}


















