package com.dfour.blockbreaker.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.dfour.blockbreaker.BlockBreaker;

public class LevelDesignerScreen implements Screen {
	
	// TODO get rid of this stage crap and make my own level designer
	
	private BlockBreaker parent;
	private Stage stage;
	private SpriteBatch pb;
	
	// Stage Items
	private Table table;
	private Table levelTable;
	private Table guiTable;
	private TextureAtlas atlas;
	private TextureAtlas atlasGui;
	private AtlasRegion bg;
	private Skin skin;
	private TextButtonStyle textButtonStyle;
	private int sw;
	private int sh;
	
	private static final String BRICK = "x";
	private static final String LIGHT = "s";
	private static final String LEFT = "/";
	private static final String RIGHT = "\\";
	private static final String	EMPTY = "-";
	private static final String SPINNERC = "c";
	private static final String SPINNERA = "a";
	private static final String BLACKHOLE = "b";
	
	private String currentObject = EMPTY;
	
	//texs
	private TextureRegionDrawable brickPic;
	private TextureRegionDrawable blank;
	private TextureRegionDrawable lightImage;
	private TextureRegionDrawable obstacleImage;
	private TextureRegionDrawable flipped;
	
	
	private static final int OB_WIDTH = 30;
	private static final int OB_HEIGHT = 15;
	
	
	
	
	
	public LevelDesignerScreen(BlockBreaker p){
		parent = p;
		stage = new Stage(new ScreenViewport());
		pb = new SpriteBatch();
		
		loadImages();
		
		table = new Table();
		table.setFillParent(true);
		table.setDebug(BlockBreaker.debug);
		
		
		ClickListener ldcl = new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				String cobj = LevelDesignerScreen.this.currentObject;
				ImageButton button = (ImageButton) e.getTarget().getParent();
				if(cobj.equalsIgnoreCase(BRICK)){
					button.getImage().setDrawable(brickPic);
					button.invalidate();
				}else if(cobj.equalsIgnoreCase(LIGHT)){
					button.setBackground(lightImage);
				}else if(cobj.equalsIgnoreCase(LEFT)){
					button.setBackground(obstacleImage);
				}else if(cobj.equalsIgnoreCase(RIGHT)){
					button.setBackground(flipped);
				}
				if(e.getButton() == 1){
					button.setBackground(blank);
				}
				button.invalidate();					
			}
		};
		
		
		// holds objects to represent level
		levelTable = new Table();
		levelTable.setDebug(BlockBreaker.debug);
		for(int i = 0; i< 40; i++){
			for(int j = 0; j< 30; j++){
				ImageButton btn = new ImageButton(blank);
				btn.addListener(ldcl);
				levelTable.add(btn);
			}
			levelTable.row();
		}
		
		
		ImageButton btnBrick = new ImageButton(brickPic);
		btnBrick.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				currentObject = BRICK;			
			}
		});
		ImageButton btnLight = new ImageButton(lightImage);
		btnLight.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				currentObject = LIGHT;			
			}
		});
		ImageButton btnObstacleLeft = new ImageButton(obstacleImage);
		btnObstacleLeft.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				currentObject = LEFT;			
			}
		});
		ImageButton btnObstacleRight= new ImageButton(flipped);
		btnObstacleRight.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				currentObject = RIGHT;			
			}
		});
		ImageButton btnBack = new ImageButton(skin);
		btnBack.addListener(new ClickListener(){

			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				parent.changeScreen(BlockBreaker.MENU);
			}
			
		});
		
		
		// holds menu for selecting gui
		guiTable = new Table();
		guiTable.setDebug(false);
		guiTable.add(new Label("Add Items",skin));
		guiTable.row();
		guiTable.add(btnBrick);
		guiTable.row();
		guiTable.add(btnLight).width(OB_WIDTH).height(OB_HEIGHT).pad(10, 0, 0, 0);
		guiTable.row();
		guiTable.add(btnObstacleLeft).width(OB_WIDTH).height(OB_HEIGHT).pad(10, 0, 0, 0);
		guiTable.row();
		guiTable.add(btnObstacleRight).width(OB_WIDTH).height(OB_HEIGHT).pad(10, 0, 0, 0);
		guiTable.row();
		guiTable.add(btnBack);
		
		table.add(levelTable);
		table.add(guiTable);
		
		stage.addActor(table);
		
	}
	
	private void loadImages() {
		atlas = parent.assMan.manager.get("images/images.pack");
		atlasGui = parent.assMan.manager.get("gui/loadingGui.pack");
		
		bg = atlasGui.findRegion("background");
		
		skin = parent.assMan.manager.get("skin/bbskin.json",Skin.class);
		
		NinePatchDrawable npn = new NinePatchDrawable(atlasGui.createPatch("btn_norm"));
		NinePatchDrawable npo = new NinePatchDrawable(atlasGui.createPatch("btn_over"));
		NinePatchDrawable npd = new NinePatchDrawable(atlasGui.createPatch("btn_down"));

		textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = npn;
		textButtonStyle.down = npd;
		textButtonStyle.over = npo;
		textButtonStyle.checked = npd;
		textButtonStyle.font = parent.assMan.manager.get("font/visitor.fnt", BitmapFont.class);
		
		Pixmap pmap = new Pixmap(OB_WIDTH,OB_HEIGHT,Format.RGBA8888);
		pmap.setColor(Color.BLACK);
		pmap.drawRectangle(0, 0, OB_WIDTH, OB_HEIGHT);
		TextureRegion trBlank = new TextureRegion();
		trBlank.setRegion(new Texture(pmap));
		pmap.dispose();
		
		blank = new TextureRegionDrawable(trBlank);
		
		pmap = new Pixmap(OB_WIDTH,OB_HEIGHT, Pixmap.Format.RGBA8888);
		float r = (float) Math.random() * 0.7f+ 0.3f;
		float g = (float) Math.random() * 0.7f+ 0.3f;
		float b = (float) Math.random() * 0.7f+ 0.3f;
		pmap.setColor(new Color(r,g,b,0.3f));
		pmap.fill();
		pmap.setColor(r,g,b,1f);
		pmap.drawRectangle(0, 0, OB_WIDTH,OB_HEIGHT);
		
		TextureRegion trbrickPic = new TextureRegion();
		trbrickPic.setRegion(new Texture(pmap));
		
		brickPic = new TextureRegionDrawable(trbrickPic);
		
		pmap.dispose();
		
		lightImage = new TextureRegionDrawable(atlas.findRegion("lightBulb"));
		obstacleImage = new TextureRegionDrawable(atlas.findRegion("obstacle"));
		
		TextureRegion trflipped = new TextureRegion(atlas.findRegion("obstacle"));
		trflipped.flip(true, false);
		
		flipped = new TextureRegionDrawable(trflipped);
		
	}
	
	

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		
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
}
