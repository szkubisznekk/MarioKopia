package org.szkubisznekk.world;

import org.joml.*;

/**
 * Stores the position of an object.
 */
public class PositionComponent
{
	/**
	 * The position.
	 */
	public Vector2f Position;

	/**
	 * Sets the position.
	 *
	 * @param position The position.
	 */
	public PositionComponent(Vector2f position)
	{
		Position = position;
	}
}