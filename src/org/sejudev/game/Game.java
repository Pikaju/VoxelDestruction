package org.sejudev.game;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.sejudev.game.shader.Shader;
import org.sejudev.game.util.Camera;
import org.sejudev.game.util.Delta;
import org.sejudev.game.util.FrustumCuller;
import org.sejudev.game.util.Vec3;
import org.sejudev.game.world.World;

public class Game {

	public static Game i = new Game();

	public static float time;

	public static final float FOV = 90;
	public static final float ZNEAR = 0.1f;
	public static final float ZFAR = 3000.0f;

	private boolean running;

	public Camera camera;
	private World world;

	public static void main(String[] args) {
		i.start();
	}

	public void init() {
		try {
			Display.setDisplayMode(new DisplayMode(1280, 720));
			Display.setTitle("Sejudev Engine");
			Display.setResizable(true);
			Display.setVSyncEnabled(true);
			Display.create();
			Mouse.create();
			Keyboard.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glShadeModel(GL11.GL_SMOOTH);

		Shader.init();

		camera = new Camera();
		camera.setUpDisplay(FOV, ZNEAR, ZFAR);

		world = new World();
	}
	

	public void cleanup() {
		Display.destroy();
		Mouse.destroy();
		Keyboard.destroy();
	}

	public void start() {
		if(running) {
			return;
		}
		running = true;
		init();
		run();
	}

	public void stop() {
		if(!running) {
			return;
		}
		running = false;
		cleanup();
		System.exit(0);
	}

	public void run() {
		while(running) {
			update();
			render();

			Display.update();
			if(Display.isCloseRequested()) stop();
			if(Display.wasResized()) camera.setUpDisplay(FOV, ZNEAR, ZFAR);
		}
	}

	private boolean wireframe = false;

	public void update() {
		Delta.tick();
		Game.time += Delta.getDelta();
		camera.debugUpdate();
		world.update();
		while(Mouse.next()) {
			if(Mouse.getEventButtonState()) {
				if(Mouse.getEventButton() == 0) {
					Vec3 pick = world.pickBlock();
					world.explode((int) pick.x, (int) pick.y, (int) pick.z, 5);
				}
				if(Mouse.getEventButton() == 1) {
					world.generateDayLight();
				}
			}
		}
	}

	public void render() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glLoadIdentity();
		GL11.glPushMatrix();
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, wireframe ? GL11.GL_LINE : GL11.GL_FILL);
		camera.setUpMatrix();
		FrustumCuller.recreateFrustum();
		world.render();
		renderCross();
		GL11.glPopMatrix();
	}
	
	public void renderCross() {
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glPointSize(3);
		GL11.glBegin(GL11.GL_POINTS);
		GL11.glColor3f(0, 0, 0);
		GL11.glVertex3f(0, 0, -0.2f);
		GL11.glEnd();
		GL11.glPointSize(2);
		GL11.glBegin(GL11.GL_POINTS);
		GL11.glColor3f(1, 1, 1);
		GL11.glVertex3f(0, 0, -0.2f);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
}
