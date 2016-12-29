package org.sejudev.game.world.block;

import org.sejudev.game.graphics.VBO;
import org.sejudev.game.util.ColorTranslator;
import org.sejudev.game.util.Vec3;
import org.sejudev.game.world.World;

public class Block {

	public static void render(VBO vbo, World world, int x, int y, int z, int col) {
		if(col == 0x000000) return;
		boolean[] vis = getVis(world, x, y, z);
		Vec3 p1 = new Vec3(x, y, z);
		Vec3 p2 = new Vec3(x, y, z + 1);
		Vec3 p3 = new Vec3(x + 1, y, z + 1);
		Vec3 p4 = new Vec3(x + 1, y, z);
		Vec3 p5 = new Vec3(x, y + 1, z);
		Vec3 p6 = new Vec3(x, y + 1, z + 1);
		Vec3 p7 = new Vec3(x + 1, y + 1, z + 1);
		Vec3 p8 = new Vec3(x + 1, y + 1, z);
		Vec3 c1 = ColorTranslator.getColor(col).mul(world.getLight(x, y, z));
		Vec3 c2 = ColorTranslator.getColor(col).mul(world.getLight(x, y, z + 1));
		Vec3 c3 = ColorTranslator.getColor(col).mul(world.getLight(x + 1, y, z + 1));
		Vec3 c4 = ColorTranslator.getColor(col).mul(world.getLight(x + 1, y, z));
		Vec3 c5 = ColorTranslator.getColor(col).mul(world.getLight(x, y + 1, z));
		Vec3 c6 = ColorTranslator.getColor(col).mul(world.getLight(x, y + 1, z + 1));
		Vec3 c7 = ColorTranslator.getColor(col).mul(world.getLight(x + 1, y + 1, z + 1));
		Vec3 c8 = ColorTranslator.getColor(col).mul(world.getLight(x + 1, y + 1, z));
		
		if(vis[0]) vbo.addQuad(p5, p6, p7, p8, new Vec3[] { c5, c6, c7, c8 });
		if(vis[1]) vbo.addQuad(p2, p1, p4, p3, new Vec3[] { c2, c1, c4, c3 });
		if(vis[2]) vbo.addQuad(p7, p3, p4, p8, new Vec3[] { c7, c3, c4, c8 });
		if(vis[3]) vbo.addQuad(p5, p1, p2, p6, new Vec3[] { c5, c1, c2, c6 });
		if(vis[4]) vbo.addQuad(p6, p2, p3, p7, new Vec3[] { c6, c2, c3, c7 });
		if(vis[5]) vbo.addQuad(p8, p4, p1, p5, new Vec3[] { c8, c4, c1, c5 });
	}

	private static boolean[] getVis(World world, int x, int y, int z) {
		return new boolean[] {
			!world.isOpaqueCube(x, y + 1, z),
			!world.isOpaqueCube(x, y - 1, z),
			!world.isOpaqueCube(x + 1, y, z),
			!world.isOpaqueCube(x - 1, y, z),
			!world.isOpaqueCube(x, y, z + 1),
			!world.isOpaqueCube(x, y, z - 1)
		};
	}
}
