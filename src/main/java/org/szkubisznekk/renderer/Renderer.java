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
	public enum HorizontalAlign
	{
		Left,
		Center,
		Right
	}

	public enum VerticalAlign
	{
		Bottom,
		Center,
		Top
	}

	private record DrawCommand(Vector2f Position, float Depth, int TextureID) {}

	private static class BatchRenderer
	{
		private final VertexArray m_mesh;
		private final ArrayList<DrawCommand> m_drawCommands = new ArrayList<>();
		private final Buffer m_instanceBuffer;

		public BatchRenderer(VertexArray mesh, int bufferLocation)
		{
			m_mesh = mesh;

			m_instanceBuffer = new Buffer(0, Buffer.Usage.DynamicDraw);
			glBindBufferBase(GL_SHADER_STORAGE_BUFFER, bufferLocation, m_instanceBuffer.getHandle());
		}

		public void destruct()
		{
			m_mesh.destruct();
			m_instanceBuffer.destruct();
		}

		public void clear()
		{
			m_drawCommands.clear();
		}

		public void submit(Vector2f position, float depth, int textureID)
		{
			m_drawCommands.add(new DrawCommand(position, depth, textureID));
		}

		public void render(Shader shader)
		{
			float[] drawCommandData = getDrawCommandData(m_drawCommands);
			m_instanceBuffer.setData(drawCommandData, Buffer.Usage.StaticDraw);
			shader.bind();
			m_mesh.bind();
			glDrawElementsInstanced(GL_TRIANGLES, m_mesh.getIndexCount(), GL_UNSIGNED_INT, NULL, m_drawCommands.size());
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

	private static Renderer s_instance;

	private static final float[] s_spriteVertices = {
		-0.5f, -0.5f, 0.0f, 0.0f,
		0.5f, -0.5f, 1.0f, 0.0f,
		-0.5f, 0.5f, 0.0f, 1.0f,
		0.5f, 0.5f, 1.0f, 1.0f
	};

	private static final float[] s_textVertices = {
		0f, 0f, 0.0f, 0.0f,
		10f, 0f, 1.0f, 0.0f,
		0f, 14f, 0.0f, 1.0f,
		10f, 14f, 1.0f, 1.0f
	};
	private static final int[] s_spriteIndices = {
		0, 1, 2,
		1, 3, 2
	};

	public Camera Camera;
	public Shader SpriteShader;
	public Texture SpriteAtlas;
	public Shader TextShader;
	public Texture TextAtlas;

	private final Window m_window;
	private final BatchRenderer m_spriteRenderer;
	private final BatchRenderer m_textRenderer;

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

		// Create OpenGL context,
		glfwMakeContextCurrent(m_window.getHandle());
		createCapabilities();

		// Set OpenGL settings.
		glClearColor(0.36078431372f, 0.58039215686f, 0.98823529411f, 1.0f);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_BLEND);
		glEnable(GL_DEPTH_TEST);

		// Create batch renderers.
		m_spriteRenderer = new BatchRenderer(new VertexArray(
			new Buffer(s_spriteVertices, Buffer.Usage.StaticDraw),
			new Buffer(s_spriteIndices, Buffer.Usage.StaticDraw),
			s_spriteIndices.length), 0);
		SpriteShader = new Shader(Path.of("res/shaders/Sprite"));

		SpriteAtlas = Texture.load(Path.of("res/textures/texture_atlas.png"));
		SpriteAtlas.bind(0);

		m_textRenderer = new BatchRenderer(new VertexArray(
			new Buffer(s_textVertices, Buffer.Usage.StaticDraw),
			new Buffer(s_spriteIndices, Buffer.Usage.StaticDraw),
			s_spriteIndices.length), 1);
		TextShader = new Shader(Path.of("res/shaders/Text"));

		TextAtlas = Texture.load(Path.of("res/textures/font.png"));
		TextAtlas.bind(1);

		// Initialize uniform buffer object
		m_pvBuffer = new Buffer(32 * 4, Buffer.Usage.DynamicDraw);
		glBindBufferBase(GL_UNIFORM_BUFFER, 0, m_pvBuffer.getHandle());

		// Handle Window Resize
		m_window.OnResize.add((Window.Size size) ->
		{
			recalculateTextMatrices(size);

			m_aspectRatio = (float)size.Width() / size.Height();

			glViewport(0, 0, size.Width(), size.Height());
		});
		Window.Size windowSize = m_window.getSize();

		m_aspectRatio = (float)windowSize.Width() / windowSize.Height();

		recalculateTextMatrices(windowSize);
	}

	public void destruct()
	{
		SpriteAtlas.destruct();
		TextAtlas.destruct();
		m_spriteRenderer.destruct();
		m_textRenderer.destruct();
		m_pvBuffer.destruct();
	}

	public static Renderer get()
	{
		return s_instance;
	}

	public void beginFrame()
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		m_spriteRenderer.clear();
		m_textRenderer.clear();

		recalculateSpriteMatrices();

		m_viewMatrix.get(m_pvMatrixData);
		m_projectionMatrix.get(m_pvMatrixData, 16);
		m_pvBuffer.setSubData(0, m_pvMatrixData);
	}

	public void endFrame()
	{
		m_spriteRenderer.render(SpriteShader);

		glDisable(GL_DEPTH_TEST);

		m_textRenderer.render(TextShader);

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

		m_spriteRenderer.submit(position, depth, textureID);
	}

	public void submitTextRelative(Vector2f position, String string, HorizontalAlign horizontalAlign, VerticalAlign verticalAlign)
	{
		Vector2f initialPos = new Vector2f(position);

		int textWidth = string.length() * 10 + (string.length() - 1) * 2;
		switch(horizontalAlign)
		{
			case Center -> initialPos.sub(textWidth * 0.5f, 0.0f);
			case Right -> initialPos.sub(textWidth, 0.0f);
		}
		switch(verticalAlign)
		{
			case Center -> initialPos.sub(0.0f, 7.0f);
			case Top -> initialPos.sub(0.0f, 14.0f);
		}

		string = string.toUpperCase();
		for(int i = 0; i < string.length(); i++)
		{
			char c = string.charAt(i);
			int characterValue = (int)c - 32;

			if(characterValue >= 64)
			{
				continue;
			}

			Vector2f pos = new Vector2f(initialPos).add(i * 12f, 0f);
			m_textRenderer.submit(pos, 0.0f, characterValue);
		}
	}

	public void submitTextAbsolute(Vector2f position, String string, HorizontalAlign horizontalAlign, VerticalAlign verticalAlign)
	{
		Window.Size windowSize = m_window.getSize();
		float halfX = windowSize.Width() * 0.5f;
		float halfY = windowSize.Height() * 0.5f;
		Vector2f initialPos = new Vector2f(position.x * halfX, position.y * halfY);

		submitTextRelative(initialPos, string, horizontalAlign, verticalAlign);
	}

	private void recalculateSpriteMatrices()
	{
		m_viewMatrix.identity();
		m_viewMatrix.translate(-Camera.Position().x(), -Camera.Position().y, -1.0f);

		m_projectionMatrix.identity();
		float halfHeight = Camera.Size() * 0.5f;
		float halfWidth = halfHeight * m_aspectRatio;
		m_projectionMatrix.ortho(-halfWidth, halfWidth, -halfHeight, halfHeight, 0.1f, 10.0f);
	}

	private void recalculateTextMatrices(Window.Size windowSize)
	{
		m_textProjectionMatrix.identity();
		float halfWidth = windowSize.Width() * 0.5f;
		float halfHeight = windowSize.Height() * 0.5f;
		m_textProjectionMatrix.ortho(-halfWidth, halfWidth, -halfHeight, halfHeight, -1.0f, 2.0f);
		TextShader.setUniform("u_textProjectionMatrix", m_textProjectionMatrix);
	}
}
