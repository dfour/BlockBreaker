package com.dfour.blockbreaker;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Graphics.Monitor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.dfour.blockbreaker.loaders.BBAssetManager;
import com.dfour.blockbreaker.view.ApplicationScreen;
import com.dfour.blockbreaker.view.ControlMapScreen;
import com.dfour.blockbreaker.view.CustomMapScreen;
import com.dfour.blockbreaker.view.LevelDesignerScreen;
import com.dfour.blockbreaker.view.LoadingScreen;
import com.dfour.blockbreaker.view.PreferencesScreen;
import com.dfour.blockbreaker.view.ShopScreen;
import com.dfour.blockbreaker.AppPreferences;
import com.dfour.blockbreaker.view.EndScreen;
import com.dfour.blockbreaker.view.MenuScreen;

public class BlockBreaker extends Game {
	//TODO finish designing end screen
	//TODO create bug reporter( in game or online )
	//TODO try make bricks that hit black hole spin into oblivion (see add animations todo)
	//TODO add animations to entities using boolean to state image vs animation for (normal, death, black hole death)
	//DONTBOTHER try move all objects into external xml definition files (and make obstacles use interfaces(one for each action: magnetise, explode etc))
	//TODO NEEDS TESTING hide cursor when in game and show when in menus with custom cursor
	//TODO sticky pad/ tractor beam
	//TODO negative power ups such as drunk/slow/angled
	//TODO add larger pad mechanic
	//TODO make GUI class that overlays application screen using stage
	//TODO add continue button (saving score and cash in preferences ?? possible cheat)
	//TODO NEEDS TESTING add release ball key to prefs
	//TODO look into changing box2d step size to alter time
	
	private MenuScreen menu;
	private PreferencesScreen prefs;
	private AppPreferences preferences;
	private ApplicationScreen app;
	private LoadingScreen loadingScreen;
	private LevelDesignerScreen designScreen;
	private ShopScreen shopScreen;
	private EndScreen end;
	private boolean musicLoaded = false;
	public BBAssetManager assMan = new BBAssetManager();
	
	public static boolean debug = false;
	
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
	
	@Override
	public void create () {
		loadingScreen = new LoadingScreen(this);
		preferences = new AppPreferences();
		
		this.updateScreenSize();
		
		//Pixmap pm = new Pixmap(Gdx.files.internal("normalCursor.png"));
		//Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 0, 0));
		//pm.dispose();
		
		
		
		setScreen(loadingScreen);
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
				shopScreen = new ShopScreen(this, app.bbModel);
				this.setScreen(shopScreen);
				break;
			case CONTROL:
				this.setScreen(new ControlMapScreen(this));
				break;
			case CUSTOM_MAP:
				this.setScreen(new CustomMapScreen(this));
				break;
			case APPLICATION:
				if(currentSound != gameMusic && preferences.isMusicEnabled()){
					currentSound.stop();
					currentSound = gameMusic;
					currentSongId = currentSound.loop(preferences.getVolume());
				}
				if(app == null) app = new ApplicationScreen(this);
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
		int width = Integer.valueOf(sizes[0]);
		int height = Integer.valueOf(sizes[1]);
		
		DisplayMode displayModeSelected = null;
		
		// set the screen size
		if(preferences.getWindowed()){
		// if pref windowed
			return this.setWindowedMode(width,height);
		}else{
			//if pref fullScreen
			boolean screenModeMatched = false;
			DisplayMode[] displayModes = this.getDisplayModes(this.getCurrentMonitor());
			for(DisplayMode dMode: displayModes){
				if(dMode.width == width && dMode.height == height){
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














