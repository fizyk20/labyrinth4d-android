package com.fizyk.engine4d;

import android.opengl.GLES20;

import com.fizyk.math4d.Hyperplane;
import com.fizyk.math4d.Vector4;

public class Point extends Primitive {
	
	public Point(Vector4 pos, Color c)
	{
		v = new Vertex[1];
		v[0] = new Vertex(pos, c);
	}
	
	public Point(Vector4 pos)
	{
		v = new Vertex[1];
		v[0] = new Vertex(pos);
	}
	
	public Point(Vertex v)
	{
		this.v = new Vertex[1];
		this.v[0] = v;
	}

	@Override
	public void draw(Renderer graph4d) 
	{
		GLES20.glEnableVertexAttribArray(graph4d.getVertexHandle());
		GLES20.glVertexAttribPointer(graph4d.getVertexHandle(), 3, GLES20.GL_FLOAT, false,
	            0, getVData());

		GLES20.glEnableVertexAttribArray(graph4d.getColorHandle());
		GLES20.glVertexAttribPointer(graph4d.getColorHandle(), 4, GLES20.GL_FLOAT, false,
	            0, getCData());
		
		GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1);
		
		GLES20.glDisableVertexAttribArray(graph4d.getVertexHandle());
		GLES20.glDisableVertexAttribArray(graph4d.getColorHandle());
	}

	@Override
	public Vertex vertex(int i) {
		return v[0];
	}

	@Override
	public int nVertices() {
		return 1;
	}

	@Override
	public Primitive intersect(Hyperplane h) {
		if(Math.abs(h.dotPlane(v[0].pos)) < 1e-15)
			return this;
		else
			return null;
	}

}
