package org.szkubisznekk.input;

import org.szkubisznekk.core.*;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;

/**
 * Manages input devices.
 */
public class InputDeviceManager
{
	private final Window m_window;
	private final ArrayList<InputDevice> m_inputDevices = new ArrayList<>();

	/**
	 * Assigns the window.
	 *
	 * @param window The window used to handle inputs.
	 */
	public InputDeviceManager(Window window)
	{
		m_window = window;
	}

	/**
	 * Updates all input devices.
	 */
	public void update()
	{
		glfwPollEvents();

		for(var inputDevice : m_inputDevices)
		{
			inputDevice.update();
		}
	}

	/**
	 * Add a new input device.
	 *
	 * @param type The type of the input device.
	 * @param <T>  The type of the input device.
	 */
	public <T extends InputDevice> void addInputDevice(Class<T> type)
	{
		try
		{
			InputDevice inputDevice = type.getConstructor().newInstance();
			inputDevice.init(m_window);
			m_inputDevices.add(inputDevice);
		}
		catch(Exception ignored) {}
	}

	/**
	 * Remove an input device.
	 *
	 * @param type The type of the input device.
	 * @param <T>  The type of the input device.
	 */
	public <T extends InputDevice> void removeInputDevice(Class<T> type)
	{
		int n = m_inputDevices.size();
		int i = 0;
		while(i < n && m_inputDevices.get(i).getClass() != type) {i++;}
		if(i < n)
		{
			m_inputDevices.remove(i);
		}
	}
}
