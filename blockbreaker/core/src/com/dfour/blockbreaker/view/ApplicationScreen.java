package com.dfour.blockbreaker.view;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.dfour.blockbreaker.BBModel;
import com.dfour.blockbreaker.BlockBreaker;
import com.dfour.blockbreaker.controller.AppController;
import com.dfour.blockbreaker.entity.Ball;
import com.dfour.blockbreaker.entity.Bomb;
import com.dfour.blockbreaker.entity.Brick;
import com.dfour.blockbreaker.entity.ExplosionParticle;
import com.dfour.blockbreaker.entity.LightBall;
import com.dfour.blockbreaker.entity.Obstacle;
import com.dfour.blockbreaker.entity.PowerUp;

public class ApplicationScreen implements Screen {
	private BlockBreaker parent;
	public BBModel bbModel;
	private AppController controller;
	private Box2DDebugRenderer debugRenderer;
	private Matrix4 debugMatrix;
	private OrthographicCamera cam;
	
	private RayHandler rayHandler;
	private SpriteBatch sb; 
	private SpriteBatch pb; 
	
	private int sw;
	private int sh;
	
	private boolean isPaused = false;
	private TextureRegion background;
	//private Texture visFont = parent.assMan.manager.get("font/visitor.png",Texture.class);
	private BitmapFont font;
	Texture secretCatImage;
	TextureRegion gameOver;
	
	ParticleEffect sparks = new ParticleEffect();
	ParticleEffect explosion = new ParticleEffect();
	ParticleEffect lazerEffect = new ParticleEffect();
	ParticleEffect pupEffect = new ParticleEffect();
	ParticleEffect lazerHitEffect = new ParticleEffect();
	private ParticleEffectPool partySparksPool;
	private ParticleEffectPool partyExplosionPool;
	private ParticleEffectPool partyLazerPool;
	private ParticleEffectPool partyHitLazerPool;
	private ParticleEffectPool partyPupPool;
	private Array<PooledEffect> effects = new Array<PooledEffect>();
	
	private Texture wall;
	private Texture expParty;
	
	//new lazor
	TextureRegion lazerStartBg;
	TextureRegion lazerStartOver;
	TextureRegion lazerMidBg;
	TextureRegion lazerMidOver;
	TextureRegion lazerEndBg ;
	TextureRegion lazerEndOver;
	TextureRegion lazerAllOver;
	float lazerOffset = 0;
	
	
	public ExtendViewport viewport;
	private ShapeRenderer sr;
	
	public static final int SPARK = 0;
	public static final int EXPLOSION = 1;
	public static final int POWERUP = 2;
	public static final int LAZER = 3;
	public static final int LAZER_HIT = 4;
	
	private float gameOverTimer = 5f;
	private TextureAtlas atlas;
	private TextureAtlas atlasGui;
	private TextureAtlas atlasLazor;
	
	private float fadeIn = 1f;
	//private float fadeOut = 1f;
	private float currentAlpha = 1f;
	
	
	public ApplicationScreen(BlockBreaker p) {
		parent = p;
		controller = new AppController();
		bbModel = new BBModel(controller,parent.assMan);
		
		loadImages();
		
		cam = new OrthographicCamera(800,600);
		cam.position.x = 400;
	    cam.position.y = 300;
	    cam.update();
		
		debugMatrix = new Matrix4(cam.combined);
		debugMatrix.scl(BBModel.BOX_TO_WORLD);
		viewport = new ExtendViewport(800, 600, cam);
		debugRenderer = new Box2DDebugRenderer(true,true,true,true, true, true);
		
		rayHandler = bbModel.rayHandler;
		rayHandler.setCombinedMatrix(debugMatrix, 40, 30, 80, 60);
	
		
		sr = new ShapeRenderer();
		sb = new SpriteBatch();
		pb = new SpriteBatch();
		
		createEffects();
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(controller);
		if(bbModel.gameOver){
			bbModel.level = 0;
		}
		bbModel.init();
		isPaused = false;
	}
	
	@Override
	public void resize(int width, int height) {
		sw = width;
		sh = height;
		bbModel.sw = width;
		bbModel.sh = height;
		pb.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
		viewport.update(width, height, false);
		debugMatrix = viewport.getCamera().combined.cpy();
		debugMatrix.scl(BBModel.BOX_TO_WORLD);
		bbModel.cam = cam;
	}

	@Override
	public void render(float delta) {
		//clear screen
		Gdx.gl.glClearColor(.4f, .4f, .4f, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    
	    if(!isPaused){
	    	bbModel.doLogic(delta);	
	    }
	    
	    if(fadeIn > 0){
	    	currentAlpha = 1 - fadeIn;
	    	fadeIn-=delta;
	    }
	    
	    addParticleEffects();
	    
		sb.setProjectionMatrix(cam.combined); // set SpriteBatch Matrix
		
		pb.begin();
			drawBrickBackground();			
		pb.end();
		
		// render Lighting
		rayHandler.setCombinedMatrix(debugMatrix, 40, 30, 80, 60);
		rayHandler.updateAndRender();
		
		
		sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		sb.begin();
			// draw walls
			sb.setColor(1,1,1,(0.3f * currentAlpha));
			sb.draw(wall, 0, 	10,	10,	580);
			sb.draw(wall, 0, 	0,	800	,10);
			sb.draw(wall, 0, 	590,800	,10);
			sb.draw(wall, 790, 	10,	10	,580);
			sb.setColor(1,1,1,currentAlpha);
			drawGameObjects(delta);
		sb.end();
		
		
		sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		sb.setColor(1,1,1,currentAlpha);
		sb.begin();
			drawEffects(delta);
			drawBombParticles(delta);
			fireLazors();
		sb.end();
		
		if(controller.isDebugMode){
			debugRenderer.render(bbModel.world, debugMatrix);
		}

		drawGuideLazors();

		pb.begin();
			font.draw(pb, "Score: "+bbModel.score+"00", 20 , sh - 20);
			font.draw(pb, "Magnet  Power: "+bbModel.magnetPower, 20 , sh - 30);
			font.draw(pb, "Magnet  Strength: "+bbModel.magnetStrength, 20 , sh - 40);
			font.draw(pb, "Mag Ball Next: "+bbModel.nextBallIsMag, 20 , sh - 50);
			if(bbModel.gameOver){
				this.doGameOverStuff(delta);
				pb.draw(this.gameOver, sw/2 -this.gameOver.getRegionWidth() / 2,sh/2 -this.gameOver.getRegionHeight() / 2);
				font.draw(pb, "Thanks for playing. Updates coming soon.", 100 , sh/2 - 150);
				font.draw(pb, "Visit gamedev.is-sweet.co.uk for more info.", 100 , sh/2 - 160);
				font.draw(pb, "Press Escape to return to the menu and try and beat your score of "+bbModel.score+"00", 100 , sh/2 - 170);
			}
		pb.end();
		
		if(bbModel.showShop){
			bbModel.showShop = false;
			parent.changeScreen(BlockBreaker.SHOP);
		}
		
		if(controller.getEscape()){
			controller.setEscape(false);
			this.parent.changeScreen(BlockBreaker.MENU);
			bbModel.empty();
		}
	}
	
	@Override
	public void pause() {
		isPaused = true;
	}

	@Override
	public void resume() {
		isPaused = false;
	}

	@Override
	public void hide() {
		isPaused = true;
	}

	@Override
	public void dispose() {
		rayHandler.dispose();
		debugRenderer.dispose();
		sb.disableBlending();
		sr.dispose();
		pb.dispose();
	}
	
	private void drawBrickBackground() {
		for(int i = 0; i < this.sw; i += 20){
			for(int j = 0; j < this.sh; j+= 10){
				pb.draw(background, i, j,20,10);
			}
		}
	}

	private void drawGuideLazors() {
		if(bbModel.isGuideLazerOn){
			sr.setProjectionMatrix(debugMatrix); // set ShapeRenderer Matrix
			sr.begin(ShapeType.Line);
				sr.setColor(new Color(0.2f,.7f,.2f,(.5f* currentAlpha)));
				for(Ball ball: bbModel.entFactory.balls){
					Vector2 goingto = new Vector2();
					goingto.x = ball.body.getPosition().x + (ball.body.getLinearVelocity().x) /10;
					goingto.y = ball.body.getPosition().y + (ball.body.getLinearVelocity().y) /10;
					sr.line(ball.body.getPosition().lerp(goingto, 0.005f), goingto);
				}
			sr.end();
		}
	}

	private void drawGameObjects(float delta) {
		for(Brick brick :bbModel.entFactory.bricks ){
			brick.sprite.draw(sb,currentAlpha);
		}
		for(LightBall lBalls :bbModel.entFactory.lightBalls ){
			lBalls.sprite.draw(sb,currentAlpha);
		}
		for(Ball ball :bbModel.entFactory.balls){
			ball.sprite.draw(sb,currentAlpha);
		}
		for(PowerUp pup :bbModel.entFactory.pups){
			pup.sprite.draw(sb,currentAlpha);
		}
		for(Bomb bomb :bbModel.entFactory.bombs){
			bomb.sprite.draw(sb,currentAlpha);
		}
		for(Obstacle obsstacle :bbModel.entFactory.obstacles){
			obsstacle.sprite.draw(sb,currentAlpha);
		}
		bbModel.pad.sprite.draw(sb,currentAlpha);
		bbModel.pad.drawAnimation(sb, delta);		
	}

	private void addParticleEffects() {
		for(Vector2 pos: bbModel.sparks){
			this.addPartyEffect(pos, SPARK);
			bbModel.sparks.removeValue(pos, true);
		}
	    
	    for(Vector2 pos: bbModel.explosions){
			this.addPartyEffect(pos, EXPLOSION);
			bbModel.explosions.removeValue(pos, true);
		}
	    
	    for(Vector2 pos: bbModel.pupExplosions){
			this.addPartyEffect(pos, POWERUP);
			bbModel.pupExplosions.removeValue(pos, true);
		}
		
	}

	private void fireLazors() {
		if(bbModel.isFiringLazer){
			sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
			float x = bbModel.pad.body.getPosition().x;
			float y = bbModel.pad.body.getPosition().y;
			
			float lazerLengthLeft = 6;
			float lazerLengthRight = 6;
			
			if(bbModel.lazerEndLeft != null){
				lazerLengthLeft = bbModel.lazerEndLeft.y - y - 6;
				this.addPartyEffect(bbModel.lazerEndLeft, LAZER_HIT);
			}
			if(bbModel.lazerEndRight != null){
				lazerLengthRight = bbModel.lazerEndRight.y - y - 6;
				this.addPartyEffect(bbModel.lazerEndRight, LAZER_HIT);
			}
			
			drawLazor(x-5,y,lazerLengthLeft);
			drawLazor(x+5,y,lazerLengthRight);
			
			lazerOffset+=5f;
			if(lazerOffset > 40){
				lazerOffset = 0;
			}
		}
	}

	private void drawBombParticles(float delta) {
		for(ExplosionParticle particle:bbModel.entFactory.explosionParticles){
			particle.lifeTime-= delta;
			sb.setColor(.2f,.2f,.9f,(particle.lifeTime / 3));
			sb.draw(expParty,
					particle.body.getPosition().x * BBModel.BOX_TO_WORLD,
					particle.body.getPosition().y * BBModel.BOX_TO_WORLD,
					3,3);

		}
	}

	private void drawEffects(float delta) {
		sb.setColor(1,1,1,currentAlpha);
		for (int i = effects.size - 1; i >= 0; i--) {
		    PooledEffect effect = effects.get(i);
		    effect.draw(sb, delta);
		    if (effect.isComplete()) {
		        effect.free();
		        effects.removeIndex(i);
		    }
		}
	}

	
	private void drawLazor(float x, float y, float length){
		if(controller.isMouse1Down()){
			sb.setColor(.2f,1f,.2f,currentAlpha);
		}else if(controller.isMouse2Down()){
			sb.setColor(1f,.2f,.2f,currentAlpha);
		}else{
			sb.setColor(.2f,.2f,1f,currentAlpha);
		}
		
		if(length < 0){
			length = 0;
		}
		length*= 10;
		
		float xpos = (x -2)*BBModel.BOX_TO_WORLD;
		float ypos = (y)*BBModel.BOX_TO_WORLD;
		// draw standard lazera
		sb.draw(lazerStartBg, 	xpos, ypos-10		,40	,40);
		sb.draw(lazerStartOver, xpos, ypos-10		,40	,40);
		sb.draw(lazerMidBg, 	xpos, ypos+30		,40	,length);
		sb.draw(lazerMidOver, 	xpos, ypos+30		,40	,length);
		sb.draw(lazerEndBg, 	xpos, ypos+length+30,40	,40);
		sb.draw(lazerEndOver, 	xpos, ypos+length+30,40	,40);
		
		
		sb.setColor(.4f,.4f,.4f,currentAlpha);
		sb.draw(lazerAllOver,xpos+5,ypos+10,30,this.lazerOffset);
		for(int i = 0; i < length/40; i++){
			sb.draw(lazerAllOver,xpos+5,ypos+(i*40)+this.lazerOffset,30,40);
		}	
	}
		
	private void addPartyEffect(Vector2 pos, int type){
		PooledEffect effect;
		switch(type){
		case SPARK:	 	effect = partySparksPool.obtain(); break;
		case EXPLOSION:	effect = partyExplosionPool.obtain(); break;
		case LAZER:		effect = partyLazerPool.obtain();break;
		case POWERUP:	effect = partyPupPool.obtain();break;
		case LAZER_HIT:	effect = partyHitLazerPool.obtain();break;
		default:	 	effect = partySparksPool.obtain(); break;
		}
		effect.setPosition(pos.x * BBModel.BOX_TO_WORLD, pos.y * BBModel.BOX_TO_WORLD);
		effects.add(effect);
	}
	
	private void doGameOverStuff(float delta) {
		float x = (float) Math.random() * 80;
		float y = (float) Math.random() * 60;
		this.addPartyEffect(new Vector2(x,y), (int)(Math.random()*3)+1);
		gameOverTimer-= delta;
		if(gameOverTimer < 0){
			parent.changeScreen(BlockBreaker.ENDGAME);
			gameOverTimer = 5f;
		}

	}
	

	/**
	 *  loads required images
	 */
	private void loadImages(){
		atlas = parent.assMan.manager.get("images/images.pack");
		atlasGui = parent.assMan.manager.get("gui/loadingGui.pack");
		atlasLazor = parent.assMan.manager.get("lazor/lazor.pack");
		
		background 		= atlasGui.findRegion("background");
		font 			= parent.assMan.manager.get("font/visitor.fnt", BitmapFont.class);
		gameOver 		= atlas.findRegion("gameover");
		lazerStartBg 	= atlasLazor.findRegion("lazorStart");
		lazerStartOver 	= atlasLazor.findRegion("lazorStartOver");
		lazerMidBg 		= atlasLazor.findRegion("laser");
		lazerMidOver 	= atlasLazor.findRegion("lazorOver");
		lazerEndBg 		= atlasLazor.findRegion("lazorEnd");
		lazerEndOver 	= atlasLazor.findRegion("lazorEndOver");
		lazerAllOver 	= atlasLazor.findRegion("lazorAllOver");
		
		Pixmap pmap = new Pixmap(10,10, Pixmap.Format.RGBA4444);
		pmap.setColor(1,1,1,0.5f);
		pmap.fill();
		wall = new Texture(pmap);
		
		pmap = new Pixmap(5,5, Pixmap.Format.RGBA4444);
		pmap.setColor(1,1,1,1);
		pmap.fillCircle(3, 3, 2);
		expParty = new Texture(pmap);
		pmap.dispose();	
	}
	
	private void createEffects(){
		// make effects
		sparks = parent.assMan.manager.get("particles/sparks.pe",ParticleEffect.class);
		explosion = parent.assMan.manager.get("particles/explosion.pe",ParticleEffect.class);
		lazerEffect = parent.assMan.manager.get("particles/lazer.pe",ParticleEffect.class);
		lazerHitEffect = parent.assMan.manager.get("particles/laserHitSparks.pe",ParticleEffect.class);
		pupEffect = parent.assMan.manager.get("particles/pupGetEffect.pe",ParticleEffect.class);
		// scale effects
		sparks.scaleEffect(1/2f);
		explosion.scaleEffect(1/4f);
		lazerEffect.scaleEffect(1/4f);
		lazerHitEffect.scaleEffect(1/3f);
		//create object pools
		partySparksPool = new ParticleEffectPool(sparks, 5, 20);
		partyExplosionPool = new ParticleEffectPool(explosion, 5, 20);
		partyLazerPool = new ParticleEffectPool(lazerEffect, 5, 20);
		partyHitLazerPool = new ParticleEffectPool(lazerHitEffect, 5, 20);
		partyPupPool = new ParticleEffectPool(pupEffect, 5, 20);
	}
}
