package com.dfour.blockbreaker.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.dfour.blockbreaker.BlockBreaker;

public class DesktopLauncher {
	public static void main (String[] arg) {
		
		// common andoird resolutions
		// 480  x 800
		// 480  x 854
		// 600  x 1024
		// 1024 x 786
		// 1920 x 1200
		// 640  x 960
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1024;
		config.height = 768;
		new LwjglApplication(new BlockBreaker(), config);
	}
}
