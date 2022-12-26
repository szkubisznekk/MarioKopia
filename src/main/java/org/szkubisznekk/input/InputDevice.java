package org.szkubisznekk.input;

import org.szkubisznekk.core.*;

/**
 * Provides callbacks and functions for an input device.
 */
public class InputDevice
{
	/**
	 * The window used to handle the inputs.
	 */
	protected Window m_window;

	/**
	 * Assigns the window.
	 * Called when the input device is creates.
	 * @param window The window used to handle inputs.
	 */
	public void init(Window window)
	{
		m_window = window;
	}

	/**
	 * Called every frame.
	 */
	public void update() {}
}
