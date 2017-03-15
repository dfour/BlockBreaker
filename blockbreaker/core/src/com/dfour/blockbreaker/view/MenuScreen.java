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

	private TextButton button;
	private TextButton button2;
	private TextButton button3;
	private TextButton button4;

	public MenuScreen(BlockBreaker p){
		super(p)	;
	}
	
	@Override
	public void show() {
		super.show();
		
		button = new TextButton("Start", skin);
		button2 = new TextButton("Preferences", skin);
		button3 = new TextButton("Exit", skin);
		button4 = new TextButton("Level Designer", skin);

        displayTable.add(button).width(300).height(50);
        displayTable.row().pad(10, 0, 10, 0);
        displayTable.add(button4).width(300).height(50);
        displayTable.row();
        displayTable.add(button2).width(300).height(50);
        displayTable.row().pad(10, 0, 10, 0);;
        displayTable.add(button3).width(300).height(50);

		

		button.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				MenuScreen.this.returnScreen = BlockBreaker.APPLICATION;
				MenuScreen.this.isReturning = true;	
				}
		});
		
		button2.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				MenuScreen.this.returnScreen = BlockBreaker.PREFERENCES;
				MenuScreen.this.isReturning = true;	
			}
		});
		
		button3.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				Gdx.app.exit();
			}
		});
		
		button4.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				MenuScreen.this.returnScreen = BlockBreaker.LEVEL_DESIGNER;
				MenuScreen.this.isReturning = true;	
			}
		});
		
		button.setChecked(false);
		button2.setChecked(false);
		button3.setChecked(false);
		button4.setChecked(false);
		
	}
}
