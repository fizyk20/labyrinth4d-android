package com.fizyk.engine4d;

import java.util.Vector;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.fizyk.engine4d.Shader.MatrixType;
import com.fizyk.math4d.Matrix4;
import com.fizyk.math4d.Vector4;

public class Renderer {
	
	private Vector<Primitive> primBuffer;
	private Vector<Primitive> localBuffer;
	private MatrixStack matrixStack;

	public Camera camera;
	public Shader shader;
	private SimpleShader simpleShader;
	private LightShader lightShader;
				
	public Renderer()
	{
		camera = new Camera();
		primBuffer = new Vector<Primitive>();
		localBuffer = new Vector<Primitive>();
		matrixStack = new MatrixStack();
		simpleShader = new SimpleShader();
		lightShader = new LightShader();
		
		shader = simpleShader;
	    shader.useProgram();
	    
		GLES20.glDisable(GLES20.GL_CULL_FACE);
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
	}
	
	public void enableLighting(boolean enable)
	{
		if(enable)
			shader = lightShader;
		else
			shader = simpleShader;
		shader.useProgram();
	}
	
	public void setLightDir(float x, float y, float z)
	{
		float[] lightDir = {x, y, z};
		lightShader.setLightDir(lightDir);
	}
	
	public void setupProjection(int width, int height)
	{
		float ratio = (float)width/height;
		GLES20.glViewport(0, 0, width, height);
		float[] mProjectionMatrix = new float[16];
		Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 1, 1000);
		lightShader.setMatrix(MatrixType.PROJECTION, mProjectionMatrix);
		simpleShader.setMatrix(MatrixType.PROJECTION, mProjectionMatrix);
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
		
		tetrahedron(v[0], v[2], v[1], v[4]);
		tetrahedron(v[5], v[7], v[4], v[1]);
		tetrahedron(v[6], v[4], v[7], v[2]);
		tetrahedron(v[3], v[1], v[2], v[7]);
		tetrahedron(v[2], v[7], v[1], v[4]);
	}
	
	public void cube(Vector4[] vertices, int i0, int i1, int i2, int i3, int i4, int i5, int i6, int i7)
	{
		Vector4[] v = {
				vertices[i0],
				vertices[i1],
				vertices[i2],
				vertices[i3],
				vertices[i4],
				vertices[i5],
				vertices[i6],
				vertices[i7]
			};
		
		tetrahedron(v[0], v[2], v[1], v[4]);
		tetrahedron(v[5], v[7], v[4], v[1]);
		tetrahedron(v[6], v[4], v[7], v[2]);
		tetrahedron(v[3], v[1], v[2], v[7]);
		tetrahedron(v[2], v[7], v[1], v[4]);
	}
	
	public void tesseract(double a)
	{
		Vector4[] v = {
				new Vector4(-a/2, -a/2, -a/2, -a/2),
				new Vector4(-a/2, -a/2, -a/2,  a/2),
				new Vector4(-a/2, -a/2,  a/2, -a/2),
				new Vector4(-a/2, -a/2,  a/2,  a/2),
				new Vector4(-a/2,  a/2, -a/2, -a/2),
				new Vector4(-a/2,  a/2, -a/2,  a/2),
				new Vector4(-a/2,  a/2,  a/2, -a/2),
				new Vector4(-a/2,  a/2,  a/2,  a/2),
				new Vector4( a/2, -a/2, -a/2, -a/2),
				new Vector4( a/2, -a/2, -a/2,  a/2),
				new Vector4( a/2, -a/2,  a/2, -a/2),
				new Vector4( a/2, -a/2,  a/2,  a/2),
				new Vector4( a/2,  a/2, -a/2, -a/2),
				new Vector4( a/2,  a/2, -a/2,  a/2),
				new Vector4( a/2,  a/2,  a/2, -a/2),
				new Vector4( a/2,  a/2,  a/2,  a/2)
		};
		cube(v, 0,1,2,3,4,5,6,7);
		cube(v, 8,9,10,11,12,13,14,15);
		cube(v, 0,1,2,3,8,9,10,11);
		cube(v, 4,5,6,7,12,13,14,15);
		cube(v, 0,1,4,5,8,9,12,13);
		cube(v, 2,3,6,7,10,11,14,15);
		cube(v, 0,2,4,6,8,10,12,14);
		cube(v, 1,3,5,7,9,11,13,15);
	}
	
	public void setBlending(double a)
	{
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		if(a < 0.99) GLES20.glDepthMask(false);
		else 
		{
			GLES20.glDisable(GLES20.GL_BLEND);
			GLES20.glDepthMask(true);
		}
	}
	
	public void render()
	{
		matrixStack.zeroStack();
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
		
		GLES20.glDepthMask(true);
		GLES20.glClearColor(0.f, 0.f, 0.3f, 1.f);
		GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
	    
	    // draw local buffer
	    for(int i = 0; i < localBuffer.size(); i++)
	    {
	    	Primitive prim = localBuffer.get(i);
	    	setBlending(prim.vertex(0).c.a);
	    	prim.draw(this);
	    }
	}
}
