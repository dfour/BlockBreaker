package com.dfour.blockbreaker;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class IconBar extends Table {
	private Skin skin;
	private ProgressBar pb;
	
	public IconBar(Image im, Skin skn){
		skin = skn;
		this.add(im);
		pb = new ProgressBar(0, 100, 0.1f, false, skin,"big");
		pb.setFillParent(true);
		this.add(pb).width(300);
		
	}

	@Override
	public void act(float delta) {
		super.act(delta);
	}
	
	public void updateProgress(float progress){
		pb.setValue(progress * 100);
	}
	
}
