package com.dfour.blockbreaker.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dfour.blockbreaker.BlockBreaker;

/**
 * A screen to allow users to pick a custom map to play on.
 * @author John Day johnday@gamedevelopment.blog
 */
public class CustomMapScreen extends Scene2DScreen{
	private FileHandle[] files;
	private Label errorLabel;
	private List<FileHandle> mapList;
	public CustomMapScreen(BlockBreaker p) {
		super(p);
	}
	
	@Override
	public void show(){
		super.show();
		
		mapList = new List<FileHandle>(skin);
		mapList.setBounds(0, 0, 400, 300);
		errorLabel = new Label("Select a Map to play.",skin);
		
		// search for custom map files in folder
		files = Gdx.files.external("blockbreaker/custommap/").list();
		if(files.length != 0){
			mapList.setItems(files);
		}else{
			errorLabel.setText("No Custom Maps Saved! \r\n Make some Custom Maps in the level designer!");
		}
		
		// add button to play selected map
		TextButton btnPlay = new TextButton("Play",skin);
		btnPlay.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				BlockBreaker.isCustomMapMode = true;
				BlockBreaker.customMaps = CustomMapScreen.this.mapList.getSelection().toArray();
				CustomMapScreen.this.returnScreen = BlockBreaker.APPLICATION;
				CustomMapScreen.this.isReturning = true;
			}
			
		});
		
		// add back button
		TextButton btnBack = new TextButton("Back",skin);
		btnBack.addListener(new ClickListener(){

			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				CustomMapScreen.this.returnScreen = BlockBreaker.MENU;
				CustomMapScreen.this.isReturning = true;
			}
			
		});
		
		// add all items to displayTable
		displayTable.add(errorLabel);
		displayTable.row().padTop(10);
		displayTable.add(mapList).width(500).height(400);
		displayTable.row().padTop(10);
		Table buttonTable = new Table();
		buttonTable.add(btnBack);
		buttonTable.add(btnPlay);
		displayTable.add(buttonTable);
	}
}
