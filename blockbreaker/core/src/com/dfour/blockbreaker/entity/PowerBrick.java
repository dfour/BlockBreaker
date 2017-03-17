package com.dfour.blockbreaker.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;

public class PowerBrick extends Brick{

	public PowerBrick(Body bod) {
		super(bod);
		width = 20;
		height = 10;
		Pixmap pmap = new Pixmap(width,height, Pixmap.Format.RGBA8888);
		color = new Color(1,1,1,0.3f);
		pmap.setColor(color);
		pmap.fill();
		pmap.setColor(1,1,1,1);
		pmap.drawRectangle(0, 0, width,height);
		this.sprite = new Sprite(new Texture(pmap));
		pmap.dispose();
	}

	@Override
	public void update() {
		super.update();
		// TODO change from random colour to hue cycle using BButils
		float r = (float) Math.random() * 0.7f+ 0.3f;
		float g = (float) Math.random() * 0.7f+ 0.3f;
		float b = (float) Math.random() * 0.7f+ 0.3f;
		this.sprite.setColor(r,g,b,1f);
	}

}
