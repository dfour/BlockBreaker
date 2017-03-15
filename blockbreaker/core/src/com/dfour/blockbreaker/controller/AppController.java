package com.dfour.blockbreaker.controller;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.dfour.blockbreaker.BlockBreaker;

public class AppController implements InputProcessor{
	private boolean left,right,up,down,escape = false;
	private boolean isMouse1Down = false;
	private boolean isMouse2Down = false;
	private boolean isMouse3Down = false;
	public boolean isDragged = false;
	public boolean isPauseDown = false;
	public boolean ffive = false;
	public boolean fsix = false;
	public boolean ffour = false;
	public boolean feight = false;
	public boolean useBomb = false;
	private Vector2 mouseLocation;
	private BlockBreaker parent;
	
	public AppController(BlockBreaker p){
		mouseLocation = new Vector2(0,0);
		parent = p;
	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == parent.getPreferences().getControlsLeft()){left = true;return true;}
		if(keycode == parent.getPreferences().getControlsRight()){right = true;return true;}
		if(keycode == parent.getPreferences().getControlsUp()){up = true;return true;}
		if(keycode == parent.getPreferences().getControlsDown()){down = true;return true;}
		if(keycode == parent.getPreferences().getControlsQuit()){escape = true;return true;}
		if(keycode == parent.getPreferences().getControlsPause()){isPauseDown = true;return true;}
		if(keycode == parent.getPreferences().getControlsBomb()){useBomb = true;return true;}   
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// configurable keys
		if(keycode == parent.getPreferences().getControlsLeft()){left = false;return true;}
		if(keycode == parent.getPreferences().getControlsRight()){right = false;return true;}
		if(keycode == parent.getPreferences().getControlsUp()){up = false;return true;}
		if(keycode == parent.getPreferences().getControlsDown()){down = false;return true;}
		if(keycode == parent.getPreferences().getControlsQuit()){escape = false;return true;}
		if(keycode == parent.getPreferences().getControlsPause()){isPauseDown = false;return true;}
		if(keycode == parent.getPreferences().getControlsBomb()){useBomb = false;return true;} 
		
		// non-configurable keys
		switch (keycode)
        {
        case Keys.F3:
        	BlockBreaker.debug = !BlockBreaker.debug;
        	return false;
        case Keys.F4:
        	this.ffour = true;
        	return false;
        case Keys.F5:
        	this.ffive = !this.ffive;
        	return false;
        case Keys.F6:
        	this.fsix = !this.fsix;
        	return false;
        case Keys.F8:
        	this.feight = !this.feight;
        	return false;
        }
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(button == 0){
			isMouse1Down = true;
		}else if(button == 1){
			isMouse2Down = true;
		}else if(button == 2){
			isMouse3Down = true;
		}
		mouseLocation.x = screenX;
		mouseLocation.y = screenY;
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		isDragged = false;
		if(button == 0){
			isMouse1Down = false;
		}else if(button == 1){
			isMouse2Down = false;
		}else if(button == 2){
			isMouse3Down = false;
		}
		mouseLocation.x = screenX;
		mouseLocation.y = screenY;
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		isDragged = true;
		mouseLocation.x = screenX;
		mouseLocation.y = screenY;
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		mouseLocation.x = screenX;
		mouseLocation.y = screenY;
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
	
	public boolean getLeft(){
		return left;
	}
	
	public boolean getRight(){
		return right;
	}
	
	public boolean getUp(){
		return up;
	}
	
	public boolean getDown(){
		return down;
	}
	
	public boolean getEscape(){
		return escape;
	}
	
	public void setEscape(boolean esc){
		escape = esc;
	}
	
	public boolean isMouse1Down(){
		return isMouse1Down;
	}
	
	public boolean isMouse2Down(){
		return isMouse2Down;
	}
	
	public boolean isMouse3Down(){
		return isMouse3Down;
	}
	
	public Vector2 getMousePosition(){
		return mouseLocation;
	}
	
	public void overrideMouseLocation(int x, int y){
		mouseLocation = new Vector2(x,y);
	}
	
}
