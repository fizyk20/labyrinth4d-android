package com.fizyk.engine4d;

import com.fizyk.math4d.*;

public class Camera {

	private Vector4 lookat;
	private Vector4 up;
	private Vector4 right;
	private Vector4 normal;
	private Vector4 location;
	
	public enum ConfigVector { LOOKAT, UP, RIGHT, NORMAL, LOCATION };
	
	public Camera()
	{
		loadIdentity();
	}
	
	public void loadIdentity()
	{
		lookat = new Vector4(0., 0., -1., 0.);
		up = new Vector4(0., 1., 0., 0.);
		right = new Vector4(1., 0., 0., 0.);
		normal = new Vector4(0., 0., 0., 1.);
		location = new Vector4();
	}
	
	public void applyMatrix(Matrix4 m)
	{
		lookat = m.mul(lookat);
		up = m.mul(up);
		right = m.mul(right);
		normal = m.mul(normal);
		
		lookat.normalize();
		up.normalize();
		right.normalize();
		normal.normalize();
	}
	
	public void translate(Vector4 v)
	{
		location = location.add(v);
	}
	
	public void rotateXY(double phi)
	{
		applyMatrix(Matrix4.rotationXY(phi));
	}
	
	public void rotateXZ(double phi)
	{
		applyMatrix(Matrix4.rotationXZ(phi));
	}
	
	public void rotateXW(double phi)
	{
		applyMatrix(Matrix4.rotationXW(phi));
	}
	
	public void rotateYZ(double phi)
	{
		applyMatrix(Matrix4.rotationYZ(phi));
	}
	
	public void rotateYW(double phi)
	{
		applyMatrix(Matrix4.rotationYW(phi));
	}
	
	public void rotateZW(double phi)
	{
		applyMatrix(Matrix4.rotationZW(phi));
	}
	
	public void rotate(Vector4 v1, Vector4 v2, double phi)
	{
		applyMatrix(Matrix4.rotation(v1, v2, phi));
	}
	
	public Vector4 getVector(ConfigVector which)
	{
		switch(which)
		{
		case LOOKAT:
			return lookat;
		case UP:
			return up;
		case RIGHT:
			return right;
		case NORMAL:
			return normal;
		case LOCATION:
			return location;
		default:
			return new Vector4();	// to suppress an error
		}
	}
	
	public Hyperplane getHyperplane()
	{
		return new Hyperplane(normal, -normal.dot(location));
	}
	
	public Vector4 calculateLocal(Vector4 pos)
	{
		Vector4 tmp;
		tmp = pos.sub(location);
		double x, y, z, w;
		x = tmp.dot(right)/right.len();
		y = tmp.dot(up)/up.len();
		z = tmp.dot(lookat)/lookat.len();
		w = tmp.dot(normal)/normal.len();
		return new Vector4(x, y, z, w);
	}
}
