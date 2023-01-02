package org.szkubisznekk.world;

import org.joml.Vector2f;
import org.szkubisznekk.renderer.Renderer;

import java.io.File;
import java.util.ArrayList;

/**
 * Manages all worlds and systems run on the worlds.
 */
public class WorldManager
{
	private int m_current = -1;
	private final ArrayList<World> m_worlds = new ArrayList<>();
	private final ArrayList<SystemBase> m_systems = new ArrayList<>();

	/**
	 * Loads all world in a folder.
	 *
	 * @param path The path to the folder.
	 */
	public WorldManager(String path)
	{
		File folder = new File(path);
		File[] maps = folder.listFiles();

		if(maps != null)
		{
			for(var map : maps)
			{
				m_worlds.add(new World(map.getPath()));
			}
		}
	}

	/**
	 * Runs stop on all systems with the current world.
	 */
	public void destruct()
	{
		World world = m_worlds.get(m_current);
		if(m_current != -1)
		{
			for(var system : m_systems)
			{
				system.stop(world);
			}
		}
	}

	/**
	 * Runs stop on all systems with the current world, then sets the next world as current and runs start on all systems with that.
	 * Worlds are ordered by filename.
	 */
	public void loadNext()
	{
		World world = m_worlds.get(++m_current);
		world.reset();

		if(m_current != -1)
		{
			for(var system : m_systems)
			{
				system.stop(world);
			}
		}

		for(var system : m_systems)
		{
			system.start(world);
		}
	}

	/**
	 * Reloads the current world. Runs stop on all systems, then start on all systems.
	 */
	public void reloadCurrent()
	{
		World world = m_worlds.get(m_current);
		world.reset();

		for(var system : m_systems)
		{
			system.stop(world);
		}

		for(var system : m_systems)
		{
			system.start(world);
		}
	}

	/**
	 * Runs update on all systems with the current world.
	 */
	public void updateCurrent()
	{
		World world = m_worlds.get(m_current);
		for(var system : m_systems)
		{
			system.update(world);
		}
	}

	/**
	 * Renders the current world.
	 */
	public void submitCurrent()
	{
		World world = m_worlds.get(m_current);
		for(int y = 0; y < World.HEIGHT; y++)
		{
			for(int x = 0; x < World.WIDTH; x++)
			{
				Renderer.get().submit(new Vector2f((float)x, (float)y), -1.0f, world.getTilemap().getTile(x, y));
			}
		}
	}

	/**
	 * Add a ne system.
	 *
	 * @param type The type of the system.
	 * @param <T>  The type of the system.
	 */
	public <T extends SystemBase> void addSystem(Class<T> type)
	{
		try
		{
			SystemBase system = type.getConstructor().newInstance();
			m_systems.add(system);
		}
		catch(Exception ignored) {}
	}

	/**
	 * Remove a system.
	 *
	 * @param type The type of the system.
	 * @param <T>  The type of the system.
	 */
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
