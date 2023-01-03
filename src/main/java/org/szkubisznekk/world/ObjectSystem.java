package org.szkubisznekk.world;

import org.szkubisznekk.audio.*;
import org.szkubisznekk.core.*;
import org.szkubisznekk.renderer.*;

import org.joml.Vector2f;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Manages collection of objects.
 */
public class ObjectSystem extends SystemBase
{
	private int m_forintsCollectedOnCurrentMap = 0;
	private World m_lastWorld = null;

	@Override
	public void start(World world)
	{
		if(m_lastWorld != world)
		{
			GameState.Forints += m_forintsCollectedOnCurrentMap;
			m_lastWorld = world;
		}
		m_forintsCollectedOnCurrentMap = 0;
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
							m_forintsCollectedOnCurrentMap += 5;
							AudioManager.get().play("res/audio/coin_collect.ogg", 1.0f, false);
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
			Action.callAction(world.OnFinish);
		}

		Renderer.get().submitTextRelative(
			new Vector2f(-0.95f, 0.95f),
			"FORINTS: " + (GameState.Forints + m_forintsCollectedOnCurrentMap),
			Renderer.HorizontalAlign.Left,
			Renderer.VerticalAlign.Top);
	}
}
