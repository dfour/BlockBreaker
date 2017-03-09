package com.dfour.blockbreaker.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.dfour.blockbreaker.BBModel;

public class Obstacle extends Entity{
	
	public Obstacle(Body bod, AtlasRegion atlasRegion, boolean flip){
		
		if(null == atlasRegion){
			Pixmap pmap = new Pixmap(10,10, Pixmap.Format.RGBA8888);
			pmap.setColor(Color.FIREBRICK);
			pmap.fill();
			sprite = new Sprite(new Texture(pmap));
			
		}else{
			sprite = new Sprite(atlasRegion);
		}
		sprite.flip(flip, false);
		
		this.body = bod;
		
		sprite.setX(bod.getPosition().x);
		sprite.setY(bod.getPosition().y);
		
		sprite.setOriginCenter();
		sprite.setScale(0.35f);
		sprite.setPosition(
				(body.getPosition().x) * BBModel.BOX_TO_WORLD -(55/2), 
				(body.getPosition().y)* BBModel.BOX_TO_WORLD -(45/2));
	}
}
