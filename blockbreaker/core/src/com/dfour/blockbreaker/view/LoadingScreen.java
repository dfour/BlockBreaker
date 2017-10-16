package com.dfour.blockbreaker.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.dfour.blockbreaker.BlockBreaker;

public class LoadingScreen implements Screen{
	
	private int sw = 1024;
	private int sh = 786;
	private BlockBreaker parent;
	private float percent;
	
	public final int IMAGE = 0;
	public final int FONT = 1;
	public final int PARTY = 2;
	public final int SOUND = 3;
	public final int MUSIC = 4;
	
	public float countDown = 3f;
	
	public int currentLoad = 0;
	
	private Stage stage;

	private Image loading;
	private TiledDrawable bgTiled;
	private SpriteBatch pb;
	private TextureAtlas atlas;
	private Image title;
	private Texture loadingGreen;
	private Table table;
	private Image loaded;
	private Label loadingLabel;
	private Table tableInner;

	public LoadingScreen(BlockBreaker bb) {
		parent = bb;
	    stage = new Stage(new ScreenViewport());
		pb = new SpriteBatch();
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		
		countDown = 1f;
		
		parent.assMan.loadLoadingData();
		parent.assMan.manager.finishLoading();

		LabelStyle lstyle = new LabelStyle();
		lstyle.font = new BitmapFont();
		loadingLabel = new Label("Loading Images", lstyle);
		
		loadImages();
		
		Stack stack = new Stack();
		stack.add(loading);
		stack.add(loaded);
		

		table = new Table();
		table.setFillParent(true);
        table.setDebug(BlockBreaker.debug);
        tableInner = new Table();
        tableInner.setDebug(BlockBreaker.debug);
        tableInner.add(loadingLabel);
        tableInner.row();
        tableInner.add(stack);
        
        
        table.add(title).pad(10);
        table.row().expandY();
        table.add(tableInner);
        
		stage.addActor(table);
	
		
		parent.assMan.loadSkin();
		parent.assMan.loadImages();
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    
	    if (parent.assMan.manager.update()) { // Load some, will return true if done loading
            currentLoad+= 1;
            switch(currentLoad){
            case FONT:	parent.assMan.loadFonts(); 
            	loadingLabel.setText("Loading Fonts");
            	break;
            case PARTY:	parent.assMan.loadParticleEffects();
            	loadingLabel.setText("Loading Particle Effects");
            	break;
            case SOUND:	parent.assMan.loadSounds();
            	loadingLabel.setText("Loading Sounds");
            	break;
            case MUSIC:	parent.assMan.loadMusic(); 
            	loadingLabel.setText("Loading Music");
            	break;
            case 5:	parent.assMan.loadMusic(); 
            	loadingLabel.setText("Loading Fonts");
            	break;
            }
	    	if (currentLoad >5){
	    		loadingLabel.setText("");
	    		countDown -= delta;
	    		if(countDown < 0){
	    			parent.changeScreen(BlockBreaker.MENU);
	    		}
	    		tableInner.setColor(1, 1, 1, countDown);
	    		percent = 1;
            }
        }else{
        	percent = Interpolation.linear.apply(percent, parent.assMan.manager.getProgress(), 0.05f);
        }
	    	   
	    
	    if(percent == 1){
	    	loading.setVisible(false);
	    	loaded.setVisible(true);
	    }
	    
	    pb.begin();
	    
	    for(int i = 0; i < sw; i += 20){
			for(int j = 0; j < (sh * percent); j+= 10){
				pb.draw(loadingGreen, i, j,20,10);
			}
		}
	    
		bgTiled.draw(pb, 0, 0, sw, sh);
		pb.end();
	    
	    stage.act();
	    stage.draw();
		
	}

	@Override
	public void resize(int width, int height) {	
		sw = width;
		sh = height;
		pb.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {		
	}

	@Override
	public void resume() {		
	}

	@Override
	public void hide() {		
	}

	@Override
	public void dispose() {	
	}
	
	private void loadImages() {
		atlas = parent.assMan.manager.get("gui/loadingGui.pack");
		bgTiled = new TiledDrawable(atlas.findRegion("background"));
		title = new Image(atlas.findRegion("blockBreakerTitle"));
		loading = new Image(atlas.findRegion("loading"));
		loaded = new Image(atlas.findRegion("loaded"));
		loaded.setVisible(false);
		
		Pixmap pmap = new Pixmap(1,1, Pixmap.Format.RGBA8888);
		pmap.setColor(new Color(0,0,0,1));
		pmap.fill();
		new Image(new Texture(pmap));
		
		pmap = new Pixmap(1,1, Pixmap.Format.RGBA8888);
		pmap.setColor(new Color(.4f, .4f, .4f, 1));
		pmap.fill();
		loadingGreen = new Texture(pmap);
		
		pmap.dispose();
	}
}
