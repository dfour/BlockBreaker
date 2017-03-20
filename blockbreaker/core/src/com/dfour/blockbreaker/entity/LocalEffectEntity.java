package com.dfour.blockbreaker.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;

public class LocalEffectEntity extends Entity {
	public boolean hasPartyEffect = false;
	protected Array<Body> bodiesInEffectedZone = new Array<Body>();

	
	public LocalEffectEntity(Body b, AtlasRegion tex) {
		super(b, tex);
	}
	
	public LocalEffectEntity(Body b, Animation anim) {
		super(b, anim);
	}
	
	
	@Override
	public void update(){
		super.update();
	}

	public void addToEffectedObjects(Body body) {
		if(bodiesInEffectedZone.indexOf(body, true) == -1){
			bodiesInEffectedZone.add(body);
		}
	}
	
	public void removeFromEffectedObjects(Body body){
		if(bodiesInEffectedZone.indexOf(body, true) >= 0 ){
			bodiesInEffectedZone.removeValue(body, true);
		}
	}
}
