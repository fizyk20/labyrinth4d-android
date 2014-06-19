package com.fizyk.engine4d;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.fizyk.math4d.Hyperplane;

public class Primitive
{
	protected Vertex[] v;
	
	public FloatBuffer getVData()
	{
		ByteBuffer bb = ByteBuffer.allocateDirect(nVertices()*4*3);
		bb.order(ByteOrder.nativeOrder());
		FloatBuffer vData = bb.asFloatBuffer();
		for(int i = 0; i < nVertices(); i++)
			for(int j = 0; j < 3; j++)
				vData.put((float)v[i].pos.getCoord(j));
		vData.position(0);
		return vData;
	}
	
	public FloatBuffer getCData()
	{
		ByteBuffer bb = ByteBuffer.allocateDirect(nVertices()*4*4);
		bb.order(ByteOrder.nativeOrder());
		FloatBuffer cData = bb.asFloatBuffer();
		for(int i = 0; i < nVertices(); i++)
			for(int j = 0; j < 4; j++)
				cData.put((float)v[i].c.toVector().getCoord(j));
		cData.position(0);
		return cData;
	}
	
	public void draw(Renderer graph4d)
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
