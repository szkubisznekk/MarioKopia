package org.szkubisznekk.core;

import org.szkubisznekk.renderer.*;

import java.io.IOException;
import java.nio.file.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46C.*;

public class Application
{
	private boolean m_running = true;
	private final Window m_window;
	private final Renderer m_renderer;

	public Application()
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

	public void run() throws IOException
	{
		Shader shader = new Shader(Path.of("res/shaders/Standard"));

		float[] vertices = {
			-0.5f, -0.5f, 0.0f, 0.0f,
			0.5f, -0.5f, 1.0f, 0.0f,
			-0.5f, 0.5f, 0.0f, 1.0f,
			0.5f, 0.5f, 1.0f, 1.0f
		};

		int[] indices = {
			0, 1, 2,
			1, 3, 2
		};

		Buffer vertexBuffer = new Buffer(vertices, GL_STATIC_DRAW);
		Buffer elementBuffer = new Buffer(indices, GL_STATIC_DRAW);
		VertexArray vertexArray = new VertexArray(vertexBuffer, elementBuffer, indices.length);
		while (m_running)
		{
			glfwPollEvents();

			m_renderer.beginFrame();

			m_renderer.submit(vertexArray, shader);

			m_renderer.endFrame();
		}

		shader.destruct();
		vertexBuffer.destruct();
		elementBuffer.destruct();
		vertexArray.destruct();
	}
}
