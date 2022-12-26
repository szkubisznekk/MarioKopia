package org.szkubisznekk.renderer;

import static org.lwjgl.opengl.GL46C.*;

/**
 * Wrapping an OpenGL vertex array object.
 */
public class VertexArray
{
	private final int m_handle;
	private int m_indexCount;

	/**
	 * Creates a vertex array object.
	 */
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

	/**
	 * Creates a vertex array object and assigns buffers.
	 * @param vertexBuffer The vertex buffer.
	 * @param elementBuffer The element buffer.
	 * @param indexCount The number of indices.
	 */
	public VertexArray(Buffer vertexBuffer, Buffer elementBuffer, int indexCount)
	{
		this();
		assignVertexBuffer(vertexBuffer);
		assignElementBuffer(elementBuffer, indexCount);
	}

	/**
	 * Destructs the OpenGL vertex array object.
	 */
	public void destruct()
	{
		glDeleteVertexArrays(m_handle);
	}

	/**
	 * Returns the handle of the OpenGL vertex array object.
	 * @return The handle of the OpenGL vertex array object.
	 */
	public int getHandle()
	{
		return m_handle;
	}

	/**
	 * Returns the number of indices.
	 * @return The number of indices.
	 */
	public int getIndexCount()
	{
		return m_indexCount;
	}

	/**
	 * Assigns a vertex buffer to the vertex array object.
	 * @param vertexBuffer The vertex buffer.
	 */
	public void assignVertexBuffer(Buffer vertexBuffer)
	{
		glVertexArrayVertexBuffer(m_handle, 0, vertexBuffer.getHandle(), 0, 4 * 4);
	}

	/**
	 * Assigns an element buffer to the vertex array object.
	 * @param elementBuffer The element buffer.
	 */
	public void assignElementBuffer(Buffer elementBuffer, int indexCount)
	{
		m_indexCount = indexCount;
		glVertexArrayElementBuffer(m_handle, elementBuffer.getHandle());
	}

	/**
	 * Binds the vertex array object as current.
	 */
	public void bind()
	{
		glBindVertexArray(m_handle);
	}

	/**
	 * Unbinds the currently bound vertex array object.
	 */
	public static void unbind()
	{
		glBindVertexArray(0);
	}
}
