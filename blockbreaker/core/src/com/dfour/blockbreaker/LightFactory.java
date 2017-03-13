package com.dfour.blockbreaker;

import box2dLight.ChainLight;
import box2dLight.DirectionalLight;
import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;

/** A factory to create Box2DLights 
 * @author darkd
 *
 */
public class LightFactory {
	public static final int RAYS_PER_LIGHT_LOW = 50;
	public static final int RAYS_PER_LIGHT_MID = 150;
	public static final int RAYS_PER_LIGHT_HIGH = 300;
	public static final int LIGHT_SIZE_LOW = 20;
	public static final int LIGHT_SIZE_MID = 50;
	public static final int LIGHT_SIZE_HIGH = 150;
	public static final float LIGHT_SOFTNESS_HARD = 1f;
	public static final float LIGHT_SOFTNESS_DEFUALT = 2.5f;
	public static final float LIGHT_SOFTNESS_SOFT = 5f;
	
	public int rays = RAYS_PER_LIGHT_MID;
	public int size = LIGHT_SIZE_MID;
	public float soft = LIGHT_SOFTNESS_HARD;
	
	public short filter = -1;
	
	private RayHandler rayHandler;
	private Array<Light>  allLights = new Array<Light>();
	private Array<PointLight>  staticLights = new Array<PointLight>();
	
	public PointLightPool plp;
	
	public LightFactory (RayHandler rh){
		rayHandler = rh;
		plp = new PointLightPool(rh,rays);
	}
	
	public LightFactory(RayHandler rh, int rayCount, int lightSize){
		rayHandler = rh;
		this.rays = rayCount;
		this.size = lightSize;
		plp = new PointLightPool(rh,rays);
	}

	public PointLight addPointLight(Body bod){
		Color col = new Color(1,1,1,1);
		return this.addPointLight(bod, col);
	}
	
	public PointLight addPointLight(Body body, Color col){
		PointLight sl = plp.newObject();
		sl.setActive(true);
		sl.setDistance(size);
		sl.setColor(col);
		sl.attachToBody(body);
		sl.setSoftnessLength(soft);
		if(filter != 0){
			sl.setContactFilter(filter, filter, filter);
		}
		allLights.add(sl);
		return sl;
	}
	
	public PointLight addStaticPointLight(int x, int y, Color col){
		PointLight spl = addPointLight(x,y,col);
		staticLights.add(spl);
		return spl;
	}
	
	public PointLight addPointLight(float x,float y){
		float r = (float) Math.random() * 0.7f+ 0.3f;
		float g = (float) Math.random() * 0.7f+ 0.3f;
		float b = (float) Math.random() * 0.7f+ 0.3f;
		Color col = new Color(r,g,b,1);
		return this.addPointLight(x, y, col);
	}
	
	public PointLight addPointLight(float x, float y, Color col){
		PointLight sl = plp.obtain();
		sl.setActive(true);
		sl.setPosition(x,y);
		sl.setDistance(size);
		sl.setColor(col);
		sl.setSoftnessLength(soft);
		if(filter != 0){
			sl.setContactFilter(filter, filter, filter);
		}
		allLights.add(sl);
		return sl;
	}
	
	public DirectionalLight addDirectionalLight(){
		return addDirectionalLight(-90);
	}
	
	public DirectionalLight addDirectionalLight(int direction){
		return this.addDirectionalLight(direction,false);
	}
	
	public DirectionalLight addDirectionalLight(int direction, boolean xray){
		Color col = new Color(1,1,1,1);
		return addDirectionalLight(direction, false, col);
	}
	
	public DirectionalLight addDirectionalLight(int direction, boolean xray, Color col){
		DirectionalLight dl = new DirectionalLight(rayHandler, rays, null, direction);
		dl.setColor(col);
		dl.setXray(xray);
		dl.setSoftnessLength(soft);
		if(filter != 0){
			dl.setContactFilter(filter, filter, filter);
		}
		allLights.add(dl);
		return dl;
	}
	
	public ChainLight addChainLight(float[] points){
		return addChainLight(points,null,true);
	}
	
	public ChainLight addChainLight(float[] points,Color col){
		return addChainLight(points,col,true);
	}
	
	public ChainLight addChainLight(float[] points, Color col, boolean reverse){
		int direction = reverse ? 1:-1;
		ChainLight cl = new ChainLight(
				rayHandler, rays , col, size,direction ,
				points);
		allLights.add(cl);
		return cl;
	}
	
	public void updatePools(){
		plp = new PointLightPool(rayHandler,rays);
	}
	
	public void clearStaticLights(){
		for(PointLight light : staticLights){
			plp.free(light);
		}
	}
}
