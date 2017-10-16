package com.dfour.blockbreaker.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.dfour.blockbreaker.BlockBreaker;

public class Scene2DScreen implements Screen{
	protected Stage stage;
	protected Skin skin;
	protected BlockBreaker parent;
	protected SpriteBatch pb;
	protected Table rootTable;
	protected Table displayTable;
	protected int sw;
	protected int sh;
	protected TextureAtlas atlasGui;
	protected TiledDrawable bgTiled;
	protected float fadeIn = 0.7f;
	protected float fadeOut = 0.7f;
	protected boolean isReturning = false;
	protected int returnScreen;
	
	public Scene2DScreen (BlockBreaker p){
		parent = p;
		stage = new Stage(new ScreenViewport());
		pb = new SpriteBatch();
		
		atlasGui = parent.assMan.manager.get("gui/loadingGui.pack");
		bgTiled = new TiledDrawable(atlasGui.findRegion("background"));
		skin = parent.assMan.manager.get("skin/bbskin.json",Skin.class);
		
	}

	@Override
	public void show() {
		if(Gdx.graphics.getWidth() != parent.screenWidthPreferred
				|| Gdx.graphics.getHeight() != parent.screenHeightPreferred){
			Gdx.graphics.setWindowedMode(parent.screenWidthPreferred, parent.screenHeightPreferred);
		}
		
		Image title = new Image(atlasGui.findRegion("blockBreakerTitle"));

		
		rootTable = new Table();
		rootTable.setFillParent(true);
		rootTable.setDebug(BlockBreaker.debug);
		rootTable.add(title).pad(10);
		rootTable.row().expandY();
		displayTable = new Table();
		displayTable.setDebug(BlockBreaker.debug);
		displayTable.setBackground(new NinePatchDrawable(atlasGui.createPatch("darkblockbutton")));
	    displayTable.pad(30);
		rootTable.add(displayTable);
		stage.addActor(rootTable);
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(.4f, .4f, .4f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// new better tiling
		pb.begin();
		bgTiled.draw(pb, 0, 0, sw, sh);
		pb.end();
		
		
		
		if(fadeIn > 0){
			fadeIn -= delta;
			displayTable.setColor(1,1,1,1-fadeIn);
		}else if(this.isReturning){
			fadeOut -= delta;
			displayTable.setColor(1,1,1,fadeOut);
			if(fadeOut <= 0){
				parent.changeScreen(returnScreen);
			}
		}
		
		stage.act();
		stage.draw();		
	}

	@Override
	public void resize(int width, int height) {
		sw = width;
		sh = height;
		pb.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
		stage.getViewport().update(width, height, true);
		
		parent.screenWidthPreferred = sw;
		parent.screenHeightPreferred = sh;
		
		parent.getPreferences().setScreenSize(Gdx.graphics.getWidth()+"x"+Gdx.graphics.getHeight());
		
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
		stage.dispose();
		skin.dispose();
	}

}
