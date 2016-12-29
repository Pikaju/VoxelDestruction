
varying vec4 vertex;
varying vec4 color;
varying vec3 normal;

void main() {
	float normalCol = (normal.y * 0.1) + (normal.x * 0.05) + (normal.z * 0.025);
	gl_FragColor = vec4(color.xyz + normalCol, 1);
}