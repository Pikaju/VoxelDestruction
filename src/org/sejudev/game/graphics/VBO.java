package org.sejudev.game.graphics;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.sejudev.game.util.Vec3;

public class VBO {
	
	public List<Float> verticies = new ArrayList<Float>();
	public List<Float> colors = new ArrayList<Float>();
	public List<Float> normals = new ArrayList<Float>();
	
	private int vertexHandle;
	private int colorHandle;
	private int normalHandle;
	
	private float r;
	private float g;
	private float b;
	
	private float nx;
	private float ny;
	private float nz;
	
	private int mode;
	
	public VBO(int mode) {
		this.mode = mode;
	}
	
	public void clear() {
		verticies.clear();
		colors.clear();
		normals.clear();
	}
	
	public void create() {
		if(vertexHandle == 0) {
			vertexHandle = GL15.glGenBuffers();
			colorHandle = GL15.glGenBuffers();
			normalHandle = GL15.glGenBuffers();
		}
	}
	
	public void delete() {
		if(vertexHandle != 0) {
			GL15.glDeleteBuffers(vertexHandle);
			GL15.glDeleteBuffers(colorHandle);
			GL15.glDeleteBuffers(normalHandle);
		}
	}
	
	public void vertex(float x, float y, float z) {
		verticies.add(x);
		verticies.add(y);
		verticies.add(z);
		colors.add(r);
		colors.add(g);
		colors.add(b);
		normals.add(nx);
		normals.add(ny);
		normals.add(nz);
	}
	
	public void color(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public void normal(float nx, float ny, float nz) {
		this.nx = nx;
		this.ny = ny;
		this.nz = nz;
	}
	
	public void refill() {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexHandle);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, asBuffer(verticies), GL15.GL_STREAM_DRAW);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, colorHandle);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, asBuffer(colors), GL15.GL_STREAM_DRAW);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, normalHandle);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, asBuffer(normals), GL15.GL_STREAM_DRAW);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	public void render() {
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
		GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexHandle);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, colorHandle);
		GL11.glColorPointer(3, GL11.GL_FLOAT, 0, 0);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, normalHandle);
		GL11.glNormalPointer(GL11.GL_FLOAT, 0, 0);
		
		GL11.glDrawArrays(mode, 0, verticies.size() / 3);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
		GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
	}
	
	public static FloatBuffer asBuffer(List<Float> list) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(list.size());
		for(float f : list) {
			buffer.put(f);
		}
		buffer.flip();
		return buffer;
	}
	
	public void addQuad(Vec3 p1, Vec3 p2, Vec3 p3, Vec3 p4, Vec3[] colors) {
		Vec3.getSurfaceNormal(p1, p2, p3).toVboNormal(this);
		colors[0].toVboColor(this);
		p1.toVboVertex(this);
		if(colors.length > 0) colors[1].toVboColor(this);
		p2.toVboVertex(this);
		if(colors.length > 1) colors[2].toVboColor(this);
		p3.toVboVertex(this);
		
		colors[0].toVboColor(this);
		p1.toVboVertex(this);
		if(colors.length > 1) colors[2].toVboColor(this);
		p3.toVboVertex(this);
		if(colors.length > 2) colors[3].toVboColor(this);
		p4.toVboVertex(this);
	}
	
	public void addTriangle(Vec3 p1, Vec3 p2, Vec3 p3, Vec3[] colors) {
		Vec3.getSurfaceNormal(p1, p2, p3).toVboNormal(this);
		colors[0].toVboColor(this);
		p1.toVboVertex(this);
		if(colors.length > 0) colors[1].toVboColor(this);
		p2.toVboVertex(this);
		if(colors.length > 1) colors[2].toVboColor(this);
		p3.toVboVertex(this);
	}
}
