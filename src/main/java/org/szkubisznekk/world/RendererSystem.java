package org.szkubisznekk.world;

import org.szkubisznekk.renderer.*;

/**
 * Submits all objects ot the renderer.
 */
public class RendererSystem extends SystemBase
{
	@Override
	public void update(World world)
	{
		world.getEntities().findEntitiesWith(PositionComponent.class, SpriteComponent.class).stream().forEach(result ->
		{
			PositionComponent positionComponent = result.comp1();
			SpriteComponent spriteComponent = result.comp2();
			Renderer.get().submit(positionComponent.Position, spriteComponent.Depth, spriteComponent.TextureID);
		});
	}
}
