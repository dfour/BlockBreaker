package com.dfour.blockbreaker;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class AppPreferences {
	private static final String PREF_VOLUME = "volume";
	private static final String PREF_MUSIC_ENABLED = "music.enabled";
	private static final String PREF_SOUND_ENABLED = "sound.enabled";
	private static final String PREF_LIGHTING_QUALITY = "Lighting Quality";
	private static final String PREF_SOUND_VOL = "sound";
	private static final String PREF_SCREEN_SIZE = "Screen Size";
	private static final String PREF_WINDOWED = "Windowed";
	private static final String PREFS_NAME = "dfour";

	protected Preferences getPrefs() {
		return Gdx.app.getPreferences(PREFS_NAME);
	}

	public boolean isSoundEffectsEnabled() {
		return getPrefs().getBoolean(PREF_SOUND_ENABLED, true);
	}

	public void setSoundEffectsEnabled(boolean soundEffectsEnabled) {
		getPrefs().putBoolean(PREF_SOUND_ENABLED, soundEffectsEnabled);
		getPrefs().flush();
	}

	public boolean isMusicEnabled() {
		return getPrefs().getBoolean(PREF_MUSIC_ENABLED, true);
	}

	public void setMusicEnabled(boolean musicEnabled) {
		getPrefs().putBoolean(PREF_MUSIC_ENABLED, musicEnabled);
		getPrefs().flush();
	}

	public float getVolume() {
		return getPrefs().getFloat(PREF_VOLUME, 0.5f);
	}

	public void setVolume(float volume) {
		getPrefs().putFloat(PREF_VOLUME, volume);
		getPrefs().flush();
	}
	
	public float getLightingQuality() {
		return getPrefs().getFloat(PREF_LIGHTING_QUALITY, 0.3f);
	}

	public void setLightingQuality(float volume) {
		getPrefs().putFloat(PREF_LIGHTING_QUALITY, volume);
		getPrefs().flush();
	}
	
	public float getSoundVolume() {
		return getPrefs().getFloat(PREF_SOUND_VOL, 0.5f);
	}

	public void setSoundVolume(float volume) {
		getPrefs().putFloat(PREF_SOUND_VOL, volume);
		getPrefs().flush();
	}
	
	public void setScreenSize(String screenSize){
		getPrefs().putString(PREF_SCREEN_SIZE, screenSize);
		getPrefs().flush();
	}
	
	public String getScreenSize(){
		return getPrefs().getString(PREF_SCREEN_SIZE, "1024x768");
		
	}
	
	public void setWindowed(boolean windowed){
		getPrefs().putBoolean(PREF_WINDOWED, windowed);
		getPrefs().flush();
	}
	
	public boolean getWindowed(){
		return getPrefs().getBoolean(PREF_WINDOWED, true);
		
	}
}