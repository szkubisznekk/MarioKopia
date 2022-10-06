package org.szkubisznekk.renderer;

import java.nio.*;
import java.nio.file.*;

import org.lwjgl.*;

import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.opengl.GL46C.*;

public class Texture
{
	private final int m_handle;

	static
	{
		stbi_set_flip_vertically_on_load(true);
	}

	public Texture(Path path)
	{
		m_handle = glGenTextures();

		glBindTexture(GL_TEXTURE_2D, m_handle);

		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		IntBuffer channelCount = BufferUtils.createIntBuffer(1);
		ByteBuffer data = stbi_load(path.toString(), width, height, channelCount, 0);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(), height.get(), 0, GL_RGB, GL_UNSIGNED_BYTE, data);

		stbi_image_free(data);
	}

	public void destruct()
	{
		glDeleteTextures(m_handle);
	}

	public int getHandle()
	{
		return m_handle;
	}

	public void bind(int location)
	{
		glActiveTexture(GL_TEXTURE0 + location);
		glBindTexture(GL_TEXTURE_2D, m_handle);
	}
}
