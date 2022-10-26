package org.szkubisznekk.world;

public class Block
{
	public static Block Air = new Block((byte)0);
	public static Block Brick = new Block((byte)1);

	public final byte ID;

	public Block(byte ID)
	{
		this.ID = ID;
	}
}
