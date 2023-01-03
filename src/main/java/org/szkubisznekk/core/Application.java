package org.szkubisznekk.core;

import org.szkubisznekk.input.*;
import org.szkubisznekk.menu.*;
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
		GameState.load();

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

		m_worldManager = new WorldManager("res/maps", GameState.LastFinishedMap);
		m_worldManager.addSystem(PlayerSystem.class);
		m_worldManager.addSystem(ObjectSystem.class);
		m_worldManager.addSystem(PhysicsSystem.class);
		m_worldManager.addSystem(CameraSystem.class);
		m_worldManager.addSystem(RendererSystem.class);

		m_audioManager = new AudioManager();

		m_renderer = new Renderer(m_window);

		Button startButton = new Button("Start");
		startButton.OnInteract.add(() ->
		{
			m_worldManager.reloadCurrent();
			Menu.setShown(false);
		});

		Slider skinSlider = new Slider("Skin", 0f, 2);
		skinSlider.OnInteract.add(() -> GameState.PlayerSkin = (byte)skinSlider.getValue());

		Slider volumeSlider = new Slider("Volume", 0.5f, 10);
		volumeSlider.OnInteract.add(() -> m_audioManager.setVolume(volumeSlider.getValue()));

		Button exitButton = new Button("Exit");
		exitButton.OnInteract.add(() -> m_running = false);

		Menu.init();
		Menu.addOption(startButton);
		Menu.addOption(skinSlider);
		Menu.addOption(volumeSlider);
		Menu.addOption(exitButton);
	}

	/**
	 * Destroys all systems.
	 */
	public void destruct()
	{
		GameState.save();
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
		m_audioManager.play("res/audio/omfg_hello.ogg", 0.2f, true);

		while(m_running)
		{
			Time.update();
			m_inputDeviceManager.update();
			m_renderer.beginFrame();

			if(Menu.isShown())
			{
				m_renderer.submitMenu();
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
