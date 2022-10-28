package org.szkubisznekk.world;

import dev.dominion.ecs.api.*;
import org.joml.*;
import org.szkubisznekk.core.*;
import org.szkubisznekk.input.*;

import java.lang.Math;

public class PlayerSystem extends SystemBase
{
	public PlayerSystem(Dominion registry)
	{
		super(registry);
	}

	@Override
	public void start()
	{
		m_registry.createEntity(new PlayerComponent(),
			new PositionComponent(new Vector2f(0.0f, 5.0f)),
			new VelocityComponent(new Vector2f(0.0f, 0.0f)),
			new SpriteComponent(0.0f, (byte)1));

		m_registry.findEntitiesWith(PlayerComponent.class).stream().forEach(result ->
		{
			PlayerComponent playerComponent = result.comp();

			Controls.OnMove.add((Float value) ->
			{
				playerComponent.Move = value;
			});

			Controls.OnJump.add(() ->
			{
				if (playerComponent.IsGrounded)
				{
					playerComponent.Jump = true;
				}
			});
		});
	}

	@Override
	public void update()
	{
		m_registry.findEntitiesWith(PlayerComponent.class, VelocityComponent.class, PositionComponent.class).stream().forEach(result ->
		{
			PlayerComponent playerComponent = result.comp1();
			VelocityComponent entityVelocityData = result.comp2();
			PositionComponent positionComponent = result.comp3();

			entityVelocityData.Velocity.x = playerComponent.Move * playerComponent.Speed;

			playerComponent.IsGrounded = positionComponent.Position.y <= 1.0f;
			if (playerComponent.IsGrounded)
			{
				positionComponent.Position.y = (float)Math.ceil(positionComponent.Position.y);
				entityVelocityData.Velocity.y = 0.0f;

				if (playerComponent.Jump)
				{
					entityVelocityData.Velocity.y = 10.0f;
					playerComponent.Jump = false;
				}
			}
			else
			{
				entityVelocityData.Velocity.y -= 20.0f * Time.getDeltaTime();
			}
		});
	}
}
