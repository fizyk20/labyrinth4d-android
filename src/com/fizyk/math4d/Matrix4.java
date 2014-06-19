package com.fizyk.math4d;

public class Matrix4 {

	public double[][] elem;
	
	public Matrix4()
	{
		elem = new double[5][5];
		for(int i=0; i<5; i++)
			for(int j=0; j<5; j++)
				elem[i][j] = 0.;
	}
	
	public Matrix4(Matrix4 arg)
	{
		elem = new double[5][5];
		for(int i=0; i<5; i++)
			for(int j=0; j<5; j++)
				elem[i][j] = arg.elem[i][j];
	}
	
	public void loadIdentity()
	{
		for(int i=0; i<5; i++)
			for(int j=0; j<5; j++)
				elem[i][j] = (i == j) ? 1. : 0.;
	}
	
	public Matrix4 mul(Matrix4 arg)
	{
		Matrix4 result = new Matrix4();
		for(int i=0; i<5; i++)
			for(int j=0; j<5; j++)
				for(int k=0; k<5; k++)
					result.elem[i][j] += elem[i][k] * arg.elem[k][j];
		return result;
	}
	
	public Vector4 mul(Vector4 arg)
	{
		Vector4 result = new Vector4();
		result.coord[4] = 0.; // default value is 1.
		for(int i=0; i<5; i++)
			for(int j=0; j<5; j++)
				result.coord[i] += elem[i][j] * arg.coord[j];
		return result;
	}
	
	// --------------- Static methods --------------------
	
	public static Matrix4 translation(Vector4 v)
	{
		Matrix4 result = new Matrix4();
		result.loadIdentity();
		
		for(int i=0; i<4; i++)
			result.elem[i][4] = v.getCoord(i);
		
		return result;
	}
	
	public static Matrix4 rotation(Vector4 v1, Vector4 v2, double phi)
	{
		Matrix4 result = new Matrix4();
		result.elem[4][4] = 1.;
		
		v1.normalize();
		v2 = v2.sub(v1.mul(v1.dot(v2)));
		v2.normalize();
		
		result.elem[0][0] = (v1.x()*v1.x() + v2.x()*v2.x())*(1.-Math.cos(phi)) + Math.cos(phi);
		result.elem[1][0] = (v1.x()*v1.y() + v2.x()*v2.y())*(1.-Math.cos(phi)) + (v1.z()*v2.w() - v1.w()*v2.z())*Math.sin(phi);
		result.elem[2][0] = (v1.x()*v1.z() + v2.x()*v2.z())*(1.-Math.cos(phi)) + (v1.w()*v2.y() - v1.y()*v2.w())*Math.sin(phi);
		result.elem[3][0] = (v1.x()*v1.w() + v2.x()*v2.w())*(1.-Math.cos(phi)) + (v1.y()*v2.z() - v1.z()*v2.w())*Math.sin(phi);
		result.elem[0][1] = (v1.y()*v1.x() + v2.y()*v2.x())*(1.-Math.cos(phi)) + (v1.w()*v2.z() - v1.z()*v2.w())*Math.sin(phi);
		result.elem[1][1] = (v1.y()*v1.y() + v2.y()*v2.y())*(1.-Math.cos(phi)) + Math.cos(phi);
		result.elem[2][1] = (v1.y()*v1.z() + v2.y()*v2.z())*(1.-Math.cos(phi)) + (v1.x()*v2.w() - v1.w()*v2.x())*Math.sin(phi);
		result.elem[3][1] = (v1.y()*v1.w() + v2.y()*v2.w())*(1.-Math.cos(phi)) + (v1.z()*v2.x() - v1.x()*v2.z())*Math.sin(phi);
		result.elem[0][2] = (v1.z()*v1.x() + v2.z()*v2.x())*(1.-Math.cos(phi)) + (v1.y()*v2.w() - v1.w()*v2.y())*Math.sin(phi);
		result.elem[1][2] = (v1.z()*v1.y() + v2.z()*v2.y())*(1.-Math.cos(phi)) + (v1.w()*v2.x() - v1.x()*v2.w())*Math.sin(phi);
		result.elem[2][2] = (v1.z()*v1.z() + v2.z()*v2.z())*(1.-Math.cos(phi)) + Math.cos(phi);
		result.elem[3][2] = (v1.z()*v1.w() + v2.z()*v2.w())*(1.-Math.cos(phi)) + (v1.x()*v2.y() - v1.y()*v2.x())*Math.sin(phi);
		result.elem[0][3] = (v1.w()*v1.x() + v2.w()*v2.x())*(1.-Math.cos(phi)) + (v1.z()*v2.y() - v1.y()*v2.z())*Math.sin(phi);
		result.elem[1][3] = (v1.w()*v1.y() + v2.w()*v2.y())*(1.-Math.cos(phi)) + (v1.x()*v2.z() - v1.z()*v2.x())*Math.sin(phi);
		result.elem[2][3] = (v1.w()*v1.z() + v2.w()*v2.z())*(1.-Math.cos(phi)) + (v1.y()*v2.x() - v1.x()*v2.y())*Math.sin(phi);
		result.elem[3][3] = (v1.w()*v1.w() + v2.w()*v2.w())*(1.-Math.cos(phi)) + Math.cos(phi);
		
		return result;
	}
	
	public static Matrix4 rotationXY(double phi)
	{
		Matrix4 result = new Matrix4();
		result.loadIdentity();
		
		result.elem[0][0] = Math.cos(phi);
		result.elem[0][1] = -Math.sin(phi);
		result.elem[1][0] = Math.sin(phi);
		result.elem[1][1] = Math.cos(phi);
		
		return result;
	}
	
	public static Matrix4 rotationXZ(double phi)
	{
		Matrix4 result = new Matrix4();
		result.loadIdentity();
		
		result.elem[0][0] = Math.cos(phi);
		result.elem[0][2] = -Math.sin(phi);
		result.elem[2][0] = Math.sin(phi);
		result.elem[2][2] = Math.cos(phi);
		
		return result;
	}
	
	public static Matrix4 rotationXW(double phi)
	{
		Matrix4 result = new Matrix4();
		result.loadIdentity();
		
		result.elem[0][0] = Math.cos(phi);
		result.elem[0][3] = -Math.sin(phi);
		result.elem[3][0] = Math.sin(phi);
		result.elem[3][3] = Math.cos(phi);
		
		return result;
	}
	
	public static Matrix4 rotationYZ(double phi)
	{
		Matrix4 result = new Matrix4();
		result.loadIdentity();
		
		result.elem[1][1] = Math.cos(phi);
		result.elem[1][2] = -Math.sin(phi);
		result.elem[2][1] = Math.sin(phi);
		result.elem[2][2] = Math.cos(phi);
		
		return result;
	}
	
	public static Matrix4 rotationYW(double phi)
	{
		Matrix4 result = new Matrix4();
		result.loadIdentity();
		
		result.elem[1][1] = Math.cos(phi);
		result.elem[1][3] = -Math.sin(phi);
		result.elem[3][1] = Math.sin(phi);
		result.elem[3][3] = Math.cos(phi);
		
		return result;
	}
	
	public static Matrix4 rotationZW(double phi)
	{
		Matrix4 result = new Matrix4();
		result.loadIdentity();
		
		result.elem[2][2] = Math.cos(phi);
		result.elem[2][3] = -Math.sin(phi);
		result.elem[3][2] = Math.sin(phi);
		result.elem[3][3] = Math.cos(phi);
		
		return result;
	}
	
	public static Matrix4 scale(double sx, double sy, double sz, double sw)
	{
		Matrix4 result = new Matrix4();
		
		result.elem[0][0] = sx;
		result.elem[1][1] = sy;
		result.elem[2][2] = sz;
		result.elem[3][3] = sw;
		result.elem[4][4] = 1.;
		
		return result;
	}
	
	public static Matrix4 scale(double s)
	{
		return scale(s, s, s, s);
	}
}
