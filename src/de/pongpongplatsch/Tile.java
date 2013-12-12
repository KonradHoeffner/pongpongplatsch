package de.pongpongplatsch;
import java.awt.Color;

public class Tile
{
	Object sound;
	final Color color;
	
	Tile(Color color)
	{
		this.color=color;
	}
	
	Cell createCell()
	{
		return new Cell(this);
	}
}