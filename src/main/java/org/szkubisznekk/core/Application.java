package org.szkubisznekk.core;

import dev.dominion.ecs.api.*;
import org.joml.Vector2f;
import org.szkubisznekk.renderer.*;

import java.io.IOException;
import java.nio.file.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46C.*;

public class Application
{
	private boolean m_running = true;
	private final Window m_window;
	private final Renderer m_renderer;

	public Application()
	{
		m_window = new Window();
		m_window.OnClose.add(() ->
		{
			m_running = false;
		});

		m_renderer = new Renderer(m_window);
	}

	public void destruct()
	{
		m_renderer.destruct();
		m_window.destruct();
	}

	private static class Position
	{
		Vector2f P;

		public Position(Vector2f p)
		{
			P = p;
		}
	}

	private static class Velocity
	{
		public Vector2f D;

		public Velocity(Vector2f d)
		{
			D = d;
		}
	}

	private Position m_position;

	public void run() throws IOException
	{
		Time.initialize();

		m_position = new Position(new Vector2f(0.0f, 0.0f));
		Dominion world = Dominion.create();
		Entity entity = world.createEntity(m_position, new Velocity(new Vector2f((float)(Math.random() * 2.0 + 1.0), (float)(Math.random() * 2.0 + 1.0))));
		Runnable system = () ->
		{
			world.findEntitiesWith(Position.class, Velocity.class).stream().forEach(result ->
			{
				Position position = result.comp1();
				Velocity velocity = result.comp2();

				if (position.P.x <= -0.5f)
				{
					velocity.D.x *= -1.0f;
					position.P.x += 0.001f;
				}
				else if (position.P.x >= 0.5)
				{
					velocity.D.x *= -1.0f;
					position.P.x -= 0.001f;
				}

				if (position.P.y <= -0.5f)
				{
					velocity.D.y *= -1.0f;
					position.P.y += 0.001f;
				}
				else if (position.P.y >= 0.5)
				{
					velocity.D.y *= -1.0f;
					position.P.y -= 0.001f;
				}

				position.P.x += velocity.D.x * Time.getDeltaTime();
				position.P.y += velocity.D.y * Time.getDeltaTime();
			});
		};

		Shader shader = new Shader(Path.of("res/shaders/Standard"));

		float[] vertices = {
			-0.5f, -0.5f, 0.0f, 0.0f,
			0.5f, -0.5f, 1.0f, 0.0f,
			-0.5f, 0.5f, 0.0f, 1.0f,
			0.5f, 0.5f, 1.0f, 1.0f
		};

		int[] indices = {
			0, 1, 2,
			1, 3, 2
		};

		Buffer vertexBuffer = new Buffer(vertices, Buffer.Usage.StaticDraw);
		Buffer elementBuffer = new Buffer(indices, Buffer.Usage.StaticDraw);
		VertexArray vertexArray = new VertexArray(vertexBuffer, elementBuffer, indices.length);

		while (m_running)
		{
			glfwPollEvents();
			Time.update();

			system.run();

			m_renderer.beginFrame();

			shader.setUniform("u_position", m_position.P);
			shader.setUniform("u_time", Time.getTime());
			m_renderer.submit(vertexArray, shader);

			m_renderer.endFrame();
		}

		shader.destruct();
		vertexBuffer.destruct();
		elementBuffer.destruct();
		vertexArray.destruct();
	}
}
