package org.szkubisznekk.world;

import dev.dominion.ecs.api.*;
import org.szkubisznekk.renderer.*;

public class RendererSystem extends SystemBase
{
	public RendererSystem(Dominion registry)
	{
		super(registry);
	}

	@Override
	public void update()
	{
		m_registry.findEntitiesWith(PositionComponent.class, SpriteComponent.class).stream().forEach(result ->
		{
			PositionComponent positionComponent = result.comp1();
			SpriteComponent spriteComponent = result.comp2();
			Renderer.get().submit(positionComponent.Position, spriteComponent.Depth, spriteComponent.TextureID);
		});
	}
}
