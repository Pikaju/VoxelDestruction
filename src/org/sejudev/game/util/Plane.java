package org.sejudev.game.util;

public class Plane {
	
	private float a;
	private float b;
	private float c;
	private float d;
	
	public Plane(float a, float b, float c, float d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}
	
	public Plane normalize() {
		float t = (float) Math.sqrt(Math.abs(a * a) + Math.abs(b * b) + Math.abs(c * c));
		a /= t;
		b /= t;
		c /= t;
		d /= t;
		return this;
	}

	public float getA() {
		return a;
	}

	public void setA(float a) {
		this.a = a;
	}

	public float getB() {
		return b;
	}

	public void setB(float b) {
		this.b = b;
	}

	public float getC() {
		return c;
	}

	public void setC(float c) {
		this.c = c;
	}

	public float getD() {
		return d;
	}

	public void setD(float d) {
		this.d = d;
	}
}
