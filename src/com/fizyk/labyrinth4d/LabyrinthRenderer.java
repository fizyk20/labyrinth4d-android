package com.fizyk.labyrinth4d;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

public class LabyrinthRenderer implements GLSurfaceView.Renderer {

	@Override
	public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) 
	{
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);	
	}

	@Override
	public void onSurfaceChanged(GL10 arg0, int width, int height)
	{
		GLES20.glViewport(0, 0, width, height);
	}
	
	@Override
	public void onDrawFrame(GL10 arg0)
	{
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
	}

}
