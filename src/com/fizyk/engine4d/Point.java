package com.fizyk.engine4d;

import javax.microedition.khronos.opengles.GL10;

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
	public void draw(GL10 gl) 
	{
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, getVData());
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, getCData());
		
		gl.glDrawArrays(GL10.GL_POINTS, 0, 1);
		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
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
