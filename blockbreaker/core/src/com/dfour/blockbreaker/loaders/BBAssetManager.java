package com.dfour.blockbreaker.loaders;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader.ParticleEffectParameter;
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class BBAssetManager {
	
	public final AssetManager manager = new AssetManager();
	
	// Textures
	public final String imagesPack 		= "images/images.pack";
	public final String lazorPack 		= "lazor/lazor.pack";
	
	/*
	public final String obbTitle			= "gui/blockBreakerTitle.png";
	public final String obbLoading		= "gui/loading.png";
	public final String ovisFontTexture 	= "font/visitor.png";
	public final String ocatborder 		= "secretCatImage.png";
	public final String ogameOver 		= "gameover.png";
	public final String olazerStartBg 	= "lazor/lazorStart.png";
	public final String olazerStartOver 	= "lazor/lazorStartOver.png";
	public final String olazerMidBg 		= "lazor/lazor.png";
	public final String olazerMidOver 	= "lazor/lazorOver.png";
	public final String olazerEndBg 		= "lazor/lazorEnd.png";
	public final String olazerEndOver 	= "lazor/lazorEndOver.png";
	public final String olazerAllOver 	= "lazor/lazorAllOver.png";
	public final String oball			= "ball.png";
	public final String omagBall			= "mag_ball.png";
	public final String olightBulb		= "lightBulb.png";
	public final String opaddle			= "paddel.png";
	public final String opaddlePull		= "paddel-magnet-pull.png";
	public final String opaddlePush		= "paddel-magnet-push.png";
	public final String ohover1			= "hover_1.png";
	public final String ohover2			= "hover_2.png";
	public final String ohover3			= "hover_3.png";
	public final String ohover4			= "hover_4.png";
	public final String ohover5			= "hover_5.png";
	public final String ohover6			= "hover_6.png";
	public final String omag_power_pup	= "mag_power_pup.png";
	public final String omag_strength_pup= "mag_strength_pup.png";
	public final String oextra_ball_pup	= "extra_ball_pup.png";
	public final String oextra_mag_ball_pup= "extra_mag_ball_pup.png";
	public final String olazer_pup		= "lazer_pup.png";
	public final String oscore_pup		= "score_pup.png";
	public final String oball_lazer_pup	= "ball_lazer_pup.png";
	public final String obomb_pup		= "bomb_pup.png";
	public final String oprogressBarStart= "gui/progressBarStart.png";
	public final String oprogressBarMiddle= "gui/progressBarMiddle.png";
	public final String oprogressBarEnd	= "gui/progressBarEnd.png";
	public final String oprogressBarSurround	= "gui/progressBarSurround.png";
	public final String opnkButtonNine	= "gui/btn_blue_pink_glossy.9.png";
	public final String obomb			= "bomb.png";
	*/
	
	public void loadImages(){
		manager.load(imagesPack, TextureAtlas.class);
		manager.load(lazorPack, TextureAtlas.class);
	}
	
	// fonts
	public final String visfont 			= "font/visitor.fnt";
	
	public void loadFonts(){
		manager.load(visfont, BitmapFont.class);
	}
	
	// Particle Effects
	public final String sparksPE		="particles/sparks.pe";
	public final String explosionPE		="particles/explosion.pe";
	public final String lazerPE			="particles/lazer.pe";
	public final String pupGetEffectPE	="particles/pupGetEffect.pe";
	public final String laserHitPE		="particles/laserHitSparks.pe";
	
	public void loadParticleEffects(){
		ParticleEffectParameter pep = new ParticleEffectParameter(); 
		pep.atlasFile = "images/images.pack";
		manager.load(sparksPE, ParticleEffect.class, pep);
		manager.load(explosionPE, ParticleEffect.class, pep);
		manager.load(lazerPE, ParticleEffect.class, pep);
		manager.load(pupGetEffectPE, ParticleEffect.class, pep);
		manager.load(laserHitPE, ParticleEffect.class, pep);
	}
	
	//Sounds
	public final String explosionSound	= "sounds/explosion.wav";
	public final String boingSound		= "sounds/boing.wav";
	public final String pingSound		= "sounds/ping.wav";
	
	public void loadSounds(){
		manager.load(explosionSound, Sound.class);
		manager.load(boingSound, Sound.class);
		manager.load(pingSound, Sound.class);
	}
	//Music
	public final String introMusic 		= "music/sawsquarenoise_-_09_-_OST_09_Metallius.mp3";
	public final String gameMusic1		= "music/sawsquarenoise_-_11_-_OST_11_Where_Am_I_.mp3";
	public final String endMusic 		= "music/sawsquarenoise_-_10_-_OST_10_Go_Go_Metallius.mp3";
	
	public void loadMusic(){
		manager.load(introMusic, Sound.class);
		manager.load(gameMusic1, Sound.class);
		manager.load(endMusic, Sound.class);
	}
	
	// Skin
	public final String skin	= "uiskin.json";
	
	public void loadSkin(){
		SkinParameter params = new SkinParameter("uiskin.atlas");
		manager.load(skin, Skin.class, params);
		
	}

	public final String loadingImages 	= "gui/loadingGui.pack";
	
	public void loadLoadingData(){
		manager.load(loadingImages, TextureAtlas.class);
	}
	
	public void dispose(){
		manager.dispose();
	}
}
