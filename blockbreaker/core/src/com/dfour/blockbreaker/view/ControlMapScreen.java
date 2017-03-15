package com.dfour.blockbreaker.view;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dfour.blockbreaker.AppPreferences;
import com.dfour.blockbreaker.BlockBreaker;

public class ControlMapScreen extends Scene2DScreen{
	private AppPreferences prefs;
	private TextField txfControlLeft;
	private TextField txfControlRight;
	private TextField txfControlPush;
	private TextField txfControlPull;
	private TextField txfControlBomb;
	private TextField txfControlPause;
	private TextField txfControlQuit;
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
				parent.changeScreen(BlockBreaker.PREFERENCES);		
			}
		});
		
		btnDefault.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				prefs.setControlsLeft(Keys.LEFT);
				prefs.setControlsRight(Keys.RIGHT);
				prefs.setControlsUp(Keys.UP);
				prefs.setControlsDown(Keys.DOWN);
				prefs.setControlsBomb(Keys.B);
				prefs.setControlsPause(Keys.P);
				prefs.setControlsQuit(Keys.ESCAPE);
				
			}
		});
		
		btnSave.addListener(new ClickListener(){

			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				prefs.setControlsLeft(Keys.valueOf(txfControlLeft.getText()));
				prefs.setControlsRight(Keys.valueOf(txfControlRight.getText()));
				prefs.setControlsUp(Keys.valueOf(txfControlPush.getText()));
				prefs.setControlsDown(Keys.valueOf(txfControlPull.getText()));
				prefs.setControlsBomb(Keys.valueOf(txfControlBomb.getText()));
				prefs.setControlsPause(Keys.valueOf(txfControlPause.getText()));
				prefs.setControlsQuit(Keys.valueOf(txfControlQuit.getText()));
			}
			
		});
		
		Label lblControlLeft = new Label("Left",skin);
		Label lblControlRight = new Label("Right",skin);
		Label lblControlPush = new Label("Push",skin);
		Label lblControlPull = new Label("Pull",skin);
		Label lblControlBomb = new Label("Bomb",skin);
		Label lblControlPause = new Label("Pause",skin);
		Label lblControlQuit = new Label("Quit",skin);
		
		txfControlLeft = new TextField(Keys.toString(prefs.getControlsLeft()),skin);
		txfControlRight = new TextField(Keys.toString(prefs.getControlsRight()),skin);
		txfControlPush = new TextField(Keys.toString(prefs.getControlsUp()),skin);
		txfControlPull = new TextField(Keys.toString(prefs.getControlsDown()),skin);
		txfControlBomb = new TextField(Keys.toString(prefs.getControlsBomb()),skin);
		txfControlPause = new TextField(Keys.toString(prefs.getControlsPause()),skin);
		txfControlQuit = new TextField(Keys.toString(prefs.getControlsQuit()),skin);
		
		rootTable.add(lblControlLeft);
		rootTable.add(txfControlLeft);
		rootTable.row();
		rootTable.add(lblControlRight);
		rootTable.add(txfControlRight);
		rootTable.row();
		rootTable.add(lblControlPush);
		rootTable.add(txfControlPush);
		rootTable.row();
		rootTable.add(lblControlPull);
		rootTable.add(txfControlPull);
		rootTable.row();
		rootTable.add(lblControlBomb);
		rootTable.add(txfControlBomb);
		rootTable.row();
		rootTable.add(lblControlPause);
		rootTable.add(txfControlPause);
		rootTable.row();
		rootTable.add(lblControlQuit);
		rootTable.add(txfControlQuit);
		rootTable.row();
		
		rootTable.add(btnDefault);
		rootTable.add(btnSave);
		rootTable.row();
		rootTable.add(btnBack);
	}

	@Override
	public void render(float delta) {
		super.render(delta);
	}
}
