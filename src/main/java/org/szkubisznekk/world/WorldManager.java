package org.szkubisznekk.world;

import org.joml.Vector2f;
import org.szkubisznekk.renderer.Renderer;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public class WorldManager
{
	private World m_current = null;
	private final HashMap<Path, World> m_worlds = new HashMap<>();
	private final ArrayList<SystemBase> m_systems = new ArrayList<>();

	public WorldManager(String path)
	{
		File folder = new File(path);
		File[] maps = folder.listFiles();

		if(maps != null)
		{
			for(var map : maps)
			{
				m_worlds.put(Path.of(map.getPath()), new World(map.getPath()));
			}
		}
	}

	public void destruct()
	{
		if(m_current != null)
		{
			for(var system : m_systems)
			{
				system.stop(m_current);
			}
		}
	}

	public void load(String path)
	{
		if(m_current != null)
		{
			for(var system : m_systems)
			{
				system.stop(m_current);
			}
		}

		m_current = m_worlds.get(Path.of(path));

		for(var system : m_systems)
		{
			system.start(m_current);
		}
	}

	public void updateCurrent()
	{
		for(var system : m_systems)
		{
			system.update(m_current);
		}
	}

	public void submitCurrent()
	{
		for(int y = 0; y < World.HEIGHT; y++)
		{
			for(int x = 0; x < World.WIDTH; x++)
			{
				Renderer.get().submit(new Vector2f((float)x, (float)y), -1.0f, m_current.getTilemap().getTile(x, y));
			}
		}
	}

	public <T extends SystemBase> void addSystem(Class<T> type)
	{
		try
		{
			SystemBase system = type.getConstructor().newInstance();
			m_systems.add(system);
		}
		catch(Exception ignored) {}
	}

	public <T extends SystemBase> void removeSystem(Class<T> type)
	{
		int n = m_systems.size();
		int i = 0;
		while(i < n && m_systems.get(i).getClass() != type) {i++;}
		if(i < n)
		{
			m_systems.remove(i);
		}
	}
}
