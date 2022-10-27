package org.szkubisznekk.world;

import org.joml.*;

public class EntityPositionData
{
	public Vector2f Position = new Vector2f(0.0f);

	public EntityPositionData() {}

	public EntityPositionData(Vector2f position)
	{
		Position = position;
	}
}