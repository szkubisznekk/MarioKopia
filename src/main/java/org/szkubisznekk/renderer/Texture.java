package org.szkubisznekk.renderer;

import org.lwjgl.*;

import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.opengl.GL46C.*;

import java.nio.*;
import java.util.HashMap;

/**
 * Wrapping an OpenGL texture.
 */
public class Texture
{
	private static final HashMap<String, Texture> s_textures = new HashMap<>();

	private final int m_handle;

	static
	{
		stbi_set_flip_vertically_on_load(true);
	}

	/**
	 * Returns a texture. If the texture hasn't been yet loaded, then loads it into the cache.
	 * @param path The path to the texture.
	 * @return A texture.
	 */
	public static Texture get(String path)
	{
		if(s_textures.containsKey(path))
		{
			return s_textures.get(path);
		}

		Texture texture = new Texture(path);
		s_textures.put(path, texture);
		return texture;
	}

	/**
	 * Creates a texture and loads it from a file.
	 * @param path The path to the file.
	 */
	private Texture(String path)
	{
		m_handle = glGenTextures();

		bind(31);

		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		IntBuffer channelCount = BufferUtils.createIntBuffer(1);
		ByteBuffer data = stbi_load(path, width, height, channelCount, 0);

		if(data != null)
		{
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, (channelCount.get(0) == 4) ? GL_RGBA : GL_RGB, GL_UNSIGNED_BYTE, data);

			stbi_image_free(data);
		}
		else
		{
			System.err.printf("Failed to load texture: (%s)\n", path);
		}
	}

	/**
	 * Destructs the OpenGL texture.
	 */
	public void destruct()
	{
		glDeleteTextures(m_handle);
	}

	/**
	 * Returns the handle of the OpenGL texture.
	 * @return The handle of the OpenGL texture.
	 */
	public int getHandle()
	{
		return m_handle;
	}

	/**
	 * Binds the texture to a location.
	 * @param location The location.
	 */
	public void bind(int location)
	{
		glActiveTexture(GL_TEXTURE0 + location);
		glBindTexture(GL_TEXTURE_2D, m_handle);
	}
}
