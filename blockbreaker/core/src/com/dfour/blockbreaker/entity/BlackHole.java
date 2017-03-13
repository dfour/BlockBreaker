package com.dfour.blockbreaker.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.dfour.blockbreaker.BBModel;

public class BlackHole extends Entity{
	public boolean hasPartyEffect = false;
	public float lastPartyEffect = 0f;
	private Array<Body> bodiesInGravity = new Array<Body>();

	public BlackHole(Body bod, AtlasRegion atlasRegion ){
		this.body = bod;
		if(null == atlasRegion){
			Pixmap pmap = new Pixmap(20,20, Pixmap.Format.RGBA8888);
			pmap.setColor(Color.BLACK);
			pmap.fillCircle(10, 10,10);
			sprite = new Sprite(new Texture(pmap));
			pmap.dispose();
			
		}else{
			sprite = new Sprite(atlasRegion);
		}
		sprite.setX(bod.getPosition().x);
		sprite.setY(bod.getPosition().y);
		
		sprite.setOriginCenter();
		sprite.setScale(0.35f);
		sprite.setPosition(
				(body.getPosition().x) * BBModel.BOX_TO_WORLD -(64/2), 
				(body.getPosition().y) * BBModel.BOX_TO_WORLD -(64/2));

	}
	
	@Override
	public void update(){
		for(Body body : bodiesInGravity){
			float velx = this.body.getPosition().x - body.getPosition().x;
			float vely = this.body.getPosition().y - body.getPosition().y;
			float length = (float) Math.sqrt(velx * velx + vely * vely);
			if (length != 0) {
				velx = velx / length;
				vely = vely / length;
			}

			body.applyForceToCenter(new Vector2(velx * 150, vely * 150), true);	
		}
		
	}

	public void gravitise(Body body) {
		if(bodiesInGravity.indexOf(body, true) == -1){
			bodiesInGravity.add(body);
		}
	}
	
	public void unGravitise(Body body){
		if(bodiesInGravity.indexOf(body, true) >= 0 ){
			bodiesInGravity.removeValue(body, true);
		}
	}
}
