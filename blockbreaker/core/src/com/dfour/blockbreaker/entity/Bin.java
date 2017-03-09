package com.dfour.blockbreaker.entity;

import com.badlogic.gdx.physics.box2d.Body;

/**
 * This class is the void below the paddel which will signify which bricks have fell
 * into the void and should be deleted.
 * @author darkd
 *
 */
public class Bin {
	
	public Body body;
	
	/** Creates a new void
	 * @param bod the body with a sensor fixture to determine an area to void bricks
	 */
	public Bin(Body bod){
		this.body = bod;
	}
}
