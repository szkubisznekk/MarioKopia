package org.szkubisznekk.core;

import static org.lwjgl.glfw.GLFW.*;

public class Time
{
	private static double s_start;
	private static double s_last;
	private static double s_current;

	public static void initialize()
	{
		s_start = glfwGetTime();
		s_last = s_start;
		s_current = s_start;
	}

	public static void update()
	{
		s_last = s_current;
		s_current = glfwGetTime();
	}

	public static float getTime()
	{
		return (float) (s_current - s_start);
	}

	public static float getDeltaTime()
	{
		return (float) (s_current - s_last);
	}
}
