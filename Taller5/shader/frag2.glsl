uniform float offset;
uniform float f;
uniform float A;
uniform sampler2D texture;

varying vec4 vertColor;
varying vec4 vertTexCoord;

void main()
{
	float coordX= vertTexCoord.x;
	float coordY= vertTexCoord.y;
	
	if(coordY > 0.68){
		coordX+= A * sin(coordY*10*3.1416+offset)/100;
		coordY+= A * sin(coordX*2*3.1416+offset)/100;
		coordY = 2 * 0.68 - coordY;
	}
	
	gl_FragColor= texture2D (texture, vec2(coordX, coordY));
}