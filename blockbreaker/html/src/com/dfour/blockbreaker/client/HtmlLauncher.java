package com.dfour.blockbreaker.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.dfour.blockbreaker.BlockBreaker;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(1024,786);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new BlockBreaker();
        }
}