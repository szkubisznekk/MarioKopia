package org.szkubisznekk.world;

import dev.dominion.ecs.api.*;
import org.joml.*;
import org.szkubisznekk.renderer.*;

public class CameraSystem extends SystemBase
{
	public CameraSystem(Dominion registry)
	{
		super(registry);
	}

	@Override
	public void start()
	{
		Renderer.get().Camera = new Camera(new Vector2f(0.0f, 7.5f), 16.0f);
	}

	@Override
	public void update()
	{
		m_registry.findEntitiesWith(PlayerComponent.class, PositionComponent.class).stream().forEach(result ->
		{
			PositionComponent positionComponent = result.comp2();

			Renderer.get().Camera.Position().x = positionComponent.Position.x;
		});
	}
}
