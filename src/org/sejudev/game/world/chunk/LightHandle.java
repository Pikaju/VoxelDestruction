package org.sejudev.game.world.chunk;

import java.util.ArrayList;
import java.util.List;

public class LightHandle {

	public float[][][] ints;
	
	public Chunk chunk;
	
	private List<Light> lights = new ArrayList<Light>();
	
	public LightHandle(Chunk chunk) {
		this.chunk = chunk;
		ints = new float[Chunk.SIZE][Chunk.SIZE][Chunk.SIZE];
	}
	
	public void clear() {
		for(int x = 0; x < Chunk.SIZE; x++) {
			for(int y = 0; y < Chunk.SIZE; y++) {
				for(int z = 0; z < Chunk.SIZE; z++) {
					ints[x][y][z] = 0;
				}
			}	
		}
	}
	
	public void refresh() {
		for(int i = 0; i < lights.size(); i++) {
			Light l = lights.get(i);
			spread(l.intensity, l.x, l.y, l.z, true);
		}
		lights.clear();
	}
	
	public void spread(float intensity, int x, int y, int z, boolean first) {
		if(intensity <= 0) return;
		if(x < 0 || y < 0 || z < 0 || x >= Chunk.SIZE || y >= Chunk.SIZE || z >= Chunk.SIZE) {
			return;
		}
		if(intensity <= ints[x][y][z] && !first) return;
		boolean b = false;
		for(int xx = x - 1; xx < x + 1; xx++) {
			for(int yy = y - 1; yy < y + 1; yy++) {
				for(int zz = z - 1; zz < z + 1; zz++) {
					if(chunk.getUnboundBlock(xx, yy, zz) == 0)
						b = true;
				}	
			}	
		}
		if(!b) return;
		
		intensity -= 0.1f;
		
		ints[x][y][z] = intensity;
		spread(intensity, x, y + 1, z, false);
		spread(intensity, x, y - 1, z, false);
		spread(intensity, x + 1, y, z, false);
		spread(intensity, x - 1, y, z, false);
		spread(intensity, x, y, z + 1, false);
		spread(intensity, x, y, z - 1, false);
	}
	
	public void addLight(int x, int y, int z, float intensity) {
		lights.add(new Light(x, y, z, intensity));
		ints[x][y][z] = intensity;
	}
	
	public float getLight(int bx, int by, int bz) {
		return ints[bx][by][bz];
	}
}
