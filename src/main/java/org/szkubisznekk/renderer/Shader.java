package org.szkubisznekk.renderer;

import org.joml.*;
import org.lwjgl.*;

import static org.lwjgl.opengl.GL46C.*;

import java.io.IOException;
import java.nio.*;
import java.nio.file.*;
import java.util.HashMap;

/**
 * Wrapping an OpenGL program.
 */
public class Shader
{
	private static final HashMap<String, Shader> s_shaders = new HashMap<>();

	private final int m_handle;

	/**
	 * Returns a shader. If the shader hasn't been yet loaded, then loads it into the cache.
	 *
	 * @param path The path to the source of the shaders.
	 * @return A shader.
	 */
	public static Shader get(String path)
	{
		if(s_shaders.containsKey(path))
		{
			return s_shaders.get(path);
		}

		Shader shader = new Shader(path);
		s_shaders.put(path, shader);
		return shader;
	}

	/**
	 * Creates a program using a vertex and a fragment shader.
	 *
	 * @param path The path to the source of the shaders.
	 */
	private Shader(String path)
	{
		m_handle = glCreateProgram();

		ShaderDescription[] shaders = loadShaders(path);
		for(var shader : shaders)
		{
			shader.Handle = glCreateShader(shader.Type);
			glShaderSource(shader.Handle, shader.Source);
			glCompileShader(shader.Handle);
			glAttachShader(m_handle, shader.Handle);
		}

		glLinkProgram(m_handle);

		for(var shader : shaders)
		{
			glDeleteShader(shader.Handle);
		}
	}

	/**
	 * Destruct the OpenGL program.
	 */
	public void destruct()
	{
		glDeleteProgram(m_handle);
	}

	/**
	 * Returns the handle of the OpenGL program.
	 *
	 * @return The handle of the OpenGL program.
	 */
	public int getHandle()
	{
		return m_handle;
	}

	/**
	 * Set a uniform in the shader.
	 *
	 * @param name  The name of the uniform.
	 * @param value The new value of the uniform.
	 */
	public void setUniform(String name, int value)
	{
		bind();
		glUniform1i(glGetUniformLocation(m_handle, name), value);
	}

	/**
	 * Set a uniform in the shader.
	 *
	 * @param name  The name of the uniform.
	 * @param value The new value of the uniform.
	 */
	public void setUniform(String name, float value)
	{
		bind();
		glUniform1f(glGetUniformLocation(m_handle, name), value);
	}

	/**
	 * Set a uniform in the shader.
	 *
	 * @param name  The name of the uniform.
	 * @param value The new value of the uniform.
	 */
	public void setUniform(String name, Vector2i value)
	{
		bind();
		IntBuffer buffer = BufferUtils.createIntBuffer(2);
		value.get(buffer);
		glUniform2iv(glGetUniformLocation(m_handle, name), buffer);
	}

	/**
	 * Set a uniform in the shader.
	 *
	 * @param name  The name of the uniform.
	 * @param value The new value of the uniform.
	 */
	public void setUniform(String name, Vector2f value)
	{
		bind();
		FloatBuffer buffer = BufferUtils.createFloatBuffer(2);
		value.get(buffer);
		glUniform2fv(glGetUniformLocation(m_handle, name), buffer);
	}

	/**
	 * Set a uniform in the shader.
	 *
	 * @param name  The name of the uniform.
	 * @param value The new value of the uniform.
	 */
	public void setUniform(String name, Vector3i value)
	{
		bind();
		IntBuffer buffer = BufferUtils.createIntBuffer(3);
		value.get(buffer);
		glUniform3iv(glGetUniformLocation(m_handle, name), buffer);
	}

	/**
	 * Set a uniform in the shader.
	 *
	 * @param name  The name of the uniform.
	 * @param value The new value of the uniform.
	 */
	public void setUniform(String name, Vector3f value)
	{
		bind();
		FloatBuffer buffer = BufferUtils.createFloatBuffer(3);
		value.get(buffer);
		glUniform3fv(glGetUniformLocation(m_handle, name), buffer);
	}

	/**
	 * Set a uniform in the shader.
	 *
	 * @param name  The name of the uniform.
	 * @param value The new value of the uniform.
	 */
	public void setUniform(String name, Vector4i value)
	{
		bind();
		IntBuffer buffer = BufferUtils.createIntBuffer(4);
		value.get(buffer);
		glUniform4iv(glGetUniformLocation(m_handle, name), buffer);
	}

	/**
	 * Set a uniform in the shader.
	 *
	 * @param name  The name of the uniform.
	 * @param value The new value of the uniform.
	 */
	public void setUniform(String name, Vector4f value)
	{
		bind();
		FloatBuffer buffer = BufferUtils.createFloatBuffer(4);
		value.get(buffer);
		glUniform4fv(glGetUniformLocation(m_handle, name), buffer);
	}

	/**
	 * Set a uniform in the shader.
	 *
	 * @param name  The name of the uniform.
	 * @param value The new value of the uniform.
	 */
	public void setUniform(String name, Matrix3f value)
	{
		bind();
		FloatBuffer buffer = BufferUtils.createFloatBuffer(9);
		value.get(buffer);
		glUniformMatrix3fv(glGetUniformLocation(m_handle, name), false, buffer);
	}

	/**
	 * Set a uniform in the shader.
	 *
	 * @param name  The name of the uniform.
	 * @param value The new value of the uniform.
	 */
	public void setUniform(String name, Matrix4f value)
	{
		bind();
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		value.get(buffer);
		glUniformMatrix4fv(glGetUniformLocation(m_handle, name), false, buffer);
	}

	/**
	 * Binds the program as current.
	 */
	public void bind()
	{
		glUseProgram(m_handle);
	}

	/**
	 * Unbinds the currently bound program.
	 */
	public static void unbind()
	{
		glUseProgram(0);
	}

	/**
	 * Loads one shader source file.
	 *
	 * @param path The path to the file.
	 * @param type The type of the shader.
	 * @return The source of the shader.
	 */
	private static String getShaderSource(String path, int type)
	{
		String actualPath = path + ((type == GL_VERTEX_SHADER) ? ".vert" : ".frag");
		try
		{
			return Files.readString(Path.of(actualPath));
		}
		catch(IOException e)
		{
			System.err.printf("Failed to load shader: (%s)\n", actualPath);
			return "";

		}
	}

	/**
	 * Loads the vertex and fragment shader from files.
	 *
	 * @param path The path to the source files.
	 * @return The description of the vertex and fragment shader.
	 */
	private static ShaderDescription[] loadShaders(String path)
	{
		return new ShaderDescription[]{
			new ShaderDescription(GL_VERTEX_SHADER, -1, getShaderSource(path, GL_VERTEX_SHADER)),
			new ShaderDescription(GL_FRAGMENT_SHADER, -1, getShaderSource(path, GL_FRAGMENT_SHADER)),
		};
	}

	/**
	 * Stores data related to a shader.
	 */
	private static class ShaderDescription
	{
		public int Type;
		public int Handle;
		public String Source;

		public ShaderDescription(int type, int handle, String source)
		{
			Type = type;
			Handle = handle;
			Source = source;
		}
	}
}
