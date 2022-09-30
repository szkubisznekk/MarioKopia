package org.szkubisznekk;

import static org.lwjgl.glfw.GLFW.*;

public class Application
{
	private boolean m_running = true;
	private final Window m_window;

	public Application()
	{
		m_window = new Window();
		m_window.OnClose.add(() ->
		{
			m_running = false;
		});
	}

	public void destruct()
	{
		m_window.destruct();
	}

	public void run()
	{
		while (m_running)
		{
			glfwPollEvents();

			glfwSwapBuffers(m_window.getHandle());
		}
	}
}
