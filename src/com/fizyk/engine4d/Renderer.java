package com.fizyk.engine4d;

import java.util.Vector;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.fizyk.math4d.Matrix4;
import com.fizyk.math4d.Vector4;

public class Renderer {
	
	private Vector<Primitive> primBuffer;
	private Vector<Primitive> localBuffer;
	private MatrixStack matrixStack;

	public Camera camera;
	
	private final String vertexShader =
		    "uniform mat4 u_MVPMatrix;      \n"     // A constant representing the combined model/view/projection matrix.
		 
		  + "attribute vec4 a_Position;     \n"     // Per-vertex position information we will pass in.
		  + "attribute vec4 a_Color;        \n"     // Per-vertex color information we will pass in.
		 
		  + "varying vec4 v_Color;          \n"     // This will be passed into the fragment shader.
		 
		  + "void main()                    \n"     // The entry point for our vertex shader.
		  + "{                              \n"
		  + "   v_Color = a_Color;          \n"     // Pass the color through to the fragment shader.
		                                            // It will be interpolated across the triangle.
		  + "   gl_Position = u_MVPMatrix   \n"     // gl_Position is a special variable used to store the final position.
		  + "               * a_Position;   \n"     // Multiply the vertex by the matrix to get the final point in
		  + "}                              \n";    // normalized screen coordinates.
	
	private final String fragmentShader =
		      "precision mediump float;       \n"     // Set the default precision to medium. We don't need as high of a
			            							  // precision in the fragment shader.
			+ "varying vec4 v_Color;          \n"     // This is the color from the vertex shader interpolated across the
													  // triangle per fragment.
			+ "void main()                    \n"     // The entry point for our fragment shader.
			+ "{                              \n"
			+ "   gl_FragColor = v_Color;     \n"     // Pass the color directly through the pipeline.
			+ "}                              \n";
	
	private int programHandle;
	
	private int mMVPMatrixHandle;
	private int mPositionHandle;
	private int mColorHandle;
				
	public Renderer()
	{
		camera = new Camera();
		primBuffer = new Vector<Primitive>();
		localBuffer = new Vector<Primitive>();
		matrixStack = new MatrixStack();
		
		int vertexShaderHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
		GLES20.glShaderSource(vertexShaderHandle, vertexShader);
	    GLES20.glCompileShader(vertexShaderHandle);
	    int fragmentShaderHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
		GLES20.glShaderSource(fragmentShaderHandle, fragmentShader);
	    GLES20.glCompileShader(fragmentShaderHandle);
	    
	    programHandle = GLES20.glCreateProgram();
	    GLES20.glAttachShader(programHandle, vertexShaderHandle);
	    GLES20.glAttachShader(programHandle, fragmentShaderHandle);
	    GLES20.glBindAttribLocation(programHandle, 0, "a_Position");
	    GLES20.glBindAttribLocation(programHandle, 1, "a_Color");
	    GLES20.glLinkProgram(programHandle);
	    
	    mMVPMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");
	    mPositionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
	    mColorHandle = GLES20.glGetAttribLocation(programHandle, "a_Color");
	    
	    GLES20.glUseProgram(programHandle);
	}
	
	private float[] mProjectionMatrix = new float[16];
	
	public void setupProjection(int width, int height)
	{
		float ratio = (float)width/height;
		GLES20.glViewport(0, 0, width, height);
		Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 1, 1000);
	}
	
	public int getVertexHandle()
	{
		return mPositionHandle;
	}
	
	public int getColorHandle()
	{
		return mColorHandle;
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
		
		GLES20.glClearColor(0.f, 0.f, 0.3f, 1.f);
		GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mProjectionMatrix, 0);
	    
	    // draw local buffer
	    for(int i = 0; i < localBuffer.size(); i++)
	    	localBuffer.get(i).draw(this);
	}
}
