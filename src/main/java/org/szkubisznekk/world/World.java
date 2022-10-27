package org.szkubisznekk.world;

import org.szkubisznekk.renderer.*;

import org.joml.*;

public class World
{
	public static final int HEIGHT = 16;
	public static final int WIDTH = 256;

	private final byte[] m_blocks = new byte[WIDTH * HEIGHT];

	public World()
	{
		for (int y = 0; y < HEIGHT; y++)
		{
			for (int x = 0; x < WIDTH; x++)
			{
				m_blocks[getIndex(x, y)] =(y == 0) ? Blocks.Brick : Blocks.Air;
			}
		}
	}

	public void update()
	{
		submit();
	}

	private void submit()
	{
		for (int y = 0; y < HEIGHT; y++)
		{
			for (int x = 0; x < WIDTH; x++)
			{
				Renderer.get().submit(new Vector2f((float)x, (float)y), -1.0f, m_blocks[getIndex(x, y)]);
			}
		}
	}

	private static int getIndex(int x, int y)
	{
		return y * WIDTH + x;
	}
}
