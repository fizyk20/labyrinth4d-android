package com.fizyk.engine4d;

import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import com.fizyk.math4d.Matrix4;
import com.fizyk.math4d.Vector4;

public class Renderer {
	
	private Vector<Primitive> primBuffer;
	private Vector<Primitive> localBuffer;
	private MatrixStack matrixStack;

	public Camera camera;
	
	public Renderer()
	{
		camera = new Camera();
		primBuffer = new Vector<Primitive>();
		localBuffer = new Vector<Primitive>();
		matrixStack = new MatrixStack();                 // creates OpenGL ES program executables
	}
	
	public void setupProjection(GL10 gl, int width, int height)
	{
		float ratio = (float)width/height;
		gl.glMatrixMode(GL10.GL_PROJECTION);        // set matrix to projection mode
	    gl.glLoadIdentity();                        // reset the matrix to its default state
	    gl.glFrustumf(-ratio, ratio, -1, 1, 0, 1000);  // apply the projection matrix
	}
	
	public void pushMatrix()
	{
		matrixStack.pushMatrix();
	}
	
	public void popMatrix()
	{
		matrixStack.popMatrix();
	}
	
	public void rotate(Vector4 v1, Vector4 v2, double phi)
	{
		matrixStack.mul(Matrix4.rotation(v1, v2, phi));
	}
	
	public void rotateXY(double phi)
	{
		matrixStack.mul(Matrix4.rotationXY(phi));
	}
	
	public void rotateXZ(double phi)
	{
		matrixStack.mul(Matrix4.rotationXZ(phi));
	}
	
	public void rotateXW(double phi)
	{
		matrixStack.mul(Matrix4.rotationXW(phi));
	}
	
	public void rotateYZ(double phi)
	{
		matrixStack.mul(Matrix4.rotationYZ(phi));
	}
	
	public void rotateYW(double phi)
	{
		matrixStack.mul(Matrix4.rotationYW(phi));
	}
	
	public void rotateZW(double phi)
	{
		matrixStack.mul(Matrix4.rotationZW(phi));
	}
	
	public void translate(Vector4 v)
	{
		matrixStack.mul(Matrix4.translation(v));
	}
	
	public void setColor(Color c)
	{
		Color.current = c;
	}
	
	public void tetrahedron(Vector4 v1, Vector4 v2, Vector4 v3, Vector4 v4)
	{
		Vector4 t1, t2, t3, t4; // transformed vectors
		Matrix4 m = matrixStack.getMatrix();
		t1 = m.mul(v1);
		t2 = m.mul(v2);
		t3 = m.mul(v3);
		t4 = m.mul(v4);
		primBuffer.add(new Tetrahedron(t1, t2, t3, t4));
	}
	
	public void cube(double a)
	{
		Vector4[] v = {
			new Vector4(-a/2, -a/2, -a/2, 0.),
			new Vector4(-a/2, -a/2,  a/2, 0.),
			new Vector4(-a/2,  a/2, -a/2, 0.),
			new Vector4(-a/2,  a/2,  a/2, 0.),
			new Vector4( a/2, -a/2, -a/2, 0.),
			new Vector4( a/2, -a/2,  a/2, 0.),
			new Vector4( a/2,  a/2, -a/2, 0.),
			new Vector4( a/2,  a/2,  a/2, 0.)
		};
		
		tetrahedron(v[0], v[1], v[4], v[2]);
		tetrahedron(v[5], v[4], v[1], v[7]);
		tetrahedron(v[7], v[2], v[3], v[1]);
		tetrahedron(v[2], v[7], v[6], v[4]);
		tetrahedron(v[2], v[7], v[1], v[4]);
	}
	
	public void render(GL10 gl)
	{
		localBuffer.clear();
		while(primBuffer.size() > 0)
		{
			Primitive tmp = primBuffer.firstElement().intersect(camera.getHyperplane());
			if(tmp != null)
			{
				// recalculate coords into camera system
				for(int i = 0; i < tmp.nVertices(); i++)
					tmp.vertex(i).pos = camera.calculateLocal(tmp.vertex(i).pos);
				
				// add transformed primitive to local buffer
				localBuffer.add(tmp);
			}
			primBuffer.remove(0);
		}
		
		gl.glMatrixMode(GL10.GL_MODELVIEW);
	    gl.glLoadIdentity(); 
	    
	    // draw local buffer
	    for(int i = 0; i < localBuffer.size(); i++)
	    	localBuffer.get(i).draw(gl);
	}
}
