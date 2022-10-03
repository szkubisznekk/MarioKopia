package org.szkubisznekk.renderer;

import org.szkubisznekk.core.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.*;
import static org.lwjgl.opengl.GL46C.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Renderer
{
	private final Window m_window;

	public Renderer(Window window)
	{
		m_window = window;
		m_window.OnResize.add((Window.Size size) ->
		{
			glViewport(0, 0, size.Width(), size.Height());
		});
		glfwMakeContextCurrent(m_window.getHandle());
		createCapabilities();

		glClearColor(1.0f, 0.0f, 1.0f, 1.0f);
	}

	public void destruct()
	{

	}

	public void beginFrame()
	{
		glClear(GL_COLOR_BUFFER_BIT);
	}

	public void endFrame()
	{
		glfwSwapBuffers(m_window.getHandle());
	}

	public void submit(VertexArray vertexArray, Shader shader)
	{
		shader.bind();
		vertexArray.bind();
		glDrawElements(GL_TRIANGLES, vertexArray.getIndexCount(), GL_UNSIGNED_INT, NULL);
	}
}
