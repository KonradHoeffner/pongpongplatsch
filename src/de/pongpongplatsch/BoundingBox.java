package de.pongpongplatsch;

import javax.vecmath.Point2f;
import javax.vecmath.Vector2f;

public class BoundingBox
{
	public Point2f min;
	public Point2f max;

	public BoundingBox(Point2f min, Point2f max)
	{
		this.min = min;
		this.max = max;
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
	
	public void extend(BoundingBox box)
	{
		min = new Point2f(Math.min(min.x, box.min.x), Math.min(min.y, box.min.y));
		max = new Point2f(Math.max(max.x, box.max.x), Math.max(max.y, box.max.y));
		//		return new BoundingBox(	new Point2f(Math.min(min.x, box.min.x), Math.min(min.y, box.min.y)),
		//														new Point2f(Math.max(max.x, box.max.x), Math.max(max.y, box.max.y)));
	}
	
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
		
	}
}