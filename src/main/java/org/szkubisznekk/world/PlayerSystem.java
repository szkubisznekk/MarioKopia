package org.szkubisznekk.world;

import org.joml.*;
import org.szkubisznekk.audio.*;
import org.szkubisznekk.core.*;
import org.szkubisznekk.input.*;

/**
 * Manages player input and player movement.
 */
public class PlayerSystem extends SystemBase
{
	@Override
	public void start(World world)
	{
		world.getEntities().createEntity(
			new PlayerComponent(7.0f),
			new PositionComponent(new Vector2f(0.0f, 16.0f)),
			new RigidbodyComponent(new Vector2f(0.0f, 0.0f)),
			new SpriteComponent(0.0f, (byte)(57 + GameState.PlayerSkin)));

		world.getEntities().findEntitiesWith(PlayerComponent.class, RigidbodyComponent.class).stream().forEach(result ->
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
	public void update(World world)
	{
		world.getEntities().findEntitiesWith(PlayerComponent.class, RigidbodyComponent.class, PositionComponent.class).stream().forEach(result ->
		{
			PlayerComponent playerComponent = result.comp1();
			RigidbodyComponent rigidbodyComponent = result.comp2();
			PositionComponent positionComponent = result.comp3();

			if(positionComponent.Position.y < 0f)
			{
				WorldManager.get().reloadCurrent();
				AudioManager.get().play("res/audio/death.ogg", 1.0f, false);
			}

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
