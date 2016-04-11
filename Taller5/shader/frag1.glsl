uniform sampler2D texture;
varying vec4 vertColor;
varying vec4 vertTexCoord;

void main()
{
	float coordX= vertTexCoord.x;
	float coordY= vertTexCoord.y;
	gl_FragColor= texture2D (texture, vec2(coordX, coordY));
}