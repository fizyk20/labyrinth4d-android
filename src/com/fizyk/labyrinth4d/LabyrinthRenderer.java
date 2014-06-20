package com.fizyk.labyrinth4d;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.fizyk.engine4d.Color;
import com.fizyk.engine4d.Renderer;
import com.fizyk.math4d.Vector4;

import android.opengl.GLSurfaceView;
import android.os.SystemClock;

public class LabyrinthRenderer implements GLSurfaceView.Renderer {
	
	private Renderer graph4d;
	
	public LabyrinthRenderer()
	{
	}

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig arg1) 
	{
		graph4d = new Renderer();
	}

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height)
	{
		graph4d.setupProjection(width, height);
	}
	
	@Override
	public void onDrawFrame(GL10 unused)
	{
		graph4d.setColor(new Color(0., 1., 1.));
		graph4d.setLightDir(-.3f, -1.f, 3.f);
		long time = SystemClock.uptimeMillis() % 5000L;
		double angle = (double)time / 5000. * 6.283;
		graph4d.rotate(new Vector4(0., 0., 0., 1.), new Vector4(0., 1., 0., 0.), 0.5);
		graph4d.rotate(new Vector4(0., 0., 0., 1.), new Vector4(1., 0., 0., 0.), 0.5);
		graph4d.rotate(new Vector4(0., 0., 0., 1.), new Vector4(.5, .5, -8., 0.), angle);
		graph4d.translate(new Vector4(-.5, -.5, 8., 0.));
		graph4d.cube(2.0);
		graph4d.render();
	}

}
