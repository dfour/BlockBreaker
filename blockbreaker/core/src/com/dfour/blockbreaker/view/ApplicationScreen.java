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
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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
import com.dfour.blockbreaker.ParticleEffectManager;
import com.dfour.blockbreaker.Player;
import com.dfour.blockbreaker.controller.AppController;
import com.dfour.blockbreaker.entity.Ball;
import com.dfour.blockbreaker.entity.Bomb;
import com.dfour.blockbreaker.entity.Brick;
import com.dfour.blockbreaker.entity.ExplosionParticle;
import com.dfour.blockbreaker.entity.LightBall;
import com.dfour.blockbreaker.entity.LocalEffectEntity;
import com.dfour.blockbreaker.entity.MultiPad;
import com.dfour.blockbreaker.entity.Obstacle;
import com.dfour.blockbreaker.entity.Portal;
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
	private TextureRegion gameOver;
	private TextureRegion gameOverwin;
	
	
	private ParticleEffectManager pem = new ParticleEffectManager();
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
	
	private float gameOverTimer = 5f;
	private TextureAtlas atlas;
	private TextureAtlas atlasGui;
	private TextureAtlas atlasLazor;
	
	public float fadeIn = 1f;
	private float fadeOut = 1f;
	private float currentAlpha = 1f;
	private boolean isReturning;
	private int nextScreen;
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
		
	private SystemCursor inMenu;
	private ShaderProgram shockwaveShader;
	private float shockWaveTime = 500f;
	private float shaderSmokeTime = 0f;
	private FrameBuffer fbBase;
	
	private float spa = 10f; // refraction
	private float spb = 0.8f; // strength
	private float spc = 0.1f; // wave width
	private Vector2 center = new Vector2(0.5f,0.5f);
	private ShaderProgram smokeShader;
	
	private float fpsTimer = 0;
	private int FPS;
	private Player lp;
	

	
	public ApplicationScreen(BlockBreaker p, BBModel model) {
		parent = p;
		
		bbModel = model;
		lp = bbModel.lp;
		controller = model.controller;
		
		loadImages();
		cam = new OrthographicCamera(800,600);
		cam.position.x = 400;
	    cam.position.y = 280;
	    cam.update();
		
		debugMatrix = new Matrix4(cam.combined);
		debugMatrix.scl(BBModel.BOX_TO_WORLD);
		viewport = new ExtendViewport(800, 600, cam);
		debugRenderer = new Box2DDebugRenderer(true,true,true,true,true,true);
		
		rayHandler = bbModel.rayHandler;
		rayHandler.setCombinedMatrix(debugMatrix, 40, 30, 80, 60);
	
		
		sr = new ShapeRenderer();
		sb = new SpriteBatch();
		pb = new SpriteBatch();
		
		// TOLOOKAT https://www.shadertoy.com/view/4scXWB
		
		Gdx.app.setLogLevel(com.badlogic.gdx.Application.LOG_INFO);
		
		shockWaveTime = 50f;
		
		ShaderProgram.pedantic = false;
		shockwaveShader = new ShaderProgram(Gdx.files.internal("shaders/shockwave.vert"),
				Gdx.files.internal("shaders/shockwave.frag"));

		smokeShader = new ShaderProgram(Gdx.files.internal("shaders/smoke.vert"),
				Gdx.files.internal("shaders/starnest.frag"));
		
		if (!shockwaveShader.isCompiled() || !smokeShader.isCompiled()) {
			Gdx.app.log("Shader", "S"+shockwaveShader.getLog());
			Gdx.app.log("Shader", "F"+smokeShader.getLog());
			System.exit(0);
		}
		
		if (shockwaveShader.getLog().length()!=0 || smokeShader.getLog().length()!=0){
			Gdx.app.log("Shader", "S"+shockwaveShader.getLog());
			Gdx.app.log("Shader", "F"+smokeShader.getLog());
		}
		createEffects();
		
		
		stage = new Stage(new ExtendViewport(800,600));
		displayTable = new Table();
		displayTable.setDebug(false);
		displayTable.setFillParent(true);
		skin = parent.assMan.manager.get("skin/bbskin.json",Skin.class);
		
		inMenu = Cursor.SystemCursor.Arrow;
		
		NinePatchDrawable npd = new NinePatchDrawable(atlasGui.createPatch("drawer"));
		
		pauseMenuTable = new Table();
		//pauseMenuTable.setBackground(npd);
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
				ApplicationScreen.this.toInGameSettings();
				isPaused = false;
			}
		});
		
		TextButton btnQuit = new TextButton("Quit Level",skin);
		btnQuit.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				ApplicationScreen.this.toInMenuSettings();
				quitToMenu();
			}
		});
		
		
		
		pauseMenuTable.add(btnResume).width(250).pad(5);
		pauseMenuTable.row();
		pauseMenuTable.add(btnQuit).width(250).pad(5);
		
		
		
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
		laserBar.updateProgress((lp.lazerTimer / lp.baseLazerTimer));
		Label scoreTextLabel = new Label("Score:",skin,"small");
		Label cashTextLabel = new Label ("Cash:",skin,"small");
		Label bombTextLabel = new Label ("Bombs:",skin,"small");
		Label livesTextLabel = new Label ("Lives Left: ",skin,"small");
		magnetUnitsTotal = new Label (lp.magnetPower+" / "+lp.baseMagnetPower,skin,"small");
		scoreCountLabel = new Label(""+bbModel.score,skin,"small");
		cashCountLabel = new Label("$"+lp.cash,skin,"small");
		bombCountLabel = new Label(""+lp.bombsLeft,skin,"small");
		laserCountLabel = new Label(""+lp.lazerTimer,skin,"small");
		laserGuideCountLabel = new Label(""+lp.guideLazerTimer,skin,"small");
		livesCountLabel = new Label(""+lp.livesLeft,skin,"small");
		
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
		
		displayTable.add(pauseMenuTable).width(800).height(580);
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
		toInGameSettings();
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
		createFrameBuffer();
	}

	@Override
	public void render(float delta) {
		controller.update(delta);
		
		fpsTimer += delta;
		FPS+=1;
		if(fpsTimer >= 1f){
			fpsTimer = 0;
			if(FPS < 50){
				System.out.println("FPS="+FPS);
			}
			FPS = 0;
		}
		
		//clear screen
		Gdx.gl.glClearColor(.4f, .4f, .4f, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    
	    shockWaveTime+= delta / 2f;
	    if(controller.isMouse1Down()){
	    	shaderSmokeTime+=delta*3;
	    }else if(controller.isMouse2Down()){
	    	shaderSmokeTime-=delta*3;
	    }else{
	    	shaderSmokeTime+=delta;
	    }   
	    
	    smokeShader.begin();
	    smokeShader.setUniformf("iResolution", new Vector2(1024,768));
	    smokeShader.setUniformf("iMouse", 0, -controller.getMousePosition().x / 512f, 0,0);
	    smokeShader.setUniformf("iTime", shaderSmokeTime);
	    smokeShader.end();
	    
	    
	    if(controller.isPauseDown){
	    	isPaused = !isPaused;
	    	controller.isPauseDown = false;
	    	if(isPaused){
	    		bbModel.sendPause(true);
	    		pauseMenuTable.setVisible(true);
	    		toInMenuSettings();
	    	}else{
	    		bbModel.sendPause(false);
	    		pauseMenuTable.setVisible(false);
	    		toInGameSettings();
	    	}  	
	    }
	    
	    
	    if(!isPaused){
	    	bbModel.doLogic(delta);
	    	if(!BlockBreaker.debug || (BlockBreaker.debug && BlockBreaker.debug_texture_render)){
		    	magnetBar.updateProgress((lp.magnetPower/(float)lp.baseMagnetPower));
		    	laserBar.updateProgress((lp.lazerTimer / lp.baseLazerTimer));
		    	guideBar.updateProgress((lp.guideLazerTimer/ lp.baseGuideLazerTimer));
				magnetUnitsTotal.setText(lp.magnetPower+" / "+lp.baseMagnetPower+" @ "+(lp.magnetRechargeRate * 60));
		    	scoreCountLabel.setText(bbModel.score+"");
		    	cashCountLabel.setText("$"+lp.cash);
		    	bombCountLabel.setText(""+lp.bombsLeft);
		    	laserCountLabel.setText((int)lp.lazerTimer+" seconds");
		    	laserGuideCountLabel.setText((int)lp.guideLazerTimer+" seconds");
		    	livesCountLabel.setText(""+lp.livesLeft);
	    	}
	    }
	    
	    if(fadeIn > 0){
	    	currentAlpha = 1 - fadeIn;
	    	fadeIn-=delta;
	    }else if(this.isReturning){
			fadeOut -= delta;
			currentAlpha = fadeOut;
			if(fadeOut <= 0){
				toInMenuSettings();
				pauseMenuTable.setVisible(false);
				isPaused = false;
				parent.changeScreen(nextScreen);
			}
		}else{
			currentAlpha = 1;  // set alpha to 1(fixes display bug when lagging during fade in)
		}
	    
	    //draw everything to fbBase
	    fbBase.begin();
	    Gdx.gl.glClearColor(0,0,0,1);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    pb.begin();
			shaderStarField();
		pb.end();
	    
	    if(!BlockBreaker.debug || (BlockBreaker.debug && BlockBreaker.debug_texture_render)){
		    if(!isPaused){
		    	addParticleEffects();
		    }
		    
			sb.setProjectionMatrix(cam.combined); // set SpriteBatch Matrix
					
			// render Lighting
			rayHandler.setCombinedMatrix(debugMatrix, 40, 30, 80, 60);
			rayHandler.updateAndRender();
			
			sb.begin();
				// draw walls
				sb.setColor(1,1,1,(0.7f * currentAlpha));
				sb.draw(leftWall, 0, 	0,	10,	590);
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
			
			drawGuideLazors();
	    }
	 		
	    this.fbBase.end();
	    
	    
	    shockwaveShader.begin();
	    shockwaveShader.setUniformf("time", shockWaveTime);
	    shockwaveShader.setUniformf("center", center.x,center.y);
	    shockwaveShader.setUniformf("shockParams", spa,spb,spc);
	    shockwaveShader.end();
	    
	    pb.begin();
	    pb.setShader(shockwaveShader);
		pb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_DST_COLOR);
		pb.draw(fbBase.getColorBufferTexture(),0,0,this.sw,this.sh);
		pb.end();
		
		
		
		if(BlockBreaker.debug && BlockBreaker.debug_b2d_render){
			debugRenderer.render(bbModel.world, debugMatrix);
		}

		if(!BlockBreaker.debug || (BlockBreaker.debug && BlockBreaker.debug_texture_render)){
			//
			pb.begin();
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
		 }
		
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

	// called once on reseize so BO is right size
	private void createFrameBuffer(){
		System.out.println("Making new FrameBuffers:"+this.sw+"x"+this.sh);
		fbBase = new FrameBuffer(Format.RGBA8888,this.sw,this.sh, false);
	}
	
	private void shaderStarField() {
		pb.setShader(smokeShader);
		pb.draw(fbBase.getColorBufferTexture(),0,0,this.sw,this.sh);
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
		//isPaused = true;
	}

	@Override
	public void resume() {
		//isPaused = false;
		createFrameBuffer();
	}

	@Override
	public void hide() {
		//isPaused = true;
	}

	@Override
	public void dispose() {
		rayHandler.dispose();
		debugRenderer.dispose();
		sb.disableBlending();
		sr.dispose();
		pb.dispose();
		fbBase.dispose();
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
		
		for(Portal portal : bbModel.entFactory.portals){
			portal.draw(sb,currentAlpha, delta);
		}
		
		for(LocalEffectEntity lee : bbModel.entFactory.localEffectEntities){
			lee.draw(sb,currentAlpha,delta);
			if(!lee.hasPartyEffect){
				bbModel.blackHolePE.add(lee.body.getPosition());
				lee.hasPartyEffect = true;
			}
		}
		// draw multi pads
		for(MultiPad mpad:bbModel.entFactory.mpads){
			mpad.sprite.draw(sb,currentAlpha);
			mpad.drawAnimation(sb, delta);	
		}
		
		lp.pad.sprite.draw(sb,currentAlpha);
		lp.pad.drawAnimation(sb, delta);		
	}

	private void addParticleEffects() {
		checkForExplosions();
		for(Ball ball :bbModel.entFactory.balls){
			if(ball.isMagBall){
				addPartyEffect(ball.body.getPosition(),ParticleEffectManager.MAGGHOST_TAIL);
			}else{
				addPartyEffect(ball.body.getPosition(),ParticleEffectManager.GHOST_TAIL);
			}
		}
	    addParticleEffectArray(bbModel.magPowerFText,ParticleEffectManager.MAGPOWER_FTEXT);
	    addParticleEffectArray(bbModel.laserFtext,ParticleEffectManager.LASER_FTEXT);
	    addParticleEffectArray(bbModel.guideFText,ParticleEffectManager.GUIDE_FTEXT);
	    addParticleEffectArray(bbModel.ballFText,ParticleEffectManager.BALL_FTEXT);
	    addParticleEffectArray(bbModel.bombFText,ParticleEffectManager.BOMB_FTEXT);
	    addParticleEffectArray(bbModel.magBallPEToShow,ParticleEffectManager.MAGBALL_FTEXT);
	    addParticleEffectArray(bbModel.cashPEToShow,ParticleEffectManager.CASH_FTEXT);
	    addParticleEffectArray(bbModel.blackHolePE,ParticleEffectManager.BLACK_HOLE);
	    addParticleEffectArray(bbModel.pupExplosions,ParticleEffectManager.POWERUP);
	    addParticleEffectArray(bbModel.explosions,ParticleEffectManager.EXPLOSION);
	    addParticleEffectArray(bbModel.sparks,ParticleEffectManager.SPARK);
	    addParticleEffectArray(bbModel.scoreFText,ParticleEffectManager.SCORE_FTEXT);
	    addParticleEffectArray(bbModel.slowFText,ParticleEffectManager.SLOW_FTEXT);
	    addParticleEffectArray(bbModel.drunkFText,ParticleEffectManager.DRUNK_FTEXT);
	    addParticleEffectArray(bbModel.stickyFText,ParticleEffectManager.STICKYPAD_FTEXT);
		
	}
	
	private void checkForExplosions(){
		for(Vector2 pos: bbModel.bombShader){
			System.out.println("bombpos"+pos);
			this.addShockWave(pos.x,pos.y);
			bbModel.bombShader.removeValue(pos,true);
		}
	}
	
	private void addParticleEffectArray(Array<Vector2> positions, int particleType){
		for(Vector2 pos: positions){
			this.addPartyEffect(pos, particleType);
			positions.removeValue(pos, true);
		}
	}

	private void fireLazors() {
		if(lp.isFiringLazer){
			sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
			float x = lp.pad.body.getPosition().x;
			float y = lp.pad.body.getPosition().y;
			
			float lazerLengthLeft = 6;
			float lazerLengthRight = 6;
			
			if(bbModel.lazerEndLeft != null){
				lazerLengthLeft = bbModel.lazerEndLeft.y - y - 6;
				this.addPartyEffect(bbModel.lazerEndLeft,ParticleEffectManager.LAZER_HIT);
			}
			if(bbModel.lazerEndRight != null){
				lazerLengthRight = bbModel.lazerEndRight.y - y - 6;
				this.addPartyEffect(bbModel.lazerEndRight, ParticleEffectManager.LAZER_HIT);
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
		sb.setColor(BBUtils.hsvToRgba(((lp.lazerTimer / lp.baseLazerTimer) * 0.4f), 1, 1, 0.75f));
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
		
		PooledEffect effect = pem.getPooledParticleEffect(type);
		effect.setPosition(pos.x * BBModel.BOX_TO_WORLD, pos.y * BBModel.BOX_TO_WORLD);
		effects.add(effect);	
	}
	
	private void doGameOverStuff(float delta, boolean win) {
		toInMenuSettings(); // hopefully fixes no curse in end game screen bug
		float x = (float) Math.random() * 80;
		float y = (float) Math.random() * 60;
		if(win){
			this.addPartyEffect(new Vector2(x,y), (int)(Math.random()*3)+1);
		}
		gameOverTimer-= delta;
		if(gameOverTimer < 0){
			Gdx.graphics.setSystemCursor(inMenu);
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
		
		pem.addParticleEffect(ParticleEffectManager.SPARK, parent.assMan.manager.get("particles/sparks.pe",ParticleEffect.class), 1/2f);
		pem.addParticleEffect(ParticleEffectManager.EXPLOSION, parent.assMan.manager.get("particles/explosion.pe",ParticleEffect.class), 1/4f);
		pem.addParticleEffect(ParticleEffectManager.LAZER, parent.assMan.manager.get("particles/lazer.pe",ParticleEffect.class), 1/4f);
		pem.addParticleEffect(ParticleEffectManager.LAZER_HIT, parent.assMan.manager.get("particles/laserHitSparks.pe",ParticleEffect.class), 1/3f);
		pem.addParticleEffect(ParticleEffectManager.POWERUP, parent.assMan.manager.get("particles/pupGetEffect.pe",ParticleEffect.class));
		pem.addParticleEffect(ParticleEffectManager.BLACK_HOLE, parent.assMan.manager.get("particles/blackhole.pe",ParticleEffect.class), 1/4f);
		
		pem.addParticleEffect(ParticleEffectManager.CASH_FTEXT, parent.assMan.manager.get("particles/cash.pe",ParticleEffect.class));
		pem.addParticleEffect(ParticleEffectManager.MAGBALL_FTEXT, parent.assMan.manager.get("particles/magballpe.pe",ParticleEffect.class));
		pem.addParticleEffect(ParticleEffectManager.BOMB_FTEXT, parent.assMan.manager.get("particles/bombpe.pe",ParticleEffect.class));
		pem.addParticleEffect(ParticleEffectManager.BALL_FTEXT, parent.assMan.manager.get("particles/ballplusone.pe",ParticleEffect.class));
		pem.addParticleEffect(ParticleEffectManager.GUIDE_FTEXT, parent.assMan.manager.get("particles/guidelaser.pe",ParticleEffect.class));
		pem.addParticleEffect(ParticleEffectManager.LASER_FTEXT, parent.assMan.manager.get("particles/laserplus.pe",ParticleEffect.class));
		pem.addParticleEffect(ParticleEffectManager.MAGPOWER_FTEXT, parent.assMan.manager.get("particles/magpowerplus.pe",ParticleEffect.class));
		pem.addParticleEffect(ParticleEffectManager.SCORE_FTEXT, parent.assMan.manager.get("particles/scoreplus.pe",ParticleEffect.class));
		pem.addParticleEffect(ParticleEffectManager.SLOW_FTEXT, parent.assMan.manager.get("particles/slow.pe",ParticleEffect.class));
		pem.addParticleEffect(ParticleEffectManager.DRUNK_FTEXT, parent.assMan.manager.get("particles/drunk.pe",ParticleEffect.class));
		pem.addParticleEffect(ParticleEffectManager.STICKYPAD_FTEXT, parent.assMan.manager.get("particles/stickypad.pe",ParticleEffect.class));
		
		pem.addParticleEffect(ParticleEffectManager.GHOST_TAIL, parent.assMan.manager.get("particles/ballghosttail.pe",ParticleEffect.class), 1/3f, 5, 100);
		pem.addParticleEffect(ParticleEffectManager.MAGGHOST_TAIL, parent.assMan.manager.get("particles/magballghosttail.pe",ParticleEffect.class), 1/3f, 5, 100);
		
	}
	
	private void toInGameSettings(){
		if(BlockBreaker.debug_mouse_capture){
			Gdx.input.setCursorCatched(true);
			Gdx.graphics.setSystemCursor(inMenu);
		}
	}
	
	private void toInMenuSettings(){
		if(BlockBreaker.debug_mouse_capture){
			Gdx.input.setCursorCatched(false);
			Gdx.graphics.setSystemCursor(inMenu);
		}
	}
	
	private void addShockWave(float x,float y){
		Vector3 sbtvec = new Vector3(x * BBModel.BOX_TO_WORLD,y * BBModel.BOX_TO_WORLD,0);
		viewport.project(sbtvec);
		center.x = (sbtvec.x / sw);
		center.y = 1- (sbtvec.y / sh);
		shockWaveTime = 0f;
	}
	
	
}
