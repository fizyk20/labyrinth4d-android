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
		graph4d.setLightDir(-.3f, -1.f, 3.f);
		long time = SystemClock.uptimeMillis();
		double angle1 = (double)(time % 5000L) / 5000. * 6.283;
		double angle2 = (double)(time % 6000L) / 6000. * 6.283;
		//graph4d.rotate(new Vector4(0., 0., 0., 1.), new Vector4(0., 1., 0., 0.), 0.5);
		//graph4d.rotate(new Vector4(0., 0., 0., 1.), new Vector4(1., 0., 0., 0.), 0.5);
		graph4d.rotate(new Vector4(0., 0., 0., 1.), new Vector4(0., 1., 0., 0.), angle1);
		graph4d.rotate(new Vector4(1., 0., 0., 0.), new Vector4(0., 0., 1., 0.), angle2);
		graph4d.translate(new Vector4(0., 0., 8., 0.));
		graph4d.setColor(new Color(0., 1., 1., 1.));
		graph4d.tesseract(2.0);
		graph4d.render();
	}

}
