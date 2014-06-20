package com.fizyk.engine4d;

import android.opengl.GLES20;
import android.opengl.Matrix;

abstract class Shader {
	
	protected int program;
	private int vertexShader;
	private int fragmentShader;
	protected String mVertexString = "a_Position";
	protected String mColorString = "a_Color";
	protected String mNormalString = "a_Normal";
	protected float[] mProjectionMatrix = new float[16];
	protected float[] mViewMatrix = new float[16];
	protected float[] mModelMatrix = new float[16];
	
	enum MatrixType {PROJECTION, MODEL, VIEW};
	
	public Shader()
	{
		vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
		fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
	    program = GLES20.glCreateProgram();
	    
	    Matrix.setIdentityM(mProjectionMatrix, 0);
	    Matrix.setIdentityM(mViewMatrix, 0);
	    Matrix.setIdentityM(mModelMatrix, 0);
	}
	
	protected void init(String vertexCode, String fragmentCode)
	{
		GLES20.glShaderSource(vertexShader, vertexCode);
	    GLES20.glCompileShader(vertexShader);
		GLES20.glShaderSource(fragmentShader, fragmentCode);
	    GLES20.glCompileShader(fragmentShader);
	    GLES20.glAttachShader(program, vertexShader);
	    GLES20.glAttachShader(program, fragmentShader);
	}
	
	protected void linkProgram()
	{
		GLES20.glLinkProgram(program);
	}
	
	public void useProgram()
	{
		GLES20.glUseProgram(program);
	}
	
	public int getVertexHandle()
	{
		return GLES20.glGetAttribLocation(program, mVertexString);
	}
	
	public int getColorHandle()
	{
		return GLES20.glGetAttribLocation(program, mColorString);
	}
	
	public int getNormalHandle()
	{
		return GLES20.glGetAttribLocation(program, mNormalString);
	}
	
	public void setMatrix(MatrixType type, float[] matrix)
	{
		switch(type)
		{
		case PROJECTION:
			mProjectionMatrix = matrix;
			break;
		case VIEW:
			mViewMatrix = matrix;
			break;
		case MODEL:
			mModelMatrix = matrix;
			break;
		}
		recalculateMatrices();
	}
	
	protected abstract void recalculateMatrices();

}
