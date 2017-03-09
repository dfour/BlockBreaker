package com.dfour.blockbreaker.entity;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Ball extends Entity{
	public boolean isDead = false;
	public boolean isAttached = true; 
	public final int MAX_SPEED = 100;
	public boolean isMagBall = false;
	public PointLight light;
	
	public Ball(Body bod, AtlasRegion atlasRegion){
		sprite = new Sprite(atlasRegion);
		this.body = bod;
		
		sprite.setX(bod.getPosition().x);
		sprite.setY(bod.getPosition().y);
		
		sprite.setOriginCenter();
		sprite.setScale(0.5f);
	}
	
	public void setMagBall(AtlasRegion atlasRegion){
		sprite.setRegion(atlasRegion);
		this.isMagBall = true;
	}
	
	public void setNormalBall(AtlasRegion atlasRegion){
		sprite.setRegion(atlasRegion);
		this.isMagBall = false;
	}

	public void update() {
		// increase speed
		body.getLinearVelocity();
		body.applyLinearImpulse((body.getLinearVelocity()), body.getPosition(), true);
		
		
		// limit max speed
		Vector2 vel = body.getLinearVelocity();
		float speed = vel.len();
		if ( speed > MAX_SPEED ) {
			body.setLinearVelocity( vel.scl(MAX_SPEED /speed));
		}
		
		sprite.setPosition((body.getPosition().x) * 10 - 8, (body.getPosition().y)* 10 -8);
		//sprite.setRotation(body.getAngle() * 57.2958f);
		
	}
}
