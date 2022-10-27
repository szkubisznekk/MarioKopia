package org.szkubisznekk.world;

import org.szkubisznekk.core.Time;
import org.szkubisznekk.input.*;
import org.szkubisznekk.renderer.*;

import org.joml.*;
import dev.dominion.ecs.api.*;

import java.lang.Math;
import java.util.ArrayList;

public class World
{
	public static final int HEIGHT = 16;
	public static final int WIDTH = 256;

	private final byte[] m_blocks = new byte[WIDTH * HEIGHT];
	private final Dominion m_ecs = Dominion.create();
	private final ArrayList<Runnable> m_systems = new ArrayList<>();

	public World()
	{
		for (int y = 0; y < HEIGHT; y++)
		{
			for (int x = 0; x < WIDTH; x++)
			{
				m_blocks[getIndex(x, y)] = (y == 0) ? Blocks.Brick : Blocks.Air;
			}
		}

		m_ecs.createEntity(new PlayerInputData(), new EntityPositionData(new Vector2f(0.0f, 5.0f)), new EntityVelocityData(new Vector2f(0.0f, 0.0f)));

		m_ecs.findEntitiesWith(PlayerInputData.class).stream().forEach(result ->
		{
			PlayerInputData playerInputData = result.comp();

			Controls.OnMove.add((Float value) ->
			{
				playerInputData.Move = value;
			});

			Controls.OnJump.add(() ->
			{
				if (playerInputData.IsGrounded)
				{
					playerInputData.Jump = true;
				}
			});
		});

		m_systems.add(() ->
		{
			m_ecs.findEntitiesWith(PlayerInputData.class, EntityVelocityData.class, EntityPositionData.class).stream().forEach(result ->
			{
				PlayerInputData playerInputData = result.comp1();
				EntityVelocityData entityVelocityData = result.comp2();
				EntityPositionData entityPositionData = result.comp3();

				entityVelocityData.Velocity.x = playerInputData.Move * playerInputData.Speed;

				playerInputData.IsGrounded = entityPositionData.Position.y <= 1.0f;
				if (playerInputData.IsGrounded)
				{
					entityPositionData.Position.y = (float)Math.ceil(entityPositionData.Position.y);
					entityVelocityData.Velocity.y = 0.0f;

					if (playerInputData.Jump)
					{
						entityVelocityData.Velocity.y = 10.0f;
						playerInputData.Jump = false;
					}
				}
				else
				{
					entityVelocityData.Velocity.y -= 20.0f * Time.getDeltaTime();
				}
			});
		});

		m_systems.add(() ->
		{
			m_ecs.findEntitiesWith(EntityVelocityData.class, EntityPositionData.class).stream().forEach(result ->
			{
				EntityVelocityData entityVelocityData = result.comp1();
				EntityPositionData entityPositionData = result.comp2();

				entityPositionData.Position.x += entityVelocityData.Velocity.x * Time.getDeltaTime();
				entityPositionData.Position.y += entityVelocityData.Velocity.y * Time.getDeltaTime();

				Renderer.get().submit(entityPositionData.Position, 0.0f, 1);
			});
		});

		m_systems.add(() ->
		{
			m_ecs.findEntitiesWith(EntityPositionData.class).stream().forEach(result ->
			{
				EntityPositionData entityPositionData = result.comp();
				Renderer.get().submit(entityPositionData.Position, 0.0f, 1);
			});
		});
	}

	public void update()
	{
		for (var system : m_systems)
		{
			system.run();
		}

		submit();
	}

	private void submit()
	{
		for (int y = 0; y < HEIGHT; y++)
		{
			for (int x = 0; x < WIDTH; x++)
			{
				Renderer.get().submit(new Vector2f((float)x, (float)y), -1.0f, m_blocks[getIndex(x, y)]);
			}
		}
	}

	private static int getIndex(int x, int y)
	{
		return y * WIDTH + x;
	}
}
