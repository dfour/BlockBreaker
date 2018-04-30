package com.dfour.blockbreaker.view;

import java.util.HashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.dfour.blockbreaker.BlockBreaker;

/**
 * A screen to allow users to pick a custom map to play on.
 * @author John Day johnday@gamedevelopment.blog
 */
public class CustomMapScreen extends Scene2DScreen{
	private Label errorLabel;
	private List<String> mapList;
	HashMap<String,String> maps;
	public CustomMapScreen(BlockBreaker p) {
		super(p);
	}
	
	@Override
	public void show(){
		super.show();
		
		maps = parent.getPreferences().getMaps();
		
		mapList = new List<String> (skin);
		mapList.setBounds(0, 0, 400, 300);
		errorLabel = new Label("Select a Map to play.",skin);
		
		
		if(maps.size() > 0){
			Array<String> mapNames = new Array<String>();
			for(Entry<String, String> singleMap:maps.entrySet()){
				mapNames.add(singleMap.getKey());
			}
			mapList.setItems(mapNames);
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
				BlockBreaker.customMap = maps.get(CustomMapScreen.this.mapList.getSelected());
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
