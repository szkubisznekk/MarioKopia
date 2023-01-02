package org.szkubisznekk.world;

import dev.dominion.ecs.api.*;
import org.szkubisznekk.core.*;

public class FinishSystem extends SystemBase
{
	@Override
	public void update(World world)
	{
		world.getEntities().findEntitiesWith(PlayerComponent.class, PositionComponent.class).stream().forEach(result ->
		{
			PlayerComponent playerComponent = result.comp1();
			PositionComponent positionComponent = result.comp2();

			if(positionComponent.Position.x >= 256)
			{
				Utility.callAction(world.OnFinish);
			}
		});
	}
}
