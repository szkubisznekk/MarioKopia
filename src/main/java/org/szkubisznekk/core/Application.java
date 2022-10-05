package org.szkubisznekk.core;

import org.szkubisznekk.renderer.*;

import java.io.IOException;

import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class Application
{
	private boolean m_running = true;
	private final Window m_window;
	private final Renderer m_renderer;

	public Application() throws IOException
	{
		m_window = new Window();
		m_window.OnClose.add(() ->
		{
			m_running = false;
		});

		m_renderer = new Renderer(m_window);
	}

	public void destruct()
	{
		m_renderer.destruct();
		m_window.destruct();
	}

	public void run()
	{
		Time.initialize();

		while (m_running)
		{
			glfwPollEvents();
			Time.update();

			m_renderer.beginFrame();

			m_renderer.submit(new Vector2f(0.0f, 0.0f));
			m_renderer.submit(new Vector2f(0.0f, 0.0f));

			m_renderer.endFrame();
		}
	}
}
