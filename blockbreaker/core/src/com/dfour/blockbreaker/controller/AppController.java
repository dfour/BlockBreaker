package com.dfour.blockbreaker.controller;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

public class AppController implements InputProcessor{
	private boolean left,right,up,down,escape = false;
	private boolean isMouse1Down = false;
	private boolean isMouse2Down = false;
	private boolean isMouse3Down = false;
	public boolean isDragged = false;
	public boolean isPauseDown = false;
	public boolean isDebugMode = false;
	public boolean zoomIn, zoomOut = false;
	public boolean ffive = false;
	public boolean fsix = false;
	public boolean ffour = false;
	public boolean feight = false;
	public boolean useBomb = false;
	private Vector2 mouseLocation;
	
	public AppController(){
		mouseLocation = new Vector2(0,0);
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode)
        {
        case Keys.LEFT:
            left = true;
            return true;
        case Keys.RIGHT:
            right = true;
            return true;
        case Keys.UP:
            up = true;
            return true;
        case Keys.DOWN:
            down = true;
            return true;
        case Keys.ESCAPE:
        	escape = true;
            return true;
        case Keys.P:
        	isPauseDown = true;
            return true;
        case Keys.B:
        	this.useBomb = true;
            return true;
        case Keys.PLUS:
        	zoomIn = true;
            return true;
        case Keys.MINUS:
        	zoomOut = true;
            return true;
        }
        
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode)
        {
        case Keys.LEFT:
            left = false;
            return true;
        case Keys.RIGHT:
            right = false;
            return true;
        case Keys.UP:
            up = false;
            return true;
        case Keys.DOWN:
            down = false;
            return true;
        case Keys.ESCAPE:
        	escape = false;
            return true;
        case Keys.P:
        	isPauseDown = false;
            return true;
        case Keys.B:
        	this.useBomb = false;
            return true;
        case Keys.F3:
        	this.isDebugMode = !this.isDebugMode;
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
		// TODO Auto-generated method stub
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
		//System.out.println(button);
		if(button == 0){
			isMouse1Down = false;
		}else if(button == 1){
			isMouse2Down = false;
		}else if(button == 2){
			isMouse3Down = false;
		}
		mouseLocation.x = screenX;
		mouseLocation.y = screenY;
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		isDragged = true;
		mouseLocation.x = screenX;
		mouseLocation.y = screenY;
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		mouseLocation.x = screenX;
		mouseLocation.y = screenY;
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
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
	
}
