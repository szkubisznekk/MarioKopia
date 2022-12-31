package org.szkubisznekk.core;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * Wrapping an GLFW window.
 */
public class Window
{
	/**
	 * Window size.
	 *
	 * @param Width  Width of the window in number of pixels.
	 * @param Height Height of the window in number of pixels.
	 */
	public record Size(int Width, int Height) {}

	private static int s_windowCount = 0;

	/**
	 * Called when the close button is pressed on thw window.
	 */
	public ArrayList<Runnable> OnClose = new ArrayList<>();

	/**
	 * Called when the window is resized.
	 */
	public ArrayList<Consumer<Size>> OnResize = new ArrayList<>();

	private final long m_handle;
	private Size m_size;

	/**
	 * Creates a window and sets callbacks.
	 */
	public Window()
	{
		m_size = new Size(800, 600);

		if(s_windowCount++ == 0)
		{
			glfwInit();
		}

		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

		m_handle = glfwCreateWindow(800, 600, "Window", NULL, NULL);

		glfwSetWindowCloseCallback(m_handle, (long handle) -> Utility.callAction(OnClose));

		glfwSetFramebufferSizeCallback(m_handle, (long handle, int width, int height) ->
		{
			m_size = new Size(width, height);
			Utility.callAction(OnResize, m_size);
		});
	}

	/**
	 * Destruct thw window.
	 */
	public void destruct()
	{
		glfwDestroyWindow(m_handle);

		if(--s_windowCount == 0)
		{
			glfwTerminate();
		}
	}

	/**
	 * Returns the handle of the GLFW window.
	 *
	 * @return The handle of the GLFW window.
	 */
	public long getHandle()
	{
		return m_handle;
	}

	/**
	 * Returns the size of the window.
	 *
	 * @return The size of the window.
	 */
	public Size getSize()
	{
		return m_size;
	}
}
