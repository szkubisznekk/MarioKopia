package org.szkubisznekk.world;

import org.joml.*;

public class EntityVelocityData
{
	public Vector2f Velocity = new Vector2f(0.0f);

	public EntityVelocityData() {}

	public EntityVelocityData(Vector2f velocity)
	{
		Velocity = velocity;
	}
}
