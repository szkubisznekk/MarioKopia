package org.szkubisznekk.world;

import dev.dominion.ecs.api.*;
import org.w3c.dom.*;

import javax.xml.parsers.*;
import java.io.*;
import java.util.Base64;

/**
 * Holds all data related to a world.
 */
public class World
{
	/**
	 * The width of a world in number of tiles.
	 */
	public static final int WIDTH = 256;

	/**
	 * The height of a world in number of tiles.
	 */
	public static final int HEIGHT = 16;

	private final Tilemap m_tilemap;
	private final Dominion m_registry = Dominion.create();

	static
	{
		System.setProperty("dominion.show-banner", "false");
	}

	/**
	 * Loads a world from a world file.
	 *
	 * @param path The path to world file.
	 */
	public World(String path)
	{
		Document file = loadWorldFile(path);
		if(file != null)
		{
			m_tilemap = parseTilemap(file);
		}
		else
		{
			System.err.printf("Failed to load world: (%s)\n", path);
			m_tilemap = null;
		}
	}

	/**
	 * Returns the tilemap of the world.
	 *
	 * @return The tilemap of the world.
	 */
	Tilemap getTilemap()
	{
		return m_tilemap;
	}

	/**
	 * Returns the registry containing all entities in the world.
	 *
	 * @return The registry containing all entities in the world.
	 */
	Dominion getEntities()
	{
		return m_registry;
	}

	/**
	 * Loads the world file into an XML DOM.
	 *
	 * @param path The path to the world file.
	 * @return XML DOM of the world file.
	 */
	private static Document loadWorldFile(String path)
	{
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			Document document = builder.parse(new File(path));
			document.getDocumentElement().normalize();

			return document;
		}
		catch(Exception e)
		{
			return null;
		}
	}

	/**
	 * Build a tilemap out of a world file's XML DOM.
	 *
	 * @param document The world file's XML DOM.
	 * @return A tilemap built out of the world file's data.
	 */
	private static Tilemap parseTilemap(Document document)
	{
		NodeList list = document.getElementsByTagName("data");
		Node dataNode = list.item(0);

		Element dataElement = (Element)dataNode;
		String data = dataElement.getTextContent();

		data = data.replaceAll("\\s+", "");

		byte[] mapData = Base64.getDecoder().decode(data);

		Tilemap tilemap = new Tilemap(WIDTH, HEIGHT);
		for(int y = HEIGHT - 1, i = 0; y >= 0; y--)
		{
			for(int x = 0; x < WIDTH; x++, i += 4)
			{
				tilemap.setTile(x, y, mapData[i]);
			}
		}

		return tilemap;
	}
}
