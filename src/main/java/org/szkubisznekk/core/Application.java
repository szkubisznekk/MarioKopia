package org.szkubisznekk.core;

import org.szkubisznekk.input.*;
import org.szkubisznekk.renderer.*;
import org.szkubisznekk.world.*;

import java.io.IOException;

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
		Time.init();

		World world = new World();
		world.start();
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
