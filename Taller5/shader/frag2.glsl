uniform float offset;
uniform float fx;
uniform float ax;
uniform float fy;
uniform float ay;
uniform sampler2D texture;

varying vec4 vertColor;
varying vec4 vertTexCoord;

void main()
{
	float coordX= vertTexCoord.x;
	float coordY= vertTexCoord.y;
	
	if(coordY > 0.68){
		coordX+= 2 * sin(coordY*2*3.1416+offset)/100;
		coordY+= 3 * sin(coordX*3*3.1416+offset)/100;
		coordY = 2 * 0.68 - coordY;
	}
	
	gl_FragColor= texture2D (texture, vec2(coordX, coordY));
}