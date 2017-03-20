package com.dfour.blockbreaker.entity;

import box2dLight.PointLight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class LightBall extends Entity{
	public PointLight light;

	public LightBall(Body bod, AtlasRegion tex) {
		super(bod,tex);
		float r = (float) Math.random() * 0.5f+ 0.5f;
		float g = (float) Math.random() * 0.5f+ 0.5f;
		float b = (float) Math.random() * 0.5f+ 0.5f;
		sprite.setColor(r, g, b, 1f);
		sprite.setScale(0.8f);
	}
	
	public LightBall(Body bod, AtlasRegion tex, Color col) {
		super(bod,tex);
		col.add(0.5f,0.5f,0.5f,1);
		sprite.setColor(col);
		sprite.setScale(0.8f);
	}
	
	@Override
	public void update(){
		super.update();
	}
}
