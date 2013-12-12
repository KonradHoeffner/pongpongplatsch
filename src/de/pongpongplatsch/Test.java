package de.pongpongplatsch;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.util.Random;

import javax.vecmath.Point2f;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
public class Test
{
	static final int WINDOW_WIDTH = 1280;
	static final int WINDOW_HEIGHT = 720;
	static float radiusX = 0.5f;
	static float radiusY = radiusX*WINDOW_HEIGHT/WINDOW_WIDTH;
	static BoundingBox camera = new BoundingBox(new Point2f(-radiusX,-radiusY-1),new Point2f(radiusX,radiusY-1));
	static Map map = Map.createRandomMap();
	//	static Camera camera = new Camera();

	static Random r = new Random();

	static DisplayMode guessFullScreenMode() throws LWJGLException
	{
		DisplayMode displayMode = null;
		DisplayMode[] modes = Display.getAvailableDisplayModes();

		int maxWidth = 0;    
		for (DisplayMode mode: modes)
		{
			if(mode.isFullscreenCapable()&&mode.getWidth()>maxWidth)
			{
				maxWidth=mode.getWidth();
				displayMode = mode;

			}
		}
		return displayMode;
	}

	public static void drawWorld()
	{
		for(int x=0;x<map.cells[0].length;x++)
			for(int y=0;y<map.cells.length;y++)
			{
				if(map.cells[y][x]!=null) {drawTile(x-5,y-5);}
			}
	}

	static void drawTile(int x, int y)
	{		
		glBegin(GL_QUADS);
		glVertex2f(x-0.5f,y-0.5f);
		glVertex2f(x+0.5f,y-0.5f);
		glVertex2f(x+0.5f,y+0.5f);
		glVertex2f(x-0.5f,y+0.5f);
		glEnd();		
	}

	public static void main(String[] args) throws LWJGLException, InterruptedException
	{		

		//				Display.setFullscreen(true);
		//				Display.setDisplayMode(guessFullScreenMode());
		//		double 
		Display.setDisplayMode(new DisplayMode(WINDOW_WIDTH,WINDOW_HEIGHT));

		Display.create();
		glMatrixMode(GL_PROJECTION);
//		glLoadIdentity();
//		//		glFrustum(-radiusX, radiusX, -radiusY, radiusY, 100, -100);
////		glOrtho(-radiusX, radiusX, -radiusY-1, radiusY-1, 100, -100);
//		glOrtho(camera.min.x, camera.max.x, camera.min.y, camera.max.y, 100, -100);
//		glMatrixMode(GL_MODELVIEW);

		//		for(int i=0;i<3;i++)
		//		{
		//			Flower.drawFlower((float)r.nextGaussian()*2,(float)r.nextGaussian()*2);
		//			}

		//		List<Flower.Line> lines = Flower.createFlower(0, -1f);
		Flower flower = new Flower(new Point2f(0, -1f));

		double x=0;
		while(!Display.isCloseRequested())
		{
			int wheel = Mouse.getDWheel();
			//			System.out.println("bla");
			//			if(Keyboard.next())
			//			{
			////				System.out.println("key");
			//				switch(Keyboard.getEventKey())
			//				{
			//				case(Keyboard.KEY_UP):camera.y--;break;
			//				case(Keyboard.KEY_DOWN):camera.y++;break;
			//				case(Keyboard.KEY_LEFT):camera.x--;break;
			//				case(Keyboard.KEY_RIGHT):camera.x++;break;
			//				default: System.exit(0);
			//				}
			//			}
			//			
			if(wheel!=0)
			{
				if(wheel<0)	{camera.scale(1.05f);}
				else				{camera.scale(0.95f);}
				
//				if(wheel<0)	{radiusX*=1.05;radiusY*=1.05;}
//				else				{radiusX/=1.05;radiusY/=1.05;}
			}
			//			
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
//			glOrtho(-radiusX, radiusX, -radiusY-1, radiusY-1, 100, -100);
			glOrtho(camera.min.x, camera.max.x, camera.min.y, camera.max.y, 100, -100);
			//				glFrustum(-radiusX, radiusX, -radiusY, radiusY, 100, -100);
			//				glOrtho(-radiusX+699, radiusX, -radiusY, radiusY*1000000, 100, -100);

			glMatrixMode(GL_MODELVIEW);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
//			System.out.println(flower.draw(camera));
//			flower.draw(camera);
//			Flower.drawBoundingBox(camera);
			//				Flower.drawFlower(0,-1f);
			//			}
			////						int x = Mouse.getX();
			////						int y = Mouse.getY();
			//						
			//			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
						
//			glColor3f(1f,0.0f,0.0f);
			//			
			//			drawWorld();
			Display.sync(60);
			Display.update();
			//			Thread.sleep(100);
		}
		Display.destroy();
	}

}
