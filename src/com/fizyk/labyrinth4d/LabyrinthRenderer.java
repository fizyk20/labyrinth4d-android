package com.fizyk.labyrinth4d;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.fizyk.engine4d.Renderer;
import com.fizyk.math4d.Vector4;

import android.opengl.GLSurfaceView;

public class LabyrinthRenderer implements GLSurfaceView.Renderer {
	
	private Renderer graph4d;
	
	public LabyrinthRenderer()
	{
		graph4d = new Renderer();
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig arg1) 
	{
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);	
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{
		graph4d.setupProjection(gl, width, height);
	}
	
	@Override
	public void onDrawFrame(GL10 gl)
	{
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		graph4d.translate(new Vector4(0., 0., -5., 0.));
		graph4d.cube(2.0);
		graph4d.render(gl);
	}

}
