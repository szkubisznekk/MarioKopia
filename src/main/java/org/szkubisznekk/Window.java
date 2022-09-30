package org.szkubisznekk;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window
{
	private static int s_windowCount = 0;

	public ArrayList<Runnable> OnClose = new ArrayList<>();

	private final long m_handle;

	public Window()
	{
		if (s_windowCount++ == 0)
		{
			glfwInit();
		}

		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

		m_handle = glfwCreateWindow(800, 600, "Window", NULL, NULL);

		glfwSetWindowCloseCallback(m_handle, (long handle) ->
		{
			for (var callback : OnClose)
			{
				callback.run();
			}
		});
	}

	public void destruct()
	{
		glfwDestroyWindow(m_handle);

		if (--s_windowCount == 0)
		{
			glfwTerminate();
		}
	}

	public long getHandle()
	{
		return m_handle;
	}
}
