package com.dfour.blockbreaker.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;

public class MultiPad extends Pad{
	public int clientId = 9;
	public float newPos = -1;
	

	public MultiPad(Body bod, Sprite normal, Sprite pull, Sprite push, Animation hover) {
		super(bod, normal, pull, push, hover);
	}
	
	public void updatePos(){
		if(newPos != -1){
			body.setTransform(newPos, 5, 0);
			newPos = -1;
		}
	}

}
