package org.szkubisznekk.world;

import org.joml.*;

public class VelocityComponent
{
	public Vector2f Velocity = new Vector2f(0.0f);

	public VelocityComponent() {}

	public VelocityComponent(Vector2f velocity)
	{
		Velocity = velocity;
	}
}
