package org.sejudev.game.util;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class Camera {
	
	public float x = 32;
	public float y = 32;
	public float z = 32;
	
	public float rx;
	public float ry;
	public float rz;
	
	public Camera() {
		
	}
	
	public void debugUpdate() {
		if(Mouse.isButtonDown(0) && !Mouse.isGrabbed()) {
			Mouse.setGrabbed(true);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && Mouse.isGrabbed()) {
			Mouse.setGrabbed(false);
		}
		if(Mouse.isGrabbed()) {
			rx -= Mouse.getDY() / 8.0;
			ry += Mouse.getDX() / 8.0;
			if(rx < -90) rx = -90;
			if(rx > 90) rx = 90;
			if(ry < 0) ry += 360;
			if(ry > 359) ry -= 360;
		}
		float speed = (float) (Delta.getDelta() * 0.1f);
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) moveDir(ry + 0, speed);
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) moveDir(ry - 90, speed);
		if(Keyboard.isKeyDown(Keyboard.KEY_S)) moveDir(ry + 180, speed);
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) moveDir(ry + 90, speed);
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) y += speed;
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) y -= speed;
	}
	
	private void moveDir(float rot, float speed) {
		x += Math.sin(Math.toRadians(rot)) * speed;
		z -= Math.cos(Math.toRadians(rot)) * speed;
	}

	public void setUpDisplay(float fov, float znear, float zfar) {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(fov, (float) Display.getWidth() / (float) Display.getHeight(), znear, zfar);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
	}

	public void setUpMatrix() {
		GL11.glRotatef(rx, 1, 0, 0);
		GL11.glRotatef(ry, 0, 1, 0);
		GL11.glRotatef(rz, 0, 0, 1);
		GL11.glTranslatef(-x, -y, -z);
	}

	public float distTo(float x, float y, float z) {
		float xx = this.x + x;
		float yy = this.y - z;
		float zz = this.z + z;
		return (float) Math.sqrt(Math.abs(xx * xx) + Math.abs(yy * yy) + Math.abs(zz * zz));
	}

	public void applyRotation() {
		GL11.glRotatef(rx, 1, 0, 0);
		GL11.glRotatef(ry, 0, 1, 0);
		GL11.glRotatef(rz, 0, 0, 1);
	}
}
