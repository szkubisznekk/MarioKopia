package org.szkubisznekk.world;

public class PlayerInputData
{
	public float Move = 0.0f;
	public boolean Jump = false;

	public float Speed = 5.0f;

	public boolean IsGrounded = false;

	public PlayerInputData() {}

	public PlayerInputData(float speed)
	{
		Speed = speed;
	}
}
