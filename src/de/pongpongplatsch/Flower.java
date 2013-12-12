package de.pongpongplatsch;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.vecmath.Point2f;

public class Flower
{
	private static final int MAX_DEPTH = 7;
	static Random r = new Random();
	final Point2f root;
	final BoundingBox boundingBox;
	List<Flower> flowers = new LinkedList<Flower>();
	
	public Flower(Point2f root)
	{
		this(root,Math.abs(r.nextGaussian()/2)+2,0);
		calculateBoundingBox();
	}

	void calculateBoundingBox()
	{
		for(Flower flower: flowers)
		{
			flower.calculateBoundingBox();
			boundingBox.extend(flower.boundingBox);
		}
	}
	
	static public void drawBoundingBox(BoundingBox boundingBox)
{
//		boundingBox.min=new Point2f(-1,-1);
//		boundingBox.max=new Point2f(1,1);
		glBegin(GL_LINES);	
		glVertex2f(boundingBox.min.x,boundingBox.min.y);
		glVertex2f(boundingBox.min.x,boundingBox.max.y);
		
		glVertex2f(boundingBox.min.x,boundingBox.max.y);
		glVertex2f(boundingBox.max.x,boundingBox.max.y);
		
		glVertex2f(boundingBox.max.x,boundingBox.max.y);
		glVertex2f(boundingBox.max.x,boundingBox.min.y);
		
		glVertex2f(boundingBox.max.x,boundingBox.min.y);
		glVertex2f(boundingBox.min.x,boundingBox.min.y);
		glEnd();		
	}
	
	public Flower(Point2f root,double base, int depth)
	{
		this.root=root;
		boundingBox = new BoundingBox(root,root);
		if(depth==MAX_DEPTH) return;

		
		float length = (float)Math.pow(base, -depth);

		List<float[]> vs = new LinkedList<float[]>();
		for(int i=0;i<4;i++)
		{
			int degree = r.nextInt(360);
			float cosx = (float)Math.cos(2*Math.PI*degree/360);
			float sinx = (float)Math.sin(2*Math.PI*degree/360);
		
			vs.add(new float[]{cosx,sinx});
		}
		//		float[][] xys = new float[][] {{-1,0},{1,0},{0,1},{0,-1}};
		//		float[][] xys = {{xxx,yyy}};
		for(float[] v:vs )
		{
			Flower flower = new Flower(new Point2f(root.x+length*v[0],root.y+length*v[1]), Math.min(2.5, Math.max(2,base+r.nextGaussian()/2)),depth+1);
			//todo: bounding box
			flowers.add(flower);
//			float xx = v[0];
//			float yy = v[1];
//			float newX = root.x+length*xx;
//			float newY = root.y+length*yy;
//			lines.add(new Line(x,y,newX,newY));
//			lines.addAll(createFlower(newX,newY,Math.min(2.5, Math.max(2,base+r.nextGaussian()/2)),depth+1));
		}
	}
	
	static class Line
	{
		final float x1,y1,x2,y2;
		public Line(float x1,float y1,float x2,float y2)
		{
			this.x1=x1;
			this.y1=y1;
			this.x2=x2;
			this.y2=y2;
		}
	}

	 
	public int draw(BoundingBox camera)
	{
		int count = 1;
		if(!camera.intersects(boundingBox)) {return count;}
		for(Flower flower: flowers)
		{
			glBegin(GL_LINES);	
			glVertex2f(root.x,root.y);
			glVertex2f(flower.root.x,flower.root.y);
			glEnd();
			count += flower.draw(camera);
		}	
		drawBoundingBox(boundingBox);
		return count;		
	}

	static protected float getColor(int depth)
	{
		return (float)Math.pow(1.0f/(depth+1),2);		
	}

}