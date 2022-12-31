package org.szkubisznekk.world;

/**
 * Provides a base class for systems that run functions on a world.
 */
public class SystemBase
{
	/**
	 * Runs once when the world is loaded.
	 *
	 * @param world The world.
	 */
	public void start(World world) {}

	/**
	 * Runs every frame.
	 *
	 * @param world The world.
	 */
	public void update(World world) {}

	/**
	 * Runs once the world has been unloaded.
	 *
	 * @param world The world.
	 */
	public void stop(World world) {}
}
