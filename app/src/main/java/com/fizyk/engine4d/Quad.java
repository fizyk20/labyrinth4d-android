package com.fizyk.engine4d;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Vector;

import android.opengl.GLES20;

import com.fizyk.math4d.Hyperplane;
import com.fizyk.math4d.Vector4;

public class Quad extends Primitive {
	
	public Quad(Vector4 pos1, Color c1, Vector4 pos2, Color c2, Vector4 pos3, Color c3, Vector4 pos4, Color c4)
	{
		v = new Vertex[4];
		v[0] = new Vertex(pos1, c1);
		v[1] = new Vertex(pos2, c2);
		v[2] = new Vertex(pos3, c3);
		v[3] = new Vertex(pos4, c4);
		sortVertices();
	}
	
	public Quad(Vector4 pos1, Vector4 pos2, Vector4 pos3, Vector4 pos4)
	{
		v = new Vertex[4];
		v[0] = new Vertex(pos1);
		v[1] = new Vertex(pos2);
		v[2] = new Vertex(pos3);
		v[3] = new Vertex(pos4);
		sortVertices();
	}
	
	public Quad(Vertex v1, Vertex v2, Vertex v3, Vertex v4)
	{
		v = new Vertex[4];
		v[0] = v1;
		v[1] = v2;
		v[2] = v3;
		v[3] = v4;
		sortVertices();
	}
	
	private void sortVertices()
	{
		Vector4 v0 = new Vector4();
		Vector4 v1 = new Vector4();
		Vector4 v2 = new Vector4();
		int vakt;
		
		double[] scal = new double[3];
		v0 = v[1].pos.sub(v[0].pos);
		v1 = v[2].pos.sub(v[0].pos);
		v2 = v[3].pos.sub(v[0].pos);
		scal[0] = (v1.dot(v0))/(v1.len()*v0.len());
		scal[1] = (v2.dot(v0))/(v2.len()*v0.len());
		scal[2] = (v2.dot(v1))/(v2.len()*v1.len());
		vakt = (scal[0] < scal[1]) ? 0 : 1;
		vakt = (scal[2] < scal[vakt]) ? 2 : vakt;
		
		Vertex[] v_new = new Vertex[4];
		v_new[0] = v[0];
		
		switch(vakt)
		{
		case 0:
			v_new[1] = v[1];
			v_new[2] = v[2];
			v_new[3] = v[3];
			break;
		case 1:
			v_new[1] = v[1];
			v_new[2] = v[3];
			v_new[3] = v[2];
			break;
		case 2:
			v_new[1] = v[2];
			v_new[2] = v[3];
			v_new[3] = v[1];
			break;
		}
		v = v_new;
	}
	
	public FloatBuffer getNormals()
	{
		ByteBuffer bb = ByteBuffer.allocateDirect(nVertices()*4*4);
		bb.order(ByteOrder.nativeOrder());
		FloatBuffer cData = bb.asFloatBuffer();
		
		Vector4 v1 = v[1].pos.sub(v[0].pos);
		Vector4 v2 = v[2].pos.sub(v[0].pos);
		Vector4 normal = Vector4.crossProduct3(v2, v1);
		normal.normalize();
		
		for(int i = 0; i < 4; i++)
			for(int j = 0; j < 3; j++)
				cData.put((float)normal.getCoord(j));
		
		cData.position(0);
		return cData;
	}
	
	@Override
	public void draw(Renderer graph4d)
	{
		int vertexHandle = graph4d.shader.getVertexHandle();
		int colorHandle = graph4d.shader.getColorHandle();
		int normalHandle = graph4d.shader.getNormalHandle();
		GLES20.glEnableVertexAttribArray(vertexHandle);
		GLES20.glVertexAttribPointer(vertexHandle, 3, GLES20.GL_FLOAT, false,
	            0, getVData());

		GLES20.glEnableVertexAttribArray(colorHandle);
		GLES20.glVertexAttribPointer(colorHandle, 4, GLES20.GL_FLOAT, false,
	            0, getCData());

		GLES20.glEnableVertexAttribArray(normalHandle);
		GLES20.glVertexAttribPointer(normalHandle, 3, GLES20.GL_FLOAT, false,
	            0, getNormals());
		
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
		
		GLES20.glDisableVertexAttribArray(vertexHandle);
		GLES20.glDisableVertexAttribArray(colorHandle);
		GLES20.glDisableVertexAttribArray(normalHandle);
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
