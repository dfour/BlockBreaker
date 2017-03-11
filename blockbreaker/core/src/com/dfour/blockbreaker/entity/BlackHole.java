package com.dfour.blockbreaker.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.dfour.blockbreaker.BBModel;

public class BlackHole extends Entity{
	public boolean hasPartyEffect = false;
	public float lastPartyEffect = 0f;

	public BlackHole(Body bod, AtlasRegion atlasRegion ){
		this.body = bod;
		if(null == atlasRegion){
			Pixmap pmap = new Pixmap(20,20, Pixmap.Format.RGBA8888);
			pmap.setColor(Color.BLACK);
			pmap.fillCircle(10, 10,10);
			sprite = new Sprite(new Texture(pmap));
			pmap.dispose();
			
		}else{
			sprite = new Sprite(atlasRegion);
		}
		sprite.setX(bod.getPosition().x);
		sprite.setY(bod.getPosition().y);
		
		sprite.setOriginCenter();
		sprite.setScale(0.35f);
		sprite.setPosition(
				(body.getPosition().x) * BBModel.BOX_TO_WORLD -(20/2), 
				(body.getPosition().y)* BBModel.BOX_TO_WORLD -(20/2));

	}
	
	@Override
	public void update(){
		
	}
}
