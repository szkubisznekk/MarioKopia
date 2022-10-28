package org.szkubisznekk.world;

import dev.dominion.ecs.api.*;
import org.szkubisznekk.renderer.*;

public class CameraSystem extends SystemBase
{
	public CameraSystem(Dominion registry)
	{
		super(registry);
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
