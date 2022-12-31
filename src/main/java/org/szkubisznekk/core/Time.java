package org.szkubisznekk.core;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Keeps track of the time.
 */
public class Time
{
	private static double s_start;
	private static double s_last;
	private static double s_current;

	/**
	 * Starts the clock.
	 */
	public static void init()
	{
		s_start = glfwGetTime();
		s_last = s_start;
		s_current = s_start;
	}

	/**
	 * Ticks the clock. Updates the last ticked time.
	 */
	public static void update()
	{
		s_last = s_current;
		s_current = glfwGetTime();
	}

	/**
	 * Returns the time in seconds since the start of the clock and the last ticked time.
	 *
	 * @return The time in seconds since the start of the clock and the last ticked time.
	 */
	public static float getTime()
	{
		return (float)(s_current - s_start);
	}

	/**
	 * Returns the time in seconds between the last ticked time and the one ticked before that.
	 *
	 * @return The time in seconds between the last ticked time and the one ticked before that.
	 */
	public static float getDeltaTime()
	{
		return (float)(s_current - s_last);
	}
}
