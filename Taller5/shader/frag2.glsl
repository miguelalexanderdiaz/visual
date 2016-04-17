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
		coordX+= ax * sin(coordY*fx*3.1416+offset)/100;
		coordY+= ay * sin(coordX*fy*3.1416+offset)/100;
		coordY = 2 * 0.68 - coordY;
	}
	
	gl_FragColor= texture2D (texture, vec2(coordX, coordY));
}