package org.szkubisznekk.world;

/**
 * Provides utility functions for a tilemap represented as a byte array.
 */
public class Tilemap
{
	private final int m_width;
	private final int m_height;
	private final byte[] m_tiles;

	/**
	 * Initialized a new tilemap.
	 *
	 * @param width  The width of the tilemap.
	 * @param height The height of the tilemap.
	 */
	public Tilemap(int width, int height)
	{
		m_width = width;
		m_height = height;
		m_tiles = new byte[width * height];
	}

	/**
	 * Creates a tilemap out of a byte array.
	 *
	 * @param width  The width of the tilemap.
	 * @param height The height of the tilemap.
	 * @param tiles  The byte array.
	 */
	public Tilemap(int width, int height, byte[] tiles)
	{
		m_width = width;
		m_height = height;
		m_tiles = tiles.clone();
	}

	/**
	 * Returns the index of a tile with the given coordinates.
	 *
	 * @param x The X coordinate of the tile.
	 * @param y The Y coordinate of the tile.
	 * @return The index of a tile with the given coordinates.
	 */
	public int getIndex(int x, int y)
	{
		return y * m_width + x;
	}

	/**
	 * Returns the id of a tile at a given index.
	 *
	 * @param i The index of the tile.
	 * @return The id of a tile at a given index.
	 */
	public byte getTile(int i)
	{
		if(i >= 0 && i < m_tiles.length)
		{
			return m_tiles[i];
		}

		return 0;
	}

	/**
	 * Returns the id of a tile at a given coordinate.
	 *
	 * @param x The X coordinate of the tile.
	 * @param y The Y coordinate of the tile.
	 * @return The id of a tile at a given coordinate.
	 */
	public byte getTile(int x, int y)
	{
		return getTile(getIndex(x, y));
	}

	/**
	 * Sets a tile at given index.
	 *
	 * @param i    The index of the tile.
	 * @param tile The new tile id.
	 */
	public void setTile(int i, byte tile)
	{
		if(i >= 0 && i < m_tiles.length)
		{
			m_tiles[i] = tile;
		}
	}

	/**
	 * Sets a tile at given coordinate.
	 *
	 * @param x    The X coordinate of the tile.
	 * @param y    The Y coordinate of the tile.
	 * @param tile The new tile id.
	 */
	public void setTile(int x, int y, byte tile)
	{
		setTile(getIndex(x, y), tile);
	}
}
