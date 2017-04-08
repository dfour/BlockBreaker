package com.dfour.blockbreaker;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.utils.IntMap;

public class ParticleEffectManager {
	public static final int SPARK = 0;
	public static final int EXPLOSION = 1;
	public static final int POWERUP = 2;
	public static final int LAZER = 3;
	public static final int LAZER_HIT = 4;
	public static final int BLACK_HOLE = 5;
	public static final int GHOST_TAIL = 6;
	public static final int MAGGHOST_TAIL = 7;
	public static final int CASH_FTEXT = 8;
	public static final int MAGBALL_FTEXT = 9;
	public static final int BOMB_FTEXT = 10;
	public static final int BALL_FTEXT = 11;
	public static final int GUIDE_FTEXT = 12;
	public static final int LASER_FTEXT =13;
	public static final int MAGPOWER_FTEXT = 14;
	public static final int MAGSTR_FTEXT = 15;
	public static final int SCORE_FTEXT = 16;
	public static final int SLOW_FTEXT = 17;
	public static final int DRUNK_FTEXT = 18;
	public static final int STICKYPAD_FTEXT = 19;
	
	private IntMap<ParticleEffect> partyEffects;
	private IntMap<ParticleEffectPool> partyEffectPool;
	
	public ParticleEffectManager(){
		partyEffects = new IntMap<ParticleEffect>();
		partyEffectPool = new IntMap<ParticleEffectPool>();
	}
	
	public void addParticleEffect(int type, ParticleEffect party){
		addParticleEffect(type,party,1);
	}
	
	public void addParticleEffect(int type, ParticleEffect party, float scale ){
		addParticleEffect(type,party,scale,5,20);
		
	}
	
	public void addParticleEffect(int type, ParticleEffect party, float scale, int startCapacity, int maxCapacity){
		party.scaleEffect(scale);
		partyEffects.put(type, party);
		partyEffectPool.put(type,new ParticleEffectPool(party,startCapacity,maxCapacity));
		
	}
	
	public PooledEffect getPooledParticleEffect(int type){
		return partyEffectPool.get(type).obtain();
	}
	
	

}
