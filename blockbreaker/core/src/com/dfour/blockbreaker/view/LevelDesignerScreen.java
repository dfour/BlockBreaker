package com.dfour.blockbreaker.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.dfour.blockbreaker.BlockBreaker;

public class LevelDesignerScreen extends Scene2DScreen {
	
	private static final String BRICK = "x";
	private static final String LIGHT = "s";
	private static final String LEFT = "/";
	private static final String RIGHT = "\\";
	private static final String	EMPTY = "-";
	private static final String SPINNERC = "c";
	private static final String SPINNERA = "a";
	private static final String BLACKHOLE = "b";
	private static final int OB_WIDTH = 30;
	private static final int OB_HEIGHT = 15;
	
	private String currentObject = EMPTY;
	
	//texs
	private TextureRegion brickPic;
	private TextureRegion blank;
	private TextureRegion lightImage;
	private TextureRegion obstacleImage;
	private TextureRegion flipped;
	private TextureAtlas atlas;
	private TextureRegion blackHole;
	private TextureRegion spinnerc;
	private TextureRegion spinnera;
	
	private Array<LevelBlock> levelMap;
	private TextureRegion levelTableBackground;
	
	public LevelDesignerScreen(BlockBreaker p){
		super(p);
	}
	
	@Override
	public void show(){
		super.show();
		levelMap = new Array<LevelBlock>();
		
		loadImages();
		
		ClickListener cl = new ClickListener(){

			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				if(event.getTarget() instanceof LevelBlock){
					LevelBlock lvb = (LevelBlock) event.getTarget();
					if(currentObject == BRICK){
						// i dont like this. try find another solution
						Pixmap pmap = new Pixmap(OB_WIDTH,OB_HEIGHT, Pixmap.Format.RGBA8888);
						float r = (float) Math.random() * 0.7f+ 0.3f;
						float g = (float) Math.random() * 0.7f+ 0.3f;
						float b = (float) Math.random() * 0.7f+ 0.3f;
						pmap.setColor(new Color(r,g,b,0.3f));
						pmap.fill();
						pmap.setColor(r,g,b,1f);
						pmap.drawRectangle(0, 0, OB_WIDTH,OB_HEIGHT);
						lvb.setImage(new TextureRegion(new Texture(pmap)), BRICK);
					}else if(currentObject == LIGHT){
						lvb.setImage(lightImage, LIGHT);
					}else if(currentObject == LEFT){
						lvb.setImage(obstacleImage, LEFT);
					}else if(currentObject == RIGHT){
						lvb.setImage(flipped, RIGHT);
					}else if(currentObject == EMPTY){
						lvb.setImage(blank, EMPTY);
					}else if(currentObject == SPINNERC){
						lvb.setImage(spinnerc, SPINNERC);
					}else if(currentObject == SPINNERA){
						lvb.setImage(spinnera, SPINNERA);
					}else if(currentObject == BLACKHOLE){
						lvb.setImage(blackHole, BLACKHOLE);
					}
				}
				
			}
			
		};
		
		// holds objects to represent level
		Table levelTable = new Table();
		levelTable.setDebug(BlockBreaker.debug);
		levelTable.setBackground(new TextureRegionDrawable(levelTableBackground));
		for(int i = 0; i< 30; i++){
			for(int j = 0; j< 39; j++){
				LevelBlock lvb = new LevelBlock(blank);
				lvb.setVisible(true);
				lvb.setBounds(0, 0, 20, 10);
				lvb.addListener(cl);
				levelTable.add(lvb);
				levelMap.add(lvb);
			}
			levelTable.row();
		}
				
		
		
		
		ImageButton btnBrick = new ImageButton(new TextureRegionDrawable(brickPic));
		btnBrick.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				currentObject = BRICK;		
			}
		});
		ImageButton btnLight = new ImageButton(new TextureRegionDrawable(lightImage));
		btnLight.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				currentObject = LIGHT;			
			}
		});
		ImageButton btnObstacleLeft = new ImageButton(new TextureRegionDrawable(obstacleImage));
		btnObstacleLeft.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				currentObject = LEFT;			
			}
		});
		ImageButton btnObstacleRight= new ImageButton(new TextureRegionDrawable(flipped));
		btnObstacleRight.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				currentObject = RIGHT;			
			}
		});
		
		ImageButton btnSpinnerA= new ImageButton(new TextureRegionDrawable(spinnera));
		btnSpinnerA.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				currentObject = SPINNERA;			
			}
		});
		
		ImageButton btnSpinnerC= new ImageButton(new TextureRegionDrawable(spinnerc));
		btnSpinnerC.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				currentObject = SPINNERC;			
			}
		});
		
		ImageButton btnBlackHole= new ImageButton(new TextureRegionDrawable(blackHole));
		btnBlackHole.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				currentObject = BLACKHOLE;			
			}
		});
		
		
		TextButton btnBack = new TextButton("Back",skin);
		btnBack.addListener(new ClickListener(){

			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				LevelDesignerScreen.this.returnScreen = BlockBreaker.MENU;
				LevelDesignerScreen.this.isReturning = true;
			}
			
		});
		
		TextButton btnSave = new TextButton("Save",skin);
		btnSave.addListener(new ClickListener(){

			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				// loop through levelMap and print chars
				String map = "";
				int col = 0;
				for(LevelBlock lvb: levelMap){
					map+=lvb.obstacle;
					col++;
					if(col == 39){
						col = 0;
						map+=System.lineSeparator();
					}
				}
				FileHandle file = Gdx.files.external("mymap.map");
				file.writeString(map, false);
			}
			
		});
		
		Table guiTable = new Table();
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
		guiTable.add(btnSpinnerA).width(OB_WIDTH).height(OB_HEIGHT).pad(10, 0, 0, 0);
		guiTable.row();
		guiTable.add(btnSpinnerC).width(OB_WIDTH).height(OB_HEIGHT).pad(10, 0, 0, 0);
		guiTable.row();
		guiTable.add(btnBlackHole).width(OB_WIDTH).height(OB_HEIGHT).pad(10, 0, 0, 0);
		guiTable.row();
		guiTable.add(btnBack);
		guiTable.row();
		guiTable.add(btnSave);
		
		displayTable.add(levelTable);
		displayTable.add(guiTable);	
	}
	
	private void loadImages() {
		atlas = parent.assMan.manager.get("images/images.pack");
		skin = parent.assMan.manager.get("skin/bbskin.json",Skin.class);

		
		Pixmap pmap = new Pixmap(OB_WIDTH,OB_HEIGHT,Format.RGBA8888);
		pmap.setColor(0.4f,0.4f,0.4f,1);
		pmap.fill();
		pmap.setColor(0,0,0,0.4f);
		pmap.drawRectangle(0, 0, OB_WIDTH, OB_HEIGHT);
		blank = new TextureRegion(new Texture(pmap));
		
		pmap = new Pixmap(1,1,Format.RGB888);
		pmap.setColor(.4f,.4f,.4f,1);
		pmap.fill();
		levelTableBackground = new TextureRegion(new Texture(pmap)); 
		
		
		pmap = new Pixmap(OB_WIDTH,OB_HEIGHT, Pixmap.Format.RGBA8888);
		float r = (float) Math.random() * 0.7f+ 0.3f;
		float g = (float) Math.random() * 0.7f+ 0.3f;
		float b = (float) Math.random() * 0.7f+ 0.3f;
		pmap.setColor(new Color(r,g,b,0.3f));
		pmap.fill();
		pmap.setColor(r,g,b,1f);
		pmap.drawRectangle(0, 0, OB_WIDTH,OB_HEIGHT);
		brickPic = new TextureRegion(new Texture(pmap)); 
		
		
		pmap = new Pixmap(10,10, Format.RGBA8888);
		pmap.setColor(Color.RED);
		pmap.fill();
		spinnerc = new TextureRegion(new Texture(pmap));
		
		pmap = new Pixmap(10,10, Format.RGBA8888);
		pmap.setColor(Color.BLUE);
		pmap.fill();
		spinnera = new TextureRegion(new Texture(pmap));
		
		pmap.dispose();
		
		lightImage = atlas.findRegion("lightBulb");
		obstacleImage = atlas.findRegion("obstacle");
		flipped = new TextureRegion(atlas.findRegion("obstacle"));
		blackHole = atlas.findRegion("blackhole");
		flipped.flip(true, false);
	}
	
	private class LevelBlock extends Actor{
		public String obstacle = "-";
		private TextureRegion defaultRegion;
		public LevelBlock (TextureRegion reg){
			defaultRegion = reg;
		}
		@Override
		public void draw(Batch batch, float parentAlpha) {
			super.draw(batch, parentAlpha);
			batch.draw(defaultRegion, getX(), getY(), getOriginX(), getOriginY(),
		            getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
		}
		
		public void setImage(TextureRegion region, String type){
			this.defaultRegion = region;
			this.obstacle = type;
		}
	}
	
}
