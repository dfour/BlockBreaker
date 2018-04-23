#ifdef GL_ES
precision mediump float;
#endif

uniform vec3      iResolution;           // viewport resolution (in pixels)
uniform float     iTime;                 // shader playback time (in seconds)
//uniform float     iTimeDelta;            // render time (in seconds)
//uniform int       iFrame;                // shader playback frame
//uniform float     iChannelTime[4];       // channel playback time (in seconds)
//uniform vec3      iChannelResolution[4]; // channel resolution (in pixels)
//uniform vec4      iMouse;                // mouse pixel coords. xy: current (if MLB down), zw: click
uniform sampler2D iChannel0;          	 // input channel. XX = 2D/Cube
//uniform vec4      iDate;                 // (year, month, day, time in seconds)
//uniform float     iSampleRate;           // sound sample rate (i.e., 44100)

#define	SPEED 		0.02
#define	STAR_NUMBER 100

vec3 col1 = vec3(0, 0.3, 0.5); // Coolest star color
vec3 col2 = vec3(0.3, 0.7, 0.5); // Hottest star color

float rand(float i){
    return fract(sin(dot(vec2(i, i) ,vec2(32.9898,78.233))) * 43758.5453);
}

void main(){
	vec2 uv = gl_FragCoord.xy / iResolution.y;
    float res = iResolution.x / iResolution.y;
    //vec4 fragColor = vec4(0.0);
    
    // static far stars    
   	vec4 sStar = vec4(rand(uv.x * uv.y));
    sStar *= pow(rand(uv.x * uv.y), 200.);
    sStar.xyz *= mix(col1, col2, rand(uv.x + uv.y));
    gl_FragColor += sStar;
    
    // milky way
    vec4 col = 0.5 - vec4(length(vec2(uv.x, 0.5) - uv));
    col.xyz *= mix(col1, col2, 0.75);
    gl_FragColor += col * 2.;
    vec4 c = vec4(0.);
    vec4 c2 = vec4(0.);
    vec2 rv = uv;
    rv.x -= iTime * SPEED * 0.25;
    for(int i=0;i<4;i++)
		c += texture2D(iChannel0, rv * 0.25 + rand(float(i + 10) + uv.x * uv.y) * (16. / iResolution.y) * 0.25);
    gl_FragColor -= c * 0.5;
    gl_FragColor = clamp(gl_FragColor, 0.0, 1.0);
    
    // Dynamic Stars    
    for (int i = 0; i < STAR_NUMBER; ++i){
    	float n = float(i);
        
        //position of the star
        vec3 pos = vec3(rand(n) * res + (iTime + 100.0) * SPEED, rand(n + 1.) , rand(n + 2.));
        
        // parralax effect
        pos.x = mod(pos.x * pos.z, res);
        pos.y = (pos.y + rand(n + 10.)) * 0.5;

        //drawing the star
        vec4 col = vec4(pow(length(pos.xy - uv), -1.25) * 0.001 * pos.z * rand(n + 3.));
        
        //coloring the star
        col.xyz *= mix(col1, col2, rand(n + 4.));
        
        //star flickering
        col.xyz *= mix(rand(n + 5.), 1.0, abs(cos(iTime * rand(n + 6.) * 5.)));
        
        gl_FragColor += vec4(col);
    }
}