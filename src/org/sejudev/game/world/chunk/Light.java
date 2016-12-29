package org.sejudev.game.world.chunk;

public class Light {
	
	public int x;
	public int y;
	public int z;
	public float intensity;
	
	public Light(int x, int y, int z, float intensity) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.intensity = intensity;
	}
}
