package com.dfour.blockbreaker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;

public class BBUtils {
	private static String[] fnames;
	private static String[] lnames;

	public static Color hsvToRgba(float hue, float saturation, float value, float alpha) {

	    int h = (int)(hue * 6);
	    float f = hue * 6 - h;
	    float p = value * (1 - saturation);
	    float q = value * (1 - f * saturation);
	    float t = value * (1 - (1 - f) * saturation);

	    switch (h) {
	      case 0: return new Color(value, t, p,alpha);
	      case 1: return new Color(q, value, p,alpha);
	      case 2: return new Color(p, value, t,alpha);
	      case 3: return new Color(p, q, value,alpha);
	      case 4: return new Color(t, p, value,alpha);
	      case 5: return new Color(value, p, q,alpha);
	      default: throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + hue + ", " + saturation + ", " + value);
	    }
	}

	public static String rgbToString(float r, float g, float b) {
	    String rs = Integer.toHexString((int)(r * 256));
	    String gs = Integer.toHexString((int)(g * 256));
	    String bs = Integer.toHexString((int)(b * 256));
	    return rs + gs + bs;
	}
	
	public static String generateRandomName(){
		String name = "";
		if(fnames == null){
			FileHandle fnfile = Gdx.files.internal("fname.txt");
			fnames = fnfile.readString().split("\n");
			
			FileHandle lnfile = Gdx.files.internal("lname.txt");
			lnames = lnfile.readString().split("\n");
		}
		int fni = (int)(Math.random() * fnames.length);
		name = fnames[fni].trim();
		
		int lni = (int)(Math.random() * lnames.length);
		name += "_"+lnames[lni].trim();
		
		return name;
	}
}
