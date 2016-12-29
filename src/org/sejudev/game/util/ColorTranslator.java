package org.sejudev.game.util;

public class ColorTranslator {
	
	public static Vec3 getColor(int col) {
		return new Vec3(((col >> 16) & 0xff) / 255.0f, ((col >> 8) & 0xff) / 255.0f, ((col >> 0) & 0xff) / 255.0f);
	}
}
