package com.dfour.blockbreaker;

import com.dfour.blockbreaker.entity.Pad;

/**
 * Object to store all multi player information
 * @author darkd
 *
 */
public class Player {
	public boolean isLocal = false;
	public int connectionId = -1;
	public int position = -1;
	public boolean magPull = false;
	public boolean magPush = false;
	
	//stuff transferred from model
	public int magnetRechargeRate =1;
	public int baseMagnetPower = 1000; // power at start of level
	public int magnetPower = 1000;
	public int baseMagnetStrength = 100; // power at start
	public int magnetStrength = 100;
	public int cash = 500;
	public int score = 0;
	public int livesLeft = 3;	// initial lives
	public int bombsLeft = 0;
	public boolean eternalMagBall = false;
	public float baseGuideLazerTimer = 10f;
	public float guideLazerTimer = 10f;
	public float baseLazerTimer = 5f;
	public float lazerTimer = 5f;
	public float drunkTimer = 5f;
	public float slowTimer = 5f;
	public float stickyTimer = 30f;
	public boolean isDrunk = false;
	public boolean isSlow = false;
	public boolean isFiringLazer = false;

	public Pad pad;
}
