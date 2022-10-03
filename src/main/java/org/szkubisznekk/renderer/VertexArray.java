package org.szkubisznekk.renderer;

import static org.lwjgl.opengl.GL46C.*;

public class VertexArray
{
	private final int m_handle;
	private int m_indexCount;

	public VertexArray()
	{
		m_indexCount = 0;
		m_handle = glGenVertexArrays();

		glBindVertexArray(m_handle);

		glVertexArrayAttribFormat(m_handle, 0, 2, GL_FLOAT, false, 0);
		glVertexArrayAttribBinding(m_handle, 0, 0);
		glEnableVertexArrayAttrib(m_handle, 0);

		glVertexArrayAttribFormat(m_handle, 1, 2, GL_FLOAT, false, 2 * 4);
		glVertexArrayAttribBinding(m_handle, 1, 0);
		glEnableVertexArrayAttrib(m_handle, 1);

		glBindVertexArray(0);
	}

	public VertexArray(Buffer vertexBuffer, Buffer elementBuffer, int indexCount)
	{
		this();
		assignVertexBuffer(vertexBuffer);
		assignElementBuffer(elementBuffer, indexCount);
	}

	public void destruct()
	{
		glDeleteVertexArrays(m_handle);
	}

	public int getHandle()
	{
		return m_handle;
	}

	public int getIndexCount()
	{
		return m_indexCount;
	}

	public void assignVertexBuffer(Buffer vertexBuffer)
	{
		glVertexArrayVertexBuffer(m_handle, 0, vertexBuffer.getHandle(), 0, 4 * 4);
	}

	public void assignElementBuffer(Buffer elementBuffer, int indexCount)
	{
		m_indexCount = indexCount;
		glVertexArrayElementBuffer(m_handle, elementBuffer.getHandle());
	}

	public void bind()
	{
		glBindVertexArray(m_handle);
	}

	public static void unbind()
	{
		glBindVertexArray(0);
	}
}
