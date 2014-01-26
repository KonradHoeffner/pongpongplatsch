package de.pongpongplatsch;

import javax.vecmath.Point2f;
import javax.vecmath.Tuple2f;
import javax.vecmath.Vector2f;

public class BoundingBox
{
	public Point2f min;
	public Point2f max;
	float volume = 0;

	public BoundingBox(Point2f min, Point2f max)
	{
		this.min = min;
		this.max = max;
		updateVolume();
	}

	public BoundingBox(BoundingBox box)
	{
		this(box.min, box.max);
	}

	void updateVolume()
	{
		volume  = (max.x-min.x)*(max.y-min.y);
	}
	
	@Override
	public String toString() {
		return "BoundingBox [min=" + min + ", max=" + max + "]";
	}

	//	public BoundingBox() {this(new Point2f(Float.NEGATIVE_INFINITY,Float.NEGATIVE_INFINITY),new Point2f(Float.POSITIVE_INFINITY,Float.POSITIVE_INFINITY));}

	boolean intersects(BoundingBox box)
	{
		return	((min.x<=box.min.x&&max.x>=box.min.x)||(min.x>=box.min.x&&min.x<=box.max.x))&&
						((min.y<=box.min.y&&max.y>=box.min.y)||(min.y>=box.min.y&&min.y<=box.max.y));
	}
	
	public BoundingBox add(Tuple2f point)
	{
		Point2f newMin = new Point2f(point);
		newMin.add(min);
		Point2f newMax = new Point2f(point);
		newMax.add(max);
		return new BoundingBox(newMin,newMax);
	}
	
	public void extend(BoundingBox box)
	{
		min = new Point2f(Math.min(min.x, box.min.x), Math.min(min.y, box.min.y));
		max = new Point2f(Math.max(max.x, box.max.x), Math.max(max.y, box.max.y));
		//		return new BoundingBox(	new Point2f(Math.min(min.x, box.min.x), Math.min(min.y, box.min.y)),
		//														new Point2f(Math.max(max.x, box.max.x), Math.max(max.y, box.max.y)));
		updateVolume();
	}
	
	public Point2f getCenter() {return new Point2f((min.x+max.x)/2,(min.y+max.y)/2);}
	public float getDiagonal() {Vector2f diagonal = new Vector2f();diagonal.sub(max, min);return diagonal.length();}
	
	/** keeps the center and scales length and height by factor */
	public void scale(float factor)
	{
		Vector2f v= new Vector2f(max);
		v.sub(min);
		v.scale(0.5f);
		Point2f center = new Point2f(min);
		center.add(v);
		v.scale(factor);
		min = new Point2f(center);
		max = new Point2f(center);
		min.sub(v);
		max.add(v);
		updateVolume();
	}
}