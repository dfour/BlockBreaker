package com.dfour.blockbreaker.entity;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.dfour.blockbreaker.BBModel;

public class Spinner extends Entity {
	
	public Spinner(Body bod, AtlasRegion atlasRegion, boolean clockwise){
		super(bod,atlasRegion);
		if(clockwise){
			this.body.setAngularVelocity(2f);
		}else{
			this.body.setAngularVelocity(-2f);
		}
	}

	@Override
	public void update(){
		super.update();
	}
}
