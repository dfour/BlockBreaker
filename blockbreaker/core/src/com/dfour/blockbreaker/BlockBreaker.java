package com.dfour.blockbreaker;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Graphics.Monitor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.dfour.blockbreaker.controller.AppController;
import com.dfour.blockbreaker.loaders.BBAssetManager;
import com.dfour.blockbreaker.network.AbstractNetworkBase;
import com.dfour.blockbreaker.view.ApplicationScreen;
import com.dfour.blockbreaker.view.ControlMapScreen;
import com.dfour.blockbreaker.view.CustomMapScreen;
import com.dfour.blockbreaker.view.LevelDesignerScreen;
import com.dfour.blockbreaker.view.LoadingScreen;
import com.dfour.blockbreaker.view.MultiplayerScreen;
import com.dfour.blockbreaker.view.PreferencesScreen;
import com.dfour.blockbreaker.view.ShopScreen;
import com.dfour.blockbreaker.AppPreferences;
import com.dfour.blockbreaker.view.EndScreen;
import com.dfour.blockbreaker.view.MenuScreen;

public class BlockBreaker extends Game {
		//TODO create bug reporter( in game or online )
	//TODO try make bricks that hit black hole spin into oblivion (see add animations todo)
	//TODO add animations to entities using boolean to state image vs animation for (normal, death, black hole death)
		//TODO add continue button (saving score and cash in preferences ?? possible cheat)
	//TODO update level designer
	//TODO add buying sound effect for shop (cha-ching)
	//TODO update level designer to use actual rendering like game, just without pad and info display and a obstacle menu instead
		//TODO look into making level using level generator
	//TODO update info display with spaceship dashboard look (metal with black display and green computer text)
	//TODO NEEDS CHECKING add portal in and portal out obstacles (must look like portal portals) 
	//			(portals close to edge allow blocks to escape area)
	//TODO fix bug where game ends and no pointer
	//TODO redo rendering system so box to world rendering is in sync (e.g. 16px image = 1 box unit)
		//TODO add Host > Client multiplayer then (see below)
		//DONE add screen to be host or enter Host ip
		//TODO add lobby and matching
		//TODO pad size = player count(max 4) / full pad size (multi multiplayer)
		//DONE add username preferences (with random name generator)
		//TODO add help page for networking (show ports to use or add to preferences)
		//TODO may drop multiplayer to allow working on next game ************************************
	
	private MenuScreen menu;
	private PreferencesScreen prefs;
	private AppPreferences preferences;
	private ApplicationScreen app;
	private ApplicationScreen app_multi;
	private LoadingScreen loadingScreen;
	private LevelDesignerScreen designScreen;
	private ShopScreen shopScreen;
	private EndScreen end;
	private AppController controller;
	private boolean musicLoaded = false;
	public BBAssetManager assMan = new BBAssetManager();
	public AbstractNetworkBase base;
	
	// debug vars
	public static boolean debug_mouse_capture = false;
	public static boolean debug = true;
	public static boolean debug_b2d_render = false;
	public static boolean debug_texture_render = true;
	public static boolean debug_contact_log = false;
	public static boolean debug_multilag = false;
	public static int debug_min_lag = 300;
	public static int debug_max_lag = 500;
	
	public static Array<FileHandle> customMaps;
	public static boolean isCustomMapMode = false;
	
	
	//music
	private Sound introMusic;
	private Sound gameMusic;
	private Sound endMusic;
	private Sound currentSound;
	private long currentSongId = 999999;
	
	public final static int MENU = 0;
	public final static int PREFERENCES = 1;
	public final static int APPLICATION = 2;
	public final static int ENDGAME = 3;
	public final static int LEVEL_DESIGNER = 4;
	public final static int SHOP = 5;
	public final static int CONTROL =6;
	public final static int CUSTOM_MAP = 7;
	public final static int MULTIPLAYER_MENU = 8;
	public final static int MULTIPLAYER_APPLICATION = 9;
	
	public boolean isMultiMode = false;
	
	public final static String VERSION = "0.1"; 
	
	// screen size preferred
	public int screenWidthPreferred = 1024;
	public int screenHeightPreferred= 786;
	
	@Override
	public void create () {
		loadingScreen = new LoadingScreen(this);
		preferences = new AppPreferences();
		
		this.updateScreenSize();
		
		//Pixmap pm = new Pixmap(Gdx.files.internal("normalCursor.png"));
		//Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 0, 0));
		//pm.dispose();
		
		
		setScreen(loadingScreen);
		
		// create controller and model
		controller = new AppController(this);
	}

	public void changeScreen(int screen){
		if(!musicLoaded){
			introMusic = assMan.manager.get("music/sawsquarenoise_-_09_-_OST_09_Metallius.mp3");
			gameMusic = assMan.manager.get("music/sawsquarenoise_-_11_-_OST_11_Where_Am_I_.mp3");
			endMusic = assMan.manager.get("music/sawsquarenoise_-_10_-_OST_10_Go_Go_Metallius.mp3");
			
			currentSound = introMusic;
			if(preferences.isMusicEnabled()){
				currentSongId = currentSound.loop(preferences.getVolume());
			}
			musicLoaded = true;
		}
		switch(screen){
			case MENU:
				if(currentSound != introMusic && preferences.isMusicEnabled()){
					currentSound.stop();
					currentSound = introMusic;
					currentSongId = currentSound.loop(preferences.getVolume());
				}
				this.updateScreenSize(); // for changing screensize after pref change
				menu = new MenuScreen(this);
				this.setScreen(menu);
				break;
			case PREFERENCES:
				prefs = new PreferencesScreen(this);
				this.setScreen(prefs);
				break;
			case LEVEL_DESIGNER:
				designScreen = new LevelDesignerScreen(this);
				this.setScreen(designScreen);
				break;
			case SHOP:
				BBModel model;
				if(isMultiMode){
					model = app_multi.bbModel;
				}else{
					model = app.bbModel;
				}
				shopScreen = new ShopScreen(this, model);
				this.setScreen(shopScreen);
				break;
			case CONTROL:
				this.setScreen(new ControlMapScreen(this));
				break;
			case CUSTOM_MAP:
				this.setScreen(new CustomMapScreen(this));
				break;
			case MULTIPLAYER_MENU:
				this.setScreen(new MultiplayerScreen(this));
				break;
			case MULTIPLAYER_APPLICATION:
				isMultiMode = true;
				if(app_multi == null) app_multi = new ApplicationScreen(this,new BBModelMulti(controller,assMan,base));
				this.setScreen(app_multi); 
				break;
			case APPLICATION:
				isMultiMode = false;
				if(currentSound != gameMusic && preferences.isMusicEnabled()){
					currentSound.stop();
					currentSound = gameMusic;
					currentSongId = currentSound.loop(preferences.getVolume());
				}
				if(app == null) app = new ApplicationScreen(this,new BBModel(controller,this.assMan));
				app.fadeIn = 1f;
				this.setScreen(app);
				break;
			case ENDGAME:
				if(currentSound != gameMusic && preferences.isMusicEnabled()){
					currentSound.stop();
					currentSound = endMusic;
					currentSongId = currentSound.loop(preferences.getVolume());
				}
				if(end == null) end = new EndScreen(this);
				this.setScreen(end);
				break;
		}
	}
	
	public void changeVolume(){
		currentSound.setVolume(currentSongId, preferences.getVolume());
	}
	
	public void stopMusic(){
		currentSongId = 999999;
		currentSound.stop();
	}
	
	public void startMusic(){
		if(currentSongId == 999999){
			currentSongId = currentSound.loop(preferences.getVolume());
		}
	}
	
	public AppPreferences getPreferences(){
		return this.preferences;
	}
	
	@Override
	public void dispose(){
		assMan.dispose();
	}
	
	// TODO display from https://github.com/libgdx/libgdx/wiki/Querying-&-configuring-graphics-(monitors,-display-modes,-vsync)
	public Monitor[] getMonitors(){
		Monitor[] monitors = Gdx.graphics.getMonitors();
		return monitors;
	}
	
	public Monitor getCurrentMonitor(){
		Monitor currMonitor = Gdx.graphics.getMonitor();
		return currMonitor;
	}
	
	public DisplayMode[] getDisplayModes(Monitor monitor){
		DisplayMode[] modes = Gdx.graphics.getDisplayModes(monitor);
		return modes;
	}
	
	public boolean setWindowedMode(int width, int height){
		return Gdx.graphics.setWindowedMode(width, height);
	}
	
	public boolean setFullscreenMode(DisplayMode displayMode){
		return Gdx.graphics.setFullscreenMode(displayMode);
	}
	
	public DisplayMode getLargestScreenSize(){
		DisplayMode[] displayModes = this.getDisplayModes(this.getCurrentMonitor());
		DisplayMode largestMode = null;
		
		if(displayModes.length < 1){
			System.out.println("No Monitor display modes");
		}else{
			largestMode = displayModes[0];
			for(DisplayMode dispMode: displayModes){
				if(dispMode.width > largestMode.width){
					largestMode = dispMode;
				}
			}
		}
		return largestMode;
	}
	
	public boolean updateScreenSize(){
		// TODO get pref data for screen size
		String screenSize = preferences.getScreenSize();
		// split string into width and height
		String[] sizes = screenSize.split("x");
		screenWidthPreferred = Integer.valueOf(sizes[0]);
		screenHeightPreferred = Integer.valueOf(sizes[1]);
		
		DisplayMode displayModeSelected = null;
		
		// set the screen size
		if(preferences.getWindowed()){
		// if pref windowed
			return this.setWindowedMode(screenWidthPreferred,screenHeightPreferred);
		}else{
			//if pref fullScreen
			boolean screenModeMatched = false;
			DisplayMode[] displayModes = this.getDisplayModes(this.getCurrentMonitor());
			for(DisplayMode dMode: displayModes){
				if(dMode.width == screenWidthPreferred && dMode.height == screenHeightPreferred){
					screenModeMatched = true;
					displayModeSelected = dMode;
				}
			}
			
			if(!screenModeMatched){
				displayModeSelected = this.getLargestScreenSize();
			}
			
			return this.setFullscreenMode(displayModeSelected);
		
		}
	}
}














