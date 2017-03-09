package com.dfour.blockbreaker;

import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.utils.Pool;

public class PointLightPool extends Pool<PointLight> {
	private RayHandler rayHandler;
	private int rayCount;
	
	
	public PointLightPool (RayHandler rayHandler, int rayCount){
		this.rayHandler = rayHandler;
		this.rayCount = rayCount;
	}
	
	@Override
	public void free(PointLight light){
		light.setActive(false);
		light.attachToBody(null);
		super.free(light);
	}
	
	@Override
	protected PointLight newObject() {
		return new PointLight(rayHandler, rayCount);
	}

}
