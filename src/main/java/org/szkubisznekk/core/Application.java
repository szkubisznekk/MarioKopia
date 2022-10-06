package org.szkubisznekk.core;

import org.szkubisznekk.renderer.*;

import java.io.IOException;
import java.nio.file.Path;

import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class Application
{
	float px = 0.0f;
	float py = 0.0f;
	float dx = 0.0f;
	float dy = 0.0f;

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

		glfwSetKeyCallback(m_window.getHandle(), (long handle, int key, int scancode, int action, int mods) ->
		{
			if (action == GLFW_PRESS)
			{
				switch (key)
				{
					case GLFW_KEY_A -> dx -= 1.0f;
					case GLFW_KEY_D -> dx += 1.0f;
					case GLFW_KEY_W -> dy = (py <= 0.0001f) ? 4.0f : dy;
				}
			}
			else if (action == GLFW_RELEASE)
			{
				switch (key)
				{
					case GLFW_KEY_A -> dx += 1.0f;
					case GLFW_KEY_D -> dx -= 1.0f;
				}
			}
		});
	}

	public void destruct()
	{
		m_renderer.destruct();
		m_window.destruct();
	}

	public void run()
	{
		Time.initialize();

		Camera camera = new Camera(new Vector2f(0.0f, 0.0f), 15.0f);

		Texture texture = new Texture(Path.of("res/textures/test.png"));
		texture.bind(0);

		while (m_running)
		{
			glfwPollEvents();
			Time.update();

			px += dx * Time.getDeltaTime();
			dy -= 9.81f * Time.getDeltaTime();
			py += dy * Time.getDeltaTime();
			if (py < 0.0f)
			{
				py = 0.0f;
				dy = 0.0f;
			}

			m_renderer.beginFrame(camera);

			m_renderer.submit(new Vector2f(px, py));

			m_renderer.endFrame();
		}

		texture.destruct();
	}
}
