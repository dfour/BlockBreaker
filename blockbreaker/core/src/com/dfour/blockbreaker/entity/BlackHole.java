package com.dfour.blockbreaker.entity;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.dfour.blockbreaker.BBModel;

public class BlackHole extends Entity{
	public boolean hasPartyEffect = false;
	public float lastPartyEffect = 0f;
	private Array<Body> bodiesInGravity = new Array<Body>();

	public BlackHole(Body bod, AtlasRegion atlasRegion ){
		super(bod,atlasRegion);
		sprite.setScale(0.35f);
	}
	
	@Override
	public void update(){
		super.update();
		for(Body body : bodiesInGravity){
			float velx = this.body.getPosition().x - body.getPosition().x;
			float vely = this.body.getPosition().y - body.getPosition().y;
			float length = (float) Math.sqrt(velx * velx + vely * vely);
			if (length != 0) {
				velx = velx / length;
				vely = vely / length;
			}

			body.applyForceToCenter(new Vector2(velx * 150, vely * 150), true);	
		}
		
	}

	public void gravitise(Body body) {
		if(bodiesInGravity.indexOf(body, true) == -1){
			bodiesInGravity.add(body);
		}
	}
	
	public void unGravitise(Body body){
		if(bodiesInGravity.indexOf(body, true) >= 0 ){
			bodiesInGravity.removeValue(body, true);
		}
	}
}
