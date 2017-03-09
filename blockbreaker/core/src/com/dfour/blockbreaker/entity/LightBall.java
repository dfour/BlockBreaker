package com.dfour.blockbreaker.entity;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.dfour.blockbreaker.BBModel;

public class LightBall extends Entity{
	public PointLight light;

	public LightBall(Body bod, AtlasRegion tex) {
		this.body = bod;
		this.width = tex.getRegionWidth();
		this.height = tex.getRegionHeight();
		this.sprite = new Sprite(tex);
		this.update();
		float r = (float) Math.random() * 0.5f+ 0.5f;
		float g = (float) Math.random() * 0.5f+ 0.5f;
		float b = (float) Math.random() * 0.5f+ 0.5f;
		sprite.setColor(r, g, b, 1f);
		sprite.setOriginCenter();
		sprite.setScale(0.8f);
	}
	
	@Override
	public void update(){
		sprite.setPosition(
			(body.getPosition().x) * BBModel.BOX_TO_WORLD -14, 
			(body.getPosition().y)* BBModel.BOX_TO_WORLD -14);
	}
}
