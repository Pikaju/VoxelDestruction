package org.sejudev.game.util;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;

public class FrustumCuller {
	
	private static Plane[] planes = new Plane[6];
	
	public static void recreateFrustum() {
		
		//Getting the modelviewProjectionMatrix by multiplying projectionMatrix and modelviewMatrix
		FloatBuffer pojectionMatrix = BufferUtils.createFloatBuffer(16);
		FloatBuffer modelviewMatrix = BufferUtils.createFloatBuffer(16);
		FloatBuffer mvp = BufferUtils.createFloatBuffer(16);
		glGetFloat(GL_PROJECTION_MATRIX, pojectionMatrix);
		glGetFloat(GL_MODELVIEW_MATRIX, modelviewMatrix);
		
		glPushMatrix();
			glLoadMatrix(pojectionMatrix);
			glMultMatrix(modelviewMatrix);
			glGetFloat(GL_MODELVIEW_MATRIX, mvp);
		glPopMatrix();
		
		//Extracting the view-frustum from the modelviewProjectionMatrix
		planes[0] = new Plane(mvp.get(3) + mvp.get(0), mvp.get(7) + mvp.get(4), mvp.get(11) + mvp.get(8), mvp.get(15) + mvp.get(12)).normalize(); //Left
		planes[1] = new Plane(mvp.get(3) - mvp.get(0), mvp.get(7) - mvp.get(4), mvp.get(11) - mvp.get(8), mvp.get(15) - mvp.get(12)).normalize(); //Right
		planes[2] = new Plane(mvp.get(3) + mvp.get(1), mvp.get(7) + mvp.get(5), mvp.get(11) + mvp.get(9), mvp.get(15) + mvp.get(13)).normalize(); //Bottom
		planes[3] = new Plane(mvp.get(3) - mvp.get(1), mvp.get(7) - mvp.get(5), mvp.get(11) - mvp.get(9), mvp.get(15) - mvp.get(13)).normalize(); //Top
		planes[4] = new Plane(mvp.get(3) + mvp.get(2), mvp.get(7) + mvp.get(6), mvp.get(11) + mvp.get(10), mvp.get(15) + mvp.get(14)).normalize(); //Near
		planes[5] = new Plane(mvp.get(3) - mvp.get(2), mvp.get(7) - mvp.get(6), mvp.get(11) - mvp.get(10), mvp.get(15) - mvp.get(14)).normalize(); //Far
	}
	
	public static boolean sphereInFrustum(float x, float y, float z, float radius) {
		for(Plane p : planes) {
			if(p.getA() * x + p.getB() * y + p.getC() * z + p.getD() <= -radius) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean boxInFrustum(float x, float y, float z, float width, float height, float length) {
		float centerDistX = width / 2.0f;
		float centerDistY = height / 2.0f;
		float centerDistZ = length / 2.0f;
		
		float dist = (float) Math.sqrt(centerDistX * centerDistX + centerDistY * centerDistY + centerDistZ * centerDistZ);
		return sphereInFrustum(x + centerDistX, y + centerDistY, z + centerDistZ, dist);
	}
}
