package org.szkubisznekk.world;

import org.szkubisznekk.renderer.*;

/**
 * Moves the camera to the player.
 */
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
