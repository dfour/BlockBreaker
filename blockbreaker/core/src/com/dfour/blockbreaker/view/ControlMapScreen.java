package com.dfour.blockbreaker.view;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dfour.blockbreaker.AppPreferences;
import com.dfour.blockbreaker.BlockBreaker;

public class ControlMapScreen extends Scene2DScreen{
	private AppPreferences prefs;
	private TextButton txfControlLeft;
	private TextButton txfControlRight;
	private TextButton txfControlPush;
	private TextButton txfControlPull;
	private TextButton txfControlBomb;
	private TextButton txfControlPause;
	private TextButton txfControlQuit;
	private TextButton txfControlRelease;
	
	private TextButton buttonToUpdate = null;
	
	
	public ControlMapScreen(BlockBreaker p){
		super(p);
		this.prefs = p.getPreferences();
	}

	@Override
	public void show() {
		super.show();
		TextButton btnBack = new TextButton("Back",skin);
		TextButton btnDefault = new TextButton("Default",skin);
		TextButton btnSave = new TextButton("Save",skin);
		
		btnBack.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				ControlMapScreen.this.returnScreen = BlockBreaker.PREFERENCES;
				ControlMapScreen.this.isReturning = true;		
			}
		});
		
		btnDefault.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				prefs.setControlsLeft(Keys.LEFT);
				prefs.setControlsRight(Keys.RIGHT);
				prefs.setControlsPush(Keys.UP);
				prefs.setControlsPull(Keys.DOWN);
				prefs.setControlsBomb(Keys.B);
				prefs.setControlsPause(Keys.P);
				prefs.setControlsQuit(Keys.ESCAPE);
				prefs.setControlsRelease(Keys.SPACE);
				
				txfControlLeft.setText(Keys.toString(Keys.LEFT));
				txfControlRight.setText(Keys.toString(Keys.RIGHT));
				txfControlPush.setText(Keys.toString(Keys.UP));
				txfControlPull.setText(Keys.toString(Keys.DOWN));
				txfControlBomb.setText(Keys.toString(Keys.B));
				txfControlRelease.setText(Keys.toString(Keys.SPACE));
				txfControlPause .setText(Keys.toString(Keys.P));
				txfControlQuit.setText(Keys.toString(Keys.ESCAPE));
				
				
			}
		});
		
		btnSave.addListener(new ClickListener(){

			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				prefs.setControlsLeft(Keys.valueOf(txfControlLeft.getText().toString()));
				prefs.setControlsRight(Keys.valueOf(txfControlRight.getText().toString()));
				prefs.setControlsPush(Keys.valueOf(txfControlPush.getText().toString()));
				prefs.setControlsPull(Keys.valueOf(txfControlPull.getText().toString()));
				prefs.setControlsBomb(Keys.valueOf(txfControlBomb.getText().toString()));
				prefs.setControlsRelease(Keys.valueOf(txfControlRelease.getText().toString()));
				prefs.setControlsPause(Keys.valueOf(txfControlPause.getText().toString()));
				prefs.setControlsQuit(Keys.valueOf(txfControlQuit.getText().toString()));
			}
			
		});
		
		Label lblControlLeft = new Label("Left",skin);
		Label lblControlRight = new Label("Right",skin);
		Label lblControlPush = new Label("Push",skin);
		Label lblControlPull = new Label("Pull",skin);
		Label lblControlBomb = new Label("Bomb",skin);
		Label lblControlRelease = new Label("Release",skin);
		Label lblControlPause = new Label("Pause",skin);
		Label lblControlQuit = new Label("Quit",skin);
		
		txfControlLeft = new TextButton(Keys.toString(prefs.getControlsLeft()),skin);
		txfControlRight = new TextButton(Keys.toString(prefs.getControlsRight()),skin);
		txfControlPush = new TextButton(Keys.toString(prefs.getControlsPush()),skin);
		txfControlPull = new TextButton(Keys.toString(prefs.getControlsPull()),skin);
		txfControlBomb = new TextButton(Keys.toString(prefs.getControlsBomb()),skin);
		txfControlRelease = new TextButton(Keys.toString(prefs.getControlsRelease()),skin);
		txfControlPause = new TextButton(Keys.toString(prefs.getControlsPause()),skin);
		txfControlQuit = new TextButton(Keys.toString(prefs.getControlsQuit()),skin);
		
		ClickListener cl = new ClickListener(){
			
			@Override
			public void clicked(InputEvent e, float x, float y) {
				System.out.println("Clicked:"+e.toString());
				//super.clicked(e, x, y);
				if(e.getTarget() instanceof TextButton){
					TextButton tb = (TextButton) e.getTarget();
					tb.setText("Set Key");
					buttonToUpdate = tb;
				}else if(e.getTarget().getParent() instanceof TextButton){
					TextButton tb = (TextButton) e.getTarget().getParent();
					tb.setText("Set Key");
					buttonToUpdate = tb;					
				}else{
					System.out.println(e.getTarget());
				}
			}
			
		};
		
		InputListener il = new InputListener(){
			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				System.out.println(keycode);
				if(buttonToUpdate != null){
					buttonToUpdate.setText(Keys.toString(keycode));
					buttonToUpdate = null;
				}
				return super.keyDown(event, keycode);
			}
		};
		
		stage.addListener(il);
		
		txfControlLeft.addListener(cl);
		txfControlRight.addListener(cl);
		txfControlPush.addListener(cl);
		txfControlPull.addListener(cl);
		txfControlBomb.addListener(cl);
		txfControlRelease.addListener(cl);
		txfControlPause.addListener(cl);
		txfControlQuit.addListener(cl);
		
		Table buttonTable = new Table();
	    buttonTable.add(btnBack).padRight(15);
	    buttonTable.add(btnDefault).padRight(15);
		buttonTable.add(btnSave);
		
		displayTable.add(lblControlLeft).width(200);
		displayTable.add(txfControlLeft).width(200);
		displayTable.row();
		displayTable.add(lblControlRight).width(200);
		displayTable.add(txfControlRight).width(200);
		displayTable.row();
		displayTable.add(lblControlPush).width(200);
		displayTable.add(txfControlPush).width(200);
		displayTable.row();
		displayTable.add(lblControlPull).width(200);
		displayTable.add(txfControlPull).width(200);
		displayTable.row();
		displayTable.add(lblControlBomb).width(200);
		displayTable.add(txfControlBomb).width(200);
		displayTable.row();
		displayTable.add(lblControlRelease).width(200);
		displayTable.add(txfControlRelease).width(200);
		displayTable.row();
		displayTable.add(lblControlPause).width(200);
		displayTable.add(txfControlPause).width(200);
		displayTable.row();
		displayTable.add(lblControlQuit).width(200);
		displayTable.add(txfControlQuit).width(200);
		displayTable.row().padTop(20);
		displayTable.add(buttonTable).colspan(2);
	}

	@Override
	public void render(float delta) {
		super.render(delta);
	}
}
