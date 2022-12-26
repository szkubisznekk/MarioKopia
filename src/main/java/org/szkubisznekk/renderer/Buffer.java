package org.szkubisznekk.renderer;

import static org.lwjgl.opengl.GL46C.*;

/**
 * Wrapping an OpenGL named buffer.
 */
public class Buffer
{
	/**
	 * Usage hint for a buffer's data.
	 */
	public enum Usage
	{
		StreamDraw(35040),
		StreamRead(35041),
		StreamCopy(35042),
		StaticDraw(35044),
		StaticRead(35045),
		StaticCopy(35046),
		DynamicDraw(35048),
		DynamicRead(35049),
		DynamicCopy(35050);

		private final int m_value;

		Usage(int value)
		{
			m_value = value;
		}

		public int getValue()
		{
			return m_value;
		}
	}

	private final int m_handle;

	/**
	 * Creates a buffer.
	 */
	public Buffer()
	{
		m_handle = glCreateBuffers();
	}

	/**
	 * Creates a buffer and initializes it.
	 *
	 * @param size  The size of the buffer in bytes.
	 * @param usage The usage hint.
	 */
	public Buffer(long size, Usage usage)
	{
		this();
		setData(size, usage);
	}

	/**
	 * Creates a buffer, initializes it and sets its data.
	 *
	 * @param data  The data.
	 * @param usage The usage hint.
	 */
	public Buffer(short[] data, Usage usage)
	{
		this();
		setData(data, usage);
	}

	/**
	 * Creates a buffer, initializes it and sets its data.
	 *
	 * @param data  The data.
	 * @param usage The usage hint.
	 */
	public Buffer(int[] data, Usage usage)
	{
		this();
		setData(data, usage);
	}

	/**
	 * Creates a buffer, initializes it and sets its data.
	 *
	 * @param data  The data.
	 * @param usage The usage hint.
	 */
	public Buffer(long[] data, Usage usage)
	{
		this();
		setData(data, usage);
	}

	/**
	 * Creates a buffer, initializes it and sets its data.
	 *
	 * @param data  The data.
	 * @param usage The usage hint.
	 */
	public Buffer(float[] data, Usage usage)
	{
		this();
		setData(data, usage);
	}

	/**
	 * Creates a buffer, initializes it and sets its data.
	 *
	 * @param data  The data.
	 * @param usage The usage hint.
	 */
	public Buffer(double[] data, Usage usage)
	{
		this();
		setData(data, usage);
	}

	/**
	 * Destruct the OpenGL buffer.
	 */
	public void destruct()
	{
		glDeleteBuffers(m_handle);
	}

	/**
	 * Returns the handle of the OpenGL buffer.
	 *
	 * @return The handle of the OpenGL buffer.
	 */
	public int getHandle()
	{
		return m_handle;
	}

	/**
	 * Reinitialize the buffer.
	 *
	 * @param size  The new size of the buffer in bytes.
	 * @param usage The usage hint.
	 */
	public void setData(long size, Usage usage)
	{
		glNamedBufferData(m_handle, size, usage.getValue());
	}

	/**
	 * Reinitialize the buffer and set its data.
	 *
	 * @param data  The new data.
	 * @param usage The usage hint.
	 */
	public void setData(short[] data, Usage usage)
	{
		glNamedBufferData(m_handle, data, usage.getValue());
	}

	/**
	 * Reinitialize the buffer and set its data.
	 *
	 * @param data  The new data.
	 * @param usage The usage hint.
	 */
	public void setData(int[] data, Usage usage)
	{
		glNamedBufferData(m_handle, data, usage.getValue());
	}

	/**
	 * Reinitialize the buffer and set its data.
	 *
	 * @param data  The new data.
	 * @param usage The usage hint.
	 */
	public void setData(long[] data, Usage usage)
	{
		glNamedBufferData(m_handle, data, usage.getValue());
	}

	/**
	 * Reinitialize the buffer and set its data.
	 *
	 * @param data  The new data.
	 * @param usage The usage hint.
	 */
	public void setData(float[] data, Usage usage)
	{
		glNamedBufferData(m_handle, data, usage.getValue());
	}

	/**
	 * Reinitialize the buffer and set its data.
	 *
	 * @param data  The new data.
	 * @param usage The usage hint.
	 */
	public void setData(double[] data, Usage usage)
	{
		glNamedBufferData(m_handle, data, usage.getValue());
	}

	/**
	 * Set a part of the buffer's data without reinitializing it.
	 *
	 * @param offset The offset of the data in bytes.
	 * @param data   The new data.
	 */
	public void setSubData(long offset, short[] data)
	{
		glNamedBufferSubData(m_handle, offset, data);
	}

	/**
	 * Set a part of the buffer's data without reinitializing it.
	 *
	 * @param offset The offset of the data in bytes.
	 * @param data   The new data.
	 */
	public void setSubData(long offset, int[] data)
	{
		glNamedBufferSubData(m_handle, offset, data);
	}

	/**
	 * Set a part of the buffer's data without reinitializing it.
	 *
	 * @param offset The offset of the data in bytes.
	 * @param data   The new data.
	 */
	public void setSubData(long offset, long[] data)
	{
		glNamedBufferSubData(m_handle, offset, data);
	}

	/**
	 * Set a part of the buffer's data without reinitializing it.
	 *
	 * @param offset The offset of the data in bytes.
	 * @param data   The new data.
	 */
	public void setSubData(long offset, float[] data)
	{
		glNamedBufferSubData(m_handle, offset, data);
	}

	/**
	 * Set a part of the buffer's data without reinitializing it.
	 *
	 * @param offset The offset of the data in bytes.
	 * @param data   The new data.
	 */
	public void setSubData(long offset, double[] data)
	{
		glNamedBufferSubData(m_handle, offset, data);
	}
}
