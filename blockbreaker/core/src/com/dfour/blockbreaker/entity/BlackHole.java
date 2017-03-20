package com.dfour.blockbreaker.entity;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class BlackHole extends LocalEffectEntity{
	public float lastPartyEffect = 0f;

	public BlackHole(Body bod, AtlasRegion atlasRegion ){
		super(bod,atlasRegion);
		sprite.setScale(0.5f);
	}
	
	@Override
	public void update(){
		super.update();
		for(Body body : bodiesInEffectedZone){
			float velx = this.body.getPosition().x - body.getPosition().x;
			float vely = this.body.getPosition().y - body.getPosition().y;
			float length = (float) Math.sqrt(velx * velx + vely * vely);
			if (length != 0) {
				velx = velx / length;
				vely = vely / length;
			}
			body.applyForceToCenter(new Vector2(velx * (75 - length * 2), vely * (75 - length * 2)), true);	
		}
		
	}
}
