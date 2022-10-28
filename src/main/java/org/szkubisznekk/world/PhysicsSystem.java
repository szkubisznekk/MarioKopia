package org.szkubisznekk.world;

import dev.dominion.ecs.api.*;
import org.szkubisznekk.core.*;

public class PhysicsSystem extends SystemBase
{
	public PhysicsSystem(Dominion registry)
	{
		super(registry);
	}

	@Override
	public void update()
	{
		m_registry.findEntitiesWith(VelocityComponent.class, PositionComponent.class).stream().forEach(result ->
		{
			VelocityComponent velocityComponent = result.comp1();
			PositionComponent positionComponent = result.comp2();

			positionComponent.Position.x += velocityComponent.Velocity.x * Time.getDeltaTime();
			positionComponent.Position.y += velocityComponent.Velocity.y * Time.getDeltaTime();
		});
	}
}
