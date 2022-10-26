package org.szkubisznekk.core;

import org.szkubisznekk.input.*;
import org.szkubisznekk.renderer.*;
import org.szkubisznekk.world.*;

import org.joml.Vector2f;

import java.io.IOException;
import java.nio.file.Path;

public class Application
{
	float px = 0.0f;
	float py = 0.0f;
	float dx = 0.0f;
	float dy = 0.0f;

	private boolean m_running = true;
	private final Window m_window;
	private final Renderer m_renderer;

	public Application() throws IOException
	{
		m_window = new Window();
		m_window.OnClose.add(() ->
		{
			m_running = false;
		});

		Input.init(m_window);
		Input.AddInputDevice(Keyboard.class);
		Input.AddInputDevice(Mouse.class);
		Input.AddInputDevice(Gamepad.class);

		Controls.init();
		Controls.OnMove.add((Float value) ->
		{
			dx = value * 5.0f;
		});

		Controls.OnJump.add(() ->
		{
			dy = (py <= 0.0001f) ? 7.0f : dy;
		});

		Controls.OnMenu.add(() ->
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

	public void run()
	{
		World world = new World();
		Time.init();

		m_renderer.Camera = new Camera(new Vector2f(0.0f, 7.5f), 16.0f);

		Texture texture = Texture.load(Path.of("res/textures/texture_atlas.png"));
		texture.bind(0);

		while (m_running)
		{
			Input.update();
			Time.update();

			px += dx * Time.getDeltaTime();
			dy -= 19.81f * Time.getDeltaTime();
			py += dy * Time.getDeltaTime();
			if (py < 0.0f)
			{
				py = 0.0f;
				dy = 0.0f;
			}
			m_renderer.Camera.Position().x = px;

			m_renderer.beginFrame();

			world.submit(m_renderer);
			m_renderer.submit(new Vector2f(px, py), 1);

			m_renderer.endFrame();
		}

		texture.destruct();
	}
}
