package org.szkubisznekk.world;

import dev.dominion.ecs.api.*;
import org.szkubisznekk.renderer.*;

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
