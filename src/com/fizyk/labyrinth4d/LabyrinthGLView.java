package com.fizyk.labyrinth4d;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class LabyrinthGLView extends GLSurfaceView {

	private float mPreviousX, mPreviousY;
	private LabyrinthRenderer mRenderer;
	
	public LabyrinthGLView(Context context) {
		super(context);
		// Create an OpenGL ES 2.0 context
		setEGLContextClientVersion(2);
		mRenderer = new LabyrinthRenderer();
		setRenderer(mRenderer);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		float x = e.getX();
	    float y = e.getY();

	    switch (e.getAction()) {
	        case MotionEvent.ACTION_MOVE:

	            float dx = x - mPreviousX;
	            float dy = y - mPreviousY;
	            //TODO: handle movement
	    }

	    mPreviousX = x;
	    mPreviousY = y;
	    return true;
	}

}
