package com.dfour.blockbreaker.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;

public class Portal extends Entity{
	private Portal linkedPortal;
	private Array<Body> bodiesToMove = new Array<Body>();


	public Portal(Body b, Animation anim) {
		super(b, anim);
	}
	
	public Portal(Body b, AtlasRegion tex){
		super(b,tex);
	}
	
	public void setExit(Portal p){
		linkedPortal = p;
	}

	@Override
	public void update(){
		super.update();
		for(Body bod:bodiesToMove){
			Vector2 position = new Vector2(body.getPosition().x - bod.getPosition().x ,body.getPosition().y - bod.getPosition().y);
			position.rotate(180);
			position.scl(2f);
			bod.setTransform(linkedPortal.body.getPosition().x+ position.x,
					linkedPortal.body.getPosition().y + position.y,
					body.getAngle());
			bodiesToMove.removeValue(bod, true);
		}
	}

	public void bodyInPortal(Body body) {
		bodiesToMove.add(body);
		
		
	}
}
