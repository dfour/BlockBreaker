package com.dfour.blockbreaker.view;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dfour.blockbreaker.BlockBreaker;
import com.dfour.blockbreaker.controller.AppController;

public class EndScreen implements Screen {
	
	// TODO get assets from Asset manager
	
	private BlockBreaker parent;
	private SpriteBatch pb;
	private AppController controller;
	//private Texture visFont = new Texture(Gdx.files.internal("font/visitor.png"), true);
	private BitmapFont font ;//= new BitmapFont(Gdx.files.internal("font/visitor.fnt"), new TextureRegion(visFont), false);
	private int widthOffset;
	private int sw;
	private int sh;
	private TextureAtlas atlas;
	private TextureAtlas atlasGui;
	private AtlasRegion bg;
	private AtlasRegion bgCredits;

	
	public EndScreen(BlockBreaker app){
		parent = app;
		atlas = parent.assMan.manager.get("images/images.pack");
		atlasGui = parent.assMan.manager.get("gui/loadingGui.pack");
		pb = new SpriteBatch();
		bg = atlasGui.findRegion("background");
		bgCredits = atlas.findRegion("bbendscreen");
		font 			= parent.assMan.manager.get("font/visitor.fnt", BitmapFont.class);

	}

	@Override
	public void show() {
		controller = new AppController();
		Gdx.input.setInputProcessor(controller);

	}

	@Override
	public void render(float delta) {
		
		// TODO redesign this screen
		
		Gdx.gl.glClearColor(.4f, .4f, .4f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    
	    pb.begin();
		// draw brick background
		for(int i = 0; i < this.sw; i += 20){
			for(int j = 0; j < this.sh; j+= 10){
				pb.draw(bg, i, j,20,10);
			}
		}
		pb.draw(bgCredits, sw/2 - 310, sh/2 - 300);
		pb.end();
	    
		/*
	    pb.begin();
        font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        font.draw(pb, "You Have Completed the game.",widthOffset/2,sh-10);
        font.draw(pb, "Press Escpae to return to menu.",widthOffset/2,sh -20);
        font.draw(pb, "Credits",widthOffset/2,sh -30);
        font.draw(pb, "Who made this : John Day",widthOffset/2,sh -40); 
        font.draw(pb, "Who made that : John Day",widthOffset/2,sh -50); 
        font.draw(pb, "Who made this thingy : John Day",widthOffset/2,sh -60); 
        font.draw(pb, "Who made that thingy: John Day",widthOffset/2,sh -70); 
        font.draw(pb, "Who made the dohickey : John Day",widthOffset/2,sh -80); 
        font.draw(pb, "Who made the whatchamacallit : John Day",widthOffset/2,sh -90); 
        font.draw(pb, "Who made the thingymabob : John Day",widthOffset/2,sh -100);
        font.draw(pb, "Who has no friends : John Day",widthOffset/2,sh -110); 
        font.draw(pb, "Easter Egg Manager : Charlotte Day",widthOffset/2,sh -120); 
        font.draw(pb, "Music By : sawsquarenoise",widthOffset/2,sh -130); 
        pb.end();
        
        */
		
        if(controller.getEscape()){
        	controller.setEscape(false);
        	returnToMenu();
        }
        
	}
	
	public void returnToMenu(){
		parent.changeScreen(BlockBreaker.MENU);
	}

	@Override
	public void resize(int width, int height) {
		this.sw = width;
		this.sh = height;
		widthOffset = width / 2 - 100;
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
