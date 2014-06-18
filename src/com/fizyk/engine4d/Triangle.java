package com.fizyk.engine4d;

import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import com.fizyk.math4d.Hyperplane;
import com.fizyk.math4d.Vector4;

public class Triangle extends Primitive {
	
	public Triangle(Vector4 pos1, Color c1, Vector4 pos2, Color c2, Vector4 pos3, Color c3)
	{
		v = new Vertex[3];
		v[0] = new Vertex(pos1, c1);
		v[1] = new Vertex(pos2, c2);
		v[2] = new Vertex(pos3, c3);
	}
	
	public Triangle(Vector4 pos1, Vector4 pos2, Vector4 pos3)
	{
		v = new Vertex[3];
		v[0] = new Vertex(pos1);
		v[1] = new Vertex(pos2);
		v[2] = new Vertex(pos3);
	}
	
	public Triangle(Vertex v1, Vertex v2, Vertex v3)
	{
		v = new Vertex[3];
		v[0] = v1;
		v[1] = v2;
		v[2] = v3;
	}
	
	@Override
	public void draw(GL10 gl)
	{
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, getVData());
		gl.glColorPointer(3, GL10.GL_FLOAT, 0, getCData());
		
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);
		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);

	}

	@Override
	public Vertex vertex(int i) {
		if(i < 0 || i > 2)
			return null;
		return v[i];
	}

	@Override
	public int nVertices() {
		return 3;
	}

	@Override
	public Primitive intersect(Hyperplane h)
	{
		// first we find all non-null intersections of the sides
		Vector<Primitive> p = new Vector<Primitive>();
		
		Primitive tmp = new Line(v[0], v[1]).intersect(h);
		if(tmp != null) p.add(tmp);
		
		tmp = new Line(v[0], v[2]).intersect(h);
		if(tmp != null) p.add(tmp);
		
		tmp = new Line(v[1], v[2]).intersect(h);
		if(tmp != null) p.add(tmp);
		
		if(p.size() == 0)	// if there are none, there is no intersection
			return null;
		
		if(p.size() == 1)	//if there is one, this is our intersection (should never happen)
			return p.firstElement();
		
		if(p.size() == 2)	//if there are two, we should have two points
		{
			if(p.get(0).getClass() != Point.class || p.get(1).getClass() != Point.class)
				return null;
			
			Point pt1 = (Point) p.get(0);
			Point pt2 = (Point) p.get(1);
			
			// if the points are actually one point, return a point
			if(pt1.vertex(0).pos == pt2.vertex(0).pos)
				return pt1;
			
			// else, they denote a line - return this line
			return new Line(pt1.vertex(0), pt2.vertex(0));
		}
		
		// by now we are sure that all 3 lines yielded an intersection
		if(p.get(0).getClass() == Line.class &&
		   p.get(1).getClass() == Line.class &&
		   p.get(2).getClass() == Line.class)
			return this;
		
		// if all 3 weren't lines, then we have a line and 2 points -> return line
		for(int i = 0; i < 3; i++)
			if(p.get(i).getClass() == Line.class) return p.get(i);
		
		// the execution should never get here, but just in case we return null
		return null;
	}

}
