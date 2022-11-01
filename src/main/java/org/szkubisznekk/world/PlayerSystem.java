package org.szkubisznekk.world;

import dev.dominion.ecs.api.*;
import org.joml.*;
import org.szkubisznekk.core.*;
import org.szkubisznekk.input.*;

public class PlayerSystem extends SystemBase
{
	public PlayerSystem(Dominion registry)
	{
		super(registry);
	}

	@Override
	public void start()
	{
		m_registry.createEntity(
			new PlayerComponent(),
			new PositionComponent(new Vector2f(0.0f, 16.0f)),
			new RigidbodyComponent(),
			new SpriteComponent(0.0f, (byte)1));

		m_registry.findEntitiesWith(PlayerComponent.class, RigidbodyComponent.class).stream().forEach(result ->
		{
			PlayerComponent playerComponent = result.comp1();
			RigidbodyComponent rigidbodyComponent = result.comp2();

			Controls.OnMove.add((Float value) ->
			{
				playerComponent.Move = value;
			});

			Controls.OnJump.add(() ->
			{
				if(rigidbodyComponent.IsGrounded)
				{
					playerComponent.Jump = true;
				}
			});
		});
	}

	@Override
	public void update()
	{
		m_registry.findEntitiesWith(PlayerComponent.class, RigidbodyComponent.class).stream().forEach(result ->
		{
			PlayerComponent playerComponent = result.comp1();
			RigidbodyComponent rigidbodyComponent = result.comp2();

			rigidbodyComponent.Velocity.x = playerComponent.Move * playerComponent.Speed;

			if(rigidbodyComponent.IsGrounded)
			{
				if(playerComponent.Jump)
				{
					rigidbodyComponent.Velocity.y = 18.0f;
					playerComponent.Jump = false;
				}
			}
			else
			{
				rigidbodyComponent.Velocity.y -= 40.0f * Time.getDeltaTime();
			}
		});
	}
}
