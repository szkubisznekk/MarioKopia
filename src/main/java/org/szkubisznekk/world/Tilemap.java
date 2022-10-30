package org.szkubisznekk.world;

public class Tilemap
{
	private final int m_width;
	private final int m_height;
	private final byte[] m_tiles;

	public Tilemap(int width, int height)
	{
		m_width = width;
		m_height = height;
		m_tiles = new byte[width * height];
	}

	public Tilemap(int width, int height, byte[] tiles)
	{
		m_width = width;
		m_height = height;
		m_tiles = tiles;
	}

	public int getIndex(int x, int y)
	{
		return y * m_width + x;
	}

	public byte getTile(int i)
	{
		if(i >= 0 && i < m_tiles.length)
		{
			return m_tiles[i];
		}

		return 0;
	}

	public byte getTile(int x, int y)
	{
		return getTile(getIndex(x, y));
	}

	public void setTile(int i, byte tile)
	{
		if(i >= 0 && i < m_tiles.length)
		{
			m_tiles[i] = tile;
		}
	}

	public void setTile(int x, int y, byte tile)
	{
		setTile(getIndex(x, y), tile);
	}
}
