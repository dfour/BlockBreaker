package com.dfour.blockbreaker.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
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
	private static final String POWER_BRICK = "p";
	private static final String SPEEDZONE = ">";
	private static final String SLOWZONE = "<";
	private static final int OB_WIDTH = 30;
	private static final int OB_HEIGHT = 15;
	private static final int BTNW = 50;
	private static final int BTNH = 50;
	
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
	private TextField txfMapName;
	private Label savedLabel;
	private float savedLabelTimer = 0;
	private TextureRegion speeduppic;
	private TextureRegion slowdownpic;
	
	public LevelDesignerScreen(BlockBreaker p){
		super(p);
	}
	
	@Override
	public void show(){
		super.show();
		levelMap = new Array<LevelBlock>();
		savedLabel = new Label("Map Saved",skin);
		savedLabel.setVisible(false);
		loadImages();
		
		ClickListener cl = new ClickListener(){

			@Override
			public void enter(InputEvent event, float x, float y, int pointer,Actor fromActor) {
				super.enter(event, x, y, pointer, fromActor);
				if(pointer == 0){
					clicked(event,x,y);
				}
			}

			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				if(event.getTarget() instanceof LevelBlock){
					LevelBlock lvb = (LevelBlock) event.getTarget();
					lvb.setLevelBlock();
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
				if(i !=0 && i < 28){
					lvb.addListener(cl);
				}
				levelTable.add(lvb);
				levelMap.add(lvb);
			}
			levelTable.row();
		}
		
		txfMapName = new TextField("map-name",skin);
		txfMapName.addListener(new ClickListener(){

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				if(txfMapName.getText().equalsIgnoreCase("map-name")){
					txfMapName.setText("");
				}
				return super.touchDown(event, x, y, pointer, button);
			}	
		});
		
		
		
		
		ImageButton btnBrick = new ImageButton(new ImageButtonStyle(skin.get(ImageButtonStyle.class)));
		btnBrick.addListener(new TextTooltip("Normal Brick",skin));
		btnBrick.getStyle().imageUp = new TextureRegionDrawable(brickPic);
		btnBrick.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				currentObject = BRICK;		
			}
		});
		ImageButton btnLight = new ImageButton(new ImageButtonStyle(skin.get(ImageButtonStyle.class)));
		btnLight.addListener(new TextTooltip("Light",skin));
		btnLight.getStyle().imageUp = new TextureRegionDrawable(lightImage);
		btnLight.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				currentObject = LIGHT;			
			}
		});
		ImageButton btnObstacleLeft = new ImageButton(new ImageButtonStyle(skin.get(ImageButtonStyle.class)));
		btnObstacleLeft.addListener(new TextTooltip("Diagonal Platform",skin));
		btnObstacleLeft.getStyle().imageUp =new TextureRegionDrawable(obstacleImage);
		btnObstacleLeft.setHeight(10);
		btnObstacleLeft.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				currentObject = LEFT;			
			}
		});
		ImageButton btnObstacleRight= new ImageButton(new ImageButtonStyle(skin.get(ImageButtonStyle.class)));
		btnObstacleRight.addListener(new TextTooltip("Reverse Diagonal Platform",skin));
		btnObstacleRight.getStyle().imageUp = new TextureRegionDrawable(flipped);
		btnObstacleRight.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				currentObject = RIGHT;			
			}
		});
		
		ImageButton btnSpinnerA= new ImageButton(new ImageButtonStyle(skin.get(ImageButtonStyle.class)));
		btnSpinnerA.addListener(new TextTooltip("Anti-Clockwise Spinner",skin));
		btnSpinnerA.getStyle().imageUp =new TextureRegionDrawable(spinnera);
		btnSpinnerA.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				currentObject = SPINNERA;			
			}
		});
		
		ImageButton btnSpinnerC= new ImageButton(new ImageButtonStyle(skin.get(ImageButtonStyle.class)));
		btnSpinnerC.addListener(new TextTooltip("Clockwise Spinner",skin));
		btnSpinnerC.getStyle().imageUp =new TextureRegionDrawable(spinnerc);
		btnSpinnerC.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				currentObject = SPINNERC;			
			}
		});
		
		ImageButton btnBlackHole= new ImageButton(new ImageButtonStyle(skin.get(ImageButtonStyle.class)));
		btnBlackHole.addListener(new TextTooltip("Black Hole",skin));
		btnBlackHole.getStyle().imageUp =new TextureRegionDrawable(blackHole);
		btnBlackHole.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				currentObject = BLACKHOLE;			
			}
		});
		
		ImageButton btnPowerBrick = new ImageButton(new ImageButtonStyle(skin.get(ImageButtonStyle.class)));
		btnPowerBrick.addListener(new TextTooltip("Power Up Brick",skin));
		btnPowerBrick.getStyle().imageUp = new TextureRegionDrawable(brickPic);
		btnPowerBrick.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				currentObject = POWER_BRICK;
			}
			
		});
		
		ImageButton btnSpeedZone = new ImageButton(new ImageButtonStyle(skin.get(ImageButtonStyle.class)));
		btnSpeedZone.addListener(new TextTooltip("Speed Up Zone",skin));
		btnSpeedZone.getStyle().imageUp = new TextureRegionDrawable(speeduppic);
		btnSpeedZone.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				currentObject = SPEEDZONE;
			}
			
		});
		
		ImageButton btnSlowZone = new ImageButton(new ImageButtonStyle(skin.get(ImageButtonStyle.class)));
		btnSlowZone.addListener(new TextTooltip("Slow Down Zone",skin));
		btnSlowZone.getStyle().imageUp = new TextureRegionDrawable(slowdownpic);
		btnSlowZone.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				currentObject = SLOWZONE;
			}
			
		});
		
		TextButton btnRemove = new TextButton("Erase",skin);
		btnRemove.addListener(new TextTooltip("Remove Blocks",skin));
		btnRemove.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				currentObject = EMPTY;
			}
		});
		
		TextButton btnBack = new TextButton("Back",skin);
		btnBack.addListener(new TextTooltip("Back to Menu",skin));
		btnBack.addListener(new ClickListener(){

			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				LevelDesignerScreen.this.returnScreen = BlockBreaker.MENU;
				LevelDesignerScreen.this.isReturning = true;
			}
			
		});
		
		TextButton btnClear = new TextButton("Reset",skin);
		btnClear.addListener(new TextTooltip("Clear Map",skin));
		btnClear.addListener(new ClickListener(){

			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				currentObject = EMPTY;
				for(LevelBlock lvb: levelMap){
					lvb.setLevelBlock();
				}
			}
			
		});
		
		TextButton btnSave = new TextButton("Save",skin);
		btnSave.addListener(new TextTooltip("Save Map",skin));
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
						map+="\n";
					}
				}
				FileHandle file = Gdx.files.external("blockbreaker/custommap/"+txfMapName.getText()+".map");
				file.writeString(map, false);
				savedLabelTimer = 5f;
				savedLabel.setVisible(true);
			}
			
		});
		
		Table guiTable = new Table();
		guiTable.setDebug(false);
		guiTable.add(new Label("Objects",skin)).colspan(6);
		guiTable.row();
		guiTable.add(btnBrick).height(BTNH).width(BTNW);
		guiTable.add(btnLight).height(BTNH).width(BTNW);
		guiTable.add(btnObstacleLeft).height(BTNH).width(BTNW);
		guiTable.add(btnSpinnerA).height(BTNH).width(BTNW);
		guiTable.add(btnSpeedZone).height(BTNH).width(BTNW);
		guiTable.add(btnRemove).height(BTNH).width(BTNW*2);
		guiTable.add(txfMapName).height(BTNH).width(BTNW*2);
		guiTable.add(btnClear).height(BTNH).width(BTNW*2);
		guiTable.row();
		guiTable.add(btnPowerBrick).height(BTNH).width(BTNW);
		guiTable.add(btnBlackHole).height(BTNH).width(BTNW);
		guiTable.add(btnObstacleRight).height(BTNH).width(BTNW);
		guiTable.add(btnSpinnerC).height(BTNH).width(BTNW);
		guiTable.add(btnSlowZone).height(BTNH).width(BTNW);
		guiTable.add(btnSave).height(BTNH).width(BTNW*2).colspan(2).right();
		guiTable.add(btnBack).height(BTNH).width(BTNW*2);
		guiTable.row();
		guiTable.add(savedLabel).colspan(8);
		
		displayTable.add(levelTable).pad(10);
		displayTable.row();
		displayTable.add(guiTable).pad(10);
	}
	
	
	
	@Override
	public void render(float delta) {
		super.render(delta);
		if(savedLabelTimer > 0){
			savedLabelTimer-= delta;
		}else if(savedLabelTimer <= 0 && savedLabel.isVisible()){
			savedLabel.setVisible(false);
		}
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
		slowdownpic = new TextureRegion(new Texture(pmap));
		
		pmap = new Pixmap(10,10, Format.RGBA8888);
		pmap.setColor(Color.BLUE);
		pmap.fill();
		speeduppic = new TextureRegion(new Texture(pmap));
		
		pmap.dispose();
		
		spinnera = atlas.findRegion("spinner");
		spinnerc = spinnera;
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
			if(this.obstacle == POWER_BRICK){
				float r = (float) Math.random() * 0.7f+ 0.3f;
				float g = (float) Math.random() * 0.7f+ 0.3f;
				float b = (float) Math.random() * 0.7f+ 0.3f;
				batch.setColor(r,g,b,1);
			}else{
				batch.setColor(1,1,1,1);
			}
			batch.draw(defaultRegion, getX(), getY(), getOriginX(), getOriginY(),
		            getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());

		}
		
		public void setImage(TextureRegion region, String type){
			this.defaultRegion = region;
			this.obstacle = type;
		}
		
		public void setLevelBlock(){
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
				setImage(new TextureRegion(new Texture(pmap)), BRICK);
				pmap.dispose();
			}else if(currentObject == LIGHT){
				setImage(lightImage, LIGHT);
			}else if(currentObject == LEFT){
				setImage(obstacleImage, LEFT);
			}else if(currentObject == RIGHT){
				setImage(flipped, RIGHT);
			}else if(currentObject == EMPTY){
				setImage(blank, EMPTY);
			}else if(currentObject == SPINNERC){
				setImage(spinnerc, SPINNERC);
			}else if(currentObject == SPINNERA){
				setImage(spinnera, SPINNERA);
			}else if(currentObject == BLACKHOLE){
				setImage(blackHole, BLACKHOLE);
			}else if(currentObject == SPEEDZONE){
				setImage(speeduppic,SPEEDZONE );
			}else if(currentObject == SLOWZONE){
				setImage(slowdownpic, SLOWZONE);
			}else if(currentObject == POWER_BRICK){
				Pixmap pmap = new Pixmap(OB_WIDTH,OB_HEIGHT, Pixmap.Format.RGBA8888);
				pmap.setColor(new Color(1,1,1,0.3f));
				pmap.fill();
				pmap.setColor(1,1,1,1);
				pmap.drawRectangle(0, 0, OB_WIDTH,OB_HEIGHT);
				setImage(new TextureRegion(new Texture(pmap)),POWER_BRICK);
				pmap.dispose();
			}
		}
	}
}


