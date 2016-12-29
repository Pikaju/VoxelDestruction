package org.sejudev.game.world;

import org.sejudev.game.Game;
import org.sejudev.game.shader.Shader;
import org.sejudev.game.util.Vec3;
import org.sejudev.game.world.chunk.Chunk;


public class World {
	
	private Chunk[][][] chunks;
	
	public World() {
		chunks = new Chunk[8][4][8];
		createChunks();
		generateChunks();
		generateChunks2();
		generateDayLight();
	}

	public void update() {
		for(int x = 0; x < chunks.length; x++) {
			for(int y = 0; y < chunks[0].length; y++) {
				for(int z = 0; z < chunks[0][0].length; z++) {
					getChunk(x, y, z).update();
				}
			}
		}
	}
	
	public void render() {
		Shader.world.enable();
		for(int x = 0; x < chunks.length; x++) {
			for(int y = 0; y < chunks[0].length; y++) {
				for(int z = 0; z < chunks[0][0].length; z++) {
					getChunk(x, y, z).render();
				}
			}
		}
		Shader.world.disable();
	}
	
	public void explode(int x, int y, int z, int radius) {
		for(int xx = x - radius; xx < x + radius; xx++) {
			for(int yy = y - radius; yy < y + radius; yy++) {
				for(int zz = z - radius; zz < z + radius; zz++) {
					int xl = Math.abs(xx - x);
					int yl = Math.abs(yy - y);
					int zl = Math.abs(zz - z);
					if(Math.sqrt(xl * xl + yl * yl + zl * zl) < radius) {
						setMassBlock(xx, yy, zz, 0);
					}
				}
			}
		}
	}
	
	public Chunk getChunk(int cx, int cy, int cz) {
		if(cx < 0 || cy < 0 || cz < 0 || cx >= chunks.length || cy >= chunks[0].length || cz >= chunks[0][0].length) {
			return null;
		}
		return chunks[cx][cy][cz];
	}
	
	public void setChunk(int cx, int cy, int cz, Chunk chunk) {
		if(cx < 0 || cy < 0 || cz < 0 || cx >= chunks.length || cy >= chunks[0].length || cz >= chunks[0][0].length) {
			return;
		}
		chunks[cx][cy][cz] = chunk;
	}
	
	public int getBlock(int x, int y, int z) {
		int cx = (int) Math.floor(x / (float) Chunk.SIZE);
		int cy = (int) Math.floor(y / (float) Chunk.SIZE);
		int cz = (int) Math.floor(z / (float) Chunk.SIZE);
		
		Chunk c = getChunk(cx, cy, cz);
		if(c == null) {
			return 0x000000;
		}
		
		int bx = x & (Chunk.SIZE - 1);
		int by = y & (Chunk.SIZE - 1);
		int bz = z & (Chunk.SIZE - 1);
		
		return c.getBlock(bx, by, bz);
	}
	
	public void setBlock(int x, int y, int z, int col) {
		int cx = (int) Math.floor(x / (float) Chunk.SIZE);
		int cy = (int) Math.floor(y / (float) Chunk.SIZE);
		int cz = (int) Math.floor(z / (float) Chunk.SIZE);
		
		Chunk c = getChunk(cx, cy, cz);
		if(c == null) {
			return;
		}
		
		int bx = x & (Chunk.SIZE - 1);
		int by = y & (Chunk.SIZE - 1);
		int bz = z & (Chunk.SIZE - 1);
		
		c.setBlock(bx, by, bz, col);
	}
	
	public void setGenBlock(int x, int y, int z, int col) {
		int cx = (int) Math.floor(x / (float) Chunk.SIZE);
		int cy = (int) Math.floor(y / (float) Chunk.SIZE);
		int cz = (int) Math.floor(z / (float) Chunk.SIZE);
		
		Chunk c = getChunk(cx, cy, cz);
		if(c == null) {
			return;
		}
		
		int bx = x & (Chunk.SIZE - 1);
		int by = y & (Chunk.SIZE - 1);
		int bz = z & (Chunk.SIZE - 1);
		
		c.setGenBlock(bx, by, bz, col);
	}
	
	public void setMassBlock(int x, int y, int z, int col) {
		int cx = (int) Math.floor(x / (float) Chunk.SIZE);
		int cy = (int) Math.floor(y / (float) Chunk.SIZE);
		int cz = (int) Math.floor(z / (float) Chunk.SIZE);
		
		Chunk c = getChunk(cx, cy, cz);
		if(c == null) {
			return;
		}
		
		int bx = x & (Chunk.SIZE - 1);
		int by = y & (Chunk.SIZE - 1);
		int bz = z & (Chunk.SIZE - 1);
		
		c.setMassBlock(bx, by, bz, col);
	}
	
	public boolean isOpaqueCube(int x, int y, int z) {
		return getBlock(x, y, z) != 0x000000;
	}

	public float getLight(int x, int y, int z) {
		int cx = (int) Math.floor(x / (float) Chunk.SIZE);
		int cy = (int) Math.floor(y / (float) Chunk.SIZE);
		int cz = (int) Math.floor(z / (float) Chunk.SIZE);
		
		Chunk c = getChunk(cx, cy, cz);
		if(c == null) {
			return 0x000000;
		}
		
		int bx = x & (Chunk.SIZE - 1);
		int by = y & (Chunk.SIZE - 1);
		int bz = z & (Chunk.SIZE - 1);
		
		return c.getLight(bx, by, bz);
	}
	
	public void addLight(int x, int y, int z, float intensity) {
		int cx = (int) Math.floor(x / (float) Chunk.SIZE);
		int cy = (int) Math.floor(y / (float) Chunk.SIZE);
		int cz = (int) Math.floor(z / (float) Chunk.SIZE);
		
		Chunk c = getChunk(cx, cy, cz);
		if(c == null) {
			return;
		}
		
		int bx = x & (Chunk.SIZE - 1);
		int by = y & (Chunk.SIZE - 1);
		int bz = z & (Chunk.SIZE - 1);
		
		c.addLight(bx, by, bz, intensity);
	}
	
	public void createChunks() {
		for(int x = 0; x < chunks.length; x++) {
			for(int y = 0; y < chunks[0].length; y++) {
				for(int z = 0; z < chunks[0][0].length; z++) {
					setChunk(x, y, z, new Chunk(this, x, y, z));
				}
			}
		}
	}
	
	public void generateChunks() {
		for(int x = 0; x < chunks.length; x++) {
			for(int y = 0; y < chunks[0].length; y++) {
				for(int z = 0; z < chunks[0][0].length; z++) {
					getChunk(x, y, z).generate();
				}
			}
		}
	}
	
	public void generateChunks2() {
		for(int x = 0; x < chunks.length; x++) {
			for(int y = 0; y < chunks[0].length; y++) {
				for(int z = 0; z < chunks[0][0].length; z++) {
					getChunk(x, y, z).generate2();
				}
			}
		}
	}

	public void generateDayLight() {
		for(int x = 0; x < getWidth(); x++) {
			for(int z = 0; z < getLength(); z++) {
				for(int y = getHeight() - 1; y >= 0; y--) {
					if(isOpaqueCube(x, y, z)) break;
					addLight(x, y, z, 1);
				}
			}
		}
		refreshLight();
	}

	public void generateDayLight(int cx, int cz) {
		for(int x = cx * Chunk.SIZE; x < cx * Chunk.SIZE + Chunk.SIZE; x++) {
			for(int z = cz * Chunk.SIZE; z < cz * Chunk.SIZE + Chunk.SIZE; z++) {
				for(int y = getHeight() - 1; y >= 0; y--) {
					if(isOpaqueCube(x, y, z)) break;
					addLight(x, y, z, 1);
				}
			}
		}
		refreshLight(cx, cz);
	}
	
	public void refreshLight() {
		for(int x = 0; x < chunks.length; x++) {
			for(int y = 0; y < chunks[0].length; y++) {
				for(int z = 0; z < chunks[0][0].length; z++) {
					getChunk(x, y, z).refreshLight();
				}
			}
		}
	}
	
	public void refreshLight(int cx, int cz) {
		for(int y = 0; y < chunks[0].length; y++) {
			getChunk(cx, y, cz).refreshLight();
		}
	}

	public void generateChunkMesh() {
		for(int x = 0; x < chunks.length; x++) {
			for(int y = 0; y < chunks[0].length; y++) {
				for(int z = 0; z < chunks[0][0].length; z++) {
					getChunk(x, y, z).mesh();
				}
			}
		}
	}
	
	public int getWidth() {
		return chunks.length * Chunk.SIZE;
	}
	
	public int getHeight() {
		return chunks[0].length * Chunk.SIZE;
	}
	
	public int getLength() {
		return chunks[0][0].length * Chunk.SIZE;
	}
	
	public Vec3 pickBlock() {
		float cx = Game.i.camera.x;
		float cy = Game.i.camera.y;
		float cz = Game.i.camera.z;
		float d = 0.1f;
		
		for(int i = 0; i < 1000; i++) {
			cx += Math.sin(Math.toRadians(Game.i.camera.ry)) * d * Math.cos(Math.toRadians(Game.i.camera.rx));
			cz -= Math.cos(Math.toRadians(Game.i.camera.ry)) * d * Math.cos(Math.toRadians(Game.i.camera.rx));
			cy -= Math.sin(Math.toRadians(Game.i.camera.rx)) * d;
			if(getBlock((int) cx, (int) cy, (int) cz) != 0) break;
		}
		return new Vec3(cx, cy, cz);
	}

	public void recreateChunkMesh(int x, int y, int z) {
		Chunk c = getChunk(x, y, z);
		if(c != null) {
			c.recreateMesh = true;
			generateDayLight(x, z);
		}
	}
}
