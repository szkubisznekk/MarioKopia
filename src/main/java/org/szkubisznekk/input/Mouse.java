package org.szkubisznekk.input;

import org.szkubisznekk.core.*;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * Provides callbacks and functions for a mouse.
 */
public class Mouse extends InputDevice
{
	private static Mouse s_instance;

	/**
	 * Called when any button is pressed.
	 */
	public ArrayList<Consumer<Integer>> OnButtonPress = new ArrayList<>();

	/**
	 * Called when any button is pressed.
	 */
	public ArrayList<Consumer<Integer>> OnButtonRelease = new ArrayList<>();

	/**
	 * Returns the only instance of mouse.
	 * @return The only instance of mouse.
	 */
	public static Mouse get()
	{
		return s_instance;
	}

	/**
	 * Sets up callbacks.
	 * @param window The window used to handle inputs.
	 */
	@Override
	void init(Window window)
	{
		super.init(window);
		s_instance = this;

		glfwSetInputMode(m_window.getHandle(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);

		glfwSetMouseButtonCallback(m_window.getHandle(), (long handle, int button, int action, int mods) ->
		{
			switch(action)
			{
				case InputCodes.Actions.Press ->
				{
					for(var callback : OnButtonPress)
					{
						callback.accept(button);
					}
				}
				case InputCodes.Actions.Release ->
				{
					for(var callback : OnButtonRelease)
					{
						callback.accept(button);
					}
				}
			}
		});
	}

	/**
	 * Does nothing.
	 */
	@Override
	void update()
	{
		super.update();
	}

	/**
	 * Returns whether a button is performing a specific action.
	 * @param button The button.
	 * @param action The action.
	 * @return Whether a button is performing a specific action.
	 */
	public boolean isButton(int button, int action)
	{
		return glfwGetMouseButton(m_window.getHandle(), button) == action;
	}
}
