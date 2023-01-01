package org.szkubisznekk.world;

import dev.dominion.ecs.api.*;
import org.szkubisznekk.renderer.*;

public class CameraSystem extends SystemBase
{
	@Override
	public void update(World world)
	{
		world.getEntities().findEntitiesWith(PlayerComponent.class, PositionComponent.class).stream().forEach(result ->
		{
			PositionComponent positionComponent = result.comp2();

			Renderer.get().Camera.Position().x = positionComponent.Position.x;
		});
	}
}
