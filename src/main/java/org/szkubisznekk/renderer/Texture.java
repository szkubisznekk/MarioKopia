package org.szkubisznekk.renderer;

import org.lwjgl.*;

import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.opengl.GL46C.*;

import java.nio.*;
import java.nio.file.*;
import java.util.HashMap;

public class Texture
{
	private static final HashMap<Path, Texture> m_textures = new HashMap<>();

	private final int m_handle;

	static
	{
		stbi_set_flip_vertically_on_load(true);
	}

	public static Texture load(Path path)
	{
		if(m_textures.containsKey(path))
		{
			return m_textures.get(path);
		}

		Texture texture = new Texture(path);
		m_textures.put(path, texture);
		return texture;
	}

	private Texture(Path path)
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
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, (channelCount.get(0) == 4) ? GL_RGBA : GL_RGB, GL_UNSIGNED_BYTE, data);

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
