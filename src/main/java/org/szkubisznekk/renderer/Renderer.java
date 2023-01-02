package org.szkubisznekk.renderer;

import org.szkubisznekk.core.*;
import org.szkubisznekk.menu.*;

import org.joml.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.*;
import static org.lwjgl.opengl.GL46C.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.util.ArrayList;

/**
 * Manages an OpenGL context and provides functions to draw on the screen.
 */
public class Renderer
{
	/**
	 * Horizontal positioning of text relative to its anchor position.
	 */
	public enum HorizontalAlign
	{
		Left,
		Center,
		Right
	}

	/**
	 * Vertical positioning of text relative to its anchor position.
	 */
	public enum VerticalAlign
	{
		Bottom,
		Center,
		Top
	}

	/**
	 * Stores data used to render a quad.
	 *
	 * @param Position  The position of the quad.
	 * @param Depth     The z coordinate of the quad.
	 * @param TextureID The id of the texture on the texture atlas.
	 */
	private record DrawCommand(Vector2f Position, float Depth, int TextureID) {}

	/**
	 * Used to render multiple quads using the same shader and texture.
	 */
	private static class BatchRenderer
	{
		private final VertexArray m_mesh;
		private final ArrayList<DrawCommand> m_drawCommands = new ArrayList<>();
		private final Buffer m_instanceBuffer;

		/**
		 * Creates a shader storage buffer used to store instance data,
		 *
		 * @param mesh           The drawn mesh.
		 * @param bufferLocation The location of the shader storage buffer. [0, 31]
		 */
		public BatchRenderer(VertexArray mesh, int bufferLocation)
		{
			m_mesh = mesh;

			m_instanceBuffer = new Buffer(0, Buffer.Usage.DynamicDraw);
			glBindBufferBase(GL_SHADER_STORAGE_BUFFER, bufferLocation, m_instanceBuffer.getHandle());
		}

		/**
		 * Destruct everything used.
		 */
		public void destruct()
		{
			m_mesh.destruct();
			m_instanceBuffer.destruct();
		}

		/**
		 * Clears the draw commands queue.
		 */
		public void clear()
		{
			m_drawCommands.clear();
		}

		/**
		 * Submits a quad to the batch renderer to be drawn using a shader and a texture.
		 *
		 * @param position  Thw world space position of the quad's origin.
		 * @param depth     The z coordinate of the quad.
		 * @param textureID The id of the texture on the texture atlas.
		 */
		public void submit(Vector2f position, float depth, int textureID)
		{
			m_drawCommands.add(new DrawCommand(position, depth, textureID));
		}

		/**
		 * Renders all queues quads using a shader.
		 *
		 * @param shader The used shader.
		 */
		public void render(Shader shader)
		{
			float[] drawCommandData = getDrawCommandData(m_drawCommands);
			m_instanceBuffer.setData(drawCommandData, Buffer.Usage.StaticDraw);
			shader.bind();
			m_mesh.bind();
			glDrawElementsInstanced(GL_TRIANGLES, m_mesh.getIndexCount(), GL_UNSIGNED_INT, NULL, m_drawCommands.size());
		}

		/**
		 * Creates an array containing the data for the instance buffer.
		 *
		 * @param drawCommands The list of draw commands.
		 * @return An array containing the data for the instance buffer.
		 */
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

	private static final float[] s_buttonVertices = {
		-128f, -32f, 0.0f, 0.75f,
		128f, -32f, 1.0f, 0.75f,
		-128f, 32f, 0.0f, 1.0f,
		128f, 32f, 1.0f, 1.0f
	};

	private static final int[] s_quadIndices = {
		0, 1, 2,
		1, 3, 2
	};

	/**
	 * The camera used to render sprites.
	 */
	public Camera Camera;

	/**
	 * The shader used to render sprites.
	 */
	public Shader SpriteShader;

	/**
	 * The texture atlas used to render sprites.
	 */
	public Texture SpriteAtlas;

	/**
	 * The shader used to render text.
	 */
	public Shader TextShader;

	/**
	 * The texture atlas used to render text.
	 */
	public Texture TextAtlas;

	/**
	 * The shader used to render menu.
	 */
	public Shader MenuShader;

	/**
	 * The texture atlas used to render menu.
	 */
	public Texture MenuAtlas;

	private final Window m_window;
	private final BatchRenderer m_spriteRenderer;
	private final BatchRenderer m_textRenderer;
	private final BatchRenderer m_menuRenderer;

	private float m_aspectRatio;
	private final Matrix4f m_viewMatrix = new Matrix4f();
	private final Matrix4f m_spriteProjectionMatrix = new Matrix4f();
	private final Matrix4f m_uiProjectionMatrix = new Matrix4f();

	/**
	 * Creates the OpenGL context and loads the base resources.
	 *
	 * @param window The window used to display the rendered image.
	 */
	public Renderer(Window window)
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
			new Buffer(s_quadIndices, Buffer.Usage.StaticDraw),
			s_quadIndices.length), 0);
		SpriteShader = Shader.get("res/shaders/Sprite");

		SpriteAtlas = Texture.get("res/textures/sprites.png");
		SpriteAtlas.bind(0);

		m_textRenderer = new BatchRenderer(new VertexArray(
			new Buffer(s_textVertices, Buffer.Usage.StaticDraw),
			new Buffer(s_quadIndices, Buffer.Usage.StaticDraw),
			s_quadIndices.length), 1);
		TextShader = Shader.get("res/shaders/Text");

		TextAtlas = Texture.get("res/textures/font.png");
		TextAtlas.bind(1);

		m_menuRenderer = new BatchRenderer(new VertexArray(
			new Buffer(s_buttonVertices, Buffer.Usage.StaticDraw),
			new Buffer(s_quadIndices, Buffer.Usage.StaticDraw),
			s_quadIndices.length), 2);
		MenuShader = Shader.get("res/shaders/Menu");

		MenuAtlas = Texture.get("res/textures/menu.png");
		MenuAtlas.bind(2);

		Camera = new Camera(new Vector2f(0.0f, 7.5f), 16.0f);

		// Handle Window Resize
		m_window.OnResize.add((Window.Size size) ->
		{
			recalculateUIMatrices(size);

			m_aspectRatio = (float)size.Width() / size.Height();

			glViewport(0, 0, size.Width(), size.Height());
		});
		Window.Size windowSize = m_window.getSize();

		m_aspectRatio = (float)windowSize.Width() / windowSize.Height();

		recalculateUIMatrices(windowSize);
	}

	/**
	 * Destruct everything used.
	 */
	public void destruct()
	{
		SpriteAtlas.destruct();
		MenuAtlas.destruct();
		TextAtlas.destruct();
		m_spriteRenderer.destruct();
		m_menuRenderer.destruct();
		m_textRenderer.destruct();
	}

	/**
	 * Returns the only instance of renderer.
	 *
	 * @return The only instance of renderer.
	 */
	public static Renderer get()
	{
		return s_instance;
	}

	/**
	 * Clears the screen.
	 */
	public void beginFrame()
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		m_spriteRenderer.clear();
		m_menuRenderer.clear();
		m_textRenderer.clear();

		recalculateSpriteMatrices();
	}

	/**
	 * Draws the image.
	 */
	public void endFrame()
	{
		m_spriteRenderer.render(SpriteShader);

		glDisable(GL_DEPTH_TEST);

		m_menuRenderer.render(MenuShader);
		m_textRenderer.render(TextShader);

		glEnable(GL_DEPTH_TEST);

		glfwSwapBuffers(m_window.getHandle());
	}

	/**
	 * Submits a tile to the sprite batch renderer to be drawn using the sprite shader and sprite texture atlas.
	 * Tiles will be submitted only if they are on the screen.
	 *
	 * @param position  Thw world space position of the tile's center.
	 * @param depth     The z coordinate of the tile.
	 * @param textureID The id of the texture on the sprite texture atlas.
	 */
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

	public void submitMenu()
	{
		int startPosition = (Menu.getNumberOfOptions() - 1) * 34;
		for(int i = 0; i < Menu.getNumberOfOptions(); i++)
		{
			Pair<MenuOption, Boolean> menuOption = Menu.getOption(i);
			MenuOption option = menuOption.getFirst();
			Boolean isSelected = menuOption.getSecond();

			float offset = ((isSelected) ? 0.5f : 0.0f) + option.getTextureOffset();
			m_menuRenderer.submit(new Vector2f((float)startPosition, offset), 0f, 1);
			submitTextAbsolute(
				new Vector2f(0.0f, (float)startPosition),
				option.getText(),
				Renderer.HorizontalAlign.Center,
				Renderer.VerticalAlign.Center);
			startPosition -= 68;
		}
	}

	/**
	 * Submits a text to the text batch renderer to be drawn using the text shader and text texture atlas.
	 *
	 * @param position        The position of the text in screen space. Given in number of pixels from center.
	 * @param string          The text.
	 * @param horizontalAlign Aligns the anchor point to the given position relative to the text.
	 * @param verticalAlign   Aligns the anchor point to the given position relative to the text.
	 */
	public void submitTextAbsolute(Vector2f position, String string, HorizontalAlign horizontalAlign, VerticalAlign verticalAlign)
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

	/**
	 * Submits a text to the text batch renderer to be drawn using the text shader and text texture atlas.
	 *
	 * @param position        The position of the text in screen space. Given in range of [-1.0, 1.0] from center.
	 * @param string          The text.
	 * @param horizontalAlign Aligns the anchor point to the given position relative to the text.
	 * @param verticalAlign   Aligns the anchor point to the given position relative to the text.
	 */
	public void submitTextRelative(Vector2f position, String string, HorizontalAlign horizontalAlign, VerticalAlign verticalAlign)
	{
		Window.Size windowSize = m_window.getSize();
		float halfX = windowSize.Width() * 0.5f;
		float halfY = windowSize.Height() * 0.5f;
		Vector2f initialPos = new Vector2f(position.x * halfX, position.y * halfY);

		submitTextAbsolute(initialPos, string, horizontalAlign, verticalAlign);
	}

	/**
	 * Recalculates the projection and view matrix used in the sprite rendering.
	 */
	private void recalculateSpriteMatrices()
	{
		m_viewMatrix.identity();
		m_viewMatrix.translate(-Camera.Position().x(), -Camera.Position().y, -1.0f);

		m_spriteProjectionMatrix.identity();
		float halfHeight = Camera.Size() * 0.5f;
		float halfWidth = halfHeight * m_aspectRatio;
		m_spriteProjectionMatrix.ortho(-halfWidth, halfWidth, -halfHeight, halfHeight, 0.1f, 10.0f);

		SpriteShader.setUniform("u_projectionMatrix", m_spriteProjectionMatrix);
		SpriteShader.setUniform("u_viewMatrix", m_viewMatrix);
	}

	/**
	 * Recalculates the projection matrix used in the text rendering.
	 *
	 * @param windowSize The size of the window.
	 */
	private void recalculateUIMatrices(Window.Size windowSize)
	{
		m_uiProjectionMatrix.identity();
		float halfWidth = windowSize.Width() * 0.5f;
		float halfHeight = windowSize.Height() * 0.5f;
		m_uiProjectionMatrix.ortho(-halfWidth, halfWidth, -halfHeight, halfHeight, -1.0f, 2.0f);

		TextShader.setUniform("u_projectionMatrix", m_uiProjectionMatrix);
		MenuShader.setUniform("u_projectionMatrix", m_uiProjectionMatrix);
	}
}
