package com.fizyk.engine4d;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Vector;

import android.opengl.GLES20;

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
	
	public FloatBuffer getNormals()
	{
		ByteBuffer bb = ByteBuffer.allocateDirect(nVertices()*4*4);
		bb.order(ByteOrder.nativeOrder());
		FloatBuffer cData = bb.asFloatBuffer();
		
		Vector4 v1 = v[1].pos.sub(v[0].pos);
		Vector4 v2 = v[2].pos.sub(v[0].pos);
		Vector4 normal = Vector4.crossProduct(v1, v2, new Vector4(0.,0.,0.,1.));
		normal.normalize();
		
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 3; j++)
				cData.put((float)normal.getCoord(j));
		
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
		GLES20.glVertexAttribPointer(normalHandle, 4, GLES20.GL_FLOAT, false,
	            0, getNormals());
		
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
		
		GLES20.glDisableVertexAttribArray(vertexHandle);
		GLES20.glDisableVertexAttribArray(colorHandle);
		GLES20.glDisableVertexAttribArray(normalHandle);

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
