package com.dfour.blockbreaker.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class PowerUp {
	
	public static final int MAG_POWER = 0;
	public static final int MAG_STRENGTH = 1;
	public static final int BALL = 2;
	public static final int LAZER = 3;
	public static final int MAG_BALL = 4;
	public static final int SCORE = 5;
	public static final int GUIDE_LAZER = 6;
	public static final int BOMB = 7;
	public static final int CASH5 = 8;
	public static final int CASH10 = 9;
	public static final int CASH25 = 10;
	public static final int CASH100 = 11;
	
	public Body body;
	public boolean isDead = false;
	public boolean wasEatenByPad = false;
	public Color color;
	public Sprite sprite;
	public int type = 0;
	
	public PowerUp(Body bod, TextureRegion tex, int type){
		this.type = type;
		sprite = new Sprite(tex);
		body = bod;
		sprite.setOriginCenter();
		sprite.setScale(0.5f);
		update();
	}
	
	public void update(){
		sprite.setPosition((body.getPosition().x*10) - 16f, (body.getPosition().y*10) - 16f);
		sprite.setRotation(body.getAngle() * 57.2958f);
	}
}
