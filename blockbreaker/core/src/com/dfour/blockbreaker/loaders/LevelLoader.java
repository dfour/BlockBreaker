package com.dfour.blockbreaker.loaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.dfour.blockbreaker.BBModel;

public class LevelLoader {
	
	private String folder;
	private BBModel model;
		
	public LevelLoader(String fol, BBModel model){
		folder = fol;
		this.model = model;
	}
	
	public void loadLevel(int level){
		loadLevelFile(folder+"level" + level + ".map", true);
	}
	
	public void loadLevelString(String mapData){
		String text = mapData.replaceAll("[\n\r]", "");
		//space for 10 portals
		int[] portalPairs = {0,1,2,3,4,5,6,7,8,9};
		int portalCount = 0;
		// if text.len > 1170 (old)
		System.out.println(text.length());
		if(text.length() >1170){
			for(int i =0; i < 10; i++){
				portalPairs[i] = Integer.valueOf(String.valueOf(text.charAt(1170+i)));
			}
		}
		System.out.println(portalPairs);
		
		for (int y = 0; y < 30; y++) { // for each line of 30 lines
			for (int x = 0; x < 39; x++) { // for each row of 39 chars + 0
				char coord = text.charAt((y * 39) + x); // current line * 39 chars per line + amount of chars in
				if (coord == 'x') {
					model.entFactory.makeBrick(x * 2 +1, (Math.abs(y - 30)) * 2); // each brick is 2 high and 2 wide
				} else if (coord == 's') {
					model.entFactory.makeLight(x * 2+1, (Math.abs(y - 30)) * 2);
				}else if (coord == '/') {
					model.entFactory.makeStaticObstacle(x * 2+1, (Math.abs(y - 30)) * 2,false);
				}else if (coord == '\\') {
					model.entFactory.makeStaticObstacle(x * 2+1, (Math.abs(y - 30)) * 2, true);
				}else if (coord == 'c') {
					model.entFactory.addSpinner(x * 2+1, (Math.abs(y - 30)) * 2, true);
				}else if (coord == 'a') {
					model.entFactory.addSpinner(x * 2+1, (Math.abs(y - 30)) * 2, false);
				}else if (coord == 'b'){
					model.entFactory.addBlackHole(x * 2+1, (Math.abs(y - 30)) * 2);
				}else if(coord == 'p') {
					model.entFactory.makePowerBrick(x * 2+1, (Math.abs(y - 30)) * 2);
				}else if(coord == '>') {
					model.entFactory.makeSpeedZone(x * 2+1, (Math.abs(y - 30)) * 2);
				}else if(coord == '<') {
					model.entFactory.makeSlowZone(x * 2+1, (Math.abs(y - 30)) * 2);
				}else if(coord == 'o') {
					model.entFactory.makePortal(x * 2+1, (Math.abs(y - 30)) * 2);
					portalCount++;
				}
			}
		}
		// All Loaded now pair portals
		for(int i = 0; i < portalCount; i++){
			model.entFactory.pairPortals(i,portalPairs[i]);
		}
	}
	
	
	public void loadLevelFile(String filename, boolean internal){
		FileHandle file = null;
		String text = "";
		if(internal){
			file = Gdx.files.internal(filename);
			text = file.readString(); // remove new lines and carriage returns
		}else{
			text = filename;
		}
		loadLevelString(text);
		
	}
	
	

}
