package com.dfour.blockbreaker.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.dfour.blockbreaker.BBModel;
import com.dfour.blockbreaker.BlockBreaker;

public class ShopScreen implements Screen{
	
	// TODO add shop face using stage and buttons
	// mag str
	// mag pow
	// guide time
	// lazer time
	// extra ball
	// bomb count
	
	private BlockBreaker parent;
	private BBModel bbModel;
	
	private float fadeOut = 1f;
	private TextureAtlas atlasGui;
	private AtlasRegion bg;
	private Stage stage;
	private SpriteBatch pb;
	private int sw;
	private int sh;
	private float fadeIn;
	private Table table;
	private Table shopTable;
	
	private boolean isReturning = false;
	private Skin skin;
	private Object atlas;
	private Image title;
	private TextButtonStyle textButtonStyle;
	private TextButton btnExBall;
	private TextButton btnExLazer;
	private TextButton btnExGuide;
	private TextButton btnExMPower;
	private TextButton btnExMStrength;
	private Label lblExBall;
	private Label lblExLazer;
	private Label lblExGuide;
	private Label lblExMPower;
	private Label lblExMStrength;
	private Label lblExBallc;
	private Label lblExLazerc;
	private Label lblExGuidec;
	private Label lblExMPowerc;
	private Label lblExMStrengthc;
	private Label lblCash;
	private Label lblScore;
	private TextButton btnDone;
	
	public ShopScreen (BlockBreaker p, BBModel m){
		parent = p;
		bbModel = m;
		
		atlasGui = parent.assMan.manager.get("gui/loadingGui.pack");
		bg = atlasGui.findRegion("background");

	    stage = new Stage(new ScreenViewport());
		pb = new SpriteBatch();
	}


	@Override
	public void show() {
		fadeIn = 1f;
		fadeOut = 1f;
		
		loadImages();
		
		table = new Table();
		table.setFillParent(true);
		table.setDebug(true);	
		
		shopTable = new Table();
		shopTable.setDebug(true);
		
		Gdx.input.setInputProcessor(stage);
		skin = parent.assMan.manager.get("uiskin.json",Skin.class);
		
		// make button and labels for extra ball, longer lazer, longer guide, mag power, mag strength
		btnExBall = new TextButton(""+(5^bbModel.livesLeft) * 2, textButtonStyle);
		btnExBall.addListener(new EventListener() {
			@Override
			public boolean handle(Event e){
				if(bbModel.removeCash((5^bbModel.livesLeft) * 2)){
					bbModel.livesLeft += 1;
					btnExBall.setText(""+(5^bbModel.livesLeft) * 2);
					updateCash();
					btnExBall.setChecked(false);
					return true;
				}
				return false;
			}
		});
		
		btnExLazer = new TextButton(""+(5^(int)bbModel.baseLazerTimer), textButtonStyle);
		btnExLazer.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				if(bbModel.removeCash((5^(int)bbModel.baseLazerTimer))){
					bbModel.baseLazerTimer+=0.5f;
					btnExLazer.setText(""+(5^(int)bbModel.baseLazerTimer));
					updateCash();
					btnExLazer.setChecked(false);
				}
			}
		});
		
		btnExGuide = new TextButton(""+(5^(int)bbModel.baseGuideLazerTimer), textButtonStyle);
		btnExGuide.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				if(bbModel.removeCash((5^(int)bbModel.baseGuideLazerTimer))){
					bbModel.baseGuideLazerTimer+=0.5f;
					btnExGuide.setText(""+(5^(int)bbModel.baseGuideLazerTimer));
					updateCash();
					btnExGuide.setChecked(false);
				}
			}
		});
		
		btnExMPower = new TextButton(""+(5^((int)bbModel.baseMagnetPower)/ 200), textButtonStyle);
		btnExMPower.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				if(bbModel.removeCash((5^((int)bbModel.baseMagnetPower)/ 200))){
					bbModel.baseMagnetPower+=100;
					btnExMPower.setText(""+(5^((int)bbModel.baseMagnetPower)/ 200));
					updateCash();
					btnExMPower.setChecked(false);
				}
			}
		});
		
		btnExMStrength = new TextButton("50", textButtonStyle);
		btnExMStrength.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				if(bbModel.removeCash(50)){
					bbModel.baseMagnetStrength+=50;
					updateCash();
					btnExMStrength.setChecked(false);
				}
			}
		});
		
		btnDone = new TextButton("Done", textButtonStyle);
		btnDone.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				isReturning = true;	
				btnDone.setChecked(false);
			}
		});
		
		lblExBall = new Label("Extra Life",skin);
		lblExLazer = new Label("Longer Lasting Lazer Drill",skin);
		lblExGuide = new Label("Longer Lasting Guide",skin);
		lblExMPower = new Label("More Magnet Power Storage",skin);
		lblExMStrength = new Label("Stronger Magnets",skin);
		
		lblExBallc = new Label(""+bbModel.livesLeft,skin);
		lblExLazerc = new Label(""+bbModel.baseLazerTimer,skin);
		lblExGuidec = new Label(""+bbModel.baseGuideLazerTimer,skin);
		lblExMPowerc = new Label(""+bbModel.baseMagnetPower,skin);
		lblExMStrengthc = new Label(""+bbModel.baseMagnetStrength,skin);
		
		// label for cash
		updateCash();
		
		// label for score
		lblScore = new Label("Score:"+bbModel.score,skin);
		
		Image title = new Image(atlasGui.findRegion("blockBreakerTitle"));
        table.add(title).pad(10);
        table.row().expandY();
        
        shopTable.add(lblExBall).uniformX().align(Align.left);
        shopTable.add(lblExBallc).uniformX().align(Align.right);
        shopTable.add(btnExBall);
        shopTable.row();
        shopTable.add(lblExLazer).uniformX().align(Align.left);
        shopTable.add(lblExLazerc).uniformX().align(Align.right);
        shopTable.add(btnExLazer);
        shopTable.row();
        shopTable.add(lblExGuide).uniformX().align(Align.left);
        shopTable.add(lblExGuidec).uniformX().align(Align.right);
        shopTable.add(btnExGuide);
        shopTable.row();
        shopTable.add(lblExMPower).uniformX().align(Align.left);
        shopTable.add(lblExMPowerc).uniformX().align(Align.right);
        shopTable.add(btnExMPower);
        shopTable.row();
        shopTable.add(lblExMStrength).uniformX().align(Align.left);
        shopTable.add(lblExMStrengthc).uniformX().align(Align.right);
        shopTable.add(btnExMStrength);
        shopTable.row();
        shopTable.add(lblCash);
        shopTable.row();
        shopTable.add(lblScore);
        shopTable.row();
        shopTable.add(btnDone);
        
        table.add(shopTable);
        stage.addActor(table);
	}

	private void updateCash() {
		lblCash = new Label("£"+bbModel.cash,skin);
		lblExBallc = new Label(""+bbModel.livesLeft,skin);
		lblExLazerc = new Label(""+bbModel.baseLazerTimer,skin);
		lblExGuidec = new Label(""+bbModel.baseGuideLazerTimer,skin);
		lblExMPowerc = new Label(""+bbModel.baseMagnetPower,skin);
		lblExMStrengthc = new Label(""+bbModel.baseMagnetStrength,skin);
	}


	@Override
	public void render(float delta) {
		//clear screen
				Gdx.gl.glClearColor(.4f, .4f, .4f, 1);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				
				pb.begin();
				// draw brick background
				for(int i = 0; i < sw; i += 20){
					for(int j = 0; j < sh; j+= 10){
						pb.draw(bg, i, j,20,10);
					}
				}
				pb.end();
				
				if(fadeIn > 0){
					fadeIn -= delta;
					shopTable.setColor(1,1,1,1-fadeIn);
				}else if(this.isReturning){
					fadeOut -= delta;
					shopTable.setColor(1,1,1,fadeOut);
					if(fadeOut <= 0){
						parent.changeScreen(BlockBreaker.APPLICATION);
					}
				}

				stage.act();
				stage.draw();
		
	}

	@Override
	public void resize(int width, int height) {
		sw = width;
		sh = height;
		stage.getViewport().update(width, height, true);
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
		stage.dispose();
		skin.dispose();
	}
	
	private void loadImages() {
		atlas = parent.assMan.manager.get("images/images.pack");
		atlasGui = parent.assMan.manager.get("gui/loadingGui.pack");
		
		title = new Image(atlasGui.findRegion("blockBreakerTitle"));
		bg = atlasGui.findRegion("background");
		
		skin = new Skin();
		
		NinePatchDrawable npn = new NinePatchDrawable(atlasGui.createPatch("btn_norm"));
		NinePatchDrawable npo = new NinePatchDrawable(atlasGui.createPatch("btn_over"));
		NinePatchDrawable npd = new NinePatchDrawable(atlasGui.createPatch("btn_down"));

		textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = npn;
		textButtonStyle.down = npd;
		textButtonStyle.over = npo;
		textButtonStyle.checked = npd;
		textButtonStyle.font = parent.assMan.manager.get("font/visitor.fnt", BitmapFont.class);
		
		
	}

}
