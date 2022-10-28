package org.szkubisznekk.world;

public class PlayerComponent
{
	public float Move = 0.0f;
	public boolean Jump = false;

	public float Speed = 5.0f;

	public boolean IsGrounded = false;

	public PlayerComponent() {}

	public PlayerComponent(float speed)
	{
		Speed = speed;
	}
}
