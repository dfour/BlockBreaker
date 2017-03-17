package com.dfour.blockbreaker.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;

public class PowerBrick extends Brick{

	public PowerBrick(Body bod) {
		super(bod);
		Pixmap pmap = new Pixmap(brickWidth,brickHeight, Pixmap.Format.RGBA8888);
		float w = 0.7f+ 0.3f;
		color = new Color(w,w,w,0.3f);
		pmap.setColor(color);
		pmap.fill();
		pmap.setColor(w,w,w,1f);
		pmap.drawRectangle(0, 0, brickWidth,brickHeight);
		this.sprite = new Sprite(new Texture(pmap));
		pmap.dispose();
	}

	@Override
	public void update() {
		super.update();
		float r = (float) Math.random() * 0.7f+ 0.3f;
		float g = (float) Math.random() * 0.7f+ 0.3f;
		float b = (float) Math.random() * 0.7f+ 0.3f;
		this.sprite.setColor(r,g,b,1f);
	}

}
