package org.szkubisznekk.world;

import org.joml.*;

public class PositionComponent
{
	public Vector2f Position = new Vector2f(0.0f);

	public PositionComponent() {}

	public PositionComponent(Vector2f position)
	{
		Position = position;
	}
}