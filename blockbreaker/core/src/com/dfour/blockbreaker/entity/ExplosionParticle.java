package com.dfour.blockbreaker.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class ExplosionParticle {
	public static final int NUMRAYS = 36;
	public Body body;
	public float lifeTime = 3f;
	
	public ExplosionParticle(World world, Vector2 vector, Vector2 rayDir, int blastPower){
		BodyDef bd = new BodyDef();
		bd.type = BodyType.DynamicBody;
		bd.fixedRotation = true; // rotation not necessary
		bd.bullet = true; // prevent tunneling at high speed
		bd.linearDamping = 5; // drag due to moving through air
		bd.gravityScale = 0; // ignore gravity
		bd.position.x = vector.x;
		bd.position.y = vector.y;// start at blast center
		rayDir.scl(blastPower);
		bd.linearVelocity.x = rayDir.x;
		bd.linearVelocity.y = rayDir.y;
		body = world.createBody( bd );
		body.setUserData(this);
		System.out.println(bd.linearVelocity);
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(0.1f); // very small
		 
		    FixtureDef fd = new FixtureDef();
		    fd.shape = circleShape;
		    fd.density = 120 / (float)NUMRAYS; // very high - shared across all particles
		fd.friction = 0; // friction not necessary
		fd.restitution = 0.99f; // high restitution to reflect off obstacles
		fd.filter.groupIndex = -1; // particles should not collide with each other
		body.createFixture( fd );
	}

}
