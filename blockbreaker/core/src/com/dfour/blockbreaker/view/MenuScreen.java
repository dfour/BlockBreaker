package com.dfour.blockbreaker.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.dfour.blockbreaker.BlockBreaker;

public class MenuScreen implements Screen {
	private Stage stage;
	private Skin skin;
	private BlockBreaker parent;
	private AtlasRegion bg;
	private int sw = 800;
	private int sh = 600;
	private SpriteBatch pb;
	TextureAtlas atlas;
	TextureAtlas atlasGui;
	private Image title;
	private TextButtonStyle textButtonStyle;
	private Table table;
	private TextButton button;
	private TextButton button2;
	private TextButton button3;
	private TextButton button4;
	
	private float countUp = 1f;
	private Table buttonTable;
	
	private boolean changingScreen = false;
	private int nextScreen = 0;
	private float countDown;
	

	public MenuScreen(BlockBreaker parent){
		this.parent = parent;
		stage = new Stage(new ScreenViewport());
		pb = new SpriteBatch();
		
	}
	
	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		changingScreen = false;
		countUp = 1f;
		countDown = 1f;
		
		loadImages();
		
		table = new Table();
		table.setFillParent(true);
		table.setDebug(BlockBreaker.debug);
		
		buttonTable = new Table();
		stage.addActor(table);

		button = new TextButton("Start", textButtonStyle);
		button2 = new TextButton("Preferences", textButtonStyle);
		button3 = new TextButton("Exit", textButtonStyle);
		button4 = new TextButton("Level Designer", textButtonStyle);
		table.add(title).pad(10);
        table.row().expandY();
        buttonTable.add(button).width(300).height(50);
        buttonTable.row().pad(10, 0, 10, 0);
        buttonTable.add(button4).width(300).height(50);
        buttonTable.row();
        buttonTable.add(button2).width(300).height(50);
        buttonTable.row().pad(10, 0, 10, 0);;
        buttonTable.add(button3).width(300).height(50);
        table.add(buttonTable);
        table.row();
        table.add().height(title.getHeight()+10);
		

		button.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				changingScreen = true;
				nextScreen = BlockBreaker.APPLICATION;
				//MenuScreen.this.parent.changeScreen(BlockBreaker.APPLICATION);
			}
		});
		
		button2.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				changingScreen = true;
				nextScreen = BlockBreaker.PREFERENCES;
				//MenuScreen.this.parent.changeScreen(BlockBreaker.PREFERENCES);
			}
		});
		
		button3.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				Gdx.app.exit();
			}
		});
		
		button4.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				changingScreen = true;
				nextScreen = BlockBreaker.LEVEL_DESIGNER;
				//MenuScreen.this.parent.changeScreen(BlockBreaker.PREFERENCES);
			}
		});
		
		button.setChecked(false);
		button2.setChecked(false);
		button3.setChecked(false);
		button4.setChecked(false);
		
	}


	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(.4f, .4f, .4f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(this.changingScreen){
			countDown -= delta;
			if(countDown < 0){
				countDown = 0;
				MenuScreen.this.parent.changeScreen(nextScreen);
			}
			buttonTable.setColor(1, 1, 1, countDown);
		}else{
			countUp -= delta;
			if(countUp < 0){
				countUp = 0;
			}
			buttonTable.setColor(1, 1, 1, 1-countUp);
		}
		pb.begin();
		// draw brick background
		for(int i = 0; i < this.sw; i += 20){
			for(int j = 0; j < this.sh; j+= 10){
				pb.draw(bg, i, j,20,10);
			}
		}
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
	public void hide() {
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
	}
	
	private void loadImages() {
		atlas = parent.assMan.manager.get("images/images.pack");
		atlasGui = parent.assMan.manager.get("gui/loadingGui.pack");
		
		title = new Image(atlasGui.findRegion("blockBreakerTitle"));
		bg = atlasGui.findRegion("background");
		
		skin = new Skin();
		
		NinePatchDrawable npn = new NinePatchDrawable(atlasGui.createPatch("blockbutton"));
		NinePatchDrawable npo = new NinePatchDrawable(atlasGui.createPatch("blockbuttonpr"));
		NinePatchDrawable npd = new NinePatchDrawable(atlasGui.createPatch("blockbuttonlg"));

		textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = npn;
		textButtonStyle.down = npd;
		textButtonStyle.over = npo;
		textButtonStyle.checked = npd;
		textButtonStyle.font = parent.assMan.manager.get("font/tekton.fnt", BitmapFont.class);
	}
}
