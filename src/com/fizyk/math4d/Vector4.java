package com.fizyk.math4d;

public class Vector4 {
	
	public double[] coord;	// coordinates: X, Y, Z, W + projective
	
	public Vector4()
	{
		coord = new double[5];
		coord[0] = coord[1] = coord[2] = coord[3] = 0.;
		coord[4] = 1.;
	}
	
	public Vector4(double x, double y, double z, double w)
	{
		coord = new double[5];
		coord[0] = x;
		coord[1] = y;
		coord[2] = z;
		coord[3] = w;
		coord[4] = 1.;
	}
	
	public Vector4(Vector4 arg)
	{
		coord = new double[5];
		for(int i=0; i<5; i++)
			coord[i] = arg.coord[i];
	}
	
	public Vector4 add(Vector4 arg)
	{
		Vector4 res = new Vector4();
		for(int i=0; i<4; i++)
			res.coord[i] = coord[i]/coord[4] + arg.coord[i]/arg.coord[4];
		res.coord[4] = 1.;
		return res;
	}
	
	public Vector4 sub(Vector4 arg)
	{
		Vector4 res = new Vector4();
		for(int i=0; i<4; i++)
			res.coord[i] = coord[i]/coord[4] - arg.coord[i]/arg.coord[4];
		res.coord[4] = 1.;
		return res;
	}
	
	public double dot(Vector4 arg)
	{
		double res = 0.;
		for(int i=0; i<4; i++)
			res += coord[i]/coord[4] * arg.coord[i]/arg.coord[4];
		return res;
	}
	
	public Vector4 mul(double arg)
	{
		Vector4 res = new Vector4(this);
		for(int i=0; i<4; i++)
			res.coord[i] *= arg;
		return res;
	}

	public Vector4 div(double arg)
	{
		Vector4 res = new Vector4(this);
		for(int i=0; i<4; i++)
			res.coord[i] /= arg;
		return res;
	}
	
	public boolean equals(Vector4 arg)
	{
		for(int i=0; i<4; i++)
			if(coord[i]/coord[4] != arg.coord[i]/arg.coord[4])
				return false;
		return true;
	}
	
	public void projNormalize()
	{
		for(int i=0; i<5; i++)
			coord[i] /= coord[4];
	}
	
	public double len()
	{
		double result = 0.;
		for(int i=0; i<4; i++)
		{
			double tmp = coord[i]/coord[4];
			result += tmp*tmp;
		}
		return Math.sqrt(result);
	}
	
	public void normalize()
	{
		double length = len();
		if(length < 1e-15) return;
		for(int i=0; i<4; i++)
			coord[i] /= length;
	}
	
	public double x()
	{
		return coord[0]/coord[4];
	}
	
	public double y()
	{
		return coord[1]/coord[4];
	}
	
	public double z()
	{
		return coord[2]/coord[4];
	}
	
	public double w()
	{
		return coord[3]/coord[4];
	}
	
	public double getCoord(int i)
	{
		return coord[i]/coord[4];
	}
	
	public static Vector4 crossProduct(Vector4 v1, Vector4 v2, Vector4 v3)
	{
		Vector4 result = new Vector4();
		result.coord[0] = (v1.y()*v2.z()*v3.w())+(v1.z()*v2.w()*v3.y())+(v1.w()*v2.y()*v3.z())-(v1.y()*v2.w()*v3.z())-(v1.z()*v2.y()*v3.w())-(v1.w()*v2.z()*v3.y());
		result.coord[1] = (v1.z()*v2.w()*v3.x())+(v1.w()*v2.x()*v3.z())+(v1.x()*v2.z()*v3.w())-(v1.z()*v2.x()*v3.w())-(v1.w()*v2.z()*v3.x())-(v1.x()*v2.w()*v3.z());
		result.coord[2] = (v1.w()*v2.x()*v3.y())+(v1.x()*v2.y()*v3.w())+(v1.y()*v2.w()*v3.x())-(v1.w()*v2.y()*v3.x())-(v1.x()*v2.w()*v3.y())-(v1.y()*v2.x()*v3.w());
		result.coord[3] = (v1.x()*v2.y()*v3.z())+(v1.y()*v2.z()*v3.x())+(v1.z()*v2.x()*v3.y())-(v1.x()*v2.z()*v3.y())-(v1.y()*v2.x()*v3.z())-(v1.z()*v2.y()*v3.x());
		return result;
	}
}
