package com.dfour.blockbreaker;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.dfour.blockbreaker.entity.Ball;
import com.dfour.blockbreaker.entity.Bin;
import com.dfour.blockbreaker.entity.BlackHole;
import com.dfour.blockbreaker.entity.Bomb;
import com.dfour.blockbreaker.entity.Brick;
import com.dfour.blockbreaker.entity.ExplosionParticle;
import com.dfour.blockbreaker.entity.Pad;
import com.dfour.blockbreaker.entity.PowerUp;

public class BBContactListener implements ContactListener {

	private BBModel parent;
	
	public BBContactListener(BBModel parent){
		this.parent = parent;
	}
	
	@Override
	public void beginContact(Contact contact) {
		System.out.println("Contact");
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		
		if(fa.getBody().getUserData() instanceof Brick){
			this.brickHit((Brick) fa.getBody().getUserData(), fb);
		}else if(fb.getBody().getUserData() instanceof Brick){
			this.brickHit((Brick) fb.getBody().getUserData(), fa);
		}
		
		if(fa.getBody().getUserData() instanceof Pad){
			this.padHit((Pad) fa.getBody().getUserData(), fb);
		}else if(fb.getBody().getUserData() instanceof Pad){
			this.padHit((Pad) fb.getBody().getUserData(), fa);
		}
		
		if(fa.getBody().getUserData() instanceof Bin){
			objectInVoid(fb);
		}else if(fb.getBody().getUserData() instanceof Bin){
			objectInVoid(fa);
		}
		
		if(fa.getBody().getUserData() instanceof Ball){
			ballHitSomething((Ball) fa.getUserData(),fb);
		}else if(fb.getBody().getUserData() instanceof Ball){
			ballHitSomething((Ball) fb.getUserData(),fa);
		}
		
		if(fa.getBody().getUserData() instanceof Bomb){
			bombHitSomething((Bomb) fa.getBody().getUserData(),fb);
		}else if(fb.getBody().getUserData() instanceof Bomb){
			bombHitSomething((Bomb) fb.getBody().getUserData(),fa);
		}
		
		if(fa.getBody().getUserData() instanceof BlackHole){
			if(fa.isSensor()){
				BlackHoleHitSomething((BlackHole) fa.getBody().getUserData(),fb);
			}else{
				objectInVoid(fb);
			}
		}else if(fb.getBody().getUserData() instanceof BlackHole){
			if(fb.isSensor()){
				BlackHoleHitSomething((BlackHole) fb.getBody().getUserData(),fa);
			}else{
				objectInVoid(fa);
			}
		}
	}
	private void BlackHoleHitSomething(BlackHole blackHole, Fixture fb) {
		System.out.println("Hit blackhole");
		
		blackHole.gravitise(fb.getBody());
		
	}

	private void bombHitSomething(Bomb bomb, Fixture fb) {
		System.out.println("Bomb hit something");
		if(fb.getBody().getType() == BodyType.StaticBody){
			parent.createBlast(bomb); // only go off for static bricks
		}
	}

	private void objectInVoid(Fixture fix){
		if(fix.getBody().getUserData() instanceof Brick){
			Brick brick = (Brick) fix.getBody().getUserData();
			brick.isDead = true;
		}else if(fix.getBody().getUserData() instanceof Ball){
			if(BlockBreaker.debug) System.out.println("Ball fell in void");
			Ball ball = (Ball) fix.getBody().getUserData();
			ball.isDead = true;			
		}else if(fix.getBody().getUserData() instanceof PowerUp){
			if(BlockBreaker.debug) System.out.println("PowerUp fell in void");
			PowerUp pup = (PowerUp) fix.getBody().getUserData();
			pup.isDead = true;			
		}
	}
	
	private void padHit(Pad userData, Fixture fix) {
		if(fix.getBody().getUserData() instanceof Ball){
			if(BlockBreaker.debug) System.out.println("TODO play sound");
			
		}else if(fix.getBody().getUserData() instanceof Brick){
			Brick brick = (Brick) fix.getBody().getUserData();
			parent.explosions.add(brick.body.getPosition());
			brick.wasEatenByPad = true;
			brick.isDead = true;
		}else if(fix.getBody().getUserData() instanceof PowerUp){
			if(BlockBreaker.debug) System.out.println("pup hit");
			PowerUp pup = (PowerUp) fix.getBody().getUserData();
			switch(pup.type){
			case PowerUp.MAG_POWER:
				parent.getMagPowerUp(); break;
			case PowerUp.BALL:
				parent.getExtraBall(false); break;
			case PowerUp.MAG_STRENGTH:
				parent.getMagStrengthPowerUP(); break;
			case PowerUp.LAZER:
				parent.getLazerPowerUp(); break;
			case PowerUp.MAG_BALL:
				parent.getExtraBall(true); break;
			case PowerUp.SCORE:
				parent.score+= 10; break;
			case PowerUp.GUIDE_LAZER:
				parent.addGuidLazer(); break;
			case PowerUp.BOMB:
				parent.addBombPowerUp(); break;
			case PowerUp.CASH5:
				parent.addCash(5); break;
			case PowerUp.CASH10:
				parent.addCash(10); break;
			case PowerUp.CASH25:
				parent.addCash(25); break;
			case PowerUp.CASH100:
				parent.addCash(100); break;
			}
			pup.isDead = true;
		}
	}

	private void brickHit(Brick br, Fixture fix){
		if(fix.getBody().getUserData() instanceof Ball){
			if(BlockBreaker.debug) System.out.println("Ball Hit Brick");
			parent.sparks.add(br.body.getPosition());
			br.hit();
		}else if(fix.getBody().getUserData() instanceof ExplosionParticle){
			br.hit();
		}
	}
	
	private void ballHitSomething(Ball ball, Fixture fix){
		if (BlockBreaker.debug) System.out.println("Ball Hit");
		if(fix.getUserData() instanceof Brick){
			parent.playSound(BBModel.PING_SOUND);
		}else if(fix.getBody().getUserData() instanceof Pad){
			parent.playSound(BBModel.BOING_SOUND);
		}else{
			parent.playSound(BBModel.PING_SOUND);
		}
	}
	

	@Override
	public void endContact(Contact contact) {
		System.out.println("End Contact");
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		
		if(fa.getBody().getUserData() instanceof BlackHole){
			BlackHoleLeftSomething((BlackHole) fa.getBody().getUserData(),fb);
		}else if(fb.getBody().getUserData() instanceof BlackHole){
			BlackHoleLeftSomething((BlackHole) fb.getBody().getUserData(),fa);
		}
	}
	private void BlackHoleLeftSomething(BlackHole blackHole, Fixture fb) {
		System.out.println("Hit blackhole");
		//apply force towards black hole to colliding object
		blackHole.unGravitise(fb.getBody());
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {		
	}
	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {		
	}

}
