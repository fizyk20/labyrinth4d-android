package com.fizyk.engine4d;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.Vector;

import android.opengl.GLES20;

import com.fizyk.math4d.Hyperplane;
import com.fizyk.math4d.Vector4;

public class Quad extends Primitive {

	private Vertex[] v;
	private short drawOrder[] = { 0, 1, 2, 0, 2, 3 };
	
	public Quad(Vector4 pos1, Color c1, Vector4 pos2, Color c2, Vector4 pos3, Color c3, Vector4 pos4, Color c4)
	{
		v = new Vertex[4];
		v[0] = new Vertex(pos1, c1);
		v[1] = new Vertex(pos2, c2);
		v[2] = new Vertex(pos3, c3);
		v[3] = new Vertex(pos4, c4);
	}
	
	public Quad(Vector4 pos1, Vector4 pos2, Vector4 pos3, Vector4 pos4)
	{
		v = new Vertex[4];
		v[0] = new Vertex(pos1);
		v[1] = new Vertex(pos2);
		v[2] = new Vertex(pos3);
		v[3] = new Vertex(pos4);
	}
	
	public Quad(Vertex v1, Vertex v2, Vertex v3, Vertex v4)
	{
		v = new Vertex[4];
		v[0] = v1;
		v[1] = v2;
		v[2] = v3;
		v[3] = v4;
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
		
		ShortBuffer drawListBuffer;
		// initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);
		
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
		
		GLES20.glDisableVertexAttribArray(graph4d.getVertexHandle());
		GLES20.glDisableVertexAttribArray(graph4d.getColorHandle());
	}

	@Override
	public Vertex vertex(int i) {
		if(i < 0 || i > 3)
			return null;
		return v[i];
	}

	@Override
	public int nVertices() {
		return 4;
	}

	@Override
	public Primitive intersect(Hyperplane h)
	{
		// first we find all non-null intersections of the sides
		Vector<Primitive> p = new Vector<Primitive>();
		
		Primitive tmp = new Line(v[0], v[1]).intersect(h);
		if(tmp != null) p.add(tmp);
		
		tmp = new Line(v[1], v[2]).intersect(h);
		if(tmp != null) p.add(tmp);
		
		tmp = new Line(v[2], v[3]).intersect(h);
		if(tmp != null) p.add(tmp);
		
		tmp = new Line(v[3], v[0]).intersect(h);
		if(tmp != null) p.add(tmp);
		
		// the possible results are:
		// * nothing (0 non-null results)
		// * point (2 Point results, equal)
		// * line (2 non-equal Points or Line + 2 Points)
		// * quad (4 Lines)
		
		if(p.size() == 0)
			return null;
		
		if(p.size() == 4)
			return this;
		
		if(p.size() == 2)
		{
			// should be 2 points
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
		
		// we have 3 intersections - but better check
		if(p.size() != 3)
			return null;
		
		// 3 intersections = 1 Line + 2 Points - the intersection is actually the line
		for(int i = 0; i < 3; i++)
			if(p.get(i).getClass() == Line.class)
				return p.get(i);
		
		// we should never get here, but leave this just in case
		return null;
	}

}
