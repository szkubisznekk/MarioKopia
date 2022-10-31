package org.szkubisznekk.world;

public class PlayerComponent
{
	public float Move = 0.0f;
	public boolean Jump = false;
	public float Speed = 7.0f;

	public PlayerComponent() {}

	public PlayerComponent(float speed)
	{
		Speed = speed;
	}
}
