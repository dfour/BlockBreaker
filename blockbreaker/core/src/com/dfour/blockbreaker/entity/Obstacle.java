package com.dfour.blockbreaker.entity;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.physics.box2d.Body;
public class Obstacle extends Entity{
	
	public Obstacle(Body bod, AtlasRegion atlasRegion, boolean flip){
		super(bod,atlasRegion);
		sprite.flip(flip, false);
		sprite.setScale(0.32f);
	}

	@Override
	public void update() {
		super.update();
	}
}
