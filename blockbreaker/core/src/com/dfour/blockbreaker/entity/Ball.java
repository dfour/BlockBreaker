package com.dfour.blockbreaker.entity;

import box2dLight.PointLight;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Ball extends Entity{
	public boolean isDead = false;
	public boolean isAttached = true;
	public float xOffset = 0;
	public float yOffset = 2;
	public int MAX_SPEED = 125; // upped speed to increase difficulty since removing paddle lerp
	public boolean isMagBall = false;
	public float speedModifier = 1f;
	public PointLight light;
	
	public Ball(Body bod, AtlasRegion atlasRegion){
		super(bod,atlasRegion);		
		sprite.setScale(0.5f);
	}
	
	public Ball(Body bod, Animation anim){
		super(bod,anim);
		this.width = anim.getKeyFrame(0).getRegionWidth()/2;
		this.height = anim.getKeyFrame(0).getRegionHeight()/2;
	}
	
	public void setMagBall(Animation anim){
		this.normalAnimation = anim;
		this.stateTime = 1f;
		this.isMagBall = true;
	}
	
	public void setNormalBall(Animation anim){
		this.normalAnimation = anim;
		this.stateTime = 1f;
		this.isMagBall = false;
	}

	@Override
	public void update() {
		super.update();
		// increase speed
		body.getLinearVelocity();
		body.applyLinearImpulse((body.getLinearVelocity()), body.getPosition(), true);
		
		// limit max speed
		Vector2 vel = body.getLinearVelocity();
		float speed = vel.len();
		if ( speed > MAX_SPEED ) {
			body.setLinearVelocity( vel.scl((MAX_SPEED * speedModifier) /speed));
		}
		
		// Check ball is in play area and not gone off into wilderness
		if(body.getPosition().x < -10 || body .getPosition().x > 100 || body.getPosition().y < -20 || body.getPosition().y > 80){
			this.isDead = true;
		}
	}
}
