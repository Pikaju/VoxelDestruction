package org.sejudev.game.util;

import org.sejudev.game.graphics.VBO;

public class Vec3 {
	
	public float x;
	public float y;
	public float z;
	
	public Vec3() {
		
	}
	
	public Vec3(float x, float y, float z) {
		set(x, y, z);
	}
	
	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vec3 add(float x, float y, float z) {
		set(this.x + x, this.y + y, this.z + z);
		return this;
	}
	
	public Vec3 add(float a) {
		set(this.x + a, this.y + a, this.z + a);
		return this;
	}
	
	public Vec3 sub(float x, float y, float z) {
		set(this.x - x, this.y - y, this.z - z);
		return this;
	}
	
	public Vec3 sub(float a) {
		set(this.x - a, this.y - a, this.z - a);
		return this;
	}
	
	public Vec3 mul(float x, float y, float z) {
		set(this.x * x, this.y * y, this.z * z);
		return this;
	}
	
	public Vec3 mul(float a) {
		set(this.x * a, this.y * a, this.z * a);
		return this;
	}
	
	public Vec3 div(float x, float y, float z) {
		set(this.x / x, this.y / y, this.z / z);
		return this;
	}
	
	public Vec3 div(float a) {
		set(this.x / a, this.y / a, this.z / a);
		return this;
	}
	
	public Vec3 normalize() {
		float t = (float) Math.sqrt(Math.abs(x * x) + Math.abs(y * y) + Math.abs(z * z));
		x /= t;
		y /= t;
		z /= t;
		return this;
	}
	
	public static Vec3 getSurfaceNormal(Vec3 p1, Vec3 p2, Vec3 p3) {
		Vec3 u = new Vec3(p2.x - p1.x, p2.y - p1.y, p2.z - p1.z);
		Vec3 v = new Vec3(p3.x - p1.x, p3.y - p1.y, p3.z - p1.z);
		
		Vec3 normal = new Vec3(0, 0, 0);
        normal.x = (u.y * v.z) - (u.z * v.y);
        normal.y = (u.z * v.x) - (u.x * v.z);
        normal.z = (u.x * v.y) - (u.y * v.x);
        normal.normalize();
        return normal;
	}

	public void toVboVertex(VBO vbo) {
		vbo.vertex(x, y, z);
	}
	
	public void toVboColor(VBO vbo) {
		vbo.color(x, y, z);
	}
	
	public void toVboNormal(VBO vbo) {
		vbo.normal(x, y, z);
	}
}
