package com.dfour.blockbreaker.view;

import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Cursor.SystemCursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.dfour.blockbreaker.BBModel;
import com.dfour.blockbreaker.BBUtils;
import com.dfour.blockbreaker.BlockBreaker;
import com.dfour.blockbreaker.IconBar;
import com.dfour.blockbreaker.controller.AppController;
import com.dfour.blockbreaker.entity.Ball;
import com.dfour.blockbreaker.entity.Bomb;
import com.dfour.blockbreaker.entity.Brick;
import com.dfour.blockbreaker.entity.ExplosionParticle;
import com.dfour.blockbreaker.entity.LightBall;
import com.dfour.blockbreaker.entity.LocalEffectEntity;
import com.dfour.blockbreaker.entity.Obstacle;
import com.dfour.blockbreaker.entity.PowerUp;
import com.dfour.blockbreaker.entity.Spinner;

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
	private BitmapFont visfont;
	private TextureRegion gameOver;
	private TextureRegion gameOverwin;
	
	private ParticleEffect sparks;
	private ParticleEffect explosion;
	private ParticleEffect lazerEffect;
	private ParticleEffect pupEffect;
	private ParticleEffect lazerHitEffect;
	private ParticleEffect bhEffect;
	private ParticleEffect cashEffect;
	private ParticleEffect magBallEffect;
	private ParticleEffect bombpeEffect;
	private ParticleEffect ballPlusoneEffect;
	private ParticleEffect guideLaserEffect;
	private ParticleEffect laserPlusEffect;
	private ParticleEffect magPowerPlusEffect;
	private ParticleEffect magStrPlusEffect;
	private ParticleEffect scorePlusEffect;

	private ParticleEffectPool partySparksPool;
	private ParticleEffectPool partyExplosionPool;
	private ParticleEffectPool partyLazerPool;
	private ParticleEffectPool partyHitLazerPool;
	private ParticleEffectPool partyPupPool;
	private ParticleEffectPool partyBhPool;
	private ParticleEffectPool partyCashPool;
	private ParticleEffectPool partyMagBallPool;
	private ParticleEffectPool partyBombPool;
	private ParticleEffectPool partyBallPlusOnePool;
	private ParticleEffectPool partyGuideLaserPool;
	private ParticleEffectPool partyLaserPlussPool;
	private ParticleEffectPool partyMagPowerPool;
	private ParticleEffectPool partyMagStrPool;
	private ParticleEffectPool partyScorePlusPool;
	private Array<PooledEffect> effects = new Array<PooledEffect>();
	
	private Texture expParty;
	private TextureRegion leftWall;
	private TextureRegion bottomWall;
	
	//new lazor
	private TextureRegion lazerStartBg;
	private TextureRegion lazerStartOver;
	private TextureRegion lazerMidBg;
	private TextureRegion lazerMidOver;
	private TextureRegion lazerEndBg ;
	private TextureRegion lazerEndOver;
	private TextureRegion lazerAllOver;
	private float lazerOffset = 0;
	
	
	public ExtendViewport viewport;
	private ShapeRenderer sr;
	
	public static final int SPARK = 0;
	public static final int EXPLOSION = 1;
	public static final int POWERUP = 2;
	public static final int LAZER = 3;
	public static final int LAZER_HIT = 4;
	public static final int BLACK_HOLE = 5;
	// FLYING TEXT PES
	public static final int CASH_FTEXT = 6;
	public static final int MAGBALL_FTEXT = 7;
	public static final int BOMB_FTEXT = 8;
	public static final int BALL_FTEXT = 9;
	public static final int GUIDE_FTEXT = 10;
	public static final int LASER_FTEXT =11;
	public static final int MAGPOWER_FTEXT = 12;
	public static final int MAGSTR_FTEXT = 13;
	public static final int SCORE_FTEXT = 14;
	
	public static final int GHOST_TAIL = 15;
	public static final int MAGGHOST_TAIL = 16;
	
	private float gameOverTimer = 5f;
	private TextureAtlas atlas;
	private TextureAtlas atlasGui;
	private TextureAtlas atlasLazor;
	
	public float fadeIn = 1f;
	private float fadeOut = 1f;
	private float currentAlpha = 1f;
	private boolean isReturning;
	private int nextScreen;
	private ParticleEffect ghostTailEffect;
	private ParticleEffectPool partyGhostTailPool;
	private ParticleEffect magGhostTailEffect;
	private ParticleEffectPool partyMagGhostTailPool;
	private Stage stage;
	private Table displayTable;
	private Skin skin;
	private IconBar magnetBar;
	private IconBar laserBar;
	private Label scoreCountLabel;
	private Label cashCountLabel;
	private IconBar guideBar;
	private Label bombCountLabel;
	private Label magnetUnitsTotal;
	private Label laserCountLabel;
	private Label laserGuideCountLabel;
	private Label livesCountLabel;
	private Table pauseMenuTable;
	private InputMultiplexer imp;
	
	private Cursor inGame;
	private SystemCursor inMenu;
	
	
	public ApplicationScreen(BlockBreaker p) {
		parent = p;
		controller = new AppController(p);
		bbModel = new BBModel(controller,parent.assMan);
		
		loadImages();
		cam = new OrthographicCamera(800,600);
		cam.position.x = 400;
	    cam.position.y = 270;
	    cam.update();
		
		debugMatrix = new Matrix4(cam.combined);
		debugMatrix.scl(BBModel.BOX_TO_WORLD);
		viewport = new ExtendViewport(800, 660, cam);
		debugRenderer = new Box2DDebugRenderer(true,true,true,true,true,true);
		
		rayHandler = bbModel.rayHandler;
		rayHandler.setCombinedMatrix(debugMatrix, 40, 30, 80, 60);
	
		
		sr = new ShapeRenderer();
		sb = new SpriteBatch();
		pb = new SpriteBatch();
		
		createEffects();
		
		
		stage = new Stage(new ExtendViewport(800,660));
		displayTable = new Table();
		displayTable.setDebug(false);
		displayTable.setFillParent(true);
		skin = parent.assMan.manager.get("skin/bbskin.json",Skin.class);
		
		inGame = Gdx.graphics.newCursor(new Pixmap(1,1,Format.RGBA8888), 1, 1);
		inMenu = Cursor.SystemCursor.Arrow;
		
		NinePatchDrawable npd = new NinePatchDrawable(atlasGui.createPatch("darkblockbutton"));
		
		pauseMenuTable = new Table();
		pauseMenuTable.setDebug(true);
		pauseMenuTable.setBackground(npd);
		pauseMenuTable.setVisible(false);
		
		imp = new InputMultiplexer();
		imp.addProcessor(0, stage);
		imp.addProcessor(1,controller);
		
		TextButton btnResume = new TextButton("Resume Play",skin);
		btnResume.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				pauseMenuTable.setVisible(false);
				isPaused = false;
			}
		});
		
		TextButton btnQuit = new TextButton("Quit Level",skin);
		btnQuit.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				quitToMenu();
			}
		});
		
		
		
		pauseMenuTable.add(btnResume);
		pauseMenuTable.row();
		pauseMenuTable.add(btnQuit);
		
		
		
		Table buttonTable = new Table();
		buttonTable.setDebug(false);
		buttonTable.setWidth(800);
		buttonTable.setHeight(60);
		buttonTable.pad(10);
		buttonTable.setBackground(npd);
		
		Image magnetImage = new Image(atlas.findRegion("magnet"));
		Image laserImage = new Image(atlas.findRegion("lasericon"));
		Image laserGuideImage = new Image(atlas.findRegion("laserguideicon"));
		magnetBar = new IconBar(magnetImage,skin);
		laserBar = new IconBar(laserImage,skin);
		guideBar = new IconBar(laserGuideImage,skin);
		laserBar.updateProgress((bbModel.lazerTimer / bbModel.baseLazerTimer));
		Label scoreTextLabel = new Label("Score:",skin,"small");
		Label cashTextLabel = new Label ("Cash:",skin,"small");
		Label bombTextLabel = new Label ("Bombs:",skin,"small");
		Label livesTextLabel = new Label ("Lives Left: ",skin,"small");
		magnetUnitsTotal = new Label (bbModel.magnetPower+" / "+bbModel.baseMagnetPower,skin,"small");
		scoreCountLabel = new Label(""+bbModel.score,skin,"small");
		cashCountLabel = new Label("$"+bbModel.cash,skin,"small");
		bombCountLabel = new Label(""+bbModel.bombsLeft,skin,"small");
		laserCountLabel = new Label(""+bbModel.lazerTimer,skin,"small");
		laserGuideCountLabel = new Label(""+bbModel.guideLazerTimer,skin,"small");
		livesCountLabel = new Label(""+bbModel.livesLeft,skin,"small");
		
		bombCountLabel.setAlignment(Align.right);
		magnetUnitsTotal.setAlignment(Align.right);
		laserCountLabel.setAlignment(Align.right);
		laserGuideCountLabel.setAlignment(Align.right);
		livesTextLabel.setAlignment(Align.right);
		
		buttonTable.add(magnetBar).expandX().align(Align.left).padTop(5);
		buttonTable.add(magnetUnitsTotal).width(100).padTop(5);
		buttonTable.add(livesTextLabel).width(125).padTop(5);
		buttonTable.add(livesCountLabel).width(150).padTop(5);
		buttonTable.add(bombTextLabel).width(50).align(Align.left).padTop(5);
		buttonTable.add(bombCountLabel).width(50).align(Align.left).padTop(5);
		buttonTable.row();
		
		buttonTable.add(laserBar).expandX().align(Align.left);
		buttonTable.add(laserCountLabel);
		buttonTable.add();
		buttonTable.add().width(150);
		buttonTable.add(cashTextLabel).align(Align.left);
		buttonTable.add(cashCountLabel).align(Align.right);
		buttonTable.row();
		
		buttonTable.add(guideBar).expandX().align(Align.left);
		buttonTable.add(laserGuideCountLabel);
		buttonTable.add();
		buttonTable.add().width(150);
		buttonTable.add(scoreTextLabel).align(Align.left);
		buttonTable.add(scoreCountLabel).align(Align.right);
		
		
		displayTable.add(pauseMenuTable).width(800).height(600);
		displayTable.row();
		displayTable.add(buttonTable).center().height(60).width(800);
		
		
		
		stage.addActor(displayTable);
		
		
		
		
		
		
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(imp);
		if(bbModel.gameOver){
			bbModel.level = 0;
		}
		bbModel.init();
		isPaused = false;
		isReturning = false;
		Gdx.graphics.setCursor(inGame);
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
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void render(float delta) {
		//clear screen
		Gdx.gl.glClearColor(.4f, .4f, .4f, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    
	    if(controller.isPauseDown){
	    	isPaused = !isPaused;
	    	controller.isPauseDown = false;
	    	if(isPaused){
	    		pauseMenuTable.setVisible(true);
	    		Gdx.graphics.setSystemCursor(inMenu);
	    	}else{
	    		Gdx.graphics.setCursor(inGame);
	    	}
	    	
	    }
	    
	    
	    if(!isPaused){
	    	bbModel.doLogic(delta);
	    	magnetBar.updateProgress((bbModel.magnetPower/(float)bbModel.baseMagnetPower));
	    	laserBar.updateProgress((bbModel.lazerTimer / bbModel.baseLazerTimer));
	    	guideBar.updateProgress((bbModel.guideLazerTimer/ bbModel.baseGuideLazerTimer));
			magnetUnitsTotal.setText(bbModel.magnetPower+" / "+bbModel.baseMagnetPower+" @ "+(bbModel.magnetRechargeRate * 60));
	    	scoreCountLabel.setText(bbModel.score+"");
	    	cashCountLabel.setText("$"+bbModel.cash);
	    	bombCountLabel.setText(""+bbModel.bombsLeft);
	    	laserCountLabel.setText((int)bbModel.lazerTimer+" seconds");
	    	laserGuideCountLabel.setText((int)bbModel.guideLazerTimer+" seconds");
	    	livesCountLabel.setText(""+bbModel.livesLeft);
	    }
	    
	    if(fadeIn > 0){
	    	currentAlpha = 1 - fadeIn;
	    	fadeIn-=delta;
	    }else if(this.isReturning){
			fadeOut -= delta;
			currentAlpha = fadeOut;
			if(fadeOut <= 0){
				Gdx.graphics.setSystemCursor(inMenu);
				parent.changeScreen(nextScreen);
			}
		}else{
			currentAlpha = 1;  // set alpha to 1(fixes display bug when lagging during fade in)
		}
	    
	    if(!isPaused){
	    	addParticleEffects();
	    }
	    
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
			sb.setColor(1,1,1,(0.7f * currentAlpha));
			sb.draw(leftWall, 0, 	0,	10,	590);
			//sb.draw(wall, 0, 	0,	800	,10);
			sb.draw(bottomWall, 0, 	600,800	,-11);
			sb.draw(leftWall, 800, 	0,	-10	,590);
			sb.draw(bottomWall, 0, 0,800, -10000 );
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
		
		if(BlockBreaker.debug){
			debugRenderer.render(bbModel.world, debugMatrix);
		}

		drawGuideLazors();

		pb.begin();
			if(BlockBreaker.debug){
				visfont.draw(pb, "Score: "+bbModel.score+"00", 20 , sh - 20);
				visfont.draw(pb, "Lives :"+bbModel.livesLeft, 20, sh-30);
				visfont.draw(pb, "Magnet  Power: "+bbModel.magnetPower, 20 , sh - 40);
				visfont.draw(pb, "Magnet  Strength: "+bbModel.magnetStrength, 20 , sh - 50);
				visfont.draw(pb, "Mag Ball Next: "+bbModel.nextBallIsMag, 20 , sh - 60);
				visfont.draw(pb, "Cash $"+bbModel.cash,20,sh-70);
			}
			if(bbModel.gameOver){
				if(bbModel.gameOverWin){
					this.doGameOverStuff(delta, true);
					pb.draw(gameOverwin, sw/2 -gameOverwin.getRegionWidth() / 2,sh/2 -gameOverwin.getRegionHeight() / 2);
				}else{
					this.doGameOverStuff(delta, false);
					pb.draw(gameOver, sw/2 -gameOver.getRegionWidth() / 2,sh/2 -gameOver.getRegionHeight() / 2);
				}
			}
		pb.end();
		
		stage.act();
		stage.draw();
		
		if(bbModel.showShop){
			removeConstantPE();
			bbModel.showShop = false;
			nextScreen = BlockBreaker.SHOP;
			fadeOut = 1f;
			isReturning = true;
		}
		
		if(controller.getEscape() && this.isPaused){
			quitToMenu();
		}
	}
	
	private void quitToMenu(){
		removeConstantPE();
		controller.setEscape(false);
		nextScreen = BlockBreaker.MENU;
		isReturning = true;
		bbModel.level = 0;
		bbModel.empty();
	}
	
	private void removeConstantPE() {
		for (int i = effects.size - 1; i >= 0; i--) {
		    PooledEffect effect = effects.get(i);
		    effect.free();
		    effects.removeIndex(i); 
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
			brick.draw(sb,currentAlpha,delta);
		}
		for(LightBall lBalls :bbModel.entFactory.lightBalls ){
			lBalls.update();
			lBalls.draw(sb,currentAlpha,delta);
		}
		for(Ball ball :bbModel.entFactory.balls){
			ball.draw(sb,currentAlpha,delta);
		}
		for(PowerUp pup :bbModel.entFactory.pups){
			pup.draw(sb,currentAlpha,delta);
		}
		for(Bomb bomb :bbModel.entFactory.bombs){
			bomb.draw(sb,currentAlpha,delta);
		}
		for(Obstacle obsstacle :bbModel.entFactory.obstacles){
			obsstacle.update();
			obsstacle.draw(sb,currentAlpha,delta);
		}
		for(Spinner spinner :bbModel.entFactory.spinners){
			spinner.draw(sb,currentAlpha,delta);
		}
		for(LocalEffectEntity lee : bbModel.entFactory.localEffectEntities){
			lee.draw(sb,currentAlpha,delta);
			if(!lee.hasPartyEffect){
				bbModel.blackHolePE.add(lee.body.getPosition());
				lee.hasPartyEffect = true;
			}
		}
		
		bbModel.pad.sprite.draw(sb,currentAlpha);
		bbModel.pad.drawAnimation(sb, delta);		
	}

	private void addParticleEffects() {
		for(Ball ball :bbModel.entFactory.balls){
			if(ball.isMagBall){
				addPartyEffect(ball.body.getPosition(),MAGGHOST_TAIL);
			}else{
				addPartyEffect(ball.body.getPosition(),GHOST_TAIL);
			}
		}
	    addParticleEffectArray(bbModel.magPowerFText,MAGPOWER_FTEXT);
	    addParticleEffectArray(bbModel.laserFtext,LASER_FTEXT);
	    addParticleEffectArray(bbModel.guideFText,GUIDE_FTEXT);
	    addParticleEffectArray(bbModel.ballFText,BALL_FTEXT);
	    addParticleEffectArray(bbModel.bombFText,BOMB_FTEXT);
	    addParticleEffectArray(bbModel.magBallPEToShow,MAGBALL_FTEXT);
	    addParticleEffectArray(bbModel.cashPEToShow,CASH_FTEXT);
	    addParticleEffectArray(bbModel.blackHolePE,BLACK_HOLE);
	    addParticleEffectArray(bbModel.pupExplosions,POWERUP);
	    addParticleEffectArray(bbModel.explosions,EXPLOSION);
	    addParticleEffectArray(bbModel.sparks,SPARK);
	    addParticleEffectArray(bbModel.scoreFText,SCORE_FTEXT);
		
	}
	
	private void addParticleEffectArray(Array<Vector2> positions, int particleType){
		for(Vector2 pos: positions){
			this.addPartyEffect(pos, particleType);
			positions.removeValue(pos, true);
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
		    if(!isPaused){
		    	effect.draw(sb, delta);
		    }
		    if (effect.isComplete()) {
		        effect.free();
		        effects.removeIndex(i);
		    }
		}
	}

	
	private void drawLazor(float x, float y, float length){
		sb.setColor(BBUtils.hsvToRgba(((bbModel.lazerTimer / bbModel.baseLazerTimer) * 0.4f), 1, 1, 0.75f));
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
		case BLACK_HOLE:effect = partyBhPool.obtain();break;
		case CASH_FTEXT:effect = partyCashPool.obtain();break;
		case MAGBALL_FTEXT:effect = partyMagBallPool.obtain();break;
		case BOMB_FTEXT :effect = partyBombPool.obtain();break;
		case BALL_FTEXT :effect = partyBallPlusOnePool.obtain();break;
		case GUIDE_FTEXT :effect = partyGuideLaserPool.obtain();break;
		case LASER_FTEXT :effect = partyLaserPlussPool.obtain();break;
		case MAGPOWER_FTEXT :effect = partyMagPowerPool.obtain();break;
		case MAGSTR_FTEXT :effect = partyMagStrPool.obtain();break;
		case SCORE_FTEXT: effect = partyScorePlusPool.obtain(); break;
		case GHOST_TAIL: effect = partyGhostTailPool.obtain(); break;
		case MAGGHOST_TAIL: effect = partyMagGhostTailPool.obtain(); break;
		default:	 	effect = partySparksPool.obtain(); break;
		}
		effect.setPosition(pos.x * BBModel.BOX_TO_WORLD, pos.y * BBModel.BOX_TO_WORLD);
		effects.add(effect);	
	}
	
	private void doGameOverStuff(float delta, boolean win) {
		float x = (float) Math.random() * 80;
		float y = (float) Math.random() * 60;
		if(win){
			this.addPartyEffect(new Vector2(x,y), (int)(Math.random()*3)+1);
		}
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
		visfont 		= parent.assMan.manager.get("font/visitor.fnt", BitmapFont.class);
		gameOver 		= atlas.findRegion("gameover");
		gameOverwin		= atlas.findRegion("gameoverwin");
		lazerStartBg 	= atlasLazor.findRegion("lazorStart");
		lazerStartOver 	= atlasLazor.findRegion("lazorStartOver");
		lazerMidBg 		= atlasLazor.findRegion("laser");
		lazerMidOver 	= atlasLazor.findRegion("lazorOver");
		lazerEndBg 		= atlasLazor.findRegion("lazorEnd");
		lazerEndOver 	= atlasLazor.findRegion("lazorEndOver");
		lazerAllOver 	= atlasLazor.findRegion("lazorAllOver");
		leftWall = atlas.findRegion("leftwall");
		bottomWall = atlas.findRegion("bottomwall");
		
		Pixmap pmap = new Pixmap(5,5, Pixmap.Format.RGBA4444);
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
		bhEffect = parent.assMan.manager.get("particles/blackhole.pe",ParticleEffect.class);
		cashEffect = parent.assMan.manager.get("particles/cash.pe",ParticleEffect.class);
		magBallEffect = parent.assMan.manager.get("particles/magballpe.pe",ParticleEffect.class);
		bombpeEffect = parent.assMan.manager.get("particles/bombpe.pe",ParticleEffect.class);
		ballPlusoneEffect = parent.assMan.manager.get("particles/ballplusone.pe",ParticleEffect.class);
		guideLaserEffect = parent.assMan.manager.get("particles/guidelaser.pe",ParticleEffect.class);
		laserPlusEffect = parent.assMan.manager.get("particles/laserplus.pe",ParticleEffect.class);
		magPowerPlusEffect = parent.assMan.manager.get("particles/magpowerplus.pe",ParticleEffect.class);
		magStrPlusEffect = parent.assMan.manager.get("particles/magstrplus.pe",ParticleEffect.class);
		scorePlusEffect = parent.assMan.manager.get("particles/scoreplus.pe",ParticleEffect.class);
		ghostTailEffect = parent.assMan.manager.get("particles/ballghosttail.pe",ParticleEffect.class);
		magGhostTailEffect = parent.assMan.manager.get("particles/magballghosttail.pe",ParticleEffect.class);
		

		
		
		
		
		// scale effects
		sparks.scaleEffect(1/2f);
		explosion.scaleEffect(1/4f);
		lazerEffect.scaleEffect(1/4f);
		lazerHitEffect.scaleEffect(1/3f);
		bhEffect.scaleEffect(1/4f);
		// FloatingText
		cashEffect.scaleEffect(1/3f);
		//magBallEffect.scaleEffect(1/3f);
		ghostTailEffect.scaleEffect(1/3f);
		magGhostTailEffect.scaleEffect(1/3f);
		
		//create object pools
		partySparksPool = new ParticleEffectPool(sparks, 5, 20);
		partyExplosionPool = new ParticleEffectPool(explosion, 5, 20);
		partyLazerPool = new ParticleEffectPool(lazerEffect, 5, 20);
		partyHitLazerPool = new ParticleEffectPool(lazerHitEffect, 5, 20);
		partyPupPool = new ParticleEffectPool(pupEffect, 5, 20);
		partyBhPool = new ParticleEffectPool(bhEffect,5,20);
		partyCashPool = new ParticleEffectPool(cashEffect,2,20);
		partyMagBallPool = new ParticleEffectPool(magBallEffect,2,20);
		partyBombPool = new ParticleEffectPool(bombpeEffect,2,20);
		partyBallPlusOnePool = new ParticleEffectPool(ballPlusoneEffect,2,20);
		partyGuideLaserPool = new ParticleEffectPool(guideLaserEffect,2,20);
		partyLaserPlussPool = new ParticleEffectPool(laserPlusEffect,2,20);
		partyMagPowerPool = new ParticleEffectPool(magPowerPlusEffect,2,20);
		partyMagStrPool = new ParticleEffectPool(magStrPlusEffect,2,20);
		partyScorePlusPool = new ParticleEffectPool(scorePlusEffect,2,20);
		partyGhostTailPool = new ParticleEffectPool(ghostTailEffect,2,100);
		partyMagGhostTailPool = new ParticleEffectPool(magGhostTailEffect,2,100);
		
				
	}
}
