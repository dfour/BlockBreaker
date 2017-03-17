package com.dfour.blockbreaker.entity;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Bomb extends Entity{
	public boolean isDead = false;
	
	public Bomb(Body bod, AtlasRegion atlasRegion){
		super(bod,atlasRegion);
		bod.setUserData(this);
		sprite.setScale(0.5f);
	}

	public void update(){
		super.update();
		body.applyForceToCenter(new Vector2(0, 150),true);
	}
}
