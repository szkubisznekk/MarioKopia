package org.szkubisznekk.world;

import org.szkubisznekk.renderer.*;

import org.joml.*;
import dev.dominion.ecs.api.*;

import java.util.ArrayList;

public class World
{
	public static final int WIDTH = 256;
	public static final int HEIGHT = 16;

	private final Tilemap m_tilemap = new Tilemap(WIDTH, HEIGHT);
	private final Dominion m_registry = Dominion.create();
	private final ArrayList<SystemBase> m_systems = new ArrayList<>();

	static
	{
		System.setProperty("dominion.show-banner", "false");
	}

	public World()
	{
		for(int y = 0; y < HEIGHT; y++)
		{
			for(int x = 0; x < WIDTH; x++)
			{
				m_tilemap.setTile(x, y, (y == 0 || y == 4 || x == 6) ? Tiles.Brick : Tiles.Air);
			}
		}

		m_systems.add(new PlayerSystem(m_registry));
		m_systems.add(new PhysicsSystem(m_registry, m_tilemap));
		m_systems.add(new CameraSystem(m_registry));
		m_systems.add(new RendererSystem(m_registry));
	}

	public void start()
	{
		for(var system : m_systems)
		{
			system.start();
		}
	}

	public void update()
	{
		for(var system : m_systems)
		{
			system.update();
		}

		submit();
	}

	public void stop()
	{
		for(var system : m_systems)
		{
			system.stop();
		}
	}

	private void submit()
	{
		for(int y = 0; y < HEIGHT; y++)
		{
			for(int x = 0; x < WIDTH; x++)
			{
				Renderer.get().submit(new Vector2f((float)x, (float)y), -1.0f, m_tilemap.getTile(x, y));
			}
		}
	}
}
