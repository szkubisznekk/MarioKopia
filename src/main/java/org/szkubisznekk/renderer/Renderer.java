package org.szkubisznekk.renderer;

import org.szkubisznekk.core.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import org.joml.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.*;
import static org.lwjgl.opengl.GL46C.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Renderer
{
	private static final float[] s_spriteVertices = {
		-0.5f, -0.5f, 0.0f, 0.0f,
		0.5f, -0.5f, 1.0f, 0.0f,
		-0.5f, 0.5f, 0.0f, 1.0f,
		0.5f, 0.5f, 1.0f, 1.0f
	};

	private static final int[] s_spriteIndices = {
		0, 1, 2,
		1, 3, 2
	};

	public Shader SpriteShader;

	private final Window m_window;
	private final ArrayList<Vector2f> m_instanceTransforms = new ArrayList<>();
	private final Buffer m_instanceBuffer;
	private final VertexArray m_spriteMesh;

	public Renderer(Window window) throws IOException
	{
		m_window = window;
		m_window.OnResize.add((Window.Size size) ->
		{
			glViewport(0, 0, size.Width(), size.Height());
		});
		glfwMakeContextCurrent(m_window.getHandle());
		createCapabilities();

		glClearColor(1.0f, 0.0f, 1.0f, 1.0f);

		SpriteShader = new Shader(Path.of("res/shaders/Sprite"));
		m_spriteMesh = new VertexArray(
			new Buffer(s_spriteVertices, Buffer.Usage.StaticDraw),
			new Buffer(s_spriteIndices, Buffer.Usage.StaticDraw),
			s_spriteIndices.length);

		m_instanceBuffer = new Buffer(0, Buffer.Usage.StaticDraw);
		glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 0, m_instanceBuffer.getHandle());
	}

	public void destruct()
	{
		m_spriteMesh.destruct();
		SpriteShader.destruct();
		m_instanceBuffer.destruct();
	}

	public void beginFrame()
	{
		glClear(GL_COLOR_BUFFER_BIT);
		m_instanceTransforms.clear();
	}

	public void endFrame()
	{
		float[] transformData = new float[m_instanceTransforms.size() * 2];
		for (int i = 0; i < m_instanceTransforms.size(); i++)
		{
			int offset = i * 2;
			transformData[offset] = m_instanceTransforms.get(i).x;
			transformData[offset + 1] = m_instanceTransforms.get(i).y;
		}

		m_instanceBuffer.setData(transformData, Buffer.Usage.StaticDraw);
		SpriteShader.bind();
		m_spriteMesh.bind();
		glDrawElementsInstanced(GL_TRIANGLES, m_spriteMesh.getIndexCount(), GL_UNSIGNED_INT, NULL, m_instanceTransforms.size());

		glfwSwapBuffers(m_window.getHandle());
	}

	public void submit(Vector2f position)
	{
		m_instanceTransforms.add(position);
	}
}
