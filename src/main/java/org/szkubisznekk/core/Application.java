package org.szkubisznekk.core;

import org.szkubisznekk.input.*;
import org.szkubisznekk.renderer.*;
import org.szkubisznekk.world.*;

import org.joml.*;

import java.io.IOException;
import java.nio.file.Path;

public class Application
{
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
		System.setProperty("dominion.show-banner", "false");
		World world = new World();

		m_renderer.Camera = new Camera(new Vector2f(0.0f, 7.5f), 16.0f);

		world.start();

		Time.init();
		while (m_running)
		{
			Input.update();
			Time.update();

			m_renderer.beginFrame();

			world.update();

			m_renderer.endFrame();
		}

		world.stop();
	}
}
