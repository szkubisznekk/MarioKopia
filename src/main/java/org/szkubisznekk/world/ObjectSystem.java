package org.szkubisznekk.world;

import org.joml.Vector2f;
import org.szkubisznekk.core.GameState;
import org.szkubisznekk.core.Utility;

import java.util.concurrent.atomic.AtomicBoolean;

public class ObjectSystem extends SystemBase
{
	@Override
	public void start(World world)
	{
		for(var worldObject : world.getWorldObjects())
		{
			world.getEntities().createEntity(
				new ObjectComponent((worldObject.ID() == 41 || worldObject.ID() == 49) ? ObjectComponent.Type.FinishLine : ObjectComponent.Type.Coin),
				new PositionComponent(worldObject.Position()),
				new SpriteComponent(-0.1f, worldObject.ID())
			);
		}
	}

	@Override
	public void update(World world)
	{
		AtomicBoolean shouldFinish = new AtomicBoolean(false);
		world.getEntities().findEntitiesWith(ObjectComponent.class, PositionComponent.class).stream().forEach(object ->
		{
			world.getEntities().findEntitiesWith(PlayerComponent.class, PositionComponent.class).stream().forEach(player ->
			{
				Vector2f objectPos = object.comp2().Position;
				Vector2f playerPos = player.comp2().Position;

				float xDelta = Math.abs(objectPos.x - playerPos.x);
				float yDelta = Math.abs(objectPos.y - playerPos.y);

				if(xDelta < 0.5f && yDelta < 0.5f)
				{
					switch(object.comp1().getType())
					{
						case Coin ->
						{
							GameState.Forints += 5;
							world.getEntities().deleteEntity(object.entity());
						}
						case FinishLine ->
						{
							if(!shouldFinish.get())
							{
								shouldFinish.set(true);
							}
						}
					}
				}
			});
		});

		if(shouldFinish.get())
		{
			Utility.callAction(world.OnFinish);
		}
	}
}
