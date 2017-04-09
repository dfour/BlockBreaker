package com.dfour.blockbreaker.view;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.dfour.blockbreaker.BlockBreaker;
import com.dfour.blockbreaker.controller.AppController;

public class EndScreen implements Screen {
		
	private BlockBreaker parent;
	private SpriteBatch pb;
	private AppController controller;
	private int sw;
	private int sh;
	private TextureAtlas atlas;
	private TextureAtlas atlasGui;
	private AtlasRegion bg;
	private AtlasRegion bgCredits;
	private float countDown = 20f;

	
	public EndScreen(BlockBreaker app){
		parent = app;
		atlas = parent.assMan.manager.get("images/images.pack");
		atlasGui = parent.assMan.manager.get("gui/loadingGui.pack");
		pb = new SpriteBatch();
		bg = atlasGui.findRegion("background");
		bgCredits = atlas.findRegion("bbendscreen");

	}

	@Override
	public void show() {
		controller = new AppController(parent);
		Gdx.input.setInputProcessor(controller);
		countDown = 20f;

	}

	@Override
	public void render(float delta) {
		
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
		
        if(controller.getEscape()){
        	controller.setEscape(false);
        	returnToMenu();
        }
        countDown -= delta;
        if(countDown <= 0){
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

}
