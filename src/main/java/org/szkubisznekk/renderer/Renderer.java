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
	private static final int[] s_spriteIndices = {
		0, 1, 2,
		1, 3, 2
	};

	public Camera Camera;
	public Shader SpriteShader;
	public Texture TextureAtlas;

	private final Window m_window;
	private final VertexArray m_spriteMesh;
	private final ArrayList<DrawCommand> m_drawCommands = new ArrayList<>();
	private final Buffer m_instanceBuffer;
	private float m_aspectRatio;
	private final Matrix4f m_viewMatrix = new Matrix4f();
	private final Matrix4f m_projectionMatrix = new Matrix4f();
	private final float[] m_matrixData = new float[32];
	private final Buffer m_projectionBuffer;

	public Renderer(Window window) throws IOException
	{
		s_instance = this;

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

		glClearColor(0.36078431372f, 0.58039215686f, 0.98823529411f, 1.0f);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_BLEND);
		glEnable(GL_DEPTH_TEST);

		SpriteShader = new Shader(Path.of("res/shaders/Sprite"));
		m_spriteMesh = new VertexArray(
			new Buffer(s_spriteVertices, Buffer.Usage.StaticDraw),
			new Buffer(s_spriteIndices, Buffer.Usage.StaticDraw),
			s_spriteIndices.length);

		m_instanceBuffer = new Buffer(0, Buffer.Usage.DynamicDraw);
		glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 0, m_instanceBuffer.getHandle());

		m_projectionBuffer = new Buffer(32 * 4, Buffer.Usage.DynamicDraw);
		glBindBufferBase(GL_UNIFORM_BUFFER, 0, m_projectionBuffer.getHandle());

		TextureAtlas = Texture.load(Path.of("res/textures/texture_atlas.png"));
		TextureAtlas.bind(0);
	}

	public void destruct()
	{
		TextureAtlas.destruct();
		SpriteShader.destruct();
		m_spriteMesh.destruct();
		m_instanceBuffer.destruct();
		m_projectionBuffer.destruct();
	}

	public static Renderer get()
	{
		return s_instance;
	}

	public void beginFrame()
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		m_drawCommands.clear();

		m_viewMatrix.identity();
		m_viewMatrix.translate(-Camera.Position().x(), -Camera.Position().y, -1.0f);

		m_projectionMatrix.identity();
		float halfHeight = Camera.Size() * 0.5f;
		float halfWidth = halfHeight * m_aspectRatio;
		m_projectionMatrix.ortho(-halfWidth, halfWidth, -halfHeight, halfHeight, 0.1f, 10.0f);

		m_viewMatrix.get(m_matrixData);
		m_projectionMatrix.get(m_matrixData, 16);
		m_projectionBuffer.setSubData(0, m_matrixData);
	}

	public void endFrame()
	{
		float[] drawCommandData = new float[m_drawCommands.size() * 4];
		for(int i = 0; i < m_drawCommands.size(); i++)
		{
			int offset = i * 4;
			DrawCommand drawCommand = m_drawCommands.get(i);
			drawCommandData[offset] = drawCommand.Position.x;
			drawCommandData[offset + 1] = drawCommand.Position.y;
			drawCommandData[offset + 2] = drawCommand.Depth;
			drawCommandData[offset + 3] = (float)drawCommand.TextureID;
		}

		m_instanceBuffer.setData(drawCommandData, Buffer.Usage.StaticDraw);
		SpriteShader.bind();
		m_spriteMesh.bind();
		glDrawElementsInstanced(GL_TRIANGLES, m_spriteMesh.getIndexCount(), GL_UNSIGNED_INT, NULL, m_drawCommands.size());

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

		m_drawCommands.add(new DrawCommand(position, depth, textureID));
	}
}
