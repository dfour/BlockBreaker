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

public class MenuScreen extends Scene2DScreen {

	private TextButton btnStart;
	private TextButton btnPrefs;
	private TextButton btnQuit;
	private TextButton btnLevelDesign;
	private TextButton btnCustomGame;

	public MenuScreen(BlockBreaker p){
		super(p)	;
	}
	
	@Override
	public void show() {
		super.show();
		
		btnStart = new TextButton("Start", skin, "Large");
		btnPrefs = new TextButton("Preferences", skin, "Large");
		btnQuit = new TextButton("Exit", skin, "Large");
		btnLevelDesign = new TextButton("Level Designer", skin, "Large");
		btnCustomGame = new TextButton("Custom Game",skin, "Large");

        displayTable.add(btnStart).width(300).height(50);
        displayTable.row().pad(10, 0, 0, 0);
        displayTable.add(btnCustomGame).width(300).height(50);
        displayTable.row().pad(10, 0, 0, 0);
        displayTable.add(btnLevelDesign).width(300).height(50);
        displayTable.row().pad(10, 0, 0, 0);
        displayTable.add(btnPrefs).width(300).height(50);
        displayTable.row().pad(10, 0, 0, 0);
        displayTable.add(btnQuit).width(300).height(50);

		

		btnStart.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				BlockBreaker.isCustomMapMode = false;
				MenuScreen.this.returnScreen = BlockBreaker.APPLICATION;
				MenuScreen.this.isReturning = true;	
				}
		});
		
		btnPrefs.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				MenuScreen.this.returnScreen = BlockBreaker.PREFERENCES;
				MenuScreen.this.isReturning = true;	
			}
		});
		
		btnQuit.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				Gdx.app.exit();
			}
		});
		
		btnLevelDesign.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				MenuScreen.this.returnScreen = BlockBreaker.LEVEL_DESIGNER;
				MenuScreen.this.isReturning = true;	
			}
		});
		
		btnCustomGame.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				MenuScreen.this.returnScreen = BlockBreaker.CUSTOM_MAP;
				MenuScreen.this.isReturning = true;	
			}
		});
		
		btnStart.setChecked(false);
		btnPrefs.setChecked(false);
		btnQuit.setChecked(false);
		btnLevelDesign.setChecked(false);
		
	}
}
