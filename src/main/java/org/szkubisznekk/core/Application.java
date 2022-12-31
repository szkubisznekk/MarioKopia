package org.szkubisznekk.core;

import org.szkubisznekk.input.*;
import org.szkubisznekk.renderer.*;
import org.szkubisznekk.world.*;
import org.szkubisznekk.audio.*;

/**
 * Manages every system needed to run the application.
 */
public class Application
{
	private boolean m_running = true;
	private final Window m_window;
	private final InputDeviceManager m_inputDeviceManager;
	private final WorldManager m_worldManager;
	private final AudioManager m_audioManager;
	private final Renderer m_renderer;

	/**
	 * Creates all systems.
	 */
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

		m_worldManager = new WorldManager("res/maps");
		m_worldManager.addSystem(PlayerSystem.class);
		m_worldManager.addSystem(PhysicsSystem.class);
		m_worldManager.addSystem(CameraSystem.class);
		m_worldManager.addSystem(RendererSystem.class);

		m_audioManager = new AudioManager();

		m_renderer = new Renderer(m_window);
	}

	/**
	 * Destroys all systems.
	 */
	public void destruct()
	{
		m_renderer.destruct();
		m_worldManager.destruct();
		m_audioManager.destruct();
		m_window.destruct();
	}

	/**
	 * Starts the application.
	 */
	public void run()
	{
		Time.init();

		m_audioManager.setVolume(0.5f);
		m_audioManager.play("res/audio/szkubisznekk.ogg", 1.0f, true);

		m_worldManager.load("res/maps/untitled.tmx");
		while(m_running)
		{
			Time.update();
			m_inputDeviceManager.update();
			m_worldManager.updateCurrent();

			m_renderer.beginFrame();
			m_worldManager.submitCurrent();

			m_renderer.endFrame();
		}

		m_worldManager.destruct();
	}
}
