package org.szkubisznekk.renderer;

import static org.lwjgl.opengl.GL46C.*;

public class Buffer
{
	public static class Usage
	{
		public static final int StreamDraw = 35040;
		public static final int StreamRead = 35041;
		public static final int StreamCopy = 35042;
		public static final int StaticDraw = 35044;
		public static final int StaticRead = 35045;
		public static final int StaticCopy = 35046;
		public static final int DynamicDraw = 35048;
		public static final int DynamicRead = 35049;
		public static final int DynamicCopy = 35050;
	}

	private final int m_handle;

	public Buffer()
	{
		m_handle = glCreateBuffers();
	}

	public Buffer(long size, int usage)
	{
		this();
		setData(size, usage);
	}

	public Buffer(short[] data, int usage)
	{
		this();
		setData(data, usage);
	}

	public Buffer(int[] data, int usage)
	{
		this();
		setData(data, usage);
	}

	public Buffer(long[] data, int usage)
	{
		this();
		setData(data, usage);
	}

	public Buffer(float[] data, int usage)
	{
		this();
		setData(data, usage);
	}

	public Buffer(double[] data, int usage)
	{
		this();
		setData(data, usage);
	}

	public void destruct()
	{
		glDeleteBuffers(m_handle);
	}

	public int getHandle()
	{
		return m_handle;
	}

	public void setData(long size, int usage)
	{
		glNamedBufferData(m_handle, size, usage);
	}

	public void setData(short[] data, int usage)
	{
		glNamedBufferData(m_handle, data, usage);
	}

	public void setData(int[] data, int usage)
	{
		glNamedBufferData(m_handle, data, usage);
	}

	public void setData(long[] data, int usage)
	{
		glNamedBufferData(m_handle, data, usage);
	}

	public void setData(float[] data, int usage)
	{
		glNamedBufferData(m_handle, data, usage);
	}

	public void setData(double[] data, int usage)
	{
		glNamedBufferData(m_handle, data, usage);
	}

	public void setSubData(long offset, short[] data)
	{
		glNamedBufferSubData(m_handle, offset, data);
	}

	public void setSubData(long offset, int[] data)
	{
		glNamedBufferSubData(m_handle, offset, data);
	}

	public void setSubData(long offset, long[] data)
	{
		glNamedBufferSubData(m_handle, offset, data);
	}

	public void setSubData(long offset, float[] data)
	{
		glNamedBufferSubData(m_handle, offset, data);
	}

	public void setSubData(long offset, double[] data)
	{
		glNamedBufferSubData(m_handle, offset, data);
	}
}
