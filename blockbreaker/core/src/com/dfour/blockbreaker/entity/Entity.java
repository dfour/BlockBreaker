package com.dfour.blockbreaker.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.dfour.blockbreaker.BBModel;

public class Entity {
	public Color color;
	public Sprite sprite;
	public Body body;
	protected int width;
	protected int height;
	protected boolean hasAnimation = false;
	protected boolean hasDeathAnimation = false;
	
	public Entity(Body b, Texture tex){
		body = b;
		this.width = tex.getWidth();
		this.height = tex.getHeight();
		sprite = new Sprite(tex);
		updateSprite();
	}
	
	public Entity(Body b, AtlasRegion tex){
		body = b;
		this.width = tex.getRegionWidth();
		this.height = tex.getRegionHeight();
		sprite = new Sprite(tex);
		updateSprite();
	}
	
	public Entity(Body b, TextureRegion tex){
		body = b;
		this.width = tex.getRegionWidth();
		this.height = tex.getRegionHeight();
		sprite = new Sprite(tex);
		updateSprite();
	}
	
	public Entity(Body b){
		body = b;
	}
	
	private void updateSprite(){
		sprite.setX(body.getPosition().x);
		sprite.setY(body.getPosition().y);
		sprite.setOriginCenter();
	}
	
	protected void update(){
		sprite.setPosition(body.getPosition().x * BBModel.BOX_TO_WORLD - width/2f, 
						   body.getPosition().y * BBModel.BOX_TO_WORLD - height/2f);
		sprite.setRotation(body.getAngle() * 57.2958f);
	}
	
	public void draw(SpriteBatch sb, float alpha){
		if(hasAnimation){
			// draw animation
		}else{
			//draw image
			sprite.draw(sb,alpha);
		}
		
	}

}
