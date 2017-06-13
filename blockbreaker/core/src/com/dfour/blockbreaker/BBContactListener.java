package com.dfour.blockbreaker;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.dfour.blockbreaker.entity.Ball;
import com.dfour.blockbreaker.entity.Bin;
import com.dfour.blockbreaker.entity.Bomb;
import com.dfour.blockbreaker.entity.Brick;
import com.dfour.blockbreaker.entity.ExplosionParticle;
import com.dfour.blockbreaker.entity.LocalEffectEntity;
import com.dfour.blockbreaker.entity.Pad;
import com.dfour.blockbreaker.entity.Portal;
import com.dfour.blockbreaker.entity.PowerUp;

public class BBContactListener implements ContactListener {

	private BBModel parent;
	
	public BBContactListener(BBModel parent){
		this.parent = parent;
	}
	
	@Override
	public void beginContact(Contact contact) {
		//System.out.println("Contact");
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
		
		if(fa.getBody().getUserData() instanceof Portal){
			Portal p = (Portal) fa.getBody().getUserData();
			enteredAPortal(p,fb);
		}else if(fb.getBody().getUserData() instanceof Portal){
			Portal p = (Portal) fb.getBody().getUserData();
			enteredAPortal(p,fa);
		}
		
		if(fa.getBody().getUserData() instanceof Ball){
			ballHitSomething((Ball) fa.getBody().getUserData(),fb);
		}else if(fb.getBody().getUserData() instanceof Ball){
			ballHitSomething((Ball) fb.getBody().getUserData(),fa);
		}
		
		if(fa.getBody().getUserData() instanceof Bomb){
			bombHitSomething((Bomb) fa.getBody().getUserData(),fb);
		}else if(fb.getBody().getUserData() instanceof Bomb){
			bombHitSomething((Bomb) fb.getBody().getUserData(),fa);
		}
		
		if(fa.getBody().getUserData() instanceof LocalEffectEntity){
			if(fa.isSensor()){
				inLocalEffectRange((LocalEffectEntity) fa.getBody().getUserData(),fb);
			}else{
				objectInVoid(fb);
			}
		}else if(fb.getBody().getUserData() instanceof LocalEffectEntity){
			if(fb.isSensor()){
				inLocalEffectRange((LocalEffectEntity) fb.getBody().getUserData(),fa);
			}else{
				objectInVoid(fa);
			}
		}
	}
	
	private void enteredAPortal(Portal p, Fixture fix){
		if(BlockBreaker.debug && BlockBreaker.debug_contact_log){
			System.out.println("Body in Portal");
		}
		p.bodyInPortal(fix.getBody());
	}
	
	
	private void inLocalEffectRange(LocalEffectEntity lee, Fixture fb) {		
		lee.addToEffectedObjects(fb.getBody());
	}

	private void bombHitSomething(Bomb bomb, Fixture fb) {
		if(fb.getBody().getType() == BodyType.StaticBody
				&& !fb.isSensor()){
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
			if(BlockBreaker.debug && BlockBreaker.debug_contact_log){
				System.out.println("TODO play sound");
			}
			
		}else if(fix.getBody().getUserData() instanceof Brick){
			Brick brick = (Brick) fix.getBody().getUserData();
			parent.explosions.add(brick.body.getPosition());
			brick.wasEatenByPad = true;
			brick.isDead = true;
		}else if(fix.getBody().getUserData() instanceof PowerUp){
			if(BlockBreaker.debug && BlockBreaker.debug_contact_log){
				System.out.println("pup hit");
			}
			PowerUp pup = (PowerUp) fix.getBody().getUserData();
			switch(pup.type){
			case PowerUp.MAG_POWER:
				parent.getMagPowerUp();
				parent.magPowerFText.add(pup.body.getPosition());
				break;
			case PowerUp.BALL:
				parent.getExtraBall(false);
				parent.ballFText.add(pup.body.getPosition());
				break;
			case PowerUp.MAG_STRENGTH:
				parent.getMagStrengthPowerUP(); 
				parent.magStrFText.add(pup.body.getPosition());
				break;
			case PowerUp.LAZER:
				parent.getLazerPowerUp(); 
				parent.laserFtext.add(pup.body.getPosition());
				break;
			case PowerUp.MAG_BALL:
				parent.getExtraBall(true);
				parent.magBallPEToShow.add(pup.body.getPosition());
				break;
			case PowerUp.SCORE:
				parent.score+= 10;
				parent.scoreFText.add(pup.body.getPosition());
				break;
			case PowerUp.GUIDE_LAZER:
				parent.addGuidLazer(); 
				parent.guideFText.add(pup.body.getPosition());
				break;
			case PowerUp.BOMB:
				parent.addBombPowerUp(); 
				parent.bombFText.add(pup.body.getPosition());
				break;
			case PowerUp.CASH5:
				parent.addCash(5); 
				parent.cashPEToShow.add(pup.body.getPosition());
				break;
			case PowerUp.CASH10:
				parent.addCash(10); 
				parent.cashPEToShow.add(pup.body.getPosition());
				break;
			case PowerUp.CASH25:
				parent.addCash(25); 
				parent.cashPEToShow.add(pup.body.getPosition());
				break;
			case PowerUp.CASH100:
				parent.addCash(100);
				parent.cashPEToShow.add(pup.body.getPosition());
				break;
			case PowerUp.DRUNK:
				parent.isDrunk();
				parent.drunkFText.add(pup.body.getPosition());
				break;
			case PowerUp.SLOW:
				parent.isSlow();
				parent.slowFText.add(pup.body.getPosition());
				break;
			case PowerUp.STICKY:
				parent.isSticky();
				parent.stickyFText.add(pup.body.getPosition());
				break;
			}
			pup.isDead = true;
		}
	}

	private void brickHit(Brick br, Fixture fix){
		if(fix.getBody().getUserData() instanceof Ball){
			if(BlockBreaker.debug && BlockBreaker.debug_contact_log){
				System.out.println("Ball Hit Brick");
			}
			parent.sparks.add(br.body.getPosition());
			br.hit();
		}else if(fix.getBody().getUserData() instanceof ExplosionParticle){
			br.hit();
		}
	}
	
	private void ballHitSomething(Ball ball, Fixture fix){
		if(BlockBreaker.debug && BlockBreaker.debug_contact_log){
			System.out.println("Ball Hit");
		}
		if(fix.getUserData() instanceof Brick){
			parent.playSound(BBModel.PING_SOUND);
		}else if(fix.getBody().getUserData() instanceof Pad){
			if(!ball.isAttached){
				// no sound for attached balls
				parent.ballHitPad(ball);
				parent.playSound(BBModel.BOING_SOUND);
			}
		}else if(!fix.isSensor()){
			parent.playSound(BBModel.PING_SOUND);
		}
	}
	

	@Override
	public void endContact(Contact contact) {
		//System.out.println("End Contact");
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		
		if(fa.getBody().getUserData() instanceof LocalEffectEntity){
			outOfLocalEffectRange((LocalEffectEntity) fa.getBody().getUserData(),fb);
		}else if(fb.getBody().getUserData() instanceof LocalEffectEntity){
			outOfLocalEffectRange((LocalEffectEntity) fb.getBody().getUserData(),fa);
		}
	}
	private void outOfLocalEffectRange(LocalEffectEntity lee, Fixture fb) {
		lee.removeFromEffectedObjects(fb.getBody());
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {		
	}
	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {		
	}

}
