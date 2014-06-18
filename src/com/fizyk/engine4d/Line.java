package com.fizyk.engine4d;

import javax.microedition.khronos.opengles.GL10;

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
	public void draw(GL10 gl)
	{
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, getVData());
		gl.glColorPointer(2, GL10.GL_FLOAT, 0, getCData());
		
		gl.glDrawArrays(GL10.GL_LINES, 0, 2);
		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
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
