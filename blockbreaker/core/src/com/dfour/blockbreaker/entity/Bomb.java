package com.dfour.blockbreaker.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Bomb extends Entity{
	public boolean isDead = false;
	
	public Bomb(Body bod, AtlasRegion atlasRegion){
		this.body = bod;
		//bod.getFixtureList().first().getFilterData().groupIndex = -1;
		bod.setUserData(this);
		this.sprite = new Sprite(atlasRegion);
		this.width = atlasRegion.getRegionWidth();
		this.height = atlasRegion.getRegionHeight();
		sprite.setOriginCenter();
		sprite.setSize(10f, 10f);
	}

	public void update(){
		body.applyForceToCenter(new Vector2(0, 150),true);
		sprite.setPosition((body.getPosition().x - .5f)* 10, (body.getPosition().y - .5f) * 10);
	}
}
