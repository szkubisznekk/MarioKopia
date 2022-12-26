package org.szkubisznekk.core;

import org.szkubisznekk.input.*;
import org.szkubisznekk.renderer.*;
import org.szkubisznekk.world.*;
import org.szkubisznekk.audio.*;

public class Application
{
	private boolean m_running = true;
	private final Window m_window;
	private final InputDeviceManager m_inputDeviceManager;
	private final AudioManager m_audioManager;
	private final Renderer m_renderer;

	public Application()
	{
		m_window = new Window();
		m_window.OnClose.add(() ->
		{
			m_running = false;
		});

		m_inputDeviceManager = new InputDeviceManager(m_window);
		m_inputDeviceManager.addInputDevice(Keyboard.class);
		m_inputDeviceManager.addInputDevice(Mouse.class);
		m_inputDeviceManager.addInputDevice(Gamepad.class);

		Controls.init();
		Controls.OnMenu.add(() ->
		{
			m_running = false;
		});

		m_audioManager = new AudioManager();

		m_renderer = new Renderer(m_window);
	}

	public void destruct()
	{
		m_renderer.destruct();
		m_audioManager.destruct();
		m_window.destruct();
	}

	public void run()
	{
		Time.init();

		World world = new World();
		world.start();

		AudioClip clip = new AudioClip("res/audio/fade.ogg");
		m_audioManager.setVolume(0.2f);
		m_audioManager.play(clip, true);

		while(m_running)
		{
			Time.update();
			m_inputDeviceManager.update();

			m_renderer.beginFrame();

			world.update();

			m_renderer.endFrame();
		}

		clip.destruct();
		world.stop();
	}
}
