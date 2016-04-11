uniform float offset;
uniform sampler2D texture;

varying vec4 vertColor;
varying vec4 vertTexCoord;

void main()
{
	float coordX= vertTexCoord.x;
	float coordY= vertTexCoord.y;
	coordX+= sin(coordY*10*3.1416+offset)/100;
	gl_FragColor= texture2D (texture, vec2(coordX, coordY));
}