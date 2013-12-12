package de.pongpongplatsch;

import static org.junit.Assert.*;

import javax.vecmath.Point2f;

import org.junit.Test;

public class BoundingBoxTest {

	@Test
	public void testIntersects()
	{
		BoundingBox b0 = new BoundingBox(new Point2f(0,0),new Point2f(10,10));
		BoundingBox b1 = new BoundingBox(new Point2f(-10,0),new Point2f(5,10));
		BoundingBox b2 = new BoundingBox(new Point2f(-15,0),new Point2f(-5,10));
		BoundingBox b3 = new BoundingBox(new Point2f(-15,-120),new Point2f(-5,-100));
		BoundingBox[] boxes = {b0,b1,b2,b3};
		assertTrue(b0.intersects(b1));
		assertTrue(b1.intersects(b2));
		assertFalse(b0.intersects(b2));
		for(int i=0;i<3;i++) 	assertFalse(i+"",boxes[i].intersects(boxes[3]));
		// symmetry
		for(int i=0;i<boxes.length;i++)
			for(int j=i;j<boxes.length;j++)
				{assertTrue(i+","+j,boxes[i].intersects(boxes[j])==boxes[j].intersects(boxes[i]));}
		// reflexiveness
			for(int i=0;i<boxes.length;i++) assertTrue(i+"",boxes[i].intersects(boxes[i]));
	}

}
