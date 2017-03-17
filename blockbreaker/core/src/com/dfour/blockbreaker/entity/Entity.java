package com.dfour.blockbreaker.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;

public class Entity {
	public Color color;
	public Sprite sprite;
	public Body body;
	protected int width;
	protected int height;
	protected boolean hasAnimation = false;
	protected boolean hasDeathanimation = false;
	
	
	protected void update(){
		sprite.setPosition(body.getPosition().x - width/2f, body.getPosition().y - height/2f);
		sprite.setRotation(body.getAngle() * 57.2958f);
	}

}
