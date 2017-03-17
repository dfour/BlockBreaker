package com.dfour.blockbreaker.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.dfour.blockbreaker.BBModel;

public class Brick extends Entity{
	public boolean wasHit = false;
	public boolean isStatic = true;
	public boolean isDead = false;
	public boolean wasEatenByPad = false;
	public int brickHealth = 5;
	public boolean gettingLazored = false;
	protected int brickWidth = 20;
	protected int brickHeight = 10;
	
	public Brick(Body bod){
		this(bod,null);
	}
	
	public Brick(Body bod, Texture tex){
		if(tex == null){
			Pixmap pmap = new Pixmap(brickWidth,brickHeight, Pixmap.Format.RGBA8888);
			float r = (float) Math.random() * 0.7f+ 0.3f;
			float g = (float) Math.random() * 0.7f+ 0.3f;
			float b = (float) Math.random() * 0.7f+ 0.3f;
			color = new Color(r,g,b,0.3f);
			pmap.setColor(color);
			pmap.fill();
			pmap.setColor(r,g,b,1f);
			pmap.drawRectangle(0, 0, brickWidth,brickHeight);
			this.sprite = new Sprite(new Texture(pmap));
			pmap.dispose();
		}else{
			this.sprite = new Sprite(tex);
		}
		this.body = bod;
		sprite.setOriginCenter();
	}
	
	public void hit(){
		wasHit = true;
	}
	
	@Override
	public void update(){
		if(this.brickHealth <= 0){
			this.wasEatenByPad = true;
			this.isDead = true;
		}
		sprite.setPosition(
				(body.getPosition().x) * BBModel.BOX_TO_WORLD -(brickWidth/2), 
				(body.getPosition().y)* BBModel.BOX_TO_WORLD -(brickHeight/2));
		sprite.setRotation(body.getAngle() * 57.2958f);
	}
}
