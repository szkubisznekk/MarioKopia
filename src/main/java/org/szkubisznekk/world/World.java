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
	private static World s_current;

	public static final int HEIGHT = 16;
	public static final int WIDTH = 256;

	private final byte[] m_blocks = new byte[WIDTH * HEIGHT];
	private final Dominion m_ecs = Dominion.create();
	private final ArrayList<Runnable> m_systems = new ArrayList<>();
	PositionComponent m_playerPositionData;

	public World()
	{
		s_current = this;

		for (int y = 0; y < HEIGHT; y++)
		{
			for (int x = 0; x < WIDTH; x++)
			{
				m_blocks[getIndex(x, y)] = (y == 0) ? Blocks.Brick : Blocks.Air;
			}
		}

		m_playerPositionData = new PositionComponent(new Vector2f(0.0f, 5.0f));
		m_ecs.createEntity(new PlayerComponent(), m_playerPositionData, new VelocityComponent(new Vector2f(0.0f, 0.0f)));

		m_ecs.findEntitiesWith(PlayerComponent.class).stream().forEach(result ->
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

		m_systems.add(() ->
		{
			m_ecs.findEntitiesWith(PlayerComponent.class, VelocityComponent.class, PositionComponent.class).stream().forEach(result ->
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
		});

		m_systems.add(() ->
		{
			m_ecs.findEntitiesWith(VelocityComponent.class, PositionComponent.class).stream().forEach(result ->
			{
				VelocityComponent entityVelocityData = result.comp1();
				PositionComponent positionComponent = result.comp2();

				positionComponent.Position.x += entityVelocityData.Velocity.x * Time.getDeltaTime();
				positionComponent.Position.y += entityVelocityData.Velocity.y * Time.getDeltaTime();

				Renderer.get().submit(positionComponent.Position, 0.0f, 1);
			});
		});

		m_systems.add(() ->
		{
			m_ecs.findEntitiesWith(PositionComponent.class).stream().forEach(result ->
			{
				PositionComponent positionComponent = result.comp();
				Renderer.get().submit(positionComponent.Position, 0.0f, 1);
			});
		});
	}

	public static World get()
	{
		return s_current;
	}

	public void update()
	{
		for (var system : m_systems)
		{
			system.run();
		}
		Renderer.get().Camera.Position().x = m_playerPositionData.Position.x;

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
