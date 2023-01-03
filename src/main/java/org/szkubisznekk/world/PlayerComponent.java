package org.szkubisznekk.world;

/**
 * Stores player data.
 */
public class PlayerComponent
{
	/**
	 * Current input value of the side movement.
	 */
	public float Move = 0.0f;

	/**
	 * Current state fo the jump button.
	 */
	public boolean Jump = false;

	/**
	 * The movement speed of the player.
	 */
	public float Speed;

	/**
	 * Sets the movement speed.
	 *
	 * @param speed The movement speed.
	 */
	public PlayerComponent(float speed)
	{
		Speed = speed;
	}
}
