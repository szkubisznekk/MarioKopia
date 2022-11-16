package org.szkubisznekk.world;

import static org.junit.Assert.*;

public class TilemapTest
{
	@org.junit.Test
	public void testGetIndex()
	{
		Tilemap tilemap	= new Tilemap(32, 8);
		assertEquals(tilemap.getIndex(0, 0), 0);
		assertEquals(tilemap.getIndex(31, 0), 31);
		assertEquals(tilemap.getIndex(0, 1), 32);
		assertEquals(tilemap.getIndex(31, 7), 255);
	}

	@org.junit.Test
	public void testGetTile1()
	{
		byte[] data = {
			1, 1, 1, 1, 1, 1, 1, 1,
			0, 0, 2, 0, 0, 2, 2, 2,
			0, 0, 2, 0, 0, 0, 2, 2,
			0, 0, 0, 0, 0, 0, 0, 2,
		};

		Tilemap tilemap	= new Tilemap(8, 4, data);
		assertEquals(tilemap.getTile(0), 1);
		assertEquals(tilemap.getTile(8), 0);
		assertEquals(tilemap.getTile(10), 2);
	}

	@org.junit.Test
	public void testGetTile2()
	{
		byte[] data = {
			1, 1, 1, 1, 1, 1, 1, 1,
			0, 0, 2, 0, 0, 2, 2, 2,
			0, 0, 2, 0, 0, 0, 2, 2,
			0, 0, 0, 0, 0, 0, 0, 2,
		};

		Tilemap tilemap	= new Tilemap(8, 4, data);
		assertEquals(tilemap.getTile(0, 0), 1);
		assertEquals(tilemap.getTile(0, 1), 0);
		assertEquals(tilemap.getTile(2, 1), 2);
	}

	@org.junit.Test
	public void testSetTile1()
	{
		Tilemap tilemap	= new Tilemap(32, 8);
		tilemap.setTile(0, (byte)69);
		tilemap.setTile(8, (byte)68);
		tilemap.setTile(10, (byte)67);
		assertEquals(tilemap.getTile(0), (byte)69);
		assertEquals(tilemap.getTile(8), (byte)68);
		assertEquals(tilemap.getTile(10), (byte)67);
	}

	@org.junit.Test
	public void testSetTile2()
	{
		Tilemap tilemap	= new Tilemap(32, 8);
		tilemap.setTile(0, 0, (byte)69);
		tilemap.setTile(0, 1, (byte)68);
		tilemap.setTile(2, 1, (byte)67);
		assertEquals(tilemap.getTile(0, 0), (byte)69);
		assertEquals(tilemap.getTile(0, 1), (byte)68);
		assertEquals(tilemap.getTile(2, 1), (byte)67);
	}
}