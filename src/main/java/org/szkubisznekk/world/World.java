package org.szkubisznekk.world;

import org.szkubisznekk.renderer.*;

import org.joml.*;
import dev.dominion.ecs.api.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;
import java.util.Base64;
import java.util.ArrayList;

public class World
{
	public static final int WIDTH = 256;
	public static final int HEIGHT = 16;

	private final Tilemap m_tilemap;
	private final Dominion m_registry = Dominion.create();

	static
	{
		System.setProperty("dominion.show-banner", "false");
	}

	public World(String path)
	{
		m_tilemap = decode(parse(path));
	}

	Tilemap getTilemap()
	{
		return m_tilemap;
	}

	Dominion getEntities()
	{
		return m_registry;
	}

	private static String parse(String path)
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();

			Document document = builder.parse(new File(path));
			document.getDocumentElement().normalize();

			NodeList list = document.getElementsByTagName("data");
			Node dataNode = list.item(0);

			Element dataElement = (Element)dataNode;
			return dataElement.getTextContent();
		}
		catch(ParserConfigurationException | SAXException | IOException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	private static Tilemap decode(String data)
	{
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
