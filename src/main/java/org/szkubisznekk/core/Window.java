package org.szkubisznekk.core;

import java.util.ArrayList;
import java.util.function.Consumer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window
{
	public record Size(int Width, int Height) {}

	private static int s_windowCount = 0;

	public ArrayList<Runnable> OnClose = new ArrayList<>();
	public ArrayList<Consumer<Size>> OnResize = new ArrayList<>();

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

		glfwSetFramebufferSizeCallback(m_handle, (long handle, int width, int height) ->
		{
			for (var callback : OnResize)
			{
				callback.accept(new Size(width, height));
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