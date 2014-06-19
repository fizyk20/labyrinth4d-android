package com.fizyk.engine4d;

import android.opengl.GLES20;

import com.fizyk.math4d.Hyperplane;
import com.fizyk.math4d.Vector4;

public class Line extends Primitive {
	
	public Line(Vector4 pos1, Color c1, Vector4 pos2, Color c2)
	{
		v = new Vertex[2];
		v[0] = new Vertex(pos1, c1);
		v[1] = new Vertex(pos2, c2);
	}
	
	public Line(Vector4 pos1, Vector4 pos2)
	{
		v = new Vertex[2];
		v[0] = new Vertex(pos1);
		v[1] = new Vertex(pos2);
	}
	
	public Line(Vertex v1, Vertex v2)
	{
		v = new Vertex[2];
		v[0] = v1;
		v[1] = v2;
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
		
		GLES20.glDrawArrays(GLES20.GL_LINES, 0, 2);
		
		GLES20.glDisableVertexAttribArray(graph4d.getVertexHandle());
		GLES20.glDisableVertexAttribArray(graph4d.getColorHandle());
	}

	@Override
	public Vertex vertex(int i) {
		if(i < 0 || i > 1)
			return null;
		return v[i];
	}

	@Override
	public int nVertices() {
		return 2;
	}

	@Override
	public Primitive intersect(Hyperplane h) 
	{
		double dot1 = h.dotPlane(v[0].pos);
		double dot2 = h.dotPlane(v[1].pos);
		
		if(dot1 * dot2 > 0)
			return null;
		
		if(dot1 == 0 && dot2 == 0)
			return this;
		
		if(dot1 == 0)
			return new Point(v[0]);
		if(dot2 == 0)
			return new Point(v[1]);
		
		Vector4 v1 = v[0].pos.mul(dot2/(dot2-dot1));
		Vector4 v2 = v[1].pos.mul(-dot1/(dot2-dot1));
		Vector4 c1 = v[0].c.toVector().mul(dot2/(dot2-dot1));
		Vector4 c2 = v[1].c.toVector().mul(-dot1/(dot2-dot1));
		return new Point(v1.add(v2), new Color(c1.add(c2)));
	}

}
