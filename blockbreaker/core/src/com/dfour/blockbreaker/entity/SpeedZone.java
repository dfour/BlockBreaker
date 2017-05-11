package com.dfour.blockbreaker.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class SpeedZone extends LocalEffectEntity{
	private float speedMod = 1;
	
	public SpeedZone(Body bod, AtlasRegion atlasRegion, float speedModifier) {
		super(bod, atlasRegion);
		speedMod = speedModifier;
		if(speedMod > 1){
			this.color = new Color(0.2f,0.2f,1,0.3f);
		}else{
			this.color = new Color(1,0.2f,0.2f,0.3f);
		}
		sprite.setColor(this.color);
		sprite.scale(0.7f);
		hasPartyEffect = true;
		body.setAngularVelocity(-10f);
	}


	@Override
	public void addToEffectedObjects(Body body) {
		super.addToEffectedObjects(body);
		setSpeedModifier(body,speedMod);
	}

	@Override
	public void removeFromEffectedObjects(Body body) {
		super.removeFromEffectedObjects(body);
		setSpeedModifier(body,1f);
	}
	
	private void setSpeedModifier(Body body,float val){
		if(body.getUserData() instanceof Ball){
			Ball ball = (Ball) body.getUserData();
			ball.speedModifier = val;
		}else{
			if(val == 1){
				// resetting speed
				if(speedMod > 1){
					// slow down
					body.setLinearVelocity(body.getLinearVelocity().scl(0.5f));
					body.setAngularVelocity(body.getAngularVelocity() / 2f);
				}else{
					//speed upo
					body.setLinearVelocity(body.getLinearVelocity().scl(2f));
					body.setAngularVelocity(body.getAngularVelocity() * 2f);
				}
			}else{
				// setting speed
				if(speedMod > 1){
					body.setLinearVelocity(body.getLinearVelocity().scl(2f));
					body.setAngularVelocity(body.getAngularVelocity() * 2f);
				}else{
					body.setLinearVelocity(body.getLinearVelocity().scl(0.5f));
					body.setAngularVelocity(body.getAngularVelocity() / 2f);
				}
			}
		}
	}
	

}
