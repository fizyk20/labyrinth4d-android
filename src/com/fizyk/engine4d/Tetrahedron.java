package com.fizyk.engine4d;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Vector;

import android.opengl.GLES20;

import com.fizyk.math4d.Hyperplane;
import com.fizyk.math4d.Vector4;

public class Tetrahedron extends Primitive {
	
	private short drawOrder[] = { 0, 1, 2, 0, 2, 3, 0, 3, 1, 2, 1, 3 };
	
	public Tetrahedron(Vector4 pos1, Color c1, Vector4 pos2, Color c2, Vector4 pos3, Color c3, Vector4 pos4, Color c4)
	{
		v = new Vertex[4];
		v[0] = new Vertex(pos1, c1);
		v[1] = new Vertex(pos2, c2);
		v[2] = new Vertex(pos3, c3);
		v[3] = new Vertex(pos4, c4);
	}
	
	public Tetrahedron(Vector4 pos1, Vector4 pos2, Vector4 pos3, Vector4 pos4)
	{
		v = new Vertex[4];
		v[0] = new Vertex(pos1);
		v[1] = new Vertex(pos2);
		v[2] = new Vertex(pos3);
		v[3] = new Vertex(pos4);
	}
	
	public Tetrahedron(Vertex v1, Vertex v2, Vertex v3, Vertex v4)
	{
		v = new Vertex[4];
		v[0] = v1;
		v[1] = v2;
		v[2] = v3;
		v[3] = v4;
	}
	
	public FloatBuffer getVData()
	{
		ByteBuffer bb = ByteBuffer.allocateDirect(12*4*3);
		bb.order(ByteOrder.nativeOrder());
		FloatBuffer vData = bb.asFloatBuffer();
		for(int i = 0; i < 12; i++)
			for(int j = 0; j < 3; j++)
				vData.put((float)v[drawOrder[i]].pos.getCoord(j));
		vData.position(0);
		return vData;
	}
	
	public FloatBuffer getCData()
	{
		ByteBuffer bb = ByteBuffer.allocateDirect(12*4*4);
		bb.order(ByteOrder.nativeOrder());
		FloatBuffer cData = bb.asFloatBuffer();
		for(int i = 0; i < 12; i++)
			for(int j = 0; j < 4; j++)
				cData.put((float)v[drawOrder[i]].c.toVector().getCoord(j));
		cData.position(0);
		return cData;
	}
	
	public FloatBuffer getNormals()
	{
		ByteBuffer bb = ByteBuffer.allocateDirect(12*4*4);
		bb.order(ByteOrder.nativeOrder());
		FloatBuffer cData = bb.asFloatBuffer();
		
		for(int i = 0; i < 4; i++)
		{
			Vector4 v1 = v[drawOrder[i*3 + 1]].pos.sub(v[drawOrder[i*3 + 0]].pos);
			Vector4 v2 = v[drawOrder[i*3 + 2]].pos.sub(v[drawOrder[i*3 + 0]].pos);
			Vector4 normal = Vector4.crossProduct(v2, v1, new Vector4(0.,0.,0.,1.));
			normal.normalize();
			float[] data = {(float)normal.getCoord(0), (float)normal.getCoord(1), (float)normal.getCoord(2)};
			cData.put(data);
			cData.put(data);
			cData.put(data);
		}
		
		cData.position(0);
		return cData;
	}
	
	@Override
	public void draw(Renderer graph4d)
	{
		graph4d.enableLighting(true);
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
		
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 12);
		
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
		
		tmp = new Line(v[0], v[2]).intersect(h);
		if(tmp != null) p.add(tmp);
		
		tmp = new Line(v[0], v[3]).intersect(h);
		if(tmp != null) p.add(tmp);
		
		tmp = new Line(v[1], v[2]).intersect(h);
		if(tmp != null) p.add(tmp);
		
		tmp = new Line(v[1], v[3]).intersect(h);
		if(tmp != null) p.add(tmp);
		
		tmp = new Line(v[2], v[3]).intersect(h);
		if(tmp != null) p.add(tmp);
		
		// the possible results are:
		// * nothing (0 non-null results)
		// * point (3 Point results, equal -> 3)
		// * triangle (3 non-equal Points or 3 Lines + 3 Points -> 3 or 6)
		// * line (1 Line + 4 Points -> 5)
		// * quad (4 Points -> 4)
		// * tetrahedron (6 Lines -> 6)
		
		// case: nothing
		if(p.size() == 0)
			return null;
		
		// case: quad
		if(p.size() == 4)
			return new Quad(p.get(0).vertex(0), p.get(1).vertex(0), p.get(2).vertex(0), p.get(3).vertex(0));
		
		// case: line
		if(p.size() == 5)
			for(int i = 0; i < 5; i++)
				if(p.get(i).getClass() == Line.class)
					return p.get(i);
		
		// case: 3 intersections (triangle or point)
		if(p.size() == 3)
		{
			for(int i = 0; i < 3; i++)
				if(p.get(i).getClass() != Point.class)
					return null; 	// this means that something went wrong
			
			Point p1 = (Point) p.get(0);
			Point p2 = (Point) p.get(1);
			Point p3 = (Point) p.get(2);
			
			// if points are equal, return a point
			if(p1.vertex(0).pos == p2.vertex(0).pos && p2.vertex(0).pos == p3.vertex(0).pos)
				return p1;
			
			return new Triangle(p1.vertex(0), p2.vertex(0), p3.vertex(0));
		}
		
		// case: 6 intersections (triangle or tetrahedron)
		if(p.size() == 6)
		{
			Vector<Primitive> p2 = new Vector<Primitive>();
			for(int i = 0; i < 6; i++)
				if(p.get(i).getClass() == Point.class)
					p2.add(p.get(i));
			if(p2.size() == 0)
				return this;	// all are lines
			
			if(p2.size() == 3)
				return new Triangle(p.get(0).vertex(0), p.get(1).vertex(0), p.get(2).vertex(0));
			
			return null; 	// if we got here, something went wrong
		}
		
		return null;	// if we got here, something went wrong
	}

}
