package org.szkubisznekk.core;

import org.szkubisznekk.input.*;
import org.szkubisznekk.menu.*;
import org.szkubisznekk.renderer.*;
import org.szkubisznekk.world.*;
import org.szkubisznekk.audio.*;

import java.util.ArrayList;

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
	private final Menu m_mainMenu;

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

		m_worldManager = new WorldManager("res/maps");
		m_worldManager.addSystem(PlayerSystem.class);
		m_worldManager.addSystem(PhysicsSystem.class);
		m_worldManager.addSystem(CameraSystem.class);
		m_worldManager.addSystem(RendererSystem.class);

		m_audioManager = new AudioManager();

		m_renderer = new Renderer(m_window);

		m_mainMenu = new Menu();
		Button startButton = new Button("Start");
		Slider volumeSlider = new Slider("Volume", 0.5f);
		Button exitButton = new Button("Exit");
		startButton.OnInteract.add(() ->
		{
			m_worldManager.load("res/maps/untitled.tmx");
			m_mainMenu.setShown(false);
		});
		volumeSlider.OnInteract.add(() ->
		{
			m_audioManager.setVolume(volumeSlider.getValue());
		});
		exitButton.OnInteract.add(() ->
		{
			m_running = false;
		});
		m_mainMenu.addOption(startButton);
		m_mainMenu.addOption(volumeSlider);
		m_mainMenu.addOption(exitButton);
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

		m_audioManager.setVolume(0.1f);
		m_audioManager.play("res/audio/omfg_hello.ogg", 1.0f, true);

		while(m_running)
		{
			Time.update();
			m_inputDeviceManager.update();

			m_renderer.beginFrame();

			if(m_mainMenu.isShown())
			{
				m_renderer.submitMenu(m_mainMenu);
			}
			else
			{
				m_worldManager.updateCurrent();
				m_worldManager.submitCurrent();
			}

			m_renderer.endFrame();
		}

		m_worldManager.destruct();
	}
}
