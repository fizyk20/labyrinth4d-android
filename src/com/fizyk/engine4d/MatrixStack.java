package com.fizyk.engine4d;

import java.util.Vector;

import com.fizyk.math4d.Matrix4;

public class MatrixStack {
	
	private Vector<Matrix4> stack;
	private Matrix4 curMatrix;
	
	public MatrixStack()
	{
		stack = new Vector<Matrix4>();
		curMatrix = new Matrix4();
		curMatrix.loadIdentity();
		stack.clear();
	}
	
	public void pushMatrix()
	{
		stack.add(curMatrix);
	}
	
	public void popMatrix()
	{
		if(stack.size() == 0) return;
		curMatrix = stack.lastElement();
		stack.remove(curMatrix);
	}
	
	public void mul(Matrix4 m)
	{
		curMatrix = m.mul(curMatrix);
	}
	
	public Matrix4 getMatrix()
	{
		return curMatrix;
	}
	
	public void loadIdentity()
	{
		curMatrix.loadIdentity();
	}
	
	public void zeroStack()
	{
		stack.clear();
	}
}
