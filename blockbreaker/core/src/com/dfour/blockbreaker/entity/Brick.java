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
	
	public Brick(Body bod){
		super(bod);
		width = 20;
		height = 10;
		Pixmap pmap = new Pixmap(width,height, Pixmap.Format.RGBA8888);
		float r = (float) Math.random() * 0.7f+ 0.3f;
		float g = (float) Math.random() * 0.7f+ 0.3f;
		float b = (float) Math.random() * 0.7f+ 0.3f;
		color = new Color(r,g,b,0.3f);
		pmap.setColor(color);
		pmap.fill();
		pmap.setColor(r,g,b,1f);
		pmap.drawRectangle(0, 0, width,height);
		this.sprite = new Sprite(new Texture(pmap));
		pmap.dispose();
	}
	
	public void hit(){
		wasHit = true;
	}
	
	@Override
	public void update(){
		super.update();
		if(this.brickHealth <= 0){
			this.wasEatenByPad = true;
			this.isDead = true;
		}
	}
}
