package org.szkubisznekk.world;

import org.szkubisznekk.renderer.*;

import org.joml.*;

public class World
{
	public static final int HEIGHT = 16;
	public static final int WIDTH = 256;

	private final Block[] m_blocks = new Block[WIDTH * HEIGHT];

	public World()
	{
		for (int y = 0; y < HEIGHT; y++)
		{
			for (int x = 0; x < WIDTH; x++)
			{
				m_blocks[getIndex(x, y)] =(y == 0) ? Block.Brick : Block.Air;
			}
		}
	}

	public void submit(Renderer renderer)
	{
		for (int y = 0; y < HEIGHT; y++)
		{
			for (int x = 0; x < WIDTH; x++)
			{
				renderer.submit(new Vector2f((float)x, (float)y), m_blocks[getIndex(x, y)].ID);
			}
		}
	}

	private static int getIndex(int x, int y)
	{
		return y * WIDTH + x;
	}
}
