package org.szkubisznekk.renderer;

import org.szkubisznekk.core.*;

import org.joml.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.*;
import static org.lwjgl.opengl.GL46C.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class Renderer
{
	private record DrawCommand(Vector2f Position, float Depth, int TextureID) {}

	private static Renderer s_instance;
	private static final float[] s_spriteVertices = {
		-0.5f, -0.5f, 0.0f, 0.0f,
		0.5f, -0.5f, 1.0f, 0.0f,
		-0.5f, 0.5f, 0.0f, 1.0f,
		0.5f, 0.5f, 1.0f, 1.0f
	};

	private static final float[] s_textVertices = {
		0f, 0f, 0.0f, 0.0f,
		5f, 0f, 1.0f, 0.0f,
		0f, 7f, 0.0f, 1.0f,
		5f, 7f, 1.0f, 1.0f
	};
	private static final int[] s_spriteIndices = {
		0, 1, 2,
		1, 3, 2
	};

	public Camera Camera;
	public Shader SpriteShader;
	public Shader TextShader;
	public Texture SpriteAtlas;
	public Texture TextAtlas;

	private final Window m_window;
	private final VertexArray m_spriteMesh;
	private final VertexArray m_textMesh;
	private final ArrayList<DrawCommand> m_spriteDrawCommands = new ArrayList<>();
	private final ArrayList<DrawCommand> m_textDrawCommands = new ArrayList<>();
	private final Buffer m_spriteInstanceBuffer;
	private final Buffer m_textInstanceBuffer;
	private float m_aspectRatio;
	private final Matrix4f m_viewMatrix = new Matrix4f();
	private final Matrix4f m_projectionMatrix = new Matrix4f();
	private final Matrix4f m_textProjectionMatrix = new Matrix4f();
	private final float[] m_pvMatrixData = new float[32];
	private final Buffer m_pvBuffer;

	public Renderer(Window window) throws IOException
	{
		s_instance = this;

		m_window = window;
		m_window.OnResize.add((Window.Size size) ->
		{
			m_textProjectionMatrix.identity();
			float halfWidth = size.Width() * 0.5f;
			float halfHeight = size.Height() * 0.5f;
			m_textProjectionMatrix.ortho(-halfWidth, halfWidth, -halfHeight, halfHeight, -1.0f, 2.0f);
			TextShader.setUniform("u_textProjectionMatrix", m_textProjectionMatrix);
			m_aspectRatio = (float)size.Width() / size.Height();
			glViewport(0, 0, size.Width(), size.Height());
		});
		Window.Size windowSize = m_window.getSize();
		m_aspectRatio = (float)windowSize.Width() / windowSize.Height();

		glfwMakeContextCurrent(m_window.getHandle());
		createCapabilities();

		glClearColor(0.36078431372f, 0.58039215686f, 0.98823529411f, 1.0f);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_BLEND);
		glEnable(GL_DEPTH_TEST);

		SpriteShader = new Shader(Path.of("res/shaders/Sprite"));
		m_spriteMesh = new VertexArray(
			new Buffer(s_spriteVertices, Buffer.Usage.StaticDraw),
			new Buffer(s_spriteIndices, Buffer.Usage.StaticDraw),
			s_spriteIndices.length);

		TextShader = new Shader(Path.of("res/shaders/Text"));
		m_textProjectionMatrix.identity();
		float halfWidth = windowSize.Width() * 0.5f;
		float halfHeight = windowSize.Height() * 0.5f;
		m_textProjectionMatrix.ortho(-halfWidth, halfWidth, -halfHeight, halfHeight, -1.0f, 2.0f);
		TextShader.setUniform("u_textProjectionMatrix", m_textProjectionMatrix);
		m_textMesh = new VertexArray(
			new Buffer(s_textVertices, Buffer.Usage.StaticDraw),
			new Buffer(s_spriteIndices, Buffer.Usage.StaticDraw),
			s_spriteIndices.length);

		m_spriteInstanceBuffer = new Buffer(0, Buffer.Usage.DynamicDraw);
		glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 0, m_spriteInstanceBuffer.getHandle());

		m_textInstanceBuffer = new Buffer(0, Buffer.Usage.DynamicDraw);
		glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 1, m_textInstanceBuffer.getHandle());

		m_pvBuffer = new Buffer(32 * 4, Buffer.Usage.DynamicDraw);
		glBindBufferBase(GL_UNIFORM_BUFFER, 0, m_pvBuffer.getHandle());

		SpriteAtlas = Texture.load(Path.of("res/textures/texture_atlas.png"));
		SpriteAtlas.bind(0);

		TextAtlas = Texture.load(Path.of("font.png"));
		TextAtlas.bind(1);
	}

	public void destruct()
	{
		SpriteAtlas.destruct();
		SpriteShader.destruct();
		TextAtlas.destruct();
		TextShader.destruct();
		m_textMesh.destruct();
		m_spriteMesh.destruct();
		m_spriteInstanceBuffer.destruct();
		m_pvBuffer.destruct();
	}

	public static Renderer get()
	{
		return s_instance;
	}

	public void beginFrame()
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		m_spriteDrawCommands.clear();
		m_textDrawCommands.clear();

		m_viewMatrix.identity();
		m_viewMatrix.translate(-Camera.Position().x(), -Camera.Position().y, -1.0f);

		m_projectionMatrix.identity();
		float halfHeight = Camera.Size() * 0.5f;
		float halfWidth = halfHeight * m_aspectRatio;
		m_projectionMatrix.ortho(-halfWidth, halfWidth, -halfHeight, halfHeight, 0.1f, 10.0f);

		m_viewMatrix.get(m_pvMatrixData);
		m_projectionMatrix.get(m_pvMatrixData, 16);
		m_pvBuffer.setSubData(0, m_pvMatrixData);
	}

	public void endFrame()
	{
		float[] spriteDrawCommandData = getDrawCommandData(m_spriteDrawCommands);
		m_spriteInstanceBuffer.setData(spriteDrawCommandData, Buffer.Usage.StaticDraw);
		SpriteShader.bind();
		m_spriteMesh.bind();
		glDrawElementsInstanced(GL_TRIANGLES, m_spriteMesh.getIndexCount(), GL_UNSIGNED_INT, NULL, m_spriteDrawCommands.size());

		glDisable(GL_DEPTH_TEST);

		float[] textDrawCommandData = getDrawCommandData(m_textDrawCommands);
		m_textInstanceBuffer.setData(textDrawCommandData, Buffer.Usage.StaticDraw);
		TextShader.bind();
		m_textMesh.bind();
		glDrawElementsInstanced(GL_TRIANGLES, m_textMesh.getIndexCount(), GL_UNSIGNED_INT, NULL, m_textDrawCommands.size());

		glEnable(GL_DEPTH_TEST);

		glfwSwapBuffers(m_window.getHandle());
	}

	public void submit(Vector2f position, float depth, byte textureID)
	{
		if(textureID == 0)
		{
			return;
		}

		float halfHeight = Camera.Size() * 0.5f;
		float halfWidth = halfHeight * m_aspectRatio + 1.0f;
		halfHeight += 1.0f;
		Vector2f bottomLeft = new Vector2f(Camera.Position()).sub(halfWidth, halfHeight);
		Vector2f topRight = new Vector2f(Camera.Position()).add(halfWidth, halfHeight);

		if(position.x < bottomLeft.x || position.x > topRight.x || position.y < bottomLeft.y || position.y > topRight.y)
		{
			return;
		}

		m_spriteDrawCommands.add(new DrawCommand(position, depth, textureID));
	}

	public void submitTextRelative(Vector2f position, String string)
	{
		string = string.toUpperCase();
		for(int i = 0; i < string.length(); i++)
		{
			char c = string.charAt(i);
			int characterValue = (int)c - 31;

			if(characterValue >= 64)
			{
				continue;
			}

			Vector2f pos = new Vector2f(position).add(i * 6f, 0f);
			m_textDrawCommands.add(new DrawCommand(pos, 0.0f, characterValue));
		}
	}

	public void submitTextAbsolute(Vector2f position, String string)
	{
		Window.Size windowSize = m_window.getSize();
		float halfX = windowSize.Width() * 0.5f;
		float halfY = windowSize.Height() * 0.5f;
		Vector2f initialPos = new Vector2f(position.x * halfX, position.y * halfY);

		submitTextRelative(initialPos, string);
	}

	private static float[] getDrawCommandData(ArrayList<DrawCommand> drawCommands)
	{
		float[] drawCommandData = new float[drawCommands.size() * 4];
		for(int i = 0; i < drawCommands.size(); i++)
		{
			int offset = i * 4;
			DrawCommand drawCommand = drawCommands.get(i);
			drawCommandData[offset] = drawCommand.Position.x;
			drawCommandData[offset + 1] = drawCommand.Position.y;
			drawCommandData[offset + 2] = drawCommand.Depth;
			drawCommandData[offset + 3] = (float)drawCommand.TextureID;
		}

		return drawCommandData;
	}
}
