package com.dfour.blockbreaker.entity;

import box2dLight.ChainLight;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

public class Pad  {
	public Body body;
	public boolean goingRight = false;
	public boolean goingLeft = false;
	public Sprite sprite;
	
	private Sprite normal ;
	private Sprite pull;
	private Sprite push;
	
	private Animation hover;
	private float stateTime = 0;
	
	public ChainLight lazLightLeft;
	public ChainLight lazLightRight;
	
	public boolean isStickyPad = false;

	public Pad(Body bod, Sprite normal, Sprite pull, Sprite push,Animation hover){
		super();
		this.normal = normal;
		this.pull = pull;
		this.push = push;
		this.hover = hover;
		sprite = normal;
		this.body = bod;
		setSizePos(sprite);
		setSizePos(normal);
		setSizePos(pull);
		setSizePos(push);
	}
	
	private void setSizePos(Sprite spriteToChange){
		spriteToChange.setX(body.getPosition().x);
		spriteToChange.setY(body.getPosition().y);
		spriteToChange.setOrigin(55f, 5f);
		spriteToChange.setSize(110f, 10f);
	}
	
	public void setPosition(float x, float y){
		if(x < sprite.getX()){
			goingLeft = true;
			goingRight = false;
		}else if(x > sprite.getX()){
			goingLeft = false;
			goingRight = true;
		}else{
			goingLeft = false;
			goingRight = false;
		}
		pull.setPosition((x - 5.5f)*10, (y -.5f)*10);
		push.setPosition((x - 5.5f)*10, (y -.5f)*10);
		normal.setPosition((x - 5.5f)*10, (y -.5f)*10);
		body.setTransform(x, y, 0);
	}
	
	public void setImagePull(){
		sprite=(pull);
	}
	
	public void setImagePush(){
		sprite=(push);
	}
	
	public void setImageNormal(){
		sprite=(normal);
	}
	
	public void drawAnimation(SpriteBatch sb, float delta){
		stateTime+= delta;
		sb.draw(hover.getKeyFrame(stateTime,true), (body.getPosition().x - 6.5f) * 10, (body.getPosition().y-0.8f) * 10, 130,10);
	}
	
	
}
