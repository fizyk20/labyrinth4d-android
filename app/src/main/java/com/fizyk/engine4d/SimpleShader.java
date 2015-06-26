package com.fizyk.engine4d;

import android.opengl.GLES20;
import android.opengl.Matrix;

public class SimpleShader extends Shader {

	private float[] MVPMatrix = new float[16];
	
	private String vertexCode = 
			"uniform mat4 u_MVPMatrix;		\n"     // A constant representing the combined model/view/projection matrix.
			 
			+ "attribute vec4 a_Position;	\n"     // Per-vertex position information we will pass in.
			+ "attribute vec4 a_Color;		\n"     // Per-vertex color information we will pass in.
			 
			+ "varying vec4 v_Color;		\n"     // This will be passed into the fragment shader.
			 
			+ "void main()					\n"     // The entry point for our vertex shader.
			+ "{							\n"	  
			+ "   v_Color = a_Color;		\n"     // Pass the color through to the fragment shader.
													// It will be interpolated across the triangle.
			+ "   gl_Position = u_MVPMatrix	\n"     // gl_Position is a special variable used to store the final position.
			+ "               * a_Position;	\n"     // Multiply the vertex by the matrix to get the final point in
			+ "}							\n";    // normalized screen coordinates.
	private String fragmentCode = 
			"precision mediump float;       \n"     // Set the default precision to medium. We don't need as high of a
													// precision in the fragment shader.
			+ "varying vec4 v_Color;		\n"     // This is the color from the vertex shader interpolated across the
													// triangle per fragment.
			+ "void main()					\n"     // The entry point for our fragment shader.
			+ "{							\n"
			+ "   gl_FragColor = v_Color;	\n"     // Pass the color directly through the pipeline.
			+ "}							\n";

	public SimpleShader()
	{
		init(vertexCode, fragmentCode);
	    GLES20.glBindAttribLocation(program, 0, "a_Position");
	    GLES20.glBindAttribLocation(program, 1, "a_Color");
	    linkProgram();
	}

	@Override
	protected void recalculateMatrices()
	{
		float[] tmpMatrix = new float[16];
		int mMVPMatrix;
		
		Matrix.multiplyMM(tmpMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
		Matrix.multiplyMM(MVPMatrix, 0, mProjectionMatrix, 0, tmpMatrix, 0);
		mMVPMatrix = GLES20.glGetUniformLocation(program, "u_MVPMatrix");
		GLES20.glUniformMatrix4fv(mMVPMatrix, 1, false, MVPMatrix, 0);
	}
	
	public void useProgram()
	{
		GLES20.glUseProgram(program);
		int mMVPMatrix = GLES20.glGetUniformLocation(program, "u_MVPMatrix");
		GLES20.glUniformMatrix4fv(mMVPMatrix, 1, false, MVPMatrix, 0);
	}
}
