package com.dfour.blockbreaker.view;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.dfour.blockbreaker.BlockBreaker;
/**
 * This is the application settings screen and will be for 
 * setting up the app to suit the users preferences
 * 
 * @author John Day
 *
 */
public class PreferencesScreen extends Scene2DScreen {
	
	// private Sprite mainScreen;
	private Label volumeValue, soundValue,lightingQuality;
	
	public PreferencesScreen(BlockBreaker p){
		super(p);
	}
	
	@Override
	public void show() {
		super.show();
	
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
				PreferencesScreen.this.returnScreen = BlockBreaker.MENU;
				PreferencesScreen.this.isReturning = true;		
			}
	    });
	    
	    final TextButton controlsButton = new TextButton ("Controls",skin);
	    controlsButton.addListener(new ClickListener(){

			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				PreferencesScreen.this.returnScreen = BlockBreaker.CONTROL;
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
	    lightingQuality = new Label( null, skin );
	    soundValue = new Label( null, skin );
        updateVolumeLabel();
        updateScrollLabel();
        updateLightingLabel();
        
        Table buttonTable = new Table();
        buttonTable.add(backButton).padRight(15);
        buttonTable.add(controlsButton);

        displayTable.add(new Label("Windowed",skin)).uniformX().width(325).align(Align.left);// music label
        displayTable.add(windowedCheckbox).align(Align.left);
        displayTable.row();
        displayTable.add(new Label("Music",skin)).uniformX().align(Align.left);// music label
        displayTable.add(musicCheckbox).align(Align.left);
        displayTable.row();
        displayTable.add(new Label("Sound",skin)).uniformX().align(Align.left);//sound label
        displayTable.add(soundEffectsCheckbox).align(Align.left);
        displayTable.row();
        displayTable.add(volumeValue).uniformX().align(Align.left);// volume label
        displayTable.add(volumeSlider).align(Align.left);
        displayTable.row();
        displayTable.add(soundValue).uniformX().align(Align.left);// Scroll label
        displayTable.add(soundSlider).align(Align.left);
        displayTable.row();
        displayTable.add(lightingQuality).uniformX().minWidth(325);// lighting quality label
        displayTable.add(lightSlider).align(Align.left);
        displayTable.row();
        displayTable.add(buttonTable).colspan(2).center();

        
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
        lightingQuality.setText("Lighting Quality: "+ Math.round(zoom)  );
    }
	
	private void updateScrollLabel()
    {
        float scroll = ( parent.getPreferences().getSoundVolume() * 100 );
        //scrollValue.setText("Scroll: "+ String.format( Locale.US, "%1.0f%%", scroll ) );
        soundValue.setText("Sound Volume: "+ Math.round(scroll) );
    }

}
