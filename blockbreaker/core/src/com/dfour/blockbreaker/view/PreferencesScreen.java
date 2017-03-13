package com.dfour.blockbreaker.view;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.dfour.blockbreaker.BlockBreaker;
/**
 * This is the application settings screen and will be for 
 * setting up the app to suit the users preferences
 * 
 * @author John Day
 *
 */
public class PreferencesScreen implements Screen{
	
	private Stage stage;
	private Table optionsTable;
	private BlockBreaker parent;
	private Skin skin;
	// private Sprite mainScreen;
	private Label volumeValue, soundValue,zoomValue;
	private TextureAtlas atlasGui;
	private SpriteBatch pb;
	private int sw;
	private int sh;
	private AtlasRegion bg;
	private Table table;
	
	private float fadeIn = 1f;
	private float fadeOut = 1f;
	private boolean isReturning = false;
	
	public PreferencesScreen(BlockBreaker parent){
		this.parent = parent;
		
		atlasGui = parent.assMan.manager.get("gui/loadingGui.pack");
		bg = atlasGui.findRegion("background");

	    stage = new Stage(new ScreenViewport());
		pb = new SpriteBatch();
		
	}
	
	@Override
	public void render(float delta) {
		//clear screen
		Gdx.gl.glClearColor(.4f, .4f, .4f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		pb.begin();
		// draw brick background
		for(int i = 0; i < sw; i += 20){
			for(int j = 0; j < sh; j+= 10){
				pb.draw(bg, i, j,20,10);
			}
		}
		pb.end();
		
		if(fadeIn > 0){
			fadeIn -= delta;
			optionsTable.setColor(1,1,1,1-fadeIn);
		}else if(this.isReturning){
			fadeOut -= delta;
			optionsTable.setColor(1,1,1,fadeOut);
			if(fadeOut <= 0){
				parent.changeScreen(BlockBreaker.MENU);
			}
		}

		stage.act();
		stage.draw();
		
	}

	@Override
	public void resize(int width, int height) {	
		sw = width;
		sh = height;
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void show() {
		fadeIn = 1f;
		fadeOut = 1f;
		table = new Table();
		table.setFillParent(true);
		table.setDebug(BlockBreaker.debug);
		optionsTable = new Table();
		optionsTable.setDebug(BlockBreaker.debug);
		
		
		Gdx.input.setInputProcessor(stage);
		skin = parent.assMan.manager.get("uiskin.json",Skin.class);
		
		
		
		
		
		// add inputs to stage to allow changing of preferences
		final Slider volumeSlider = new Slider( 0f, 1f, 0.1f,false, skin );
        volumeSlider.setValue( parent.getPreferences().getVolume()  );
        volumeSlider.addListener( new EventListener() {
  		@Override
			public boolean handle(Event event) {
  			parent.getPreferences().setVolume( volumeSlider.getValue() );
  			parent.changeVolume();
            updateVolumeLabel();
            return false;
			}
        } );
		
		//music
		final CheckBox musicCheckbox = new CheckBox(null, skin);
		musicCheckbox.setChecked( parent.getPreferences().isMusicEnabled() );
		musicCheckbox.addListener( new EventListener() {
	    	@Override
			public boolean handle(Event event) {
	           	boolean enabled = musicCheckbox.isChecked();
	           	parent.getPreferences().setMusicEnabled( enabled );
	           	if(enabled){
	           		parent.startMusic();
	           	}else{
	           		parent.stopMusic();
	           	}
				return false;
			}
	    });
		//Windowed
		final CheckBox windowedCheckbox = new CheckBox(null, skin);
		windowedCheckbox.setChecked( parent.getPreferences().getWindowed() );
		windowedCheckbox.addListener( new EventListener() {
	    	@Override
			public boolean handle(Event event) {
	           	boolean enabled = windowedCheckbox.isChecked();
	           	parent.getPreferences().setWindowed( enabled );
	         
	    		return false;
			}
	    });
		
		//sound
		final CheckBox soundEffectsCheckbox = new CheckBox(null, skin);
		soundEffectsCheckbox.setChecked( parent.getPreferences().isSoundEffectsEnabled() );
	    soundEffectsCheckbox.addListener( new EventListener() {
	    	@Override
			public boolean handle(Event event) {
	           	boolean enabled = soundEffectsCheckbox.isChecked();
	           	parent.getPreferences().setSoundEffectsEnabled( enabled );
				return false;
			}
	    });
	    
	    // return to main screen button
	    final TextButton backButton = new TextButton("Back", skin);
	    backButton.addListener( new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				PreferencesScreen.this.isReturning = true;		
			}
	    });
	    
	    final Slider lightSlider = new Slider( 0f, 1f, 0.1f,false, skin );
	    lightSlider.setValue( parent.getPreferences().getLightingQuality() );
	    lightSlider.addListener( new EventListener() {
  		@Override
			public boolean handle(Event event) {
  			parent.getPreferences().setLightingQuality(lightSlider.getValue() );
            updateLightingLabel();
            return false;
			}
        } );
        
        final Slider soundSlider = new Slider( 0f, 1f, 0.1f,false, skin );
        soundSlider.setValue( parent.getPreferences().getSoundVolume() );
        soundSlider.addListener( new EventListener() {
  		@Override
			public boolean handle(Event event) {
  			parent.getPreferences().setSoundVolume( soundSlider.getValue() );
            updateScrollLabel();
            return false;
			}
        } );
	    
	    //labels
	    
	    volumeValue = new Label( null, skin );
	    zoomValue = new Label( null, skin );
	    soundValue = new Label( null, skin );
        updateVolumeLabel();
        updateScrollLabel();
        updateLightingLabel();
        Image title = new Image(atlasGui.findRegion("blockBreakerTitle"));
        table.add(title).pad(10);
        table.row().expandY();
        optionsTable.add(new Label("Windowed",skin)).uniformX().align(Align.left);// music label
        optionsTable.add(windowedCheckbox).align(Align.left);
        optionsTable.row();
        optionsTable.add(new Label("Music",skin)).uniformX().align(Align.left);// music label
        optionsTable.add(musicCheckbox).align(Align.left);
        optionsTable.row();
        optionsTable.add(new Label("Sound",skin)).uniformX().align(Align.left);//sound label
        optionsTable.add(soundEffectsCheckbox).align(Align.left);
        optionsTable.row();
        optionsTable.add(volumeValue).uniformX().align(Align.left);// volume label
        optionsTable.add(volumeSlider).align(Align.left);
        optionsTable.row();
        optionsTable.add(soundValue).uniformX().align(Align.left);// Scroll label
        optionsTable.add(soundSlider).align(Align.left);
        optionsTable.row();
        optionsTable.add(zoomValue).uniformX().minWidth(200);// volumZoome label
        optionsTable.add(lightSlider).align(Align.left);
        optionsTable.row();
        optionsTable.add(backButton).colspan(2).center().minHeight(40);
        table.add(optionsTable);
        stage.addActor(table);
        
	}

	@Override
	public void hide() {		
	}

	@Override
	public void pause() {		
	}

	@Override
	public void resume() {		
	}

	@Override
	public void dispose() {	
	}
	
	/**
     * Updates the volume label next to the slider.
     */
	private void updateVolumeLabel()
    {
        float volume = ( parent.getPreferences().getVolume() * 100 );
        //volumeValue.setText("Volume: "+ String.format( Locale.US, "%1.0f%%", volume ) );
        volumeValue.setText("Music Volume: "+  Math.round(volume));
    }
	
	private void updateLightingLabel()
    {
        float zoom = ( parent.getPreferences().getLightingQuality() * 100 );
        //zoomValue.setText("Zoom: "+ String.format( Locale.US, "%1.0f%%", zoom ) );
        zoomValue.setText("Lighting Quality: "+ Math.round(zoom)  );
    }
	
	private void updateScrollLabel()
    {
        float scroll = ( parent.getPreferences().getSoundVolume() * 100 );
        //scrollValue.setText("Scroll: "+ String.format( Locale.US, "%1.0f%%", scroll ) );
        soundValue.setText("Sound Volume: "+ Math.round(scroll) );
    }

}
