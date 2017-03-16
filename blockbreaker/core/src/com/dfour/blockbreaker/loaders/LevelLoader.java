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
		FileHandle file = Gdx.files.internal(folder+"level" + level + ".map");
		String text = file.readString().replaceAll("[\n\r]", ""); // remove new lines and carriage returns
		for (int y = 0; y < 30; y++) { // for each line of 30 lines
			for (int x = 0; x < 39; x++) { // for each row of 39 chars
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
				}
			}
		}
	}
	
	

}
