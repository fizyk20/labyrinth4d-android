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
}
