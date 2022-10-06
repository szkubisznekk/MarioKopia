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

	private static final Vector3f s_up = new Vector3f(0.0f, 1.0f, 0.0f);

	public Shader SpriteShader;

	private final Window m_window;
	private final VertexArray m_spriteMesh;

	private final ArrayList<Vector2f> m_instanceTransforms = new ArrayList<>();
	private final Buffer m_instanceBuffer;

	private float m_aspectRatio = 1.0f;
	private final float[] m_matrixData = new float[32];
	private final Buffer m_projectionBuffer;

	public Renderer(Window window) throws IOException
	{
		m_window = window;
		m_window.OnResize.add((Window.Size size) ->
		{
			m_aspectRatio = (float)size.Width() / size.Height();
			glViewport(0, 0, size.Width(), size.Height());
		});
		Window.Size windowSize = m_window.getSize();
		m_aspectRatio = (float)windowSize.Width() / windowSize.Height();

		glfwMakeContextCurrent(m_window.getHandle());
		createCapabilities();

		glClearColor(1.0f, 0.0f, 1.0f, 1.0f);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_BLEND);

		SpriteShader = new Shader(Path.of("res/shaders/Sprite"));
		m_spriteMesh = new VertexArray(
			new Buffer(s_spriteVertices, Buffer.Usage.StaticDraw),
			new Buffer(s_spriteIndices, Buffer.Usage.StaticDraw),
			s_spriteIndices.length);

		m_instanceBuffer = new Buffer(0, Buffer.Usage.DynamicDraw);
		glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 1, m_instanceBuffer.getHandle());

		m_projectionBuffer = new Buffer(32 * 4, Buffer.Usage.DynamicDraw);
		glBindBufferBase(GL_UNIFORM_BUFFER, 0, m_projectionBuffer.getHandle());
	}

	public void destruct()
	{
		m_spriteMesh.destruct();
		SpriteShader.destruct();
		m_instanceBuffer.destruct();
		m_projectionBuffer.destruct();
	}

	public void beginFrame(Camera camera)
	{
		glClear(GL_COLOR_BUFFER_BIT);
		m_instanceTransforms.clear();

		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.translate(-camera.Position().x(), -camera.Position().y, -1.0f);

		Matrix4f projectionMatrix = new Matrix4f();
		float halfHeight = camera.Size() * 0.5f;
		float halfWidth = halfHeight * m_aspectRatio;
		projectionMatrix.ortho(-halfWidth, halfWidth, -halfHeight, halfHeight, 0.1f, 10.0f);

		viewMatrix.get(m_matrixData);
		projectionMatrix.get(m_matrixData, 16);
		m_projectionBuffer.setSubData(0, m_matrixData);
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
