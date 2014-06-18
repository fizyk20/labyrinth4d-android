package com.fizyk.engine4d;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import com.fizyk.math4d.Hyperplane;

public class Primitive
{
	protected Vertex[] v;
	
	public FloatBuffer getVData()
	{
		FloatBuffer vData = FloatBuffer.allocate(nVertices()*3);
		for(int i = 0; i < nVertices(); i++)
			for(int j = 0; j < 3; j++)
				vData.put((float)v[i].pos.getCoord(j));
		return vData;
	}
	
	public FloatBuffer getCData()
	{
		FloatBuffer vData = FloatBuffer.allocate(nVertices()*4);
		for(int i = 0; i < nVertices(); i++)
			for(int j = 0; j < 4; j++)
				vData.put((float)v[i].c.toVector().getCoord(j));
		return vData;
	}
	
	public void draw(GL10 gl)
	{
	}
	
	public Vertex vertex(int i)
	{
		return null;
	}
	
	public int nVertices()
	{
		return 0;
	}
	
	public Primitive intersect(Hyperplane h)
	{
		return null;
	}
}
