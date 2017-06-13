package com.dfour.blockbreaker.network;

import com.dfour.blockbreaker.entity.MultiPad;

/**
 * Object to store all multi player information
 * @author darkd
 *
 */
public class MultiPlayer {
	public int connectionId = -1;
	public int position = -1;
	public boolean magPull = false;
	public boolean magPush = false;

	public MultiPad pad;
}
