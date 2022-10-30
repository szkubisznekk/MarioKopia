package org.szkubisznekk.world;

import org.joml.*;

public class RigidbodyComponent
{
	public Vector2f Velocity = new Vector2f(0.0f);
	public boolean IsGrounded = false;

	public RigidbodyComponent() {}

	public RigidbodyComponent(Vector2f velocity, boolean isGrounded)
	{
		Velocity = velocity;
		IsGrounded = isGrounded;
	}
}
