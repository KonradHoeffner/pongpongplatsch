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
import javax.vecmath.Tuple2f;
import javax.vecmath.Vector2f;

import org.lwjgl.opengl.GL11;
// todo: there is some error with the bounding box
public class Flower
{
	static final int START_ENERGY = 1;
	static final double minAreaFractionOfCamera = Math.pow(10,-4);
	static final double minAreaFractionForGrowth = Math.pow(10,-3);
	private static final boolean DRAW_BOUNDING_BOXES = false;
	private static final float LIFE_RADIUS_TO_CAMERA_DIAGONAL = 0.05f;

	final double base;
	final int depth;
	static Random r = new Random();
	final Point2f root;
	final BoundingBox boundingBox;
	Flower parent = null;
	List<Flower> flowers = new LinkedList<Flower>();

	public Flower(Point2f root)
	{
		//		this(root,Math.abs(r.nextGaussian()/2)+2,0,START_ENERGY,null);
		this(root,1,0,START_ENERGY,null);
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

	public void growMaster(int energy)
	{
		grow(energy);
		checkChangeUp(); 
	}

	public void checkChangeUp()
	{
		float v1 = boundingBox.volume;
		calculateBoundingBox();
		float v2 = boundingBox.volume;
		if(v2>v1+0.001&&parent!=null) {parent.checkChangeUp();}
	}



	public void grow(int energy)
	{
		if(energy<=0) {return;}
		if(!flowers.isEmpty())
		{
			for(Flower flower: flowers) {flower.grow(energy-1);}
			return;
		}

//		float length = (float)Math.pow(base, -depth);
		float length = (float)0.05;

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
			Flower flower = new Flower(new Point2f(root.x+length*v[0],root.y+length*v[1]), Math.min(2.5, Math.max(2,base+r.nextGaussian()/2)),depth+1,energy-1,this);
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

	public Flower(Point2f root,double base, int depth,int energy, Flower parent)
	{		
		this.root=root;
		this.base=base;
		this.depth=depth;
		this.parent=parent;
		boundingBox = new BoundingBox(root,root);
		growMaster(energy);
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
	static int i=0;
	static int j=0;
	public int draw(BoundingBox camera)
	{
		int count = 1;
		if(!camera.intersects(boundingBox)) {return count;} // invisible because out of screen
		// use parent fraction for now todo: use normal fraction for too small test?
		if(parent!=null)
		{
			double fraction = boundingBox.volume/camera.volume;
			double parentFraction = parent.boundingBox.volume/camera.volume;
			//		if(flowers.isEmpty()) System.out.println("i am a leave"+j+++"my fraction is "+fraction);
			//			if(flowers.isEmpty()&&parentFraction>minAreaFractionForGrowth) {this.growMaster(1);/*System.out.println("grow"+i++);*/}
			if(fraction<minAreaFractionOfCamera) {return count;} // invisible because too small		
		}

		for(Flower flower: flowers)
		{
			glBegin(GL_LINES);	
			glVertex2f(root.x,root.y);
			glVertex2f(flower.root.x,flower.root.y);
			glEnd();
			count += flower.draw(camera);
		}	
		if(DRAW_BOUNDING_BOXES) {drawBoundingBox(boundingBox);}
		return count;		
	}

	static protected float getColor(int depth)
	{
		return (float)Math.pow(1.0f/(depth+1),2);		
	}

	public void growToPoint(BoundingBox camera,Tuple2f point) {growToPoint(camera, new Vector2f(point), true);}

	public void growToPoint(BoundingBox camera,Vector2f point,boolean isRoot)
	{		
		Vector2f v = new Vector2f(root);
		v.sub(point);
		//		glBegin(GL_LINES);	
		//		glVertex2f(root.x,root.y);
		//		glVertex2f(point.x,point.y);
		//		glEnd();
		//		System.out.println(v.length());
		float lifeRadius = LIFE_RADIUS_TO_CAMERA_DIAGONAL*camera.getDiagonal();
//		BoundingBox lifeBox = new BoundingBox(point,point);
//		if(isRoot)
//		{
//		
////			System.out.println(camera.getDiagonal());
//			GL11.glPushMatrix();
//			GL11.glTranslatef(point.x, point.y, 0);
//			GL11.glScalef(lifeRadius, lifeRadius, 1);
//
//			GL11.glBegin(GL11.GL_TRIANGLE_FAN);
//			GL11.glVertex2f(0, 0);
//			for(int i = 0; i <= 100; i++){ //NUM_PIZZA_SLICES decides how round the circle looks.			
//				double angle = Math.PI * 2 * i / 100;
//
//				GL11.glVertex2f((float)Math.cos(angle), (float)Math.sin(angle));
//			}
//			GL11.glEnd();
//
//			GL11.glPopMatrix();
//		}

		//		glVertex2f(root.x,root.y);
		//		glVertex2f(flower.root.x,flower.root.y);
		//		glEnd();
		if(v.length()<lifeRadius) {/*System.out.println("grow!");*/growMaster(1);}
		if(parent!=null)
		{
			double parentFraction = parent.boundingBox.volume/camera.volume;
			if(parentFraction>minAreaFractionForGrowth)
			{
				for(Flower flower: flowers)
				{
					flower.growToPoint(camera,point);
				}
			}
		} else
		{
			for(Flower flower: flowers)
			{
				flower.growToPoint(camera,point,false);
			}
		}
	}

}