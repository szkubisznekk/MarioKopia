package org.szkubisznekk.world;

import dev.dominion.ecs.api.*;
import org.szkubisznekk.core.*;

public class PhysicsSystem extends SystemBase
{
	private static final float Epsilon = 0.01f;
	private static final float OnePlusEpsilon = 1.0f + Epsilon;
	private static final float TwoEpsilon = 2.0f * Epsilon;

	private final Tilemap m_tilemap;

	public PhysicsSystem(Dominion registry, Tilemap tilemap)
	{
		super(registry);
		m_tilemap = tilemap;
	}

	@Override
	public void update()
	{
		m_registry.findEntitiesWith(RigidbodyComponent.class, PositionComponent.class).stream().forEach(result ->
		{
			RigidbodyComponent rigidbodyComponent = result.comp1();
			PositionComponent positionComponent = result.comp2();

			// Bottom
			int b = (int)Math.ceil(positionComponent.Position.y - OnePlusEpsilon);
			int bl = (int)Math.floor(positionComponent.Position.x + TwoEpsilon);
			int br = (int)Math.ceil(positionComponent.Position.x - TwoEpsilon);
			boolean hasBottom = m_tilemap.getTile(bl, b) != 0 || m_tilemap.getTile(br, b) != 0;
			rigidbodyComponent.IsGrounded = hasBottom;
			if(hasBottom && rigidbodyComponent.Velocity.y < 0.0f)
			{
				rigidbodyComponent.Velocity.y = 0.0f;
			}

			// Top
			int t = (int)Math.floor(positionComponent.Position.y + OnePlusEpsilon);
			boolean hasTop = m_tilemap.getTile(bl, t) != 0 || m_tilemap.getTile(br, t) != 0;
			if(hasTop && rigidbodyComponent.Velocity.y > 0.0f)
			{
				rigidbodyComponent.Velocity.y = 0.0f;
			}

			// Left
			int l = (int)Math.ceil(positionComponent.Position.x - OnePlusEpsilon);
			int lb = (int)Math.floor(positionComponent.Position.y + TwoEpsilon);
			int lt = (int)Math.ceil(positionComponent.Position.y - TwoEpsilon);
			boolean hasLeft = m_tilemap.getTile(l, lb) != 0 || m_tilemap.getTile(l, lt) != 0;
			if(hasLeft && rigidbodyComponent.Velocity.x < 0.0f)
			{
				rigidbodyComponent.Velocity.x = 0.0f;
			}

			// Right
			int r = (int)Math.floor(positionComponent.Position.x + OnePlusEpsilon);
			boolean hasRight = m_tilemap.getTile(r, lb) != 0 || m_tilemap.getTile(r, lt) != 0;
			if(hasRight && rigidbodyComponent.Velocity.x > 0.0f)
			{
				rigidbodyComponent.Velocity.x = 0.0f;
			}

			positionComponent.Position.x += rigidbodyComponent.Velocity.x * Time.getDeltaTime();
			positionComponent.Position.y += rigidbodyComponent.Velocity.y * Time.getDeltaTime();
		});
	}
}
