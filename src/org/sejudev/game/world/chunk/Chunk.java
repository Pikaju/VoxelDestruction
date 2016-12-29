package org.sejudev.game.world.chunk;

import org.lwjgl.opengl.GL11;
import org.sejudev.game.graphics.VBO;
import org.sejudev.game.util.FrustumCuller;
import org.sejudev.game.world.Noise;
import org.sejudev.game.world.World;
import org.sejudev.game.world.block.Block;

public class Chunk {
	
	public static final int SIZE = 16;
	
	public World world;
	private int cx;
	private int cy;
	private int cz;
	
	private int[] blocks = new int[SIZE * SIZE * SIZE];
	
	private VBO vbo;
	public boolean recreateMesh = true;
	public boolean recreateLight = false;
	
	private LightHandle light;
	
	public Chunk(World world, int cx, int cy, int cz) {
		this.world = world;
		this.cx = cx;
		this.cy = cy;
		this.cz = cz;
		light = new LightHandle(this);
		vbo = new VBO(GL11.GL_TRIANGLES);
		vbo.create();
	}
	
	public void update() {
		if(recreateLight) {
			recreateLight = false;
			world.generateDayLight(cx, cz);
		}
		if(recreateMesh) {
			recreateMesh = false;
			mesh();
		}
	}
	
	public void render() {
		if(FrustumCuller.boxInFrustum(cx * SIZE, cy * SIZE, cz * SIZE, SIZE, SIZE, SIZE))
			vbo.render();
	}
	
	public void generate() {
		for(int x = 0; x < SIZE; x++) {
			for(int y = 0; y < SIZE; y++) {
				for(int z = 0; z < SIZE; z++) {
					float scale = 10.0f;
					setGenBlock(x, y, z, Noise.noise(getWorldX(x) / scale, getWorldY(y) / scale, getWorldZ(z) / scale) > (getWorldY(y) / (float) (world.getHeight() / 2.0f)) - 1 ? 0x007000 : 0);
				}
			}
		}
	}
	
	public void generate2() {
		for(int x = 0; x < SIZE; x++) {
			for(int y = 0; y < SIZE; y++) {
				for(int z = 0; z < SIZE; z++) {
					int xx = getWorldX(x);
					int yy = getWorldY(y);
					int zz = getWorldZ(z);
					if(world.isOpaqueCube(xx, yy, zz) && world.isOpaqueCube(xx, yy + 1, zz)) {
						setGenBlock(x, y, z, 0x2a2e23);
					}
				}
			}
		}
	}
	
	public void mesh() {
		vbo.clear();
		for(int x = 0; x < SIZE; x++) {
			for(int y = 0; y < SIZE; y++) {
				for(int z = 0; z < SIZE; z++) {
					Block.render(vbo, world, getWorldX(x), getWorldY(y), getWorldZ(z), getBlock(x, y, z));
				}
			}
		}
		vbo.refill();
	}
	
	public int getWorldX(int x) {
		return x + cx * SIZE;
	}
	
	public int getWorldY(int y) {
		return y + cy * SIZE;
	}
	
	public int getWorldZ(int z) {
		return z + cz * SIZE;
	}
	
	public int getBlock(int x, int y, int z) {
		return blocks[coordsToInt(x, y, z)];
	}
	
	public void setBlock(int x, int y, int z, int col) {
		blocks[coordsToInt(x, y, z)] = col;
		world.recreateChunkMesh(cx, cy, cz);
		if(x == 0) world.recreateChunkMesh(cx - 1, cy, cz);
		if(y == 0) world.recreateChunkMesh(cx, cy - 1, cz);
		if(z == 0) world.recreateChunkMesh(cx, cy, cz - 1);
		if(x >= SIZE - 1) world.recreateChunkMesh(cx + 1, cy, cz);
		if(y >= SIZE - 1) world.recreateChunkMesh(cx, cy + 1, cz);
		if(z >= SIZE - 1) world.recreateChunkMesh(cx, cy, cz + 1);
	}
	
	public void setGenBlock(int x, int y, int z, int col) {
		blocks[coordsToInt(x, y, z)] = col;
		recreateMesh = true;
	}
	
	public void setMassBlock(int x, int y, int z, int col) {
		blocks[coordsToInt(x, y, z)] = col;
		recreateMesh = true;
		recreateLight = true;
	}
	
	public int coordsToInt(int x, int y, int z) {
		return x * SIZE * SIZE + y * SIZE + z;
	}

	public float getLight(int bx, int by, int bz) {
		return light.getLight(bx, by, bz);
	}

	public int getUnboundBlock(int x, int y, int z) {
		return world.getBlock(getWorldX(x), getWorldY(y), getWorldZ(z));
	}
	
	public void clearLight() {
		light.clear();
	}
	
	public void addLight(int x, int y, int z, float intensity) {
		light.addLight(x, y, z, intensity);
	}
	
	public void refreshLight() {
		light.refresh();
		recreateMesh = true;
	}
}
