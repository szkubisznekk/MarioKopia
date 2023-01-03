package org.szkubisznekk.world;

import org.joml.*;

/**
 * Physics component of an object.
 */
public class RigidbodyComponent
{
	/**
	 * The velocity of the object.
	 */
	public Vector2f Velocity;

	/**
	 * The current state of the object based if it is standing on something or not.
	 */
	public boolean IsGrounded = false;

	/**
	 * Sets the initial velocity.
	 *
	 * @param velocity The initial velocity.
	 */
	public RigidbodyComponent(Vector2f velocity)
	{
		Velocity = velocity;
	}
}
